import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;
import org.apache.velocity.util.TemplateNumber;

/**
 *  Handles <code>arg1  == arg2</code>
 *
 *  This operator requires that the LHS and RHS are both of the
 *  same Class OR both are subclasses of java.lang.Number
 *
 *  @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 *  @author <a href="mailto:pero@antaramusic.de">Peter Romianowski</a>
 *  @version $Id: ASTEQNode.java 463298 2006-10-12 16:10:32Z henning $
 */
public class ASTEQNode extends SimpleNode
{
    /**
     * @param id
     */
    public ASTEQNode(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTEQNode(Parser p, int id)
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
     *   Calculates the value of the logical expression
     *
     *     arg1 == arg2
     *
     *   All class types are supported.   Uses equals() to
     *   determine equivalence.  This should work as we represent
     *   with the types we already support, and anything else that
     *   implements equals() to mean more than identical references.
     *
     *
     *  @param context  internal context used to evaluate the LHS and RHS
     *  @return true if equivalent, false if not equivalent,
     *          false if not compatible arguments, or false
     *          if either LHS or RHS is null
     * @throws MethodInvocationException
     */
    public boolean evaluate(InternalContextAdapter context)
        throws MethodInvocationException
    {
        Object left = jjtGetChild(0).value(context);
        Object right = jjtGetChild(1).value(context);

        /*
         *  they could be null if they are references and not in the context
         */

        if (left == null || right == null)
        {
            log.error((left == null ? "Left" : "Right")
                           + " side ("
                           + jjtGetChild( (left == null? 0 : 1) ).literal()
                           + ") of '==' operation "
                           + "has null value. "
                           + "If a reference, it may not be in the context."
                           + " Operation not possible. "
                           + context.getCurrentTemplateName() + " [line " + getLine()
                           + ", column " + getColumn() + "]");
            return false;
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
        * If comparing Numbers we do not care about the Class.
        */

       if (left instanceof Number && right instanceof Number)
       {
           return MathUtils.compare( (Number)left, (Number)right) == 0;
       }



       /**
        * assume that if one class is a subclass of the other
        * that we should use the equals operator
        */

        if (left.getClass().isAssignableFrom(right.getClass()) ||
                right.getClass().isAssignableFrom(left.getClass()) )
        {
            return left.equals( right );
        }
        else
        {
            /**
             * Compare the String representations
             */
            if ((left.toString() == null) || (right.toString() == null))
            {
        	boolean culprit =  (left.toString() == null);
                log.error((culprit ? "Left" : "Right")
                        + " string side "
                        + "String representation ("
                        + jjtGetChild((culprit ? 0 : 1) ).literal()
                        + ") of '!=' operation has null value."
                        + " Operation not possible. "
                        + context.getCurrentTemplateName() + " [line " + getLine()
                        + ", column " + getColumn() + "]");

                return false;
            }

            else
            {
                return left.toString().equals(right.toString());
            }
        }

    }

    /**
     * @see org.apache.velocity.runtime.parser.node.SimpleNode#value(org.apache.velocity.context.InternalContextAdapter)
     */
    public Object value(InternalContextAdapter context)
        throws MethodInvocationException
    {
        return evaluate(context) ? Boolean.TRUE : Boolean.FALSE;
    }
}
