package org.apache.ivy.util;

import java.io.File;

/**
 * Utility class used to perform some checks.
 */
public final class Checks {
    private Checks() {
    }
    
    /**
     * Checks that an object is not null, and throw an exception if the object is null.
     * 
     * @param o
     *            the object to check
     * @param objectName
     *            the name of the object to check. This name will be used in the exception message.
     * @throws IllegalArgumentException
     *             if the object is null
     */
    public static void checkNotNull(Object o, String objectName) {
        if (o == null) {
            throw new IllegalArgumentException(objectName + " must not be null");
        }
    }

    public static File checkAbsolute(File f, String fileName) {
        checkNotNull(f, fileName);
        if (!f.isAbsolute()) {
            throw new IllegalArgumentException(fileName + " must be absolute: " + f.getPath());
        }
        return f;
    }
    
    public static File checkAbsolute(String path, String fileName) {
        checkNotNull(path, fileName);
        File f = new File(path);
        if (!f.isAbsolute()) {
            throw new IllegalArgumentException(fileName + " must be absolute: " + path);
        }
        return f;
    }
}
