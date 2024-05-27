package argonath.utils.reflector.reader.filter;

public class Filters {
    public static Filter of(String filterExpression) {
        return (elements, context) -> elements;
    }
}
