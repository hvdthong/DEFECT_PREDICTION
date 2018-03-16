import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.parser.Parser;

import org.apache.velocity.exception.MethodInvocationException;

import org.apache.velocity.app.event.EventCartridge;

/**
 * Node for the #set directive
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ASTSetDirective.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class ASTSetDirective extends SimpleNode
{
    private String leftReference = "";
    private Node right;
    private ASTReference left;
    boolean blather = false;

    public ASTSetDirective(int id)
    {
        super(id);
    }

    public ASTSetDirective(Parser p, int id)
    {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    /**
     *  simple init.  We can get the RHS and LHS as the the tree structure is static
     */
    public Object init(InternalContextAdapter context, Object data)
            throws Exception
    {
        /*
         *  init the tree correctly
         */

        super.init( context, data );

        right = getRightHandSide();
        left = getLeftHandSide();

        blather = rsvc.getBoolean(RuntimeConstants.RUNTIME_LOG_REFERENCE_LOG_INVALID, true);
 
        /*
         *  grab this now.  No need to redo each time
         */
        leftReference = left.getFirstToken().image.substring(1);

        return data;
    }        

    /**
     *   puts the value of the RHS into the context under the key of the LHS
     */
    public boolean render( InternalContextAdapter context, Writer writer)
        throws IOException, MethodInvocationException
    {
        /*
         *  get the RHS node, and it's value
         */

        Object value = right.value(context);

        /*
         * it's an error if we don't have a value of some sort
         */

        if ( value  == null)
        {
            /*
             *  first, are we supposed to say anything anyway?
             */
            if(blather)
            {
                EventCartridge ec = context.getEventCartridge();

                boolean doit = true;
               
                /*
                 *  if we have an EventCartridge...
                 */
                if (ec != null)
                {
                    doit = ec.shouldLogOnNullSet( left.literal(), right.literal() );
                }

                if (doit)
                {
                    rsvc.error("RHS of #set statement is null. Context will not be modified. " 
                                  + context.getCurrentTemplateName() + " [line " + getLine() 
                                  + ", column " + getColumn() + "]");
                }
            }

            return false;
        }                

        /*
         *  if the LHS is simple, just punch the value into the context
         *  otherwise, use the setValue() method do to it.
         *  Maybe we should always use setValue()
         */
        
        if (left.jjtGetNumChildren() == 0)
        {
            context.put( leftReference, value);
        }
        else
        {
            left.setValue(context, value);
        }
    
        return true;
    }

    /**
     *  returns the ASTReference that is the LHS of the set statememt
     */
    private ASTReference getLeftHandSide()
    {
        return (ASTReference) jjtGetChild(0);

    }

    /**
     *  returns the RHS Node of the set statement
     */
    private Node getRightHandSide()
    {
        return jjtGetChild(1);
    }
}
