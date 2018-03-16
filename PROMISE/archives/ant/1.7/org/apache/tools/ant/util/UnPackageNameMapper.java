package org.apache.tools.ant.util;

import java.io.File;

/**
 * Maps dotted package name matches to a directory name.
 * This is the inverse of the package mapper.
 * This is useful for matching XML formatter results against their JUnit test
 * cases.
 * <pre>
 * &lt;mapper classname="org.apache.tools.ant.util.UnPackageNameMapper"
 *         from="${test.data.dir}/TEST-*Test.xml" to="*Test.java"&gt;
 * </pre>
 *
 *
 */
public class UnPackageNameMapper extends GlobPatternMapper {
    /**
     *  Returns the part of the given string that matches the * in the
     *  &quot;from&quot; pattern replacing dots with file separators
     *
     *@param  name  Source filename
     *@return       Replaced variable part
     */
    protected String extractVariablePart(String name) {
        String var = name.substring(prefixLength,
                name.length() - postfixLength);
        return var.replace('.', File.separatorChar);
    }
}

