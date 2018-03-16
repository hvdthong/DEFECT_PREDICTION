import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;

import java.math.BigInteger;

/**
 * Handles integer numbers.  The value will be either an Integer, a Long, or a BigInteger.
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 */
public class ASTIntegerLiteral extends SimpleNode
{

    private Number value = null;

    /**
     * @param id
     */
    public ASTIntegerLiteral(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTIntegerLiteral(Parser p, int id)
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
     * @see org.apache.velocity.runtime.parser.node.SimpleNode#init(org.apache.velocity.context.InternalContextAdapter, java.lang.Object)
     */
    public Object init( InternalContextAdapter context, Object data)
        throws TemplateInitException
    {
        /*
         *  init the tree correctly
         */

        super.init( context, data );

        /**
         * Determine the size of the item and make it an Integer, Long, or BigInteger as appropriate.
         */
         String str = getFirstToken().image;
         try
         {
             value = new Integer( str );
         }
         catch ( NumberFormatException E1 )
         {
            try
            {

                value = new Long( str );

            }
            catch ( NumberFormatException E2 )
            {

                value = new BigInteger( str );
            }
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
