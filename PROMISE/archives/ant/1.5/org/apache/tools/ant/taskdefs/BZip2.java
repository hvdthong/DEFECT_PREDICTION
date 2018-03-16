package org.apache.tools.ant.taskdefs;


import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.bzip2.CBZip2OutputStream;

/**
 * Compresses a file with the BZIP2 algorithm. Normally used to compress
 * non-compressed archives such as TAR files.
 *
 * @author Magesh Umasankar
 *
 * @since Ant 1.5
 *
 * @ant.task category="packaging"
 */

public class BZip2 extends Pack {
    protected void pack() {
        CBZip2OutputStream zOut = null;
        try {
            BufferedOutputStream bos =
                new BufferedOutputStream(new FileOutputStream(zipFile));
            bos.write('B');
            bos.write('Z');
            zOut = new CBZip2OutputStream(bos);
            zipFile(source, zOut);
        } catch (IOException ioe) {
            String msg = "Problem creating bzip2 " + ioe.getMessage();
            throw new BuildException(msg, ioe, location);
        } finally {
            if (zOut != null) {
                try {
                    zOut.close();
                } catch (IOException e) {}
            }
        }
    }
}
