package argonath.reflector.reader.selector;


class ValueMapperParser {
    private ValueMapperParser() {
    }
    public static ValueMapper parse(String attrMapperStr) {
        ValueMapper valueMapper = ValueMappers.get(attrMapperStr);
        if (valueMapper == null) {
            throw new IllegalArgumentException("ValueMapper not found for key: " + attrMapperStr);
        }
        return valueMapper;
    }
}
