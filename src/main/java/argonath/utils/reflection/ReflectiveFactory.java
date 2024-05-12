package argonath.utils.reflection;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ReflectiveFactory {

    public static <T> T instantiate(Class<T> clazz) {
        T ret;
        if (ReflectiveAccessor.isList(clazz)) {
            ret = (T) new ArrayList();
        } else if (ReflectiveAccessor.isArray(clazz)) {
            throw new UnsupportedOperationException("Instantiation of Array is not supported");
        } else if (ReflectiveAccessor.isCollection(clazz)) {
            throw new UnsupportedOperationException("Instantiation of any Collection other than List is not supported");
        } else {
            ret = instantiateClassInstance(clazz);
        }
        return ret;
    }

    private static <T> T instantiateClassInstance(Class<T> clazz) {
        T ret;
        try {
            // WARNING: In theory this is very 'unsafe' however if the SPECS are compliant with the domain model, the required object will be properly instantiated
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Unsafe unsafe = (Unsafe) f.get(null);
            ret = (T) unsafe.allocateInstance(clazz);

        } catch (Exception e) {
            throw new RuntimeException("Cannot Instantiate Object of:" + clazz.getName(), e);
        }
        return ret;
    }

    public static <T> T safeInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Instantiate Error - Cannot Instantiate Class:" + clazz, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Security Error - Cannot Instantiate Class:" + clazz, e);
        }
    }


}
