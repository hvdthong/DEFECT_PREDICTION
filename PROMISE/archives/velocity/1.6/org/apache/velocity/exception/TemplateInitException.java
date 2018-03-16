import org.apache.velocity.runtime.parser.ParseException;

/**
 * Exception generated to indicate parse errors caught during
 * directive initialization (e.g. wrong number of arguments)
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 * @version $Id: TemplateInitException.java 685685 2008-08-13 21:43:27Z nbubna $
 * @since 1.5
 */
public class TemplateInitException extends VelocityException 
        implements ExtendedParseException
{
    private final String templateName;
    private final int col;
    private final int line;
    
    /**
     * Version Id for serializable
     */
    private static final long serialVersionUID = -4985224672336070621L;

    public TemplateInitException(final String msg, 
            final String templateName, final int col, final int line)
    {
        super(msg);
        this.templateName = templateName;
        this.col = col;
        this.line = line;
    }

    public TemplateInitException(final String msg, ParseException parseException,
            final String templateName, final int col, final int line)
    {
        super(msg,parseException);
        this.templateName = templateName;
        this.col = col;
        this.line = line;
    }

    /**
     * Returns the Template name where this exception occured.
     * @return the template name
     */
    public String getTemplateName()
    {
        return templateName;
    }

    /**
     * Returns the line number where this exception occured.
     * @return the line number
     */
    public int getLineNumber()
    {
        return line;
    }

    /**
     * Returns the column number where this exception occured.
     * @return the line number
     */
    public int getColumnNumber()
    {
        return col;
    }

    
    
    
}
