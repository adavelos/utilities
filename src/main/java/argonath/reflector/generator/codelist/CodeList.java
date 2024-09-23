package argonath.reflector.generator.codelist;

import java.util.List;

public class CodeList<T> {

    private String key;
    private List<CodeListItem<T>> items;

    public CodeList(String key, List<CodeListItem<T>> items) {
        this.key = key;
        this.items = items;
    }

    public String key() {
        return key;
    }

    public List<CodeListItem<T>> items() {
        if (items == null) {
            throw new IllegalStateException("Code list items not loaded");
        }
        return items;
    }
}
