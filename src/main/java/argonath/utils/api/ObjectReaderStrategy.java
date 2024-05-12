package argonath.utils.api;

import java.util.ArrayList;
import java.util.List;

public class ObjectReaderStrategy {

    // static DEFAULT
    static ObjectReaderStrategy DEFAULT = new ObjectReaderStrategy(ArrayList.class);

    private Class<? extends List> defaultListType;

    ObjectReaderStrategy(Class<? extends List> defaultListType) {
        this.defaultListType = defaultListType;
    }

    // TODO: read strategy from configuration file (properties or yaml)

    public Class<? extends List> listType() {
        return defaultListType;
    }
}
