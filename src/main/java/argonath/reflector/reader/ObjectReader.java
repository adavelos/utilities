package argonath.reflector.reader;

import argonath.reflector.reader.filter.Filters;
import argonath.reflector.reader.selector.ValueSelector;
import argonath.reflector.reader.selector.ValueSelectorItem;
import argonath.reflector.reflection.ReflectiveAccessor;
import argonath.reflector.reflection.ReflectiveMutator;
import argonath.reflector.types.builtin.Collections;
import argonath.reflector.types.iterable.IterableType;
import argonath.reflector.types.iterable.IterableTypes;
import argonath.reflector.types.simple.SimpleTypes;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectReader {

    /**
     * Single Value reader:
     * - Assumes the XPath expression is navigates across single-value fields
     * - Fails in case the XPath expression navigates across at least one multi-value field (throws {@code IllegalArgumentException})
     * <p>
     * For Iterable Types:
     * - If the iterable is the last element in the path and the requested class is the same as the iterable class,
     * then return the iterable instance
     * <p>
     * For Maps:
     * - If the map is not the last element in the path, navigation continues one the map value class
     * - If the map is the last element in the path, then:
     * - if a filter expression is not provided, then returns the map instance
     * - if a filter expression is provided, and resolves to a single value, then returns the value directly
     * - if a filter expression is provided, and resolves to multiple values, then returns a Set of values
     * (set of values is the built-in collection that returns from the 'fromCollection' method in the iterable type
     * implementation for Sets)
     */
    public static <T> T get(Object object, String path, Class<T> clazz) {
        ValueSelector selector = ValueSelector.parse(path);
        ReaderContext context = new ReaderContext(selector, object, ReaderContext.ResolutionMode.GET, clazz);
        Object obj = get(object, context);
        return ReflectiveMutator.safeCast(obj, clazz);
    }

    /**
     * Single-Value method that returns the object without casting
     */
    public static Object get(Object object, String path) {
        ValueSelector selector = ValueSelector.parse(path);
        ReaderContext context = new ReaderContext(selector, object, ReaderContext.ResolutionMode.GET, null);
        ResolvedObject resolvedObject = resolve(object, selector.first(), context);
        return resolvedObject.singleObject;
    }

    private static Object get(Object object, ReaderContext context) {
        ResolvedObject resolvedObject = resolve(object, context.selector().first(), context);
        return resolvedObject.singleObject;
    }

    /**
     * Multi Value reader:
     * - Returns a list of the field type pointed by the XPath expression (it is the last element of the expression)
     * - The list type depends on the field type and the strategy used in the reader
     * <p>
     * Note: Regardless the underlying field type, the reader will always return a list of objects, which is populated as following:
     * - if there is multiple paths to a list field, then the reader will flatten a list of lists
     * - if there is a list of single-value fields, then the reader will return a list of single-value fields
     * <p>
     * For Iterable Types:
     * - If the iterable is the last element in the path and the requested class is the same as the iterable class,
     * then return a list of elements of the iterable instance
     * <p>
     * For Maps:
     * - If the map is not the last element in the path, navigation continues one the map value class
     * - If the map is the last element in the path, then always returns a list of map values
     */
    public static <T> List<T> list(Object object, String path, Class<T> clazz) {
        ValueSelector selector = ValueSelector.parse(path);
        ReaderContext context = new ReaderContext(selector, object, ReaderContext.ResolutionMode.LIST, clazz);
        List<Object> objList = list(object, context);
        return ReflectiveMutator.safeCast(objList, clazz);
    }

    /**
     * Multi-Value method that returns the list of objects without casting
     * <p>
     * Note: Regardless the underlying field type, the reader will always return a list of objects, which is populated as following:
     * - if there is multiple paths to a list field, then the reader will flatten a list of lists
     * - if there is a list of single-value fields, then the reader will return a list of single-value fields
     */
    public static List<Object> list(Object object, String path) {
        ValueSelector selector = ValueSelector.parse(path);
        ReaderContext context = new ReaderContext(selector, object, ReaderContext.ResolutionMode.LIST, Object.class);
        return list(object, context);
    }

    private static List<Object> list(Object object, ReaderContext context) {
        ResolvedObject resolvedObjectList = resolve(object, context.selector().first(), context);
        return resolvedObjectList.objectList;
    }

    /*
        -- PSEUDO CODE FOR RESOLUTION PROCESS --

            (a) RESOLUTION_PROCESS:
                INPUT: Object O, XPathElement X
                OUTPUT: ResolvedObject (single- or multi-value)
                STEPS:
                    1. if(X.empty) => RETURN: O 									(base case)
                    2. EO = resolve(O, X)											(resolve object in path using Accessor)
                    3. if(EO is ITERABLE TYPE) => RETURN LIST_RESOLUTION_PROCESS(O,X)	(invoke ITERATION_PROCESS)
                    4. EO' = ValueMapper(EO, X)										(apply ValueMapper)
                    5. if (EO' is FINAL TYPE) => RETURN EO'							(final type: no further processing, return current value)
                    6. else: RETURN RESOLUTION_PROCESS(EO', X.next)					(non-final type: recurse to next XPathElement)

            (b) LIST_RESOLUTION_PROCESS:
                INPUT: ResolvedObject O, XPathElement X
                OUTPUT: ResolvedObject (single- or multi-value)
                STEPS:
                    1. C(EO) = IterableType.ListOfIterable(O)       				(list a collection of Iterable items)
                    2. C'(EO) = filter(C, X)  										(filter according to X.instanceSelector)
                    3. For EO(i) in C':												(iterate over filtered list)
                        - EO'(i) = RESOLUTION_PROCESS(EO'(i), X.next)				(recurse to next XPathElement)
                    4. return FLATTEN(EO''(i))										(convert list of lists to single list)
     */

    private static ResolvedObject resolve(Object object, ValueSelectorItem pathElement, ReaderContext context) {
        // Base case: empty path element (when reached the last level of the path)
        if (pathElement == null) {
            return wrapResolvedObject(object, context);
        }

        // Resolve current element Object instance
        Object resolvedObject = pathElement.extract(object);
        if (resolvedObject == null) {
            return wrapResolvedObject(null, context);
        }

        // Multi-value object (instance of Iterable type): apply list resolution process
        if (IterableTypes.isIterableType(resolvedObject)) {
            IterableType iterableType = IterableTypes.iterableType(resolvedObject);
            Field field = ReflectiveAccessor.getField(pathElement.fieldName(), object.getClass());
            return resolveIterable(resolvedObject, field.getType(), pathElement, context, iterableType);
        }

        // Single-value object: apply value mapper
        resolvedObject = pathElement.valueMapper().apply(resolvedObject);

        // Simple types are final types: no further exploration
        if (SimpleTypes.isSimpleType(resolvedObject)) {
            return wrapResolvedObject(resolvedObject, context);
        }

        // Non-Simple types: continue object traversal until base case
        return resolve(resolvedObject, pathElement.next(), context);
    }

    private static ResolvedObject resolveIterable(Object object, Class<?> iterableClassType, ValueSelectorItem pathElement, ReaderContext context, IterableType iterableType) {

        // this method is only called for multi-value mode, so there is no need to check for single object results
        Collection<Object> elements = IterableTypes.asCollection(object, iterableClassType);

        // apply filter
        Collection<Object> filteredElements = Filters.apply(pathElement.filter(), pathElement.valueMapper(), elements, context, iterableType);

        // in GET mode, if:
        //  - the iterable is the last element in the path
        //  - the requested class is the same as the iterable class
        // Then return the iterable instance: thus, we can always access the iterable instance directly in GET mode
        if (context.resolutionMode().isGet()
                && pathElement.next() == null) {
            return resolveSingleObjectFromIterable(filteredElements, iterableType, iterableClassType, context);
        }

        // Accumulator of elements is always a List, no matter the underlying iterable type collection type
        List<ResolvedObject> accumulator = filteredElements.stream()
                .map(item -> resolve(item, pathElement.next(), context)) // recursively resolve the nested elements
                .collect(Collectors.toList());

        ResolvedObject result = flatten(accumulator, context);
        return result;
    }

    private static ResolvedObject resolveSingleObjectFromIterable(Collection<Object> collection, IterableType iterableType, Class<?> iterableClassType, ReaderContext ctx) {
        if (collection.size() == 1) {
            Object singleElement = collection.iterator().next();
            if (isAssignable(singleElement, ctx)) {
                return ResolvedObject.singleObject(singleElement, ctx);
            }
        }

        // in case of multiple results, return a list with the collected results
        Object ret = iterableType.fromCollection(collection, iterableClassType);

        Class<?> reqClass = ctx.requestedClass();

        if (!IterableTypes.isIterableType(reqClass) && ctx.castResult()) {
            // throw a more intuitive message in case of miss-use of the reader
            throw new IllegalArgumentException("Non-iterable result requested when multi-values are resolved");
        }

        // try to match the wrapper collection
        if (!isAssignable(ret, ctx)) {
            throw new IllegalArgumentException("Requested class (" + reqClass + ") is not assignable from the resolved object class (" + collection.getClass() + ")");
        }

        return ResolvedObject.singleObject(ret, ctx);
    }

    private static ResolvedObject wrapResolvedObject(Object object, ReaderContext context) {
        // if object is null, return null or empty list depending on the resolution mode
        ReaderContext.ResolutionMode mode = context.resolutionMode();

        // handle 'null' object:
        if (object == null) {
            return mode.isGet() ?
                    ResolvedObject.singleObject(null, context) :
                    ResolvedObject.objectList(Collections.emptyList(Object.class));
        }

        // if object is not list
        if (!ReflectiveAccessor.isCollection(object)) {
            return ResolvedObject.singleObject(object, context);
        }

        // Cast to List to process further
        Collection<Object> collection = Collections.asCollection(object, Object.class);

        // if object is list and requested GET mode, return the list as resolved object
        if (mode == ReaderContext.ResolutionMode.GET) {
            ResolvedObject resolvedObject = resolveSingleObjectFromIterable(collection, IterableTypes.iterableType(object), object.getClass(), context);
            return resolvedObject;
        }

        List<Object> list = Collections.asList(object, Object.class);

        // return the resolved object as list
        if (list.isEmpty()) {
            list = Collections.emptyList(Object.class);
        }
        return ResolvedObject.objectList(list);
    }

    private static boolean isAssignable(Object object, ReaderContext ctx) {
        if (object == null) {
            return true;
        }
        return ctx.requestedClass().isAssignableFrom(object.getClass());
    }

    // static helper Record Class to encapsulate the resolved result and handle get vs. list mode
    private record ResolvedObject(Object singleObject, List<Object> objectList) {

        static ResolvedObject singleObject(Object singleObject, ReaderContext ctx) {
            return new ResolvedObject(singleObject, null);
        }


        static ResolvedObject objectList(List<Object> objectList) {
            return new ResolvedObject(null, objectList);
        }

        // as list: if single object: list of single object else list
        static List<Object> asList(ResolvedObject object) {
            if (object.singleObject != null) {
                return List.of(object.singleObject);
            }
            return object.objectList;
        }

    }

    private static ResolvedObject flatten(List<ResolvedObject> accumulator, ReaderContext ctx) {
        return wrapResolvedObject(accumulator.stream()
                .map(ResolvedObject::asList)
                .flatMap(List::stream)
                .collect(Collectors.toList()), ctx);
    }

}
