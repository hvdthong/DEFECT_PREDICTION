import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.parser.node.Node;

/**
 * A very simple directive that leverages the Node.literal()
 * to grab the literal rendition of a node. We basically
 * grab the literal value on init(), then repeatedly use
 * that during render().
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @version $Id: Literal.java 471381 2006-11-05 08:56:58Z wglass $
 */
public class Literal extends Directive
{
    String literalText;

    /**
     * Return name of this directive.
     * @return The name of this directive.
     */
    public String getName()
    {
        return "literal";
    }

    /**
     * Return type of this directive.
     * @return The type of this directive.
     */
    public int getType()
    {
        return BLOCK;
    }

    /**
     * Store the literal rendition of a node using
     * the Node.literal().
     * @param rs
     * @param context
     * @param node
     * @throws TemplateInitException
     */
    public void init(RuntimeServices rs, InternalContextAdapter context,
                     Node node)
        throws TemplateInitException
    {
        super.init( rs, context, node );

        literalText = node.jjtGetChild(0).literal();
    }

    /**
     * Throw the literal rendition of the block between
     * #literal()/#end into the writer.
     * @param context
     * @param writer
     * @param node
     * @return True if the directive rendered successfully.
     * @throws IOException
     */
    public boolean render( InternalContextAdapter context,
                           Writer writer, Node node)
        throws IOException
    {
        writer.write(literalText);
        return true;
    }
}
