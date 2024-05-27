package argonath.utils.reflector.reader.mapper;

public class ValueMappers {
    public static ValueMapper of(String valueMapper) {
        return (extractedObject, object) -> extractedObject;
    }


}
