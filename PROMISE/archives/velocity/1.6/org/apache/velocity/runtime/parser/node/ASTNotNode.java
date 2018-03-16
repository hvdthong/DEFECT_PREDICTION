import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.runtime.parser.Parser;

/**
 *
 */
public class ASTNotNode extends SimpleNode
{
    /**
     * @param id
     */
    public ASTNotNode(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTNotNode(Parser p, int id)
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

    /**
     * @see org.apache.velocity.runtime.parser.node.SimpleNode#evaluate(org.apache.velocity.context.InternalContextAdapter)
     */
    public boolean evaluate( InternalContextAdapter context)
        throws MethodInvocationException
    {
        if (jjtGetChild(0).evaluate(context))
            return false;
        else
            return true;
    }

    /**
     * @see org.apache.velocity.runtime.parser.node.SimpleNode#value(org.apache.velocity.context.InternalContextAdapter)
     */
    public Object value( InternalContextAdapter context)
        throws MethodInvocationException
    {
        return (jjtGetChild(0).evaluate( context ) ? Boolean.FALSE : Boolean.TRUE) ;
    }
}
