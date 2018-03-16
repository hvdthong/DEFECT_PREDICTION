import org.apache.velocity.runtime.parser.Parser;


/**
 *
 */
public class ASTVariable extends SimpleNode
{
    /**
     * @param id
     */
    public ASTVariable(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTVariable(Parser p, int id)
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
}
