package org.apache.ivy.util;

import java.io.File;

public interface FileResolver {
    public static final FileResolver DEFAULT = new FileResolver() {
        public File resolveFile(String path, String filename) {
            return new File(path);
        }
    };
    
    /**
     * Return the canonical form of a path, or raise an {@link IllegalArgumentException} if the path
     * is not valid for this {@link FileResolver}.
     * <p>
     * 
     * @param path
     *            The path of the file to resolve. Must not be <code>null</code>.
     * @param fileName
     *            The name of the file to resolve. This is used only for exception message if any.
     *            Must not be <code>null</code>.
     * 
     * @return the resolved File.
     * 
     */
    File resolveFile(String path, String filename);
}
