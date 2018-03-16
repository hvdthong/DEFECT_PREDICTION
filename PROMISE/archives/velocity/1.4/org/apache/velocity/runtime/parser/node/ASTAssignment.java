import org.apache.velocity.runtime.parser.Parser;

public class ASTAssignment extends SimpleNode
{
    public ASTAssignment(int id)
    {
        super(id);
    }

    public ASTAssignment(Parser p, int id)
    {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }
}
