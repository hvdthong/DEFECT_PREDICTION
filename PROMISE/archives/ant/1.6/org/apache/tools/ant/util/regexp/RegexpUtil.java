package org.apache.tools.ant.util.regexp;

/***
 * Regular expression utilities class which handles flag operations.
 *
 */
public class RegexpUtil {

    /**
     * Check the options has a particular flag set.
     *
     * @param options an <code>int</code> value
     * @param flag an <code>int</code> value
     * @return true if the flag is set
     */
    public static boolean hasFlag(int options, int flag) {
        return ((options & flag) > 0);
    }

    /**
     * Remove a particular flag from an int value contains the option flags.
     *
     * @param options an <code>int</code> value
     * @param flag an <code>int</code> value
     * @return the options with the flag unset
     */
    public static int removeFlag(int options, int flag) {
        return (options & (0xFFFFFFFF - flag));
    }
}
