import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.node.Node;

import org.apache.velocity.runtime.RuntimeServices;

/**
 * A very simple directive that leverages the Node.literal()
 * to grab the literal rendition of a node. We basically
 * grab the literal value on init(), then repeatedly use
 * that during render().
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @version $Id: Literal.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class Literal extends Directive
{
    String literalText;
    
    /**
     * Return name of this directive.
     */
    public String getName()
    {
        return "literal";
    }        
    
    /**
     * Return type of this directive.
     */
    public int getType()
    {
        return BLOCK;
    }        

    /**
     * Store the literal rendition of a node using
     * the Node.literal().
     */
    public void init(RuntimeServices rs, InternalContextAdapter context,
                     Node node)
        throws Exception
    {
        super.init( rs, context, node );

        literalText = node.jjtGetChild(0).literal();
    }    

    /**
     * Throw the literal rendition of the block between
     * #literal()/#end into the writer.
     */
    public boolean render( InternalContextAdapter context, 
                           Writer writer, Node node)
        throws IOException
    {
        writer.write(literalText);
        return true;
    }
}
