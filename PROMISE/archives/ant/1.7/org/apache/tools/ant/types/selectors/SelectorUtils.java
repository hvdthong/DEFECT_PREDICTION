package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.types.resources.FileResource;

/**
 * <p>This is a utility class used by selectors and DirectoryScanner. The
 * functionality more properly belongs just to selectors, but unfortunately
 * DirectoryScanner exposed these as protected methods. Thus we have to
 * support any subclasses of DirectoryScanner that may access these methods.
 * </p>
 * <p>This is a Singleton.</p>
 *
 * @since 1.5
 */
public final class SelectorUtils {

    private static SelectorUtils instance = new SelectorUtils();
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    /**
     * Private Constructor
     */
    private SelectorUtils() {
    }

    /**
     * Retrieves the instance of the Singleton.
     * @return singleton instance
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
        if (str.startsWith(File.separator)
                != pattern.startsWith(File.separator)) {
            return false;
        }

        String[] patDirs = tokenizePathAsArray(pattern);
        String[] strDirs = tokenizePathAsArray(str);

        int patIdxStart = 0;
        int patIdxEnd = patDirs.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strDirs.length - 1;

        while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            String patDir = patDirs[patIdxStart];
            if (patDir.equals("**")) {
                break;
            }
            if (!match(patDir, strDirs[strIdxStart], isCaseSensitive)) {
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
        String[] patDirs = tokenizePathAsArray(pattern);
        String[] strDirs = tokenizePathAsArray(str);

        int patIdxStart = 0;
        int patIdxEnd = patDirs.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strDirs.length - 1;

        while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            String patDir = patDirs[patIdxStart];
            if (patDir.equals("**")) {
                break;
            }
            if (!match(patDir, strDirs[strIdxStart], isCaseSensitive)) {
                patDirs = null;
                strDirs = null;
                return false;
            }
            patIdxStart++;
            strIdxStart++;
        }
        if (strIdxStart > strIdxEnd) {
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (!patDirs[i].equals("**")) {
                    patDirs = null;
                    strDirs = null;
                    return false;
                }
            }
            return true;
        } else {
            if (patIdxStart > patIdxEnd) {
                patDirs = null;
                strDirs = null;
                return false;
            }
        }

        while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
            String patDir = patDirs[patIdxEnd];
            if (patDir.equals("**")) {
                break;
            }
            if (!match(patDir, strDirs[strIdxEnd], isCaseSensitive)) {
                patDirs = null;
                strDirs = null;
                return false;
            }
            patIdxEnd--;
            strIdxEnd--;
        }
        if (strIdxStart > strIdxEnd) {
            for (int i = patIdxStart; i <= patIdxEnd; i++) {
                if (!patDirs[i].equals("**")) {
                    patDirs = null;
                    strDirs = null;
                    return false;
                }
            }
            return true;
        }

        while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
            int patIdxTmp = -1;
            for (int i = patIdxStart + 1; i <= patIdxEnd; i++) {
                if (patDirs[i].equals("**")) {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == patIdxStart + 1) {
                patIdxStart++;
                continue;
            }
            int patLength = (patIdxTmp - patIdxStart - 1);
            int strLength = (strIdxEnd - strIdxStart + 1);
            int foundIdx = -1;
            strLoop:
                        for (int i = 0; i <= strLength - patLength; i++) {
                            for (int j = 0; j < patLength; j++) {
                                String subPat = patDirs[patIdxStart + j + 1];
                                String subStr = strDirs[strIdxStart + i + j];
                                if (!match(subPat, subStr, isCaseSensitive)) {
                                    continue strLoop;
                                }
                            }

                            foundIdx = strIdxStart + i;
                            break;
                        }

            if (foundIdx == -1) {
                patDirs = null;
                strDirs = null;
                return false;
            }

            patIdxStart = patIdxTmp;
            strIdxStart = foundIdx + patLength;
        }

        for (int i = patIdxStart; i <= patIdxEnd; i++) {
            if (!patDirs[i].equals("**")) {
                patDirs = null;
                strDirs = null;
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
        int patIdxEnd = patArr.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strArr.length - 1;
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
                    if (!isCaseSensitive && Character.toUpperCase(ch)
                            != Character.toUpperCase(strArr[i])) {
                    }
                }
            }
        }

        if (patIdxEnd == 0) {
        }

        while ((ch = patArr[patIdxStart]) != '*' && strIdxStart <= strIdxEnd) {
            if (ch != '?') {
                if (isCaseSensitive && ch != strArr[strIdxStart]) {
                }
                if (!isCaseSensitive && Character.toUpperCase(ch)
                        != Character.toUpperCase(strArr[strIdxStart])) {
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

        while ((ch = patArr[patIdxEnd]) != '*' && strIdxStart <= strIdxEnd) {
            if (ch != '?') {
                if (isCaseSensitive && ch != strArr[strIdxEnd]) {
                }
                if (!isCaseSensitive && Character.toUpperCase(ch)
                        != Character.toUpperCase(strArr[strIdxEnd])) {
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
            for (int i = patIdxStart + 1; i <= patIdxEnd; i++) {
                if (patArr[i] == '*') {
                    patIdxTmp = i;
                    break;
                }
            }
            if (patIdxTmp == patIdxStart + 1) {
                patIdxStart++;
                continue;
            }
            int patLength = (patIdxTmp - patIdxStart - 1);
            int strLength = (strIdxEnd - strIdxStart + 1);
            int foundIdx = -1;
            strLoop:
            for (int i = 0; i <= strLength - patLength; i++) {
                for (int j = 0; j < patLength; j++) {
                    ch = patArr[patIdxStart + j + 1];
                    if (ch != '?') {
                        if (isCaseSensitive && ch != strArr[strIdxStart + i
                                + j]) {
                            continue strLoop;
                        }
                        if (!isCaseSensitive
                            && Character.toUpperCase(ch)
                                != Character.toUpperCase(strArr[strIdxStart + i + j])) {
                            continue strLoop;
                        }
                    }
                }

                foundIdx = strIdxStart + i;
                break;
            }

            if (foundIdx == -1) {
                return false;
            }

            patIdxStart = patIdxTmp;
            strIdxStart = foundIdx + patLength;
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
        return tokenizePath(path, File.separator);
    }

    /**
     * Breaks a path up into a Vector of path elements, tokenizing on
     *
     * @param path Path to tokenize. Must not be <code>null</code>.
     * @param separator the separator against which to tokenize.
     *
     * @return a Vector of path elements from the tokenized path
     * @since Ant 1.6
     */
    public static Vector tokenizePath (String path, String separator) {
        Vector ret = new Vector();
        if (FileUtils.isAbsolutePath(path)) {
            String[] s = FILE_UTILS.dissect(path);
            ret.add(s[0]);
            path = s[1];
        }
        StringTokenizer st = new StringTokenizer(path, separator);
        while (st.hasMoreTokens()) {
            ret.addElement(st.nextToken());
        }
        return ret;
    }

    /**
     * Same as {@link #tokenizePath tokenizePath} but hopefully faster.
     */
    private static String[] tokenizePathAsArray(String path) {
        String root = null;
        if (FileUtils.isAbsolutePath(path)) {
            String[] s = FILE_UTILS.dissect(path);
            root = s[0];
            path = s[1];
        }
        char sep = File.separatorChar;
        int start = 0;
        int len = path.length();
        int count = 0;
        for (int pos = 0; pos < len; pos++) {
            if (path.charAt(pos) == sep) {
                if (pos != start) {
                    count++;
                }
                start = pos + 1;
            }
        }
        if (len != start) {
            count++;
        }
        String[] l = new String[count + ((root == null) ? 0 : 1)];

        if (root != null) {
            l[0] = root;
            count = 1;
        } else {
            count = 0;
        }
        start = 0;
        for (int pos = 0; pos < len; pos++) {
            if (path.charAt(pos) == sep) {
                if (pos != start) {
                    String tok = path.substring(start, pos);
                    l[count++] = tok;
                }
                start = pos + 1;
            }
        }
        if (len != start) {
            String tok = path.substring(start);
            l[count/*++*/] = tok;
        }
        return l;
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

    /**
     * Returns dependency information on these two resources. If src has been
     * modified later than target, it returns true. If target doesn't exist,
     * it likewise returns true. Otherwise, target is newer than src and
     * is not out of date, thus the method returns false. It also returns
     * false if the src file doesn't even exist, since how could the
     * target then be out of date.
     *
     * @param src the original resource
     * @param target the resource being compared against
     * @param granularity the int amount in seconds of slack we will give in
     *        determining out of dateness
     * @return whether the target is out of date
     */
    public static boolean isOutOfDate(Resource src, Resource target,
                                      int granularity) {
        return isOutOfDate(src, target, (long) granularity);
    }

    /**
     * Returns dependency information on these two resources. If src has been
     * modified later than target, it returns true. If target doesn't exist,
     * it likewise returns true. Otherwise, target is newer than src and
     * is not out of date, thus the method returns false. It also returns
     * false if the src file doesn't even exist, since how could the
     * target then be out of date.
     *
     * @param src the original resource
     * @param target the resource being compared against
     * @param granularity the long amount in seconds of slack we will give in
     *        determining out of dateness
     * @return whether the target is out of date
     */
    public static boolean isOutOfDate(Resource src, Resource target,
                                      long granularity) {
        long sourceLastModified = src.getLastModified();
        boolean sourceExists = (src instanceof FileResource)
            ? sourceLastModified != 0L : src.isExists();

        long targetLastModified = target.getLastModified();
        if (targetLastModified == 0L) {
            return true;
        }
        return (sourceLastModified - granularity) > targetLastModified;
    }

    /**
     * "Flattens" a string by removing all whitespace (space, tab, linefeed,
     * carriage return, and formfeed). This uses StringTokenizer and the
     * default set of tokens as documented in the single arguement constructor.
     *
     * @param input a String to remove all whitespace.
     * @return a String that has had all whitespace removed.
     */
    public static String removeWhitespace(String input) {
        StringBuffer result = new StringBuffer();
        if (input != null) {
            StringTokenizer st = new StringTokenizer(input);
            while (st.hasMoreTokens()) {
                result.append(st.nextToken());
            }
        }
        return result.toString();
    }

    /**
     * Tests if a string contains stars or question marks
     * @param input a String which one wants to test for containing wildcard
     * @return true if the string contains at least a star or a question mark
     */
    public static boolean hasWildcards(String input) {
        return (input.indexOf('*') != -1 || input.indexOf('?') != -1);
    }

    /**
     * removes from a pattern all tokens to the right containing wildcards
     * @param input the input string
     * @return the leftmost part of the pattern without wildcards
     */
    public static String rtrimWildcardTokens(String input) {
        String[] tokens = tokenizePathAsArray(input);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tokens.length; i++) {
            if (hasWildcards(tokens[i])) {
                break;
            }
            if (i > 0 && sb.charAt(sb.length() - 1) != File.separatorChar) {
                sb.append(File.separator);
            }
            sb.append(tokens[i]);
        }
        return sb.toString();
    }
}

