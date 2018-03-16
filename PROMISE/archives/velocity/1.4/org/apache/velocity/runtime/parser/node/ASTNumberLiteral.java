import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.Parser;

public class ASTNumberLiteral extends SimpleNode
{
    private Integer value = null;

    public ASTNumberLiteral(int id)
    {
        super(id);
    }

    public ASTNumberLiteral(Parser p, int id)
    {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    /**
     *  Initialization method - doesn't do much but do the object
     *  creation.  We only need to do it once.
     */
    public Object init( InternalContextAdapter context, Object data) 
        throws Exception
    {
        /*
         *  init the tree correctly
         */

        super.init( context, data );

        value = new Integer( getFirstToken().image );

        return data;
    } 

    public Object value( InternalContextAdapter context)
    {
        return value;
    }

}
