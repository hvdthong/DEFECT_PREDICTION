import java.io.Writer;
import java.io.IOException;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.*;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;


public class ASTIfStatement extends SimpleNode
{    
    public ASTIfStatement(int id)
    {
        super(id);
    }
    
    public ASTIfStatement(Parser p, int id)
    {
        super(p, id);
    }
    
    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }
    
    public boolean render( InternalContextAdapter context, Writer writer)
        throws IOException,MethodInvocationException,
        	ResourceNotFoundException, ParseErrorException
    {
        /*
         * Check if the #if(expression) construct evaluates to true:
         * if so render and leave immediately because there
         * is nothing left to do!
         */
        if (jjtGetChild(0).evaluate(context))
        {
            jjtGetChild(1).render(context, writer);
            return true;
        }
    
        int totalNodes = jjtGetNumChildren();
        
        /*
         * Now check the remaining nodes left in the
         * if construct. The nodes are either elseif
         *  nodes or else nodes. Each of these node
         * types knows how to evaluate themselves. If
         * a node evaluates to true then the node will
         * render itself and this method will return
         * as there is nothing left to do.
         */
        for (int i = 2; i < totalNodes; i++)
        {
            if (jjtGetChild(i).evaluate(context))
            {
                jjtGetChild(i).render(context, writer);
                return true;
            }
        }
    
        /*
         * This is reached when an ASTIfStatement
         * consists of an if/elseif sequence where
         * none of the nodes evaluate to true.
         */
        return true;
    }

    public void process( InternalContextAdapter context, ParserVisitor visitor)
    {
    }
}






