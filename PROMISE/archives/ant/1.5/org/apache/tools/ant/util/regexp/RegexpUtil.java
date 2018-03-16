package org.apache.tools.ant.util.regexp;

/***
 * Regular expression utilities class which handles flag operations
 *
 * @author <a href="mailto:mattinger@mindless.com">Matthew Inger</a>
 */
public class RegexpUtil {
    public static final boolean hasFlag(int options, int flag) {
        return ((options & flag) > 0);
    }

    public static final int removeFlag(int options, int flag) {
        return (options & (0xFFFFFFFF - flag));
    }
}
