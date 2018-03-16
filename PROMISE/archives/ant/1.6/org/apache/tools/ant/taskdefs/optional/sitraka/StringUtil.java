package org.apache.tools.ant.taskdefs.optional.sitraka;

/**
 * String utilities method.
 */
public final class StringUtil {
    /** private constructor, it's a utility class */
    private StringUtil() {
    }

    /**
     * Replaces all occurences of <tt>find</tt> with <tt>replacement</tt> in the
     * source StringBuffer.
     * @param src the original string buffer to modify.
     * @param find the string to be replaced.
     * @param replacement the replacement string for <tt>find</tt> matches.
     */
    public static void replace(StringBuffer src, String find, String replacement) {
        int index = 0;
        while (index < src.length()) {
            index = src.toString().indexOf(find, index);
            if (index == -1) {
                break;
            }
            src.delete(index, index + find.length());
            src.insert(index, replacement);
            index += replacement.length() + 1;
        }
    }
}
