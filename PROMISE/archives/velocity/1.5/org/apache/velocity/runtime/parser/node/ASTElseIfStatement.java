import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;

/**
 * This class is responsible for handling the ElseIf VTL control statement.
 *
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ASTElseIfStatement.java 463298 2006-10-12 16:10:32Z henning $
*/
public class ASTElseIfStatement extends SimpleNode
{
    /**
     * @param id
     */
    public ASTElseIfStatement(int id)
    {
        super(id);
    }

    /**
     * @param p
     * @param id
     */
    public ASTElseIfStatement(Parser p, int id)
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
     * An ASTElseStatement is true if the expression
     * it contains evaluates to true. Expressions know
     * how to evaluate themselves, so we do that
     * here and return the value back to ASTIfStatement
     * where this node was originally asked to evaluate
     * itself.
     * @param context
     * @return True if all childs are true.
     * @throws MethodInvocationException
     */
    public boolean evaluate ( InternalContextAdapter context)
        throws MethodInvocationException
    {
        return jjtGetChild(0).evaluate(context);
    }

    /**
     * @see org.apache.velocity.runtime.parser.node.SimpleNode#render(org.apache.velocity.context.InternalContextAdapter, java.io.Writer)
     */
    public boolean render( InternalContextAdapter context, Writer writer)
        throws IOException,MethodInvocationException,
        	ResourceNotFoundException, ParseErrorException
    {
        return jjtGetChild(1).render( context, writer );
    }
}
