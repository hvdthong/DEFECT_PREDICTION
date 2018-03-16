import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.Parser;

import org.apache.velocity.exception.MethodInvocationException;

public class ASTModNode extends SimpleNode
{
    public ASTModNode(int id)
    {
        super(id);
    }

    public ASTModNode(Parser p, int id)
    {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

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
            rsvc.error( ( left == null ? "Left" : "Right" ) + " side ("
                           + jjtGetChild( (left == null? 0 : 1) ).literal()
                           + ") of modulus operation has null value."
                           + " Operation not possible. "
                           + context.getCurrentTemplateName() + " [line " + getLine() 
                           + ", column " + getColumn() + "]");
            return null;
        }
        
        /*
         *  if not an Integer, not much we can do either
         */

        if ( !( left instanceof Integer )  || !( right instanceof Integer ))
        {
            rsvc.error( ( !( left instanceof Integer ) ? "Left" : "Right" ) 
                           + " side of modulus operation is not a valid type. "
                           + "Currently only integers (1,2,3...) and Integer type is supported. "
                           + context.getCurrentTemplateName() + " [line " + getLine() 
                           + ", column " + getColumn() + "]");
 
            return null;
        }

        /*
         *  check for divide by 0
         */

        if ( ( (Integer) right).intValue() == 0 )
        {
            rsvc.error( "Right side of modulus operation is zero. Must be non-zero. "
                           + context.getCurrentTemplateName() + " [line " + getLine() 
                           + ", column " + getColumn() + "]");
 
            return null;
        }

        return new Integer( ( (Integer) left ).intValue() % (  (Integer) right ).intValue() );
    }
}

