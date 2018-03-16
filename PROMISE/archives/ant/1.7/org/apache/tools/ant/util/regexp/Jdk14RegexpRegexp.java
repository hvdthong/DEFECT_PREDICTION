package org.apache.tools.ant.util.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.tools.ant.BuildException;

/***
 * Regular expression implementation using the JDK 1.4 regular expression package
 */
public class Jdk14RegexpRegexp extends Jdk14RegexpMatcher implements Regexp {

    private static final int DECIMAL = 10;

    /** Constructor for Jdk14RegexpRegexp */
    public Jdk14RegexpRegexp() {
        super();
    }

    /**
     * Convert ant regexp substitution option to jdk1.4 options.
     *
     * @param options the ant regexp options
     * @return the jdk14 substition options
     */
    protected int getSubsOptions(int options) {
        int subsOptions = REPLACE_FIRST;
        if (RegexpUtil.hasFlag(options, REPLACE_ALL)) {
            subsOptions = REPLACE_ALL;
        }
        return subsOptions;
    }

    /**
     * Perform a substitution on the regular expression.
     * @param input The string to substitute on
     * @param argument The string which defines the substitution
     * @param options The list of options for the match and replace.
     * @return the result of the operation
     * @throws BuildException on error
     */
    public String substitute(String input, String argument, int options)
        throws BuildException {
        StringBuffer subst = new StringBuffer();
        for (int i = 0; i < argument.length(); i++) {
            char c = argument.charAt(i);
            if (c == '$') {
                subst.append('\\');
                subst.append('$');
            } else if (c == '\\') {
                if (++i < argument.length()) {
                    c = argument.charAt(i);
                    int value = Character.digit(c, DECIMAL);
                    if (value > -1) {
                        subst.append("$").append(value);
                    } else {
                        subst.append(c);
                    }
                } else {
                    subst.append('\\');
                }
            } else {
                subst.append(c);
            }
        }
        argument = subst.toString();

        int sOptions = getSubsOptions(options);
        Pattern p = getCompiledPattern(options);
        StringBuffer sb = new StringBuffer();

        Matcher m = p.matcher(input);
        if (RegexpUtil.hasFlag(sOptions, REPLACE_ALL)) {
            sb.append(m.replaceAll(argument));
        } else {
            boolean res = m.find();
            if (res) {
                m.appendReplacement(sb, argument);
                m.appendTail(sb);
            } else {
                sb.append(input);
            }
        }
        return sb.toString();
    }
}
