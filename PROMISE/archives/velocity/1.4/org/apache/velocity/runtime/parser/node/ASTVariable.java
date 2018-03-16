import org.apache.velocity.runtime.parser.Parser;

public class ASTVariable extends SimpleNode
{
    public ASTVariable(int id)
    {
        super(id);
    }

    public ASTVariable(Parser p, int id)
    {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }
}
