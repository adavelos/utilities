package argonath.reflector.generator.model;

public enum Optionality {
    NONE, OPTIONAL, MANDATORY;

    public static Optionality defaultValue() {
        return MANDATORY;
    }
}
