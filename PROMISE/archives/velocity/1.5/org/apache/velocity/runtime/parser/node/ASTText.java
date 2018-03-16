import java.io.Writer;
import java.io.IOException;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;
import org.apache.velocity.runtime.parser.Token;

/**
 *
 */
public class ASTText extends SimpleNode
{
    private char[] ctext;

    /**
     * @param id
     */
    public ASTText(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTText(Parser p, int id)
    {
        super(p, id);
    }

    /**
     * @see org.apache.velocity.runtime.parser.node.SimpleNode#jjtAccept(org.apache.velocity.runtime.parser.ParserVisitor, java.lang.Object)
     */
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    /**
     * @see org.apache.velocity.runtime.parser.node.SimpleNode#init(org.apache.velocity.context.InternalContextAdapter, java.lang.Object)
     */
    public Object init( InternalContextAdapter context, Object data)
    throws TemplateInitException
    {
        Token t = getFirstToken();

        String text = NodeUtils.tokenLiteral( t );

        ctext = text.toCharArray();

        return data;
    }

    /**
     * @see org.apache.velocity.runtime.parser.node.SimpleNode#render(org.apache.velocity.context.InternalContextAdapter, java.io.Writer)
     */
    public boolean render( InternalContextAdapter context, Writer writer)
        throws IOException
    {
        if (context.getAllowRendering())
        {
            writer.write(ctext);
        }
        return true;
    }
}







