package argonath.reflector.reflection;

import java.lang.reflect.*;
import java.util.List;

public class TypeExplorer {
    private TypeExplorer() {
    }

    public static Type type(Type type) {
        return ((ParameterizedType) type).getActualTypeArguments()[0];
    }

    public static Class<?> elementType(Class<?> genericClass) {
        return (Class<?>) type(genericClass);
    }

    public static boolean isArray(Type type) {
        return type instanceof Class<?> && ((Class<?>) type).isArray();
    }

    public static List<Type> nestedTypes(Type type) {
        if (type instanceof ParameterizedType paramType) {
            return List.of(paramType.getActualTypeArguments());
        } else if (type instanceof Class<?> && ((Class<?>) type).isArray()) {
            return List.of(((Class<?>) type).getComponentType());
        } else if (type instanceof GenericArrayType genType) {
            return List.of(genType.getGenericComponentType());
        } else if (type instanceof WildcardType wcType) {
            return List.of(wcType.getUpperBounds());
        } else if (type instanceof TypeVariable<?> typeVar) {
            List<Type> bounds = List.of(typeVar.getBounds());
            return bounds;
        }
        return List.of();
    }

    public static Class<?> typeClass(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        } else if (type instanceof GenericArrayType) {
            return (Class<?>) ((ParameterizedType) ((GenericArrayType) type).getGenericComponentType()).getRawType();
        } else if (type instanceof WildcardType) {
            return (Class<?>) ((WildcardType) type).getUpperBounds()[0];
        } else if (type instanceof TypeVariable) {
            return (Class<?>) ((TypeVariable<?>) type).getBounds()[0];
        }
        return null;
    }
}