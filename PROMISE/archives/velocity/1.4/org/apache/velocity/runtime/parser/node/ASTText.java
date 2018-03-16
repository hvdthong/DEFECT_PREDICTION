import java.io.Writer;
import java.io.IOException;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.Token;

public class ASTText extends SimpleNode
{
    private char[] ctext;

    public ASTText(int id)
    {
        super(id);
    }

    public ASTText(Parser p, int id)
    {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    public Object init( InternalContextAdapter context, Object data) 
        throws Exception
    {
        Token t = getFirstToken();

        String text = NodeUtils.tokenLiteral( t );
        
        ctext = text.toCharArray();

        return data;
    }

    public boolean render( InternalContextAdapter context, Writer writer)
        throws IOException
    {
        writer.write(ctext);
        return true;
    }    
}







