package org.apache.tools.ant.taskdefs.optional.jsp;

import java.io.File;

/**
 * this class implements the name mangling rules of the jasper in tomcat4.1.x
 * which is likely to remain for some time
 * @see "org.apache.jasper.JspCompilationContext"
 */
public class Jasper41Mangler implements JspMangler {


    /**
     * map from a jsp file to a java filename; does not do packages
     *
     * @param jspFile file
     * @return java filename
     */
    public String mapJspToJavaName(File jspFile) {
        String jspUri = jspFile.getAbsolutePath();
        int start = jspUri.lastIndexOf(File.separatorChar) + 1;
        int end = jspUri.length();
        StringBuffer modifiedClassName;
        modifiedClassName = new StringBuffer(jspUri.length() - start);
        if (!Character.isJavaIdentifierStart(jspUri.charAt(start))
            || jspUri.charAt(start) == '_') {
            modifiedClassName.append('_');
        }
        for (int i = start; i < end; i++) {
            char ch = jspUri.charAt(i);
            if (Character.isJavaIdentifierPart(ch)) {
                modifiedClassName.append(ch);
            } else if (ch == '.') {
                modifiedClassName.append('_');
            } else {
                modifiedClassName.append(mangleChar(ch));
            }
        }
        return modifiedClassName.toString();
    }

    /**
     * Mangle the specified character to create a legal Java class name.
     */
    private static String mangleChar(char ch) {
        String s = Integer.toHexString(ch);
        int nzeros = 5 - s.length();
        char[] result = new char[6];
        result[0] = '_';
        for (int i = 1; i <= nzeros; i++) {
            result[i] = '0';
        }
        for (int i = nzeros + 1, j = 0; i < 6; i++, j++) {
            result[i] = s.charAt(j);
        }
        return new String(result);
    }


    /**
     * taking in the substring representing the path relative to the source dir
     * return a new string representing the destination path
     * @param path not used.
     * @return null as this is not implemented.
     * @todo
     */
    public String mapPath(String path) {
        return null;
    }

}
