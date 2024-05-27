package argonath.utils.reflection;

import java.util.*;

public class ObjectFactoryStrategy {

    // DEFAULT Collection Types
    private static final Class<? extends Set> DEFAULT_SET = HashSet.class;
    private static final Class<? extends List> DEFAULT_LIST = ArrayList.class;
    private static final Class<? extends Map> DEFAULT_MAP = HashMap.class;

    // default strategy: use DEFAULT_XXX
    public static final ObjectFactoryStrategy DEFAULT_STRATEGY = new ObjectFactoryStrategy(DEFAULT_SET, DEFAULT_LIST, DEFAULT_MAP);

    private Class<? extends Set> defaultSetClass;
    private Class<? extends List> defaultListClass;
    private Class<? extends Map> defaultMapClass;

    public ObjectFactoryStrategy(Class<? extends Set> defaultSetClass, Class<? extends List> defaultListClass, Class<? extends Map> defaultMapClass) {
        this.defaultSetClass = defaultSetClass;
        this.defaultListClass = defaultListClass;
        this.defaultMapClass = defaultMapClass;
    }

    public Class<? extends Set> defaultSetClass() {
        return defaultSetClass;
    }

    public Class<? extends List> defaultListClass() {
        return defaultListClass;
    }

    public Class<?> defaultMapClass() {
        return defaultMapClass;
    }

}
