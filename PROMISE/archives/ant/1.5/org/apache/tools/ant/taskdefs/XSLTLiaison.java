package org.apache.tools.ant.taskdefs;

import java.io.File;

/**
 * Proxy interface for XSLT processors.
 *
 * @author <a href="mailto:rubys@us.ibm.com">Sam Ruby</a>
 * @author <a href="mailto:sbailliez@apache.org">Stephane Bailliez</a>
 * @see XSLTProcess
 * @since Ant 1.1
 */
public interface XSLTLiaison {

    /**
     * the file protocol prefix for systemid.
     * This file protocol must be appended to an absolute path.
     * Typically: <tt>FILE_PROTOCOL_PREFIX + file.getAbsolutePath()</tt>
     * Note that on Windows, an extra '/' must be appended to the
     * protocol prefix so that there is always 3 consecutive slashes.
     * @since Ant 1.4
     */

    /**
     * set the stylesheet to use for the transformation.
     * @param stylesheet the stylesheet to be used for transformation.
     * @since Ant 1.4
     */
    void setStylesheet(File stylesheet) throws Exception;

    /**
     * Add a parameter to be set during the XSL transformation.
     * @param name the parameter name.
     * @param expression the parameter value as an expression string.
     * @throws Exception thrown if any problems happens.
     * @since Ant 1.3
     */
    void addParam(String name, String expression) throws Exception;

    /**
     * Perform the transformation of a file into another.
     * @param infile the input file, probably an XML one. :-)
     * @param outfile the output file resulting from the transformation
     * @throws Exception thrown if any problems happens.
     * @see #setStylesheet(File)
     * @since Ant 1.4
     */
    void transform(File infile, File outfile) throws Exception;

