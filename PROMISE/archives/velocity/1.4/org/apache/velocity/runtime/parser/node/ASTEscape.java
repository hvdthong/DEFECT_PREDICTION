import java.io.Writer;
import java.io.IOException;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.Parser;

/**
 * This class is responsible for handling Escapes
 *  in VTL.
 * 
 * Please look at the Parser.jjt file which is
 * what controls the generation of this class.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: ASTEscape.java 75955 2004-03-03 23:23:08Z geirm $ 
 */
public class ASTEscape extends SimpleNode 
{
    public String val;
    private char[] ctext;

    public ASTEscape(int id)
    {
        super(id);
    }
    
    public ASTEscape(Parser p, int id) 
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
        ctext =  val.toCharArray();
        return data;
    }

    public boolean render( InternalContextAdapter context, Writer writer)
        throws IOException
    {
        writer.write(ctext);
        return true;
    }
}
