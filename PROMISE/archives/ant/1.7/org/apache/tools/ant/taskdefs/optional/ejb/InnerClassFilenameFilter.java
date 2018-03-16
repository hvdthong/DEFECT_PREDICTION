package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.File;
import java.io.FilenameFilter;

/**
 * A filename filter for inner class files of a particular class.
 */
public class InnerClassFilenameFilter implements FilenameFilter {
    private String baseClassName;

    /**
     * Constructor of filter.
     * @param baseclass the class to filter inner classes on.
     */
    InnerClassFilenameFilter(String baseclass) {
        int extidx = baseclass.lastIndexOf(".class");
        if (extidx == -1) {
            extidx = baseclass.length() - 1;
        }
        baseClassName = baseclass.substring(0, extidx);
    }

    /**
     * Check if the file name passes the filter.
     * @param dir not used.
     * @param filename the filename to filter on.
     * @return true if the filename is an inner class of the base class.
     */
    public boolean accept(File dir, String filename) {
        if ((filename.lastIndexOf(".") != filename.lastIndexOf(".class"))
            || (filename.indexOf(baseClassName + "$") != 0)) {
            return false;
        }
        return true;
    }
}
