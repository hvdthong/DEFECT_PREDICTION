import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;

/**
 *
 */
public class ASTObjectArray extends SimpleNode
{
    /**
     * @param id
     */
    public ASTObjectArray(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTObjectArray(Parser p, int id)
    {
        super(p, id);
    }


    /**
     * @see org.apache.velocity.runtime.parser.node.SimpleNode#jjtAccept(org.apache.velocity.runtime.parser.ParserVisitor, java.lang.Object)
     */
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    /**
     * @see org.apache.velocity.runtime.parser.node.SimpleNode#value(org.apache.velocity.context.InternalContextAdapter)
     */
    public Object value( InternalContextAdapter context)
        throws MethodInvocationException
    {
        int size = jjtGetNumChildren();

        List objectArray = new ArrayList();

        for (int i = 0; i < size; i++)
        {
            objectArray.add(jjtGetChild(i).value(context));
        }

        return objectArray;
    }
}
