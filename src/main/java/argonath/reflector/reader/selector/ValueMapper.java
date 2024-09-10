package argonath.reflector.reader.selector;

/**
 * The Value Mapper allows mapping of resolved objects to specific values.
 * <p>
 * The 'ValuesMappers' class contains a registry with built-in Value Mappers.
 * The 'BuiltInValueMappers' class provides two built-in ValueMapper instances: MAP_KEY_VM and MAP_VALUE_VM.
 * These instances are used to extract keys and values from Map.Entry objects, respectively.
 * <p>
 * The ValueMapper interface defines a single method apply that takes an Object as input and returns an Object.
 * This method is used to transform the input object in some way.
 * <p>
 * The ValueMapperParser class provides a parse method that retrieves a ValueMapper instance from the ValuesMappers map by its key.
 * If no ValueMapper is found for the given key, an IllegalArgumentException is thrown.
 * <p>
 * The ObjectReader class uses the ValueMapper functionality to transform the resolved object.
 * - single type resolution, just applies the value mapper on the resolved object
 * - in iterable types ('Filters.apply()'), the value mapper is applied to each element of the collection.
 * - in maps, value mapper is applied to extract the map VALUE when no value mapper is explicitly provided.
 **/
public interface ValueMapper {

    Object apply(Object input);
}
