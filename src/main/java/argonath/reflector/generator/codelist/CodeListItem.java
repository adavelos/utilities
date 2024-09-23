package argonath.reflector.generator.codelist;

import java.util.Objects;

public class CodeListItem<T> {
    private T code;
    private String description;

    public CodeListItem(T code, String description) {
        this.code = code;
        this.description = description;
    }

    public T getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeListItem<T> that = (CodeListItem<T>) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
