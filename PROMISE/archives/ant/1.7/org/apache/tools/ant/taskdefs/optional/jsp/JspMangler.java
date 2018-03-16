package org.apache.tools.ant.taskdefs.optional.jsp;

import java.io.File;

/**
 * This is an interface to the Mangler service that jspc needs to map
 * JSP file names to java files.
 * Note the complete lack of correlation
 * with Jasper's mangler interface.
 */
public interface JspMangler {


    /**
     * map from a jsp file to a java filename; does not do packages
     *
     * @param jspFile file
     * @return java filename
     */
    String mapJspToJavaName(File jspFile);

    /**
     * taking in the substring representing the path relative to the source dir
     * return a new string representing the destination path
     * @param path the path to map.
     * @return the mapped path.
     */
    String mapPath(String path);

}
