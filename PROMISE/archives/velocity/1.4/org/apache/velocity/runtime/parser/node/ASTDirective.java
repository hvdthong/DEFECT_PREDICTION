import java.io.Writer;
import java.io.IOException;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.Parser;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * This class is responsible for handling the pluggable
 * directives in VTL. ex.  #foreach()
 * 
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author <a href="mailto:kav@kav.dk">Kasper Nielsen</a>
 * @version $Id: ASTDirective.java 75955 2004-03-03 23:23:08Z geirm $ 
 */
public class ASTDirective extends SimpleNode
{
    private Directive directive;
    private String directiveName = "";
    private boolean isDirective;

    public ASTDirective(int id)
    {
        super(id);
    }

    public ASTDirective(Parser p, int id)
    {
        super(p, id);
    }


    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }
    
    public Object init( InternalContextAdapter context, Object data) 
        throws Exception
    {
        super.init( context, data );

        /*
         *  only do things that are not context dependant
         */

        if (parser.isDirective( directiveName ))
        {
            isDirective = true;
            
            directive = (Directive) parser.getDirective( directiveName )
                .getClass().newInstance();
    
            directive.init(rsvc, context,this);

            directive.setLocation( getLine(), getColumn() );
        }          
        else if (rsvc.isVelocimacro( directiveName, context.getCurrentTemplateName()  )) 
        {
            /*
             *  we seem to be a Velocimacro.
             */

            isDirective = true;
            directive = (Directive) rsvc.getVelocimacro( directiveName,  context.getCurrentTemplateName() );

            directive.init( rsvc, context, this );
            directive.setLocation( getLine(), getColumn() );
        } 
        else
        {
            isDirective = false;
        }            
    
        return data;
    }

    public boolean render( InternalContextAdapter context, Writer writer)
        throws IOException,MethodInvocationException, ResourceNotFoundException, ParseErrorException
    {
        /*
         *  normal processing
         */

        if (isDirective)
        {           
            directive.render(context, writer, this);
        }
        else
        {
            writer.write( "#");
            writer.write( directiveName );
        }

        return true;
    }

    /**
     *   Sets the directive name.  Used by the parser.  This keeps us from having to 
     *   dig it out of the token stream and gives the parse the change to override.
     */
    public void setDirectiveName( String str )
    {
        directiveName = str;
        return;
    }

    /**
     *  Gets the name of this directive.
     */
    public String getDirectiveName()
    {
        return directiveName;
    }
}


