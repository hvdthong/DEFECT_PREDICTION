import org.apache.velocity.util.introspection.Info;

/**
 * Convenience class to use when reporting out invalid syntax 
 * with line, column, and template name.
 * 
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain </a>
 * @version $Id: InvalidReferenceInfo.java 470256 2006-11-02 07:20:36Z wglass $
 */
public class InvalidReferenceInfo extends Info
{
    private String invalidReference;
    
    public InvalidReferenceInfo(String invalidReference, Info info)
    {
        super(info.getTemplateName(),info.getLine(),info.getColumn());
        this.invalidReference = invalidReference; 
    }

    /**
     * Get the specific invalid reference string.
     * @return the invalid reference string
     */
    public String getInvalidReference()
    {
        return invalidReference;
    }
    
    

    /**
     * Formats a textual representation of this object as <code>SOURCE
     * [line X, column Y]: invalidReference</code>.
     *
     * @return String representing this object.
     */
    public String toString()
    {
        return getTemplateName() + " [line " + getLine() + ", column " +
            getColumn() + "]: " + invalidReference;
    }
}
