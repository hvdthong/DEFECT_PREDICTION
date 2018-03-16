import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.Parser;

/**
 * This class is responsible for handling the Else VTL control statement.
 * 
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ASTElseStatement.java 75955 2004-03-03 23:23:08Z geirm $ 
 */
public class ASTElseStatement extends SimpleNode
{
    public ASTElseStatement(int id)
    {
        super(id);
    }

    public ASTElseStatement(Parser p, int id)
    {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }
    
    /**
     * An ASTElseStatement always evaluates to
     * true. Basically behaves like an #if(true).
     */
    public boolean evaluate( InternalContextAdapter context)
    {
        return true;
    }        
}

