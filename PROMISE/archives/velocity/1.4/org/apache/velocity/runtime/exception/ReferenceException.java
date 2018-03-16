import org.apache.velocity.runtime.parser.node.Node;

/**
 * Exception thrown when a bad reference is found.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ReferenceException.java 75955 2004-03-03 23:23:08Z geirm $ 
 */
public class ReferenceException extends Exception
{
    public ReferenceException(String exceptionMessage, Node node)
    {
        super(exceptionMessage + " [line " + node.getLine() + ",column " +
                    node.getColumn() + "] : " + node.literal() + 
            " is not a valid reference.");
    }        
}
