package org.apache.tools.ant.taskdefs.cvslib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;

/**
 * A dummy stream handler that just passes stuff to the parser.
 *
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

