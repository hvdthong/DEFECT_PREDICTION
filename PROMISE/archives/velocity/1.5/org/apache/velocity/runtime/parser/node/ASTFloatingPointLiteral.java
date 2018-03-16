import java.math.BigDecimal;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;


/**
 * Handles floating point numbers.  The value will be either a Double
 * or a BigDecimal.
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 */
public class ASTFloatingPointLiteral extends SimpleNode
{

    private Number value = null;

    /**
     * @param id
     */
    public ASTFloatingPointLiteral(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTFloatingPointLiteral(Parser p, int id)
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
     *  Initialization method - doesn't do much but do the object
     *  creation.  We only need to do it once.
     * @param context
     * @param data
     * @return The data object.
     * @throws TemplateInitException
     */
    public Object init( InternalContextAdapter context, Object data)
        throws TemplateInitException
    {
        /*
         *  init the tree correctly
         */

        super.init( context, data );

        /**
         * Determine the size of the item and make it a Double or BigDecimal as appropriate.
         */
         String str = getFirstToken().image;
         try
         {
             value = new Double( str );

         } catch ( NumberFormatException E1 )
         {

            value = new BigDecimal( str );

        }

        return data;
    }

    /**
     * @see org.apache.velocity.runtime.parser.node.SimpleNode#value(org.apache.velocity.context.InternalContextAdapter)
     */
    public Object value( InternalContextAdapter context)
    {
        return value;
    }


}
