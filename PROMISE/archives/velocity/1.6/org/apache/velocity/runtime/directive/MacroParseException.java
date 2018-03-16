import org.apache.velocity.exception.ExtendedParseException;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.Token;

/**
 *  Exception to indicate problem happened while constructing #macro()
 *
 *  For internal use in parser - not to be passed to app level
 *
 * @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 * @author <a href="hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @version $Id: MacroParseException.java 703544 2008-10-10 18:15:53Z nbubna $
 */
public class MacroParseException
        extends ParseException
        implements ExtendedParseException
{
    private final String templateName;

    /**
     * Version Id for serializable
     */
    private static final long serialVersionUID = -4985224672336070689L;

    /**
     * @param msg
     * @param templateName
     * @param currentToken
     */
    public MacroParseException(final String msg, final String templateName, final Token currentToken)
    {
        super(msg);
        this.currentToken = currentToken;
        this.templateName = templateName;
    }

    /**
     * returns the Template name where this exception occured.
     * @return The Template name where this exception occured.
     * @since 1.5
     */
    public String getTemplateName()
    {
        return templateName;
    }

    /**
     * returns the line number where this exception occured.
     * @return The line number where this exception occured.
     * @since 1.5
     */
    public int getLineNumber()
    {
        if ((currentToken != null) && (currentToken.next != null))
        {
            return currentToken.next.beginLine;
        }
        else
        {
            return -1;
        }
    }

    /**
     * returns the column number where this exception occured.
     * @return The column number where this exception occured.
     * @since 1.5
     */
    public int getColumnNumber()
    {
        if ((currentToken != null) && (currentToken.next != null))
        {
            return currentToken.next.beginColumn;
        }
        else
        {
            return -1;
        }
    }

    /**
     * This method has the standard behavior when this object has been
     * created using the standard constructors.  Otherwise, it uses
     * "currentToken" and "expectedTokenSequences" to generate a parse
     * error message and returns it.  If this object has been created
     * due to a parse error, and you do not catch it (it gets thrown
     * from the parser), then this method is called during the printing
     * of the final stack trace, and hence the correct error message
     * gets displayed.
     * @return the current message.
     * @since 1.5
     */
    public String getMessage()
    {
        if (!specialConstructor)
        {
            StringBuffer sb = new StringBuffer(super.getMessage());
            appendTemplateInfo(sb);
            return sb.toString();
        }

        int maxSize = 0;

        StringBuffer expected = new StringBuffer();

        for (int i = 0; i < expectedTokenSequences.length; i++)
        {
            if (maxSize < expectedTokenSequences[i].length)
            {
                maxSize = expectedTokenSequences[i].length;
            }

            for (int j = 0; j < expectedTokenSequences[i].length; j++)
            {
                expected.append(tokenImage[expectedTokenSequences[i][j]]).append(" ");
            }

            if (expectedTokenSequences[i][expectedTokenSequences[i].length - 1] != 0)
            {
                expected.append("...");
            }

            expected.append(eol).append("    ");
        }

        StringBuffer retval = new StringBuffer("Encountered \"");
        Token tok = currentToken.next;

        for (int i = 0; i < maxSize; i++)
        {
            if (i != 0)
            {
                retval.append(" ");
            }

            if (tok.kind == 0)
            {
                retval.append(tokenImage[0]);
                break;
            }

            retval.append(add_escapes(tok.image));
            tok = tok.next;
        }

        retval.append("\"");
        appendTemplateInfo(retval);

        if (expectedTokenSequences.length == 1)
        {
            retval.append("Was expecting:").append(eol).append("    ");
        }
        else
        {
            retval.append("Was expecting one of:").append(eol).append("    ");
        }

        retval.append(expected.toString());
        return retval.toString();
    }

    /**
     * @param sb
     * @since 1.5
     */
    protected void appendTemplateInfo(final StringBuffer sb)
    {
        sb.append(Log.formatFileString(getTemplateName(), getLineNumber(), getColumnNumber()));
        sb.append(eol);
    }
}
