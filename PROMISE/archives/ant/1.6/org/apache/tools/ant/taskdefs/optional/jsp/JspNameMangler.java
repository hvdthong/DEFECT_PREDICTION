package org.apache.tools.ant.taskdefs.optional.jsp;
import java.io.File;

/**
 * This is a class derived from the Jasper code
 * (org.apache.jasper.compiler.CommandLineCompiler) to map from a JSP filename
 * to a valid Java classname.
 *
 */
public class JspNameMangler implements JspMangler {

    /**
     * this is the list of keywords which can not be used as classnames
     */
    public static final String[] keywords = {
            "assert",
            "abstract", "boolean", "break", "byte",
            "case", "catch", "char", "class",
            "const", "continue", "default", "do",
            "double", "else", "extends", "final",
            "finally", "float", "for", "goto",
            "if", "implements", "import",
            "instanceof", "int", "interface",
            "long", "native", "new", "package",
            "private", "protected", "public",
            "return", "short", "static", "super",
            "switch", "synchronized", "this",
            "throw", "throws", "transient",
            "try", "void", "volatile", "while"
            };


    /**
     * map from a jsp file to a java filename; does not do packages
     *
     * @param jspFile file
     * @return java filename
     */
    public String mapJspToJavaName(File jspFile) {
        return mapJspToBaseName(jspFile) + ".java";
    }


    /**
     * map from a jsp file to a base name; does not deal with extensions
     *
     * @param jspFile jspFile file
     * @return exensionless potentially remapped name
     */
    private String mapJspToBaseName(File jspFile) {
        String className;
        className = stripExtension(jspFile);

        for (int i = 0; i < keywords.length; ++i) {
            if (className.equals(keywords[i])) {
                className += "%";
                break;
            }
        }

        StringBuffer modifiedClassName = new StringBuffer(className.length());
        char firstChar = className.charAt(0);
        if (Character.isJavaIdentifierStart(firstChar)) {
            modifiedClassName.append(firstChar);
        } else {
            modifiedClassName.append(mangleChar(firstChar));
        }
        for (int i = 1; i < className.length(); i++) {
            char subChar = className.charAt(i);
            if (Character.isJavaIdentifierPart(subChar)) {
                modifiedClassName.append(subChar);
            } else {
                modifiedClassName.append(mangleChar(subChar));
            }
        }
        return modifiedClassName.toString();
    }


    /**
     * get short filename from file
     *
     * @param jspFile file in
     * @return file without any jsp extension
     */
    private String stripExtension(File jspFile) {
        String className;
        String filename = jspFile.getName();
        if (filename.endsWith(".jsp")) {
            className = filename.substring(0, filename.length() - 4);
        } else {
            className = filename;
        }
        return className;
    }


    /**
     * definition of the char escaping algorithm
     *
     * @param ch char to mangle
     * @return mangled string; 5 digit hex value
     */
    private static final String mangleChar(char ch) {

        if (ch == File.separatorChar) {
            ch = '/';
        }
        String s = Integer.toHexString(ch);
        int nzeros = 5 - s.length();
        char[] result = new char[6];
        result[0] = '_';
        for (int i = 1; i <= nzeros; ++i) {
            result[i] = '0';
        }
        int resultIndex = 0;
        for (int i = nzeros + 1; i < 6; ++i) {
            result[i] = s.charAt(resultIndex++);
        }
        return new String(result);
    }

    /**
     * taking in the substring representing the path relative to the source dir
     * return a new string representing the destination path
     * not supported, as jasper in tomcat4.0 doesnt either
     */
    public String mapPath(String path) {
        return null;
    }
}

