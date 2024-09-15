package argonath.reflector.generator.model;

public enum Optionality {
    NEVER, OPTIONAL, MANDATORY;

    public static Optionality defaultValue() {
        return MANDATORY;
    }
}
