import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.parser.Parser;

/**
 * handles the range 'operator'  [ n .. m ]
 *
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 */
public class ASTIntegerRange extends SimpleNode
{
    /**
     * @param id
     */
    public ASTIntegerRange(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTIntegerRange(Parser p, int id)
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
     *  does the real work.  Creates an Vector of Integers with the
     *  right value range
     *
     *  @param context  app context used if Left or Right of .. is a ref
     *  @return Object array of Integers
     * @throws MethodInvocationException
     */
    public Object value( InternalContextAdapter context)
        throws MethodInvocationException
    {
        /*
         *  get the two range ends
         */

        Object left = jjtGetChild(0).value( context );
        Object right = jjtGetChild(1).value( context );

        /*
         *  if either is null, lets log and bail
         */

        if (left == null || right == null)
        {
            log.error((left == null ? "Left" : "Right")
                           + " side of range operator [n..m] has null value."
                           + " Operation not possible. "
                           + Log.formatFileString(this));
            return null;
        }

        /*
         *  if not a Number, not much we can do either
         */

        if ( !( left instanceof Number )  || !( right instanceof Number ))
        {
            log.error((!(left instanceof Number) ? "Left" : "Right")
                           + " side of range operator is not a valid type. "
                           + "Currently only integers (1,2,3...) and the Number type are supported. "
                           + Log.formatFileString(this));
            return null;
        }


        /*
         *  get the two integer values of the ends of the range
         */

        int l = ((Number) left).intValue() ;
        int r = ((Number) right).intValue();

        /*
         *  find out how many there are
         */

        int nbrElements = Math.abs( l - r );
        nbrElements += 1;

        /*
         *  Determine whether the increment is positive or negative.
         */

        int delta = ( l >= r ) ? -1 : 1;

        /*
         * Fill the range with the appropriate values.
         */

        List elements = new ArrayList(nbrElements);
        int value = l;

        for (int i = 0; i < nbrElements; i++)
        {
            elements.add(new Integer(value));
            value += delta;
        }

        return elements;
    }
}
