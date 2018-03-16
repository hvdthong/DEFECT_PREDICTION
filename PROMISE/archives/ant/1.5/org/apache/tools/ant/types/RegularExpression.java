package org.apache.tools.ant.types;


import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.regexp.Regexp;
import org.apache.tools.ant.util.regexp.RegexpFactory;

/***
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
 * @author Matthew Inger <a href="mailto:mattinger@mindless.com">mattinger@mindless.com</a>
 *
 * @ant.datatype name="regexp"
 */
public class RegularExpression extends DataType {
    /** Name of this data type */
    public static final String DATA_TYPE_NAME = "regexp";

    private static final RegexpFactory factory = new RegexpFactory();

    private Regexp regexp;

    public RegularExpression() {
        this.regexp = factory.newRegexp();
    }

    public void setPattern(String pattern) {
        this.regexp.setPattern(pattern);
    }

    /***
     * Gets the pattern string for this RegularExpression in the
     * given project.
     */
    public String getPattern(Project p) {
        if (isReference()) {
            return getRef(p).getPattern(p);
        }

        return regexp.getPattern();
    }

    public Regexp getRegexp(Project p) {
        if (isReference()) {
            return getRef(p).getRegexp(p);
        }
        return this.regexp;
    }

    /***
     * Get the RegularExpression this reference refers to in
     * the given project.  Check for circular references too
     */
    public RegularExpression getRef(Project p) {
        if (!checked) {
            Stack stk = new Stack();
            stk.push(this);
            dieOnCircularReference(stk, p);
        }

        
        Object o = ref.getReferencedObject(p);
        if (!(o instanceof RegularExpression)) {
            String msg = ref.getRefId() + " doesn\'t denote a " 
                + DATA_TYPE_NAME;
            throw new BuildException(msg);
        } else {
            return (RegularExpression) o;
        }
    }
}
