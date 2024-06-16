package argonath.utils.reflector.reader;

import argonath.utils.reflection.ObjectFactoryStrategy;
import argonath.utils.reflection.ReflectiveAccessor;
import argonath.utils.reflection.ReflectiveMutator;
import argonath.utils.reflector.reader.selector.Filter;
import argonath.utils.reflector.reader.selector.Selector;
import argonath.utils.reflector.reader.selector.SelectorItem;
import argonath.utils.reflector.reader.selector.ValueMapper;
import argonath.utils.reflector.reader.types.CollectionUtils;
import argonath.utils.reflector.reader.types.FinalTypes;
import argonath.utils.reflector.reader.types.IterableTypes;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectReader {

    // strategy: to be encapsulates into a different class
    private ObjectFactoryStrategy strategy;

    // constructor with strategy
    public ObjectReader(ObjectFactoryStrategy strategy) {
        this.strategy = strategy;
    }

    // static initializer with default strategy
    public static ObjectReader create() {
        return new ObjectReader(ObjectFactoryStrategy.DEFAULT_STRATEGY);
    }

    // static initializer with custom strategy
    public static ObjectReader create(ObjectFactoryStrategy strategy) {
        return new ObjectReader(strategy);
    }

    /**
     * Single Value reader:
     * - Assumes the XPath expression is navigates across single-value fields
     * - Fails in case the XPath expression navigates across at least one multi-value field (throws {@code IllegalArgumentException})
     */
    public <T> T get(Object object, String path, Class<T> clazz) {
        Object obj = get(object, path);
        return ReflectiveMutator.safeCast(obj, clazz);
    }

    /**
     * Single-Value method that returns the object without casting
     */
    public Object get(Object object, String path) {
        Selector selector = Selector.parse(path);
        Context context = new Context(selector, strategy, object, Context.ExtractMode.GET);
        ExtractedObject extractedObject = extract(object, selector.first(), context);
        return extractedObject.singleObject;
    }


    /**
     * Multi Value reader:
     * - Returns a list of the field type pointed by the XPath expression (it is the last element of the expression)
     * - The list type depends on the field type and the strategy used in the reader
     * <p>
     * Note: Regardless the underlying field type, the reader will always return a list of objects, which is populated as following:
     * - if there is multiple paths to a list field, then the reader will flatten a list of lists
     * - if there is a list of single-value fields, then the reader will return a list of single-value fields
     */
    public <T> Collection<T> list(Object object, String path, Class<T> clazz) {
        Collection<Object> objList = list(object, path);
        return ReflectiveMutator.safeCast(objList, clazz);
    }

    /**
     * Multi-Value method that returns the list of objects without casting
     * <p>
     * Note: Regardless the underlying field type, the reader will always return a list of objects, which is populated as following:
     * - if there is multiple paths to a list field, then the reader will flatten a list of lists
     * - if there is a list of single-value fields, then the reader will return a list of single-value fields
     */
    public Collection<Object> list(Object object, String path) {
        Selector selector = Selector.parse(path);
        Context context = new Context(selector, strategy, object, Context.ExtractMode.LIST);
        ExtractedObject extractedObjectList = extract(object, selector.first(), context);
        return extractedObjectList.objectList;
    }

    /*
        -- PSEUDO CODE FOR EXTRACTION PROCESS --

            (a) EXTRACTION_PROCESS:
                INPUT: Object O, XPathElement X
                OUTPUT: ExtractedObject (single- or multi-value)
                STEPS:
                    1. if(X.empty) => RETURN: O 									(base case)
                    2. EO = extract(O, X)											(extract object in path using Accessor)
                    3. if(EO is ITERABLE TYPE) => RETURN LIST_EXTRACTION_PROCESS(O,X)	(invoke ITERATION_PROCESS)
                    4. EO' = ValueMapper(EO, X)										(apply ValueMapper)
                    5. if (EO' is FINAL TYPE) => RETURN EO'							(final type: no further processing, return current value)
                    6. else: RETURN EXTRACTION_PROCESS(EO', X.next)					(non-final type: recurse to next XPathElement)

            (b) LIST_EXTRACTION_PROCESS:
                INPUT: ExtractedObject O, XPathElement X
                OUTPUT: ExtractedObject (single- or multi-value)
                STEPS:
                    1. C(EO) = IterableType.ListOfIterable(O)       				(list a collection of Iterable items)
                    2. C'(EO) = filter(C, X)  										(filter according to X.instanceSelector)
                    3. For EO(i) in C':												(iterate over filtered list)
                        - EO'(i) = EXTRACTION_PROCESS(EO'(i), X.next)				(recurse to next XPathElement)
                    4. return FLATTEN(EO''(i))										(convert list of lists to single list)
     */

    private ExtractedObject extract(Object object, SelectorItem pathElement, Context context) {
        // Base case: empty path element (when reached the last level of the path)
        if (pathElement == null) {
            return extractedObject(object, context);
        }

        // Extract current element Object instance
        Object extractedObject = pathElement.extract(object);
        if (extractedObject == null) {
            return extractedObject(null, context);
        }

        // in case the pathElement returns a List instance
        if (IterableTypes.isIterableType(extractedObject)) {
            return extractList(extractedObject, pathElement, context);
        }

        // apply value mapper
        extractedObject = ValueMapper.apply(extractedObject, pathElement.valueMapper());

        // if the current object is a simple type return
        if (FinalTypes.isFinalType(extractedObject)) {
            return extractedObject(extractedObject, context);
        }

        // for simple types the navigation continues to the nextPath level, until the base case is reached
        return extract(extractedObject, pathElement.next(), context);
    }

    private ExtractedObject extractList(Object object, SelectorItem pathElement, Context context) {

        // this method is only called for multi-value mode, so there is no need to check for single object results
        Collection<Object> elements = IterableTypes.asCollection(object, strategy);

        Collection<?> filteredElements = Filter.applyFilter(pathElement.filter(), elements, context);

        // Accumulator of elements is always a List, no matter the underlying iterable type collection type
        List<ExtractedObject> accumulator = filteredElements.stream()
                .map(item -> extract(item, pathElement.next(), context))
                .collect(Collectors.toList());

        ExtractedObject result = flatten(accumulator, context);
        return result;
    }

    private ExtractedObject extractedObject(Object object, Context context) {
        // if object is null, return null or empty list depending on the extraction mode
        Context.ExtractMode mode = context.extractMode();

        if (object == null && mode.isGet()) {
            return ExtractedObject.singleObject(null);
        } else if (object == null && mode.isList()) {
            return ExtractedObject.objectList(CollectionUtils.emptyList(strategy));
        }

        // if object is not list or if object is list and clazz is list
        if (!ReflectiveAccessor.isList(object)) {
            return ExtractedObject.singleObject(object);
        }

        // Cast to List to process further
        List<Object> list = (List<Object>) object;

        // if object is list and requested GET mode, return the list as extracted object
        if (mode == Context.ExtractMode.GET) {
            // if list size = 1 return the single object
            if (list.size() == 1) {
                return ExtractedObject.singleObject(list.get(0));
            }
            return ExtractedObject.singleObject(list);
        }

        // return the extracted object as list
        if (list.isEmpty()) {
            list = CollectionUtils.emptyList(strategy);
        }
        return ExtractedObject.objectList(list);
    }

    // static helper Record Class to encapsulate the extraction result and handle get vs. list mode
    private static record ExtractedObject(Object singleObject, List<Object> objectList) {

        static ExtractedObject singleObject(Object singleObject) {
            return new ExtractedObject(singleObject, null);
        }

        static ExtractedObject objectList(List<Object> objectList) {
            return new ExtractedObject(null, objectList);
        }

        // as list: if single object: list of single object else list
        static List<Object> asList(ExtractedObject object) {
            if (object.singleObject != null) {
                return List.of(object.singleObject);
            }
            return object.objectList;
        }

    }

    private ExtractedObject flatten(List<ExtractedObject> accumulator, Context ctx) {
        // Flatten the list of lists
        List<Object> objList = accumulator.stream()
                .map(ExtractedObject::asList)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return extractedObject(objList, ctx);
    }

}
