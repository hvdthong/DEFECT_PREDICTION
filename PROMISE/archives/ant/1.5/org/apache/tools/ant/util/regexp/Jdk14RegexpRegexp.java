package org.apache.tools.ant.util.regexp;


import org.apache.tools.ant.BuildException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/***
 * Regular expression implementation using the JDK 1.4 regular expression package
 * @author Matthew Inger <a href="mailto:mattinger@mindless.com">mattinger@mindless.com</a>
 */
public class Jdk14RegexpRegexp extends Jdk14RegexpMatcher implements Regexp {

    public Jdk14RegexpRegexp() {
        super();
    }

    protected int getSubsOptions(int options) {
        int subsOptions = REPLACE_FIRST;
        if (RegexpUtil.hasFlag(options, REPLACE_ALL)) {
            subsOptions = REPLACE_ALL;
        }
        return subsOptions;
    }

    public String substitute(String input, String argument, int options)
        throws BuildException {
        StringBuffer subst = new StringBuffer();
        for (int i = 0; i < argument.length(); i++) {
            char c = argument.charAt(i);
            if (c == '\\') {
                if (++i < argument.length()) {
                    c = argument.charAt(i);
                    int value = Character.digit(c, 10);
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
