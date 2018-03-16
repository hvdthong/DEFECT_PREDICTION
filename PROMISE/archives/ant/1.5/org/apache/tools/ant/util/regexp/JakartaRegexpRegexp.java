package org.apache.tools.ant.util.regexp;


import org.apache.tools.ant.BuildException;
import org.apache.regexp.RE;
import java.util.Vector;

/***
 * Regular expression implementation using the Jakarta Regexp package
 * @author Matthew Inger <a href="mailto:mattinger@mindless.com">mattinger@mindless.com</a>
 */
public class JakartaRegexpRegexp extends JakartaRegexpMatcher 
    implements Regexp {

    public JakartaRegexpRegexp() {
        super();
    }

    protected int getSubsOptions(int options) {
        int subsOptions = RE.REPLACE_FIRSTONLY;
        if (RegexpUtil.hasFlag(options, REPLACE_ALL)) {
            subsOptions = RE.REPLACE_ALL;
        }
        return subsOptions;
    }

    public String substitute(String input, String argument, int options)
        throws BuildException {
        Vector v = getGroups(input, options);

        StringBuffer result = new StringBuffer();
        for (int i = 0; i < argument.length(); i++) {
            char c = argument.charAt(i);
            if (c == '\\') {
                if (++i < argument.length()) {
                    c = argument.charAt(i);
                    int value = Character.digit(c, 10);
                    if (value > -1) {
                        result.append((String) v.elementAt(value));
                    } else {
                        result.append(c);
                    }
                } else {
                    result.append('\\');
                }
            } else {
                result.append(c);
            }
        }
        argument = result.toString();

        RE reg = getCompiledPattern(options);
        int sOptions = getSubsOptions(options);
        return reg.subst(input, argument, sOptions);
    }    
}
