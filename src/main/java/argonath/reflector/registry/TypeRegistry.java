package argonath.reflector.registry;

import argonath.reflector.reflection.Primitives;
import argonath.reflector.reflection.ReflectiveAccessor;

import java.util.*;

/**
 * Generic registry that maps a type to a specific value and supports hierarchical lookup.
 * There are two modes for matching the closest type:
 * - if 'upwardsOnly' is true, the registry will only look for the closest ancestor of the type
 * - if 'upwardsOnly' is false, the registry will look for the closest ancestor and if no ancestor is found, it will look for the closest descendant
 * <p>
 * For types that specify behavior, the first mode is used (Simple Type, Iterable, Value Mapper), while for generators, the second mode is used
 */
public class TypeRegistry<T> {

    private Map<Class<?>, T> registry;

    private boolean upwardsOnly;

    // pre-calculated descendants for each type
    private Map<Class<?>, SortedSet<Descendant>> descendants;

    public TypeRegistry(boolean upwardsOnly) {
        this.registry = new HashMap<>();
        this.descendants = new HashMap<>();
        this.upwardsOnly = upwardsOnly;
    }

    public void register(Class<?> type, T value) {
        registry.put(type, value);

        if (this.upwardsOnly) {
            return;
        }

        // For DESCENDING match type, pre-calculate the descendants using inverse logic, as it is not efficient to calculate them on the fly

        // Use a BFS algorithm to discover the ancestors of the type
        Queue<Class<?>> queue = new LinkedList<>();
        queue.offer(type);
        Map<Class<?>, Integer> distances = new HashMap<>();

        while (!queue.isEmpty()) {
            Class<?> current = queue.poll();
            int currentDistance = distances.getOrDefault(current, 0);

            List<Class<?>> children = ReflectiveAccessor.getAncestors(current);
            for (Class<?> child : children) {
                if (distances.containsKey(child) || child == Object.class) {
                    continue; // shorter path already found
                }
                queue.offer(child);
                distances.put(child, currentDistance + 1);
            }
        }

        distances.entrySet().forEach(entry -> {
            Class<?> ancestor = entry.getKey();
            int distance = entry.getValue();
            SortedSet<Descendant> localDescenands = this.descendants.computeIfAbsent(ancestor, k -> new TreeSet<>());
            localDescenands.add(new Descendant(type, distance));
        });
    }

    public T get(Class<?> type) {
        return registry.get(type);
    }

    public T match(Class<?> type) {
        T ret = matchAscending(type);
        if (ret != null || this.upwardsOnly) {
            return ret;
        }
        return matchDescending(type);
    }

    private T matchAscending(Class<?> type) {
        if (type.isPrimitive()) {
            type = Primitives.getWrapperType(type);
        }

        T exactMatch = registry.get(type);
        if (exactMatch != null) {
            return exactMatch;
        }

        Queue<Class<?>> ancestors = new LinkedList<>();
        ancestors.addAll(ReflectiveAccessor.getAncestors(type));

        // Discover the closest ancestor that has a match in the registry, using a BFS algorithm
        while (!ancestors.isEmpty()) {
            Class<?> ancestor = ancestors.poll();
            if (ancestor == Object.class) {
                continue;
            }
            T match = registry.get(ancestor);
            if (match != null) {
                return match;
            }
            ancestors.addAll(ReflectiveAccessor.getAncestors(ancestor));
        }

        return null;
    }

    private T matchDescending(Class<?> type) {
        if (type.isPrimitive()) {
            type = Primitives.getWrapperType(type);
        }

        T exactMatch = registry.get(type);
        if (exactMatch != null) {
            return exactMatch;
        }

        SortedSet<Descendant> localDescendants = this.descendants.get(type);
        if (localDescendants == null || localDescendants.isEmpty()) {
            return null;
        }
        return registry.get(localDescendants.first().descendantClass);
    }

    private class Descendant implements Comparable<Descendant> {
        Class<?> descendantClass;
        int distance;

        public Descendant(Class<?> descendantClass, int distance) {
            this.descendantClass = descendantClass;
            this.distance = distance;
        }

        // Compare based on the distance
        public int compareTo(Descendant other) {
            return this.distance - other.distance;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Descendant that = (Descendant) o;
            return distance == that.distance && Objects.equals(descendantClass, that.descendantClass);
        }

    }

}
