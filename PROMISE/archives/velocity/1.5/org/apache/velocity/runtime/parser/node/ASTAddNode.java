import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;

import org.apache.velocity.exception.MethodInvocationException;

import org.apache.velocity.util.TemplateNumber;

/**
 *
 */
public class ASTAddNode extends SimpleNode
{
    /**
     * @param id
     */
    public ASTAddNode(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTAddNode(Parser p, int id)
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
     *  computes the sum of the two nodes.
     * @param context
     *  @return result or null
     * @throws MethodInvocationException
     */
    public Object value( InternalContextAdapter context)
        throws MethodInvocationException
    {
        /*
         *  get the two addends
         */

        Object left = jjtGetChild(0).value(context);
        Object right = jjtGetChild(1).value(context);

        /*
         *  if either is null, lets log and bail
         */

        if (left == null || right == null)
        {
            log.error((left == null ? "Left" : "Right")
                           + " side ("
                           + jjtGetChild( (left == null? 0 : 1) ).literal()
                           + ") of addition operation has null value."
                           + " Operation not possible. "
                           + context.getCurrentTemplateName() + " [line " + getLine()
                           + ", column " + getColumn() + "]");
            return null;
        }

        /*
         *  convert to Number if applicable
         */
        if (left instanceof TemplateNumber)
        {
           left = ( (TemplateNumber) left).getAsNumber();
        }
        if (right instanceof TemplateNumber)
        {
           right = ( (TemplateNumber) right).getAsNumber();
        }

        /*
         * Arithmetic operation.
         */
        if (left instanceof Number && right instanceof Number)
        {
            return MathUtils.add((Number)left, (Number)right);
        }

        /*
         * shall we try for strings?
         */
        if (left instanceof String || right instanceof String)
        {
            return left.toString().concat(right.toString());
        }
        /*
         *  if not a Number or Strings, not much we can do right now
         */
        log.error((!(left instanceof Number || left instanceof String) ? "Left" : "Right")
                       + " side of addition operation is not a valid type. "
                       + "Currently only Strings, numbers (1,2,3...) and Number type are supported. "
                       + context.getCurrentTemplateName() + " [line " + getLine()
                       + ", column " + getColumn() + "]");

        return null;
    }
}




