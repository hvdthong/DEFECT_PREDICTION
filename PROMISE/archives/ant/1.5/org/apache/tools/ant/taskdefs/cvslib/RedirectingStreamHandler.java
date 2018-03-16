package org.apache.tools.ant.taskdefs.cvslib;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;
import org.apache.tools.ant.BuildException;

/**
 * A dummy stream handler that just passes stuff to the parser.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision: 274041 $ $Date: 2003-02-10 22:25:38 +0800 (周一, 2003-02-10) $
 */
class RedirectingStreamHandler
     extends PumpStreamHandler {
    RedirectingStreamHandler(final ChangeLogParser parser) {
        super(new RedirectingOutputStream(parser),
            new ByteArrayOutputStream());
    }


    String getErrors() {
        try {
            final ByteArrayOutputStream error 
                = (ByteArrayOutputStream) getErr();

            return error.toString("ASCII");
        } catch (final Exception e) {
            return null;
        }
    }


    public void stop() {
        super.stop();
        try {
            getErr().close();
            getOut().close();
        } catch (final IOException e) {
            throw new BuildException(e);
        }
    }
}

