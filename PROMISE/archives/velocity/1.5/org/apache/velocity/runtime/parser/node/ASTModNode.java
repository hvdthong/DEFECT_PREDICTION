import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;
import org.apache.velocity.util.TemplateNumber;

/**
 *
 */
public class ASTModNode extends SimpleNode
{
    /**
     * @param id
     */
    public ASTModNode(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTModNode(Parser p, int id)
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
                           + ") of modulus operation has null value."
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
         * Both values must be a number.
         */
        if ( ! (left instanceof Number) || ! (right instanceof Number) )
        {

            log.error((!(left instanceof Number) ? "Left" : "Right")
                           + " side "
                           + " of modulus operation is not a Number. "
                           + context.getCurrentTemplateName() + " [line " + getLine()
                           + ", column " + getColumn() + "]");
            return null;

        }

        /*
         *  check for divide / modulo by 0
         */
        if ( MathUtils.isZero ( (Number) right ) )
        {

            log.error("Right side of modulus operation is zero. Must be non-zero. "
                           + context.getCurrentTemplateName() + " [line " + getLine()
                           + ", column " + getColumn() + "]");
            return null;

        }

        return MathUtils.modulo ((Number)left, (Number)right);

    }
}

