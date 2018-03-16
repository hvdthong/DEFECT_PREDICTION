import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.Writer;
import java.io.IOException;

/**
 *  This interface caraterize objects other than ASTNodes that can be rendered
 *  to a writer using a context.
 *
 * @author <a href="mailto:claude.brisson@gmail.com">Claude Brisson</a>
 * @version $Id:$
 * @since 1.6
 */

public interface Renderable {

    public boolean render( InternalContextAdapter context, Writer writer)
        throws IOException, MethodInvocationException, ParseErrorException, ResourceNotFoundException;

}
