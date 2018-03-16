import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.Parser;

public class ASTFalse extends SimpleNode
{
    private static Boolean value = Boolean.FALSE;

    public ASTFalse(int id)
    {
        super(id);
    }

    public ASTFalse(Parser p, int id)
    {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    public boolean evaluate( InternalContextAdapter context)
    {
        return false;
    }

    public Object value( InternalContextAdapter context)
    {
        return value;
    }        
}
