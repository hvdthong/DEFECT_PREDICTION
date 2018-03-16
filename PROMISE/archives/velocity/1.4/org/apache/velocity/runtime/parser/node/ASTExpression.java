import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.Parser;

import org.apache.velocity.exception.MethodInvocationException;

public class ASTExpression extends SimpleNode
{
    public ASTExpression(int id)
    {
        super(id);
    }

    public ASTExpression(Parser p, int id)
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
        return jjtGetChild(0).evaluate(context);
    }

    public Object value( InternalContextAdapter context)
        throws MethodInvocationException
    {
        return jjtGetChild(0).value(context);
    }
}
