import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.Parser;

public class ASTTrue extends SimpleNode
{
    private static Boolean value = Boolean.TRUE;

    public ASTTrue(int id)
    {
        super(id);
    }

    public ASTTrue(Parser p, int id)
    {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    public boolean evaluate(InternalContextAdapter context)
    {
        return true;
    }        

    public Object value(InternalContextAdapter context)
    {
        return value;
    }        
}
