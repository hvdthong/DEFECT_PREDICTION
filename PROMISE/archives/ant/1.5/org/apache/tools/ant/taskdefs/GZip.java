package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;

import java.io.IOException;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Compresses a file with the GZIP algorithm. Normally used to compress
 * non-compressed archives such as TAR files.
 *
 * @author James Davidson <a href="mailto:duncan@x180.com">duncan@x180.com</a>
 * @author Jon S. Stevens <a href="mailto:jon@clearink.com">jon@clearink.com</a>
 * @author Magesh Umasankar
 *
 * @since Ant 1.1
 *
 * @ant.task category="packaging"
 */

public class GZip extends Pack {
    protected void pack() {
        GZIPOutputStream zOut = null;
        try {
            zOut = new GZIPOutputStream(new FileOutputStream(zipFile));
            zipFile(source, zOut);
        } catch (IOException ioe) {
            String msg = "Problem creating gzip " + ioe.getMessage();
            throw new BuildException(msg, ioe, location);
        } finally {
            if (zOut != null) {
                try {
                    zOut.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
