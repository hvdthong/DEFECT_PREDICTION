import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.*;

import org.apache.velocity.exception.MethodInvocationException;

public class ASTNotNode extends SimpleNode
{
    public ASTNotNode(int id)
    {
        super(id);
    }

    public ASTNotNode(Parser p, int id)
    {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    public boolean evaluate( InternalContextAdapter context)
        throws MethodInvocationException
    {
        if (jjtGetChild(0).evaluate(context))
            return false;
        else
            return true;
    }

    public Object value( InternalContextAdapter context)
        throws MethodInvocationException
    {
        return (jjtGetChild(0).evaluate( context ) ? Boolean.FALSE : Boolean.TRUE) ;
    }
}
