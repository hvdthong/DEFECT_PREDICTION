import org.apache.velocity.runtime.parser.Parser;

public class ASTWord extends SimpleNode 
{
    public ASTWord(int id) 
    {
        super(id);
    }

    public ASTWord(Parser p, int id) 
    {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data) 
    {
        return visitor.visit(this, data);
    }
}
