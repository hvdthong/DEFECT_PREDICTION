package org.apache.tools.ant.types;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.regexp.Regexp;
import org.apache.tools.ant.util.regexp.RegexpFactory;

/**
 * A regular expression datatype.  Keeps an instance of the
 * compiled expression for speed purposes.  This compiled
 * expression is lazily evaluated (it is compiled the first
 * time it is needed).  The syntax is the dependent on which
 * regular expression type you are using.  The system property
 * "ant.regexp.regexpimpl" will be the classname of the implementation
 * that will be used.
 *
 * <pre>
 * For jdk  &lt;= 1.3, there are two available implementations:
 *   org.apache.tools.ant.util.regexp.JakartaOroRegexp (the default)
 *        Based on the jakarta-oro package
 *
 *   org.apache.tools.ant.util.regexp.JakartaRegexpRegexp
 *        Based on the jakarta-regexp package
 *
 * For jdk &gt;= 1.4 an additional implementation is available:
 *   org.apache.tools.ant.util.regexp.Jdk14RegexpRegexp
 *        Based on the jdk 1.4 built in regular expression package.
 * </pre>
 *
 * <pre>
 *   &lt;regexp [ [id="id"] pattern="expression" | refid="id" ]
 *   /&gt;
 * </pre>
 *
 * @see org.apache.oro.text.regex.Perl5Compiler
 * @see org.apache.regexp.RE
 * @see java.util.regex.Pattern
 *
 * @see org.apache.tools.ant.util.regexp.Regexp
 *
 * @ant.datatype name="regexp"
 */
public class RegularExpression extends DataType {
    /** Name of this data type */
    public static final String DATA_TYPE_NAME = "regexp";
    private boolean alreadyInit = false;

    private static final RegexpFactory FACTORY = new RegexpFactory();

    private Regexp regexp = null;
    private String myPattern;
    private boolean setPatternPending = false;

    /**
     * default constructor
     */
    public RegularExpression() {
    }

    private void init(Project p) {
        if (!alreadyInit) {
            this.regexp = FACTORY.newRegexp(p);
            alreadyInit = true;
        }
    }
    private void setPattern() {
        if (setPatternPending) {
            regexp.setPattern(myPattern);
            setPatternPending = false;
        }
    }
    /**
     * sets the regular expression pattern
     * @param pattern regular expression pattern
     */
    public void setPattern(String pattern) {
        if (regexp == null) {
            myPattern = pattern;
            setPatternPending = true;
        } else {
            regexp.setPattern(pattern);
        }
    }

    /***
     * Gets the pattern string for this RegularExpression in the
     * given project.
     * @param p project
     * @return pattern
     */
    public String getPattern(Project p) {
        init(p);
        if (isReference()) {
            return getRef(p).getPattern(p);
        }
        setPattern();
        return regexp.getPattern();
    }

    /**
     * provides a reference to the Regexp contained in this
     * @param p  project
     * @return   Regexp instance associated with this RegularExpression instance
     */
    public Regexp getRegexp(Project p) {
        init(p);
        if (isReference()) {
            return getRef(p).getRegexp(p);
        }
        setPattern();
        return this.regexp;
    }

    /***
     * Get the RegularExpression this reference refers to in
     * the given project.  Check for circular references too
     * @param p project
     * @return resolved RegularExpression instance
     */
    public RegularExpression getRef(Project p) {
        return (RegularExpression) getCheckedRef(p);
    }
}
