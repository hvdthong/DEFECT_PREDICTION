import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.runtime.parser.Parser;

/**
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ASTOrNode.java 685370 2008-08-12 23:36:35Z nbubna $
*/
public class ASTOrNode extends SimpleNode
{
    /**
     * @param id
     */
    public ASTOrNode(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTOrNode(Parser p, int id)
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
     * @return The Expression value.
     * @throws MethodInvocationException
     */
    public Object value(InternalContextAdapter context )
        throws MethodInvocationException
    {
        return evaluate(context) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     *  the logical or :
     *    the rule :
     *      left || null -> left
     *      null || right -> right
     *      null || null -> false
     *      left || right ->  left || right
     * @param context
     * @return The evaluation result.
     * @throws MethodInvocationException
     */
    public boolean evaluate( InternalContextAdapter context)
        throws MethodInvocationException
    {
        Node left = jjtGetChild(0);
        Node right = jjtGetChild(1);

        /*
         *  if the left is not null and true, then true
         */

        if (left != null && left.evaluate( context ) )
            return true;

        /*
         *  same for right
         */

        if ( right != null && right.evaluate( context ) )
            return true;

        return false;
    }
}





