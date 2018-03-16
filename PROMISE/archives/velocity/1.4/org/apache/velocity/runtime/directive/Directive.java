import java.io.Writer;
import java.io.IOException;

import org.apache.velocity.runtime.RuntimeServices;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.node.Node;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;


/**
 * Base class for all directives used in Velocity.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @version $Id: Directive.java 75955 2004-03-03 23:23:08Z geirm $ 
 */
public abstract class Directive implements DirectiveConstants, Cloneable
{
    private int line = 0;
    private int column = 0;

    protected RuntimeServices rsvc = null;

    /** Return the name of this directive */
    public abstract String getName();
    
    /** Get the directive type BLOCK/LINE */
    public abstract int getType();

    /** Allows the template location to be set */
    public void setLocation( int line, int column )
    {
        this.line = line;
        this.column = column;
    }

    /** for log msg purposes */
    public int getLine()
    {
        return line;
    }
    
    /** for log msg purposes */
    public int getColumn()
    {
        return column;
    }     

    /**
     * How this directive is to be initialized.
     */
    public void init( RuntimeServices rs, InternalContextAdapter context,
                      Node node)
        throws Exception
    {
        rsvc = rs;


    }
    
    /**
     * How this directive is to be rendered 
     */
    public abstract boolean render( InternalContextAdapter context, 
                                    Writer writer, Node node )       
           throws IOException, ResourceNotFoundException, ParseErrorException, 
                MethodInvocationException;
}
