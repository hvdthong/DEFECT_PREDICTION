import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.Parser;

/**
 * This class is responsible for handling EscapedDirectives
 *  in VTL.
 *
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ASTEscapedDirective.java 517553 2007-03-13 06:09:58Z wglass $
 */
public class ASTEscapedDirective extends SimpleNode
{
    /**
     * @param id
     */
    public ASTEscapedDirective(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTEscapedDirective(Parser p, int id)
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
     * @see org.apache.velocity.runtime.parser.node.SimpleNode#render(org.apache.velocity.context.InternalContextAdapter, java.io.Writer)
     */
    public boolean render(InternalContextAdapter context, Writer writer)
        throws IOException
    {
        if (context.getAllowRendering())
        {
            writer.write(getFirstToken().image);
        }
        return true;
    }

}
