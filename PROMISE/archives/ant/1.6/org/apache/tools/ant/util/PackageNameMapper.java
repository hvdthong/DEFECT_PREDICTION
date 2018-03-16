package org.apache.tools.ant.util;

import java.io.File;

/**
 * Maps directory name matches into a dotted package name. This is
 * useful for matching JUnit test cases againt their XML formatter
 * results.
 * <pre>
 * &lt;mapper classname="org.apache.tools.ant.util.PackageNameMapper"
 *         from="*Test.java" to="${test.data.dir}/TEST-*Test.xml"/&gt;
 * </pre>
 *
 */
public class PackageNameMapper extends GlobPatternMapper {
    /**
     *  Returns the part of the given string that matches the * in the
     *  &quot;from&quot; pattern replacing file separators with dots
     *
     *@param  name  Source filename
     *@return       Replaced variable part
     */
    protected String extractVariablePart(String name) {
        String var = name.substring(prefixLength,
                name.length() - postfixLength);
        return var.replace(File.separatorChar, '.');
    }
}

