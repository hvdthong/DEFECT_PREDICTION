import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.Parser;

/**
 * Handles number addition of nodes.<br><br>
 *
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 * @author <a href="mailto:pero@antaramusic.de">Peter Romianowski</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ASTAddNode.java 712887 2008-11-11 00:27:50Z nbubna $
 */
public class ASTAddNode extends ASTMathNode
{
    /**
     * @param id
     */
    public ASTAddNode(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTAddNode(Parser p, int id)
    {
        super(p, id);
    }

    protected Object handleSpecial(Object left, Object right, InternalContextAdapter context)
    {
        /*
         * shall we try for strings?
         */
        if (left instanceof String || right instanceof String)
        {
            if (left == null)
            {
                left = jjtGetChild(0).literal();
            }
            else if (right == null)
            {
                right = jjtGetChild(1).literal();
            }
            return left.toString().concat(right.toString());
        }
        return null;
    }

    public Number perform(Number left, Number right, InternalContextAdapter context)
    {
        return MathUtils.add(left, right);
    }

}




