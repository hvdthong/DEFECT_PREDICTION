package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * Expands a file that has been compressed with the GZIP
 * algorithm. Normally used to compress non-compressed archives such
 * as TAR files.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author Magesh Umasankar
 *
 * @since Ant 1.1
 *
 * @ant.task category="packaging"
 */

public class GUnzip extends Unpack {

    private static final String DEFAULT_EXTENSION = ".gz";

    protected String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    protected void extract() {
        if (source.lastModified() > dest.lastModified()) {
            log("Expanding " + source.getAbsolutePath() + " to "
                        + dest.getAbsolutePath());

            FileOutputStream out = null;
            GZIPInputStream zIn = null;
            FileInputStream fis = null;
            try {
                out = new FileOutputStream(dest);
                fis = new FileInputStream(source);
                zIn = new GZIPInputStream(fis);
                byte[] buffer = new byte[8 * 1024];
                int count = 0;
                do {
                    out.write(buffer, 0, count);
                    count = zIn.read(buffer, 0, buffer.length);
                } while (count != -1);
            } catch (IOException ioe) {
                String msg = "Problem expanding gzip " + ioe.getMessage();
                throw new BuildException(msg, ioe, location);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException ioex) {}
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ioex) {}
                }
                if (zIn != null) {
                    try {
                        zIn.close();
                    } catch (IOException ioex) {}
                }
            }
        }
    }
}
