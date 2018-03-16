package org.apache.tools.ant.taskdefs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import org.apache.tools.ant.BuildException;

/**
 * Compresses a file with the GZIP algorithm. Normally used to compress
 * non-compressed archives such as TAR files.
 *
 * @since Ant 1.1
 *
 * @ant.task category="packaging"
 */

public class GZip extends Pack {
    /**
     * perform the GZip compression operation.
     */
    protected void pack() {
        GZIPOutputStream zOut = null;
        try {
            zOut = new GZIPOutputStream(new FileOutputStream(zipFile));
            zipFile(source, zOut);
        } catch (IOException ioe) {
            String msg = "Problem creating gzip " + ioe.getMessage();
            throw new BuildException(msg, ioe, getLocation());
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
