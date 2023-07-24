package com.fox.utils.client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PackageScanner {

    public static List<Class<?>> scanPackage(String packageName) throws ClassNotFoundException, IOException {
        List<Class<?>> classes = new ArrayList<>();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());
                scanClasses(directory, packageName, classes, classLoader);
            }

        return classes;
    }

    private static void scanClasses(File directory, String packageName, List<Class<?>> classes, ClassLoader classLoader)
            throws ClassNotFoundException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String subPackageName = packageName + "." + file.getName();
                    scanClasses(file, subPackageName, classes, classLoader);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                    Class<?> clazz = classLoader.loadClass(className);
                    classes.add(clazz);
                }
            }
        }
    }
}