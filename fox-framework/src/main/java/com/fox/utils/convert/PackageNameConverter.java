package com.fox.utils.convert;

/**
 * 包名转换器
 */
public class PackageNameConverter {
    public static String convert(String originalPackage, String targetPackage, String className) {
        if (className.startsWith(originalPackage)) {
            return className.replaceFirst(originalPackage, targetPackage);
        }
        return className;
    }
}