import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.parser.Parser;

import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.StringReader;

import org.apache.velocity.runtime.RuntimeConstants;

/**
 * ASTStringLiteral support.  Will interpolate!
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @version $Id: ASTStringLiteral.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class ASTStringLiteral extends SimpleNode
{
    /* cache the value of the interpolation switch */
    private boolean interpolate = true;
    private SimpleNode nodeTree = null;
    private String image = "";
    private String interpolateimage = "";

    public ASTStringLiteral(int id)
    {
        super(id);
    }

    public ASTStringLiteral(Parser p, int id)
    {
        super(p, id);
    }
    
    /**
     *  init : we don't have to do much.  Init the tree (there 
     *  shouldn't be one) and then see if interpolation is turned on.
     */
    public Object init(InternalContextAdapter context, Object data) 
        throws Exception
    {
        /*
         *  simple habit...  we prollie don't have an AST beneath us
         */

        super.init(context, data);

        /*
         *  the stringlit is set at template parse time, so we can 
         *  do this here for now.  if things change and we can somehow 
         * create stringlits at runtime, this must
         *  move to the runtime execution path
         *
         *  so, only if interpolation is turned on AND it starts 
         *  with a " AND it has a  directive or reference, then we 
         *  can  interpolate.  Otherwise, don't bother.
         */

        interpolate = rsvc.getBoolean(RuntimeConstants.INTERPOLATE_STRINGLITERALS , true)
            && getFirstToken().image.startsWith("\"")
            && ((getFirstToken().image.indexOf('$') != -1) 
                 || (getFirstToken().image.indexOf('#') != -1));

        /*
         *  get the contents of the string, minus the '/" at each end
         */
        
        image = getFirstToken().image.substring(1, 
                                                getFirstToken().image.length() - 1);

        /*
         * tack a space on the end (dreaded <MORE> kludge)
         */

        interpolateimage = image + " ";

        if (interpolate)
        {
            /*
             *  now parse and init the nodeTree
             */
            BufferedReader br = new BufferedReader(new StringReader(interpolateimage));

            /*
             * it's possible to not have an initialization context - or we don't
             * want to trust the caller - so have a fallback value if so
             *
             *  Also, do *not* dump the VM namespace for this template
             */

            nodeTree  = rsvc.parse(br, (context != null) ?
                    context.getCurrentTemplateName() : "StringLiteral", false);

            /*
             *  init with context. It won't modify anything
             */

            nodeTree.init(context, rsvc);
        }

        return data;
    }

    /** Accept the visitor. **/
    public Object jjtAccept(ParserVisitor visitor, Object data)
    {
        return visitor.visit(this, data);
    }

    /**
     *  renders the value of the string literal
     *  If the properties allow, and the string literal contains a $ or a #
     *  the literal is rendered against the context
     *  Otherwise, the stringlit is returned.
     */
    public Object value(InternalContextAdapter context)
    {
        if (interpolate)
        {          
            try
            {
                /*
                 *  now render against the real context
                 */

                StringWriter writer = new StringWriter();
                nodeTree.render(context, writer);
                
                /*
                 * and return the result as a String
                 */

                String ret = writer.toString();

                /*
                 *  remove the space from the end (dreaded <MORE> kludge)
                 */

                return ret.substring(0, ret.length() - 1);
            }
            catch(Exception e)
            {
                /* 
                 *  eh.  If anything wrong, just punt 
                 *  and output the literal 
                 */
                rsvc.error("Error in interpolating string literal : " + e);
            }
        }
        
        /*
         *  ok, either not allowed to interpolate, there wasn't 
         *  a ref or directive, or we failed, so
         *  just output the literal
         */

        return image;
    }
}
