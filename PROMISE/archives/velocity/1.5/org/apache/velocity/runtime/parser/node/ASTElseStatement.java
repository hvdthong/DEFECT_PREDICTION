import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;

/**
 * This class is responsible for handling the Else VTL control statement.
 *
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ASTElseStatement.java 463298 2006-10-12 16:10:32Z henning $
 */
public class ASTElseStatement extends SimpleNode
{
    /**
     * @param id
     */
    public ASTElseStatement(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTElseStatement(Parser p, int id)
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
     * An ASTElseStatement always evaluates to
     * true. Basically behaves like an #if(true).
     * @param context
     * @return Always true.
     */
    public boolean evaluate( InternalContextAdapter context)
    {
        return true;
    }
}

