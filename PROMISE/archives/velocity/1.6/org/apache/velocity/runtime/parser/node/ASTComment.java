import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.Token;

/**
 *  Represents all comments...
 *
 *  @author <a href="mailto:geirm@apache.org">Geir Magnusson Jr.</a>
 *  @version $Id: ASTComment.java 517553 2007-03-13 06:09:58Z wglass $
 */
public class ASTComment extends SimpleNode
{
    private static final char[] ZILCH = "".toCharArray();

    private char[] carr;

    /**
     * @param id
     */
    public ASTComment(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTComment(Parser p, int id)
    {
        super(p, id);
    }

    /**
     * @see org.apache.velocity.runtime.parser.node.SimpleNode#jjtAccept(org.apache.velocity.runtime.parser.node.ParserVisitor, java.lang.Object)
     */
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    /**
     *  We need to make sure we catch any of the dreaded MORE tokens.
     * @param context
     * @param data
     * @return The data object.
     */
    public Object init(InternalContextAdapter context, Object data)
    {
        Token t = getFirstToken();

        int loc1 = t.image.indexOf("##");
        int loc2 = t.image.indexOf("#*");

        if (loc1 == -1 && loc2 == -1)
        {
            carr = ZILCH;
        }
        else
        {
            carr = t.image.substring(0, (loc1 == -1) ? loc2 : loc1).toCharArray();
        }

        return data;
    }

    /**
     * @see org.apache.velocity.runtime.parser.node.SimpleNode#render(org.apache.velocity.context.InternalContextAdapter, java.io.Writer)
     */
    public boolean render( InternalContextAdapter context, Writer writer)
        throws IOException, MethodInvocationException, ParseErrorException, ResourceNotFoundException
    {

        if (context.getAllowRendering())
        {
            writer.write(carr);
        }

        return true;
    }

}
