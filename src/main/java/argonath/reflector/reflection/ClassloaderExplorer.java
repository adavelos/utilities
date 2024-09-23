package argonath.reflector.reflection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ClassloaderExplorer {
    private ClassloaderExplorer() {
    }

    public static Class<?> findClosestImplementation(Class<?> interfaceClass) {
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("The provided class: " + interfaceClass.getName() + " is not an interface");
        }

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = interfaceClass.getPackage().getName().replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);

            Queue<Class<?>> queue = new LinkedList<>();

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());

                if (!directory.exists()) {
                    continue;
                }

                List<Class<?>> classes = findClasses(directory, interfaceClass.getPackage().getName());
                for (Class<?> clazz : classes) {
                    if (!clazz.equals(interfaceClass)) {
                        queue.offer(clazz);
                    }
                }
            }

            while (!queue.isEmpty()) {
                Class<?> current = queue.poll();
                Class<?> clazz = current;

                // Add superclass to the queue
                Class<?> superClass = clazz.getSuperclass();
                if (superClass != null && superClass != Object.class) {
                    queue.offer(superClass);
                }

                for (Class<?> iface : clazz.getInterfaces()) {
                    if (iface.equals(interfaceClass)) {
                        return current; // match found
                    }
                    queue.offer(iface);
                }
            }
        } catch (IOException |
                 ClassNotFoundException e) {
            throw new IllegalArgumentException("Unexpected error while exploring classloader", e);
        }

        return null; // No implementation found
    }

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }

}