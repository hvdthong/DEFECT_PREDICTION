import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.Resource;

/**
 * Base class for directives which do input operations
 * (e.g. <code>#include()</code>, <code>#parse()</code>, etc.).
 *
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @since 1.4
 */
public abstract class InputBase extends Directive
{
    /**
     * Decides the encoding used during input processing of this
     * directive.
     *
     * Get the resource, and assume that we use the encoding of the
     * current template the 'current resource' can be
     * <code>null</code> if we are processing a stream....
     *
     * @param context The context to derive the default input encoding
     * from.
     * @return The encoding to use when processing this directive.
     */
    protected String getInputEncoding(InternalContextAdapter context)
    {
        Resource current = context.getCurrentResource();
        if (current != null)
        {
            return current.getEncoding();
        }
        else
        {
            return (String) rsvc.getProperty(RuntimeConstants.INPUT_ENCODING);
        }
    }
}
