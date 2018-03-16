import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.exception.MethodInvocationException;

/**
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ASTAndNode.java 75955 2004-03-03 23:23:08Z geirm $ 
 */
public class ASTAndNode extends SimpleNode
{
    public ASTAndNode(int id)
    {
        super(id);
    }

    public ASTAndNode(Parser p, int id)
    {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    /**
     *  Returns the value of the expression.
     *  Since the value of the expression is simply the boolean
     *  result of evaluate(), lets return that.
     */
    public Object value(InternalContextAdapter context )
        throws MethodInvocationException
    {
        return new Boolean( evaluate( context ) );
    }

    /**
     * logical and : 
     *   null && right = false
     *   left && null = false
     *   null && null = false
     */     
    public boolean evaluate( InternalContextAdapter context)
        throws MethodInvocationException
    {       
        Node left = jjtGetChild(0);
        Node right = jjtGetChild(1);

        /*
         *  if either is null, lets log and bail
         */

        if (left == null || right == null)
        {
            rsvc.error( ( left == null ? "Left" : "Right" ) + " side of '&&' operation is null."
                           + " Operation not possible. "
                           + context.getCurrentTemplateName() + " [line " + getLine() 
                           + ", column " + getColumn() + "]");
            return false;
        }
        
        /*
         *  short circuit the test.  Don't eval the RHS if the LHS is false
         */

        if( left.evaluate( context ) )
        {
            if ( right.evaluate( context ) )
            {
                return true;
            }
        }        
           
        return false;
    }
}

