import org.apache.velocity.runtime.parser.Parser;

public class ASTParameters extends SimpleNode
{
    public ASTParameters(int id)
    {
        super(id);
    }

    public ASTParameters(Parser p, int id)
    {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }
}
