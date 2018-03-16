package org.apache.tools.ant.filters.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.ConstantValue;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;

/**
 * Helper class that filters constants from a Java Class
 *
 */
public final class JavaClassHelper {

    /** System specific line separator. */
    private static final String LS = System.getProperty("line.separator");

    /**
     * Get the constants declared in a file as name=value
     *
     * @param bytes the class as a array of bytes
     * @return a StringBuffer contains the name=value pairs
     * @exception IOException if an error occurs
     */
    public static final StringBuffer getConstants(byte[] bytes)
        throws IOException {
        final StringBuffer sb = new StringBuffer();
        final ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        final ClassParser parser = new ClassParser(bis, "");
        final JavaClass javaClass = parser.parse();
        final Field[] fields = javaClass.getFields();
        for (int i = 0; i < fields.length; i++) {
            final Field field = fields[i];
            if (field != null) {
                final ConstantValue cv = field.getConstantValue();
                if (cv != null) {
                    String cvs = cv.toString();
                    if (cvs.startsWith("\"") && cvs.endsWith("\"")) {
                        cvs = cvs.substring(1, cvs.length() - 1);
                    }
                    sb.append(field.getName());
                    sb.append('=');
                    sb.append(cvs);
                    sb.append(LS);
                }
            }
        }
        return sb;
    }
}
