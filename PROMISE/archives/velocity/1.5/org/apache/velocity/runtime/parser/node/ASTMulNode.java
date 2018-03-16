import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;
import org.apache.velocity.util.TemplateNumber;

/**
 * Handles multiplication<br><br>
 *
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 * @author <a href="mailto:pero@antaramusic.de">Peter Romianowski</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ASTMulNode.java 463298 2006-10-12 16:10:32Z henning $
 */
public class ASTMulNode extends SimpleNode
{
    /**
     * @param id
     */
    public ASTMulNode(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTMulNode(Parser p, int id)
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
     *  computes the product of the two args.
     * @param context
     *  @return result or null
     * @throws MethodInvocationException
     */
    public Object value( InternalContextAdapter context )
        throws MethodInvocationException
    {
        /*
         *  get the two args
         */

        Object left = jjtGetChild(0).value( context );
        Object right = jjtGetChild(1).value( context );

        /*
         *  if either is null, lets log and bail
         */

        if (left == null || right == null)
        {
            log.error((left == null ? "Left" : "Right")
                           + " side ("
                           + jjtGetChild( (left == null? 0 : 1) ).literal()
                           + ") of multiplication operation has null value."
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
         *  if not a Number, not much we can do either
         */

        if ( !( left instanceof Number )  || !( right instanceof Number ))
        {
            log.error((!(left instanceof Number) ? "Left" : "Right")
                           + " side of multiplication operation is not a Number. "
                           +  context.getCurrentTemplateName() + " [line " + getLine()
                           + ", column " + getColumn() + "]");

            return null;
        }

        return MathUtils.multiply( (Number)left, (Number)right);
    }
}




