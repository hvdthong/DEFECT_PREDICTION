import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.runtime.parser.Parser;

/**
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ASTAndNode.java 687184 2008-08-19 22:16:39Z nbubna $
 */
public class ASTAndNode extends SimpleNode
{
    /**
     * @param id
     */
    public ASTAndNode(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTAndNode(Parser p, int id)
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
     *  Returns the value of the expression.
     *  Since the value of the expression is simply the boolean
     *  result of evaluate(), lets return that.
     * @param context
     * @return The value of the expression.
     * @throws MethodInvocationException
     */
    public Object value(InternalContextAdapter context)
        throws MethodInvocationException
    {
        return evaluate(context) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * logical and :
     *   null && right = false
     *   left && null = false
     *   null && null = false
     * @param context
     * @return True if both sides are true.
     * @throws MethodInvocationException
     */
    public boolean evaluate( InternalContextAdapter context)
        throws MethodInvocationException
    {
        Node left = jjtGetChild(0);
        Node right = jjtGetChild(1);

        /*
         * null == false
         */
        if (left == null || right == null)
        {
            return false;
        }

        /*
         *  short circuit the test.  Don't eval the RHS if the LHS is false
         */

        if( left.evaluate( context ) )
        {
            if ( right.evaluate( context ) )
            {
                return true;
            }
        }

        return false;
    }
}

