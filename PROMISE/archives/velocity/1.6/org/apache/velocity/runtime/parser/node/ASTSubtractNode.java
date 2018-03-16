import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.Parser;

/**
 * Handles subtraction of nodes (in #set() )<br><br>
 *
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 * @author <a href="mailto:pero@antaramusic.de">Peter Romianowski</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ASTSubtractNode.java 691048 2008-09-01 20:26:11Z nbubna $
 */
public class ASTSubtractNode extends ASTMathNode
{
    /**
     * @param id
     */
    public ASTSubtractNode(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTSubtractNode(Parser p, int id)
    {
        super(p, id);
    }

    public Number perform(Number left, Number right, InternalContextAdapter context)
    {
        return MathUtils.subtract(left, right);
    }
}



