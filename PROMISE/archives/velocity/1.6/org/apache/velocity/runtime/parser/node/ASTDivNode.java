import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MathException;
import org.apache.velocity.runtime.parser.Parser;

/**
 * Handles number division of nodes<br><br>
 *
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 * @author <a href="mailto:pero@antaramusic.de">Peter Romianowski</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ASTDivNode.java 691048 2008-09-01 20:26:11Z nbubna $
 */
public class ASTDivNode extends ASTMathNode
{
    /**
     * @param id
     */
    public ASTDivNode(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTDivNode(Parser p, int id)
    {
        super(p, id);
    }

    public Number perform(Number left, Number right, InternalContextAdapter context)
    {
        /*
         * check for divide by 0
         */
        if (MathUtils.isZero(right))
        {
            String msg = "Right side of division operation is zero. Must be non-zero. "
                          + getLocation(context);
            if (strictMode)
            {
                log.error(msg);
                throw new MathException(msg);
            }
            else
            {
                log.debug(msg);
                return null;
            }
        }
        return MathUtils.divide(left, right);
    }
}
