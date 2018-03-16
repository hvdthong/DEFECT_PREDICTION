package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.tools.ant.BuildException;

/**
 * <p>This is a utility class used by selectors and DirectoryScanner. The
 * functionality more properly belongs just to selectors, but unfortunately
 * DirectoryScanner exposed these as protected methods. Thus we have to
 * support any subclasses of DirectoryScanner that may access these methods.
 * </p>
 * <p>This is a Singleton.</p>
 *
 * @author Arnout J. Kuiper
 * <a href="mailto:ajkuiper@wxs.nl">ajkuiper@wxs.nl</a>
 * @author Magesh Umasankar
 * @author <a href="mailto:bruce@callenish.com">Bruce Atherton</a>
 * @since 1.5
 */
public final class SelectorUtils {

    private static SelectorUtils instance = new SelectorUtils();

    /**
     * Private Constructor
     */
    private SelectorUtils() {
    }

     /**
      * Retrieves the instance of the Singleton.
      */
    public static SelectorUtils getInstance() {
        return instance;
    }

    /**
     * Tests whether or not a given path matches the start of a given
     * pattern up to the first "**".
     * <p>
     * This is not a general purpose test and should only be used if you
     * can live with false positives. For example, <code>pattern=**\a</code>
     * and <code>str=b</code> will yield <code>true</code>.
     *
     * @param pattern The pattern to match against. Must not be
     *                <code>null</code>.
     * @param str     The path to match, as a String. Must not be
     *                <code>null</code>.
     *
     * @return whether or not a given path matches the start of a given
     * pattern up to the first "**".
     */
    public static boolean matchPatternStart(String pattern, String str) {
        return matchPatternStart(pattern, str, true);
    }
    /**
     * Tests whether or not a given path matches the start of a given
     * pattern up to the first "**".
     * <p>
     * This is not a general purpose test and should only be used if you
     * can live with false positives. For example, <code>pattern=**\a</code>
     * and <code>str=b</code> will yield <code>true</code>.
     *
     * @param pattern The pattern to match against. Must not be
     *                <code>null</code>.
     * @param str     The path to match, as a String. Must not be
     *                <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     *
     * @return whether or not a given path matches the start of a given
     * pattern up to the first "**".
     */
    public static boolean matchPatternStart(String pattern, String str,
                                               boolean isCaseSensitive) {
        if (str.startsWith(File.separator) !=
            pattern.startsWith(File.separator)) {
            return false;
        }

        Vector patDirs = tokenizePath (pattern);
        Vector strDirs = tokenizePath (str);

        int patIdxStart = 0;
        int patIdxEnd   = patDirs.size()-1;
        int strIdxStart = 0;
        int strIdxEnd   = strDirs.size()-1;

        while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            String patDir = (String)patDirs.elementAt(patIdxStart);
            if (patDir.equals("**")) {
                break;
            }
            if (!match(patDir,(String)strDirs.elementAt(strIdxStart),
                    isCaseSensitive)) {
                return false;
            }
            patIdxStart++;
            strIdxStart++;
        }

        if (strIdxStart > strIdxEnd) {
            return true;
        } else if (patIdxStart > patIdxEnd) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Tests whether or not a given path matches a given pattern.
     *
     * @param pattern The pattern to match against. Must not be
     *                <code>null</code>.
     * @param str     The path to match, as a String. Must not be
     *                <code>null</code>.
     *
     * @return <code>true</code> if the pattern matches against the string,
     *         or <code>false</code> otherwise.
     */
    public static boolean matchPath(String pattern, String str) {
        return matchPath(pattern, str, true);
    }

    /**
     * Tests whether or not a given path matches a given pattern.
     *
     * @param pattern The pattern to match against. Must not be
     *                <code>null</code>.
     * @param str     The path to match, as a String. Must not be
     *                <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     *
     * @return <code>true</code> if the pattern matches against the string,
     *         or <code>false</code> otherwise.
     */
    public static boolean matchPath(String pattern, String str,
            boolean isCaseSensitive) {
        if (str.startsWith(File.separator) !=
            pattern.startsWith(File.separator)) {
            return false;
        }

        Vector patDirs = tokenizePath (pattern);
        Vector strDirs = tokenizePath (str);

        int patIdxStart = 0;
        int patIdxEnd   = patDirs.size()-1;
        int strIdxStart = 0;
        int strIdxEnd   = strDirs.size()-1;

        while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            String patDir = (String)patDirs.elementAt(patIdxStart);
            if (patDir.equals("**")) {
                break;
            }
            if (!match(patDir,(String)strDirs.elementAt(strIdxStart),
                    isCaseSensitive)) {
                return false;
            }
            patIdxStart++;
            strIdxStart++;
        }
        if (strIdxStart > strIdxEnd) {
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (!patDirs.elementAt(i).equals("**")) {
                    return false;
                }
            }
            return true;
        } else {
            if (patIdxStart > patIdxEnd) {
                return false;
            }
        }

        while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            String patDir = (String)patDirs.elementAt(patIdxEnd);
            if (patDir.equals("**")) {
                break;
            }
            if (!match(patDir,(String)strDirs.elementAt(strIdxEnd),
                    isCaseSensitive)) {
                return false;
            }
            patIdxEnd--;
            strIdxEnd--;
        }
        if (strIdxStart > strIdxEnd) {
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (!patDirs.elementAt(i).equals("**")) {
                    return false;
                }
            }
            return true;
        }

        while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
            int patIdxTmp = -1;
            for (int i = patIdxStart+1; i <= patIdxEnd; i++) {
                if (patDirs.elementAt(i).equals("**")) {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == patIdxStart+1) {
                patIdxStart++;
                continue;
            }
            int patLength = (patIdxTmp-patIdxStart-1);
            int strLength = (strIdxEnd-strIdxStart+1);
            int foundIdx  = -1;
strLoop:
            for (int i = 0; i <= strLength - patLength; i++) {
                for (int j = 0; j < patLength; j++) {
                    String subPat = (String)patDirs.elementAt(patIdxStart+j+1);
                    String subStr = (String)strDirs.elementAt(strIdxStart+i+j);
                    if (!match(subPat,subStr, isCaseSensitive)) {
                        continue strLoop;
                    }
                }

                foundIdx = strIdxStart+i;
                break;
            }

            if (foundIdx == -1) {
                return false;
            }

            patIdxStart = patIdxTmp;
            strIdxStart = foundIdx+patLength;
        }

        for (int i = patIdxStart; i <= patIdxEnd; i++) {
            if (!patDirs.elementAt(i).equals("**")) {
                return false;
            }
        }

        return true;
    }

    /**
     * Tests whether or not a string matches against a pattern.
     * The pattern may contain two special characters:<br>
     * '*' means zero or more characters<br>
     * '?' means one and only one character
     *
     * @param pattern The pattern to match against.
     *                Must not be <code>null</code>.
     * @param str     The string which must be matched against the pattern.
     *                Must not be <code>null</code>.
     *
     * @return <code>true</code> if the string matches against the pattern,
     *         or <code>false</code> otherwise.
     */
    public static boolean match(String pattern, String str) {
        return match(pattern, str, true);
    }

    /**
     * Tests whether or not a string matches against a pattern.
     * The pattern may contain two special characters:<br>
     * '*' means zero or more characters<br>
     * '?' means one and only one character
     *
     * @param pattern The pattern to match against.
     *                Must not be <code>null</code>.
     * @param str     The string which must be matched against the pattern.
     *                Must not be <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     *
     *
     * @return <code>true</code> if the string matches against the pattern,
     *         or <code>false</code> otherwise.
     */
    public static boolean match(String pattern, String str,
            boolean isCaseSensitive) {
        char[] patArr = pattern.toCharArray();
        char[] strArr = str.toCharArray();
        int patIdxStart = 0;
        int patIdxEnd   = patArr.length-1;
        int strIdxStart = 0;
        int strIdxEnd   = strArr.length-1;
        char ch;

        boolean containsStar = false;
        for (int i = 0; i < patArr.length; i++) {
            if (patArr[i] == '*') {
                containsStar = true;
                break;
            }
        }

        if (!containsStar) {
            if (patIdxEnd != strIdxEnd) {
            }
            for (int i = 0; i <= patIdxEnd; i++) {
                ch = patArr[i];
                if (ch != '?') {
                    if (isCaseSensitive && ch != strArr[i]) {
                    }
                    if (!isCaseSensitive && Character.toUpperCase(ch) !=
                        Character.toUpperCase(strArr[i])) {
                    }
                }
            }
        }

        if (patIdxEnd == 0) {
        }

        while((ch = patArr[patIdxStart]) != '*' && strIdxStart <= strIdxEnd) {
            if (ch != '?') {
                if (isCaseSensitive && ch != strArr[strIdxStart]) {
                }
                if (!isCaseSensitive && Character.toUpperCase(ch) !=
                    Character.toUpperCase(strArr[strIdxStart])) {
                }
            }
            patIdxStart++;
            strIdxStart++;
        }
        if (strIdxStart > strIdxEnd) {
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (patArr[i] != '*') {
                    return false;
                }
            }
            return true;
        }

        while((ch = patArr[patIdxEnd]) != '*' && strIdxStart <= strIdxEnd) {
            if (ch != '?') {
                if (isCaseSensitive && ch != strArr[strIdxEnd]) {
                }
                if (!isCaseSensitive && Character.toUpperCase(ch) !=
                    Character.toUpperCase(strArr[strIdxEnd])) {
                }
            }
            patIdxEnd--;
            strIdxEnd--;
        }
        if (strIdxStart > strIdxEnd) {
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (patArr[i] != '*') {
                    return false;
                }
            }
            return true;
        }

        while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
            int patIdxTmp = -1;
            for (int i = patIdxStart+1; i <= patIdxEnd; i++) {
                if (patArr[i] == '*') {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == patIdxStart+1) {
                patIdxStart++;
                continue;
            }
            int patLength = (patIdxTmp-patIdxStart-1);
            int strLength = (strIdxEnd-strIdxStart+1);
            int foundIdx  = -1;
            strLoop:
            for (int i = 0; i <= strLength - patLength; i++) {
                for (int j = 0; j < patLength; j++) {
                    ch = patArr[patIdxStart+j+1];
                    if (ch != '?') {
                        if (isCaseSensitive && ch != strArr[strIdxStart+i+j]) {
                            continue strLoop;
                        }
                        if (!isCaseSensitive && Character.toUpperCase(ch) !=
                            Character.toUpperCase(strArr[strIdxStart+i+j])) {
                            continue strLoop;
                        }
                    }
                }

                foundIdx = strIdxStart+i;
                break;
            }

            if (foundIdx == -1) {
                return false;
            }

            patIdxStart = patIdxTmp;
            strIdxStart = foundIdx+patLength;
        }

        for (int i = patIdxStart; i <= patIdxEnd; i++) {
            if (patArr[i] != '*') {
                return false;
            }
        }
        return true;
    }

    /**
     * Breaks a path up into a Vector of path elements, tokenizing on
     * <code>File.separator</code>.
     *
     * @param path Path to tokenize. Must not be <code>null</code>.
     *
     * @return a Vector of path elements from the tokenized path
     */
    public static Vector tokenizePath (String path) {
        Vector ret = new Vector();
        StringTokenizer st = new StringTokenizer(path,File.separator);
        while (st.hasMoreTokens()) {
            ret.addElement(st.nextToken());
        }
        return ret;
    }


    /**
     * Returns dependency information on these two files. If src has been
     * modified later than target, it returns true. If target doesn't exist,
     * it likewise returns true. Otherwise, target is newer than src and
     * is not out of date, thus the method returns false. It also returns
     * false if the src file doesn't even exist, since how could the
     * target then be out of date.
     *
     * @param src the original file
     * @param target the file being compared against
     * @param granularity the amount in seconds of slack we will give in
     *        determining out of dateness
     * @return whether the target is out of date
     */
    public static boolean isOutOfDate(File src, File target, int granularity) {
        if (!src.exists()) {
            return false;
        }
        if (!target.exists()) {
            return true;
        }
        if ((src.lastModified() - granularity) > target.lastModified()) {
            return true;
        }
        return false;
    }

}

