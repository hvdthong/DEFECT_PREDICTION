import org.apache.velocity.runtime.parser.Parser;

/**
 *
 */
public class ASTWord extends SimpleNode
{
    /**
     * @param id
     */
    public ASTWord(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTWord(Parser p, int id)
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
