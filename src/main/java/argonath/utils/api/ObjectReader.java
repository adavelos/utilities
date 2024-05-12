package argonath.utils.api;

import argonath.utils.reflection.ReflectiveAccessor;
import argonath.utils.reflection.ReflectiveFactory;
import argonath.utils.reflection.ReflectiveMutator;
import argonath.utils.xpath.model.XPath;

import java.util.Collection;
import java.util.List;

public class ObjectReader {

    // strategy: to be encapsulates into a different class
    private ObjectReaderStrategy strategy;

    // constructor with strategy
    public ObjectReader(ObjectReaderStrategy strategy) {
        this.strategy = strategy;
    }

    // static initializer with default strategy
    public static ObjectReader create() {
        return new ObjectReader(ObjectReaderStrategy.DEFAULT);
    }

    // static initializer with custom strategy
    public static ObjectReader create(ObjectReaderStrategy strategy) {
        return new ObjectReader(strategy);
    }

    /**
     * Single Value reader:
     * - Assumes the XPath expression is navigates across single-value fields
     * - Fails in case the XPath expression navigates across at least one multi-value field (throws {@code IllegalArgumentException})
     */
    public <T> T get(Object object, String path, Class<T> clazz) {
        XPath xPath = XPath.parse(path);
        return get(object, xPath, clazz);
    }

    private <T> T get(Object object, XPath xPath, Class<T> clazz) {
        ExtractedObject extractedObject = extract(object, xPath, ExtractMode.GET);
        return ReflectiveMutator.safeCast(extractedObject.singleObject, clazz);
    }


    /**
     * Multi Value reader:
     * - Returns a list of the field type pointed by the XPath expression (it is the last element of the expression)
     * - Default list type is {@code ArrayList} (unless different strategy specified)
     */
    public <T> List<T> list(Object object, String path, Class<T> clazz) {
        XPath xPath = XPath.parse(path);
        ExtractedObject extractedObjectList = extract(object, xPath, ExtractMode.LIST);
        return ReflectiveMutator.safeCast(extractedObjectList.objectList, clazz);
    }

    /*
        The extraction process recursively navigates through the object graph following the XPath expression.
        In order to avoid duplication of the process, the GET/LIST modes are defined to discriminate the behavior of the extraction:
         - GET: returns a single object (or null) and fails in case the object is a list
         - LIST: returns a list of objects (or null) and wraps the single object into a list in case the object is not a list
        The result is wrapped within an 'ExtractedObject' instance in order to handle seamlessly the different extraction modes.

        All the extracted fields are stored to Object instances, and it is left to the caller method to safely cast
        to the given class type.

        There are two types of accessed fields:
            - Navigable fields, for which navigation continues further
            - Non-Navigable fields, which are the last fields in the XPath expression and are returned to the caller method
        The Navigable fields are:
            - Custom Classes
            - The following Collections containing non-Navigable fields:
                - List
                - Set
                - Map
            - Arrays of Navigable fields
        The Non-Navigable fields are:
            - Primitive Types (int, long, double, String, etc.)
            - Wrapper Types (Integer, Long, Double, etc.)
            - Enumerations
            - Arrays of Non-Navigable fields
            - String
            - Date/Time API (LocalDate, LocalDateTime, OffsetDateTime)
            - Non-Navigable Collections (i.e. Queue, Stack etc.)

        The process always flattens the results of the extraction, so that the final result is a list of objects
        without any nested lists.
     */
    private ExtractedObject extract(Object object, XPath xPath, ExtractMode mode) {
        // Base case: empty XPath (when reached the last level of the path)
        if (xPath.isEmpty()) {
            return extractedObject(object, xPath, mode);
        }

        // Get the next level element
        XPath nextPath = xPath.subpath(1);
        XPath curPath = xPath.top();

        Object extractedObject = curPath.singleElement().apply(object);
        if (extractedObject == null) {
            return extractedObject(null, xPath, mode);
        }

        // in case the selector returns a List instance
        if (ReflectiveAccessor.isNavigableCollection(extractedObject)) {
            // create a Collection of the Navigable type
            Collection<?> extractedObjectAsList = ReflectiveAccessor.asCollection(extractedObject);

            // if the type is non-navigable, then the current path should be the last element of the expression
            if (ReflectiveAccessor.isCollectionOfSimpleType(extractedObjectAsList)) {
                return extractedObject(extractedObjectAsList, xPath, mode);
            }

            // otherwise, the navigation continues for each one of the elements of the list, accumulating results to a list
            return extractList(extractedObjectAsList, nextPath, mode);
        } else {
            // for simple types the navigation continues to the nextPath level, until the base case is reached
            return extract(extractedObject, nextPath, mode);
        }
    }

    private ExtractedObject extractList(Collection<?> list, XPath xPath, ExtractMode mode) {
        // this method is only called for LIST mode, so there is no need to check for single object results
        List<Object> extractedList = createList();
        list.forEach(cur -> {
            ExtractedObject extractedObject = extract(cur, xPath, mode);
            extractedList.addAll(extractedObject.asList(strategy));
        });
        return extractedObject(extractedList, xPath, mode);
    }

    // Internal Methods to Cast & Return safely
    private ExtractedObject extractedObject(Object object, XPath xPath, ExtractMode mode) {
        // if object is null, return null or empty list depending on the extraction mode
        if (object == null && mode == ExtractMode.GET) {
            return new ExtractedObject(null);
        } else if (object == null && mode == ExtractMode.LIST) {
            return new ExtractedObject(createList());
        }
        // if object is not list or if object is list and clazz is list
        if (!ReflectiveAccessor.isList(object)) {
            return new ExtractedObject(object);
        }

        // Cast to List to process further
        List<Object> list = (List<Object>) object;

        // if object is list and requested GET mode, return the list as extracted object
        if (mode == ExtractMode.GET) {
            // if more than one element in the list, throw exception
            if (list.size() > 1) {
                throw new IllegalArgumentException("XPath expression: " + xPath + " navigates across multiple elements");
            }
            return new ExtractedObject(object);
        }
        // return the extracted object as list
        if (list.isEmpty()) {
            list = createList();
        }
        return new ExtractedObject(list);
    }

    /* Instantiate a new mutable list of the type according to the strategy */
    private List<Object> createList() {
        return createList(strategy);
    }

    // overloaded static method to createList from strategy
    private static List<Object> createList(ObjectReaderStrategy strategy) {
        return ReflectiveFactory.instantiate(strategy.listType());
    }

    private enum ExtractMode {LIST, GET}

    private static record ExtractedObject(Object singleObject, List<Object> objectList) {
        public ExtractedObject(Object singleObject) {
            this(singleObject, null);
        }

        public ExtractedObject(List<Object> objectList) {
            this(null, objectList);
        }

        public List<Object> asList(ObjectReaderStrategy strategy) {
            // records are static by default, so the strategy must be passed as an argument
            List<Object> list = createList(strategy);
            if (singleObject != null) {
                list.add(singleObject);
            }
            if (objectList != null) {
                list.addAll(objectList);
            }
            return list;
        }
    }

}
