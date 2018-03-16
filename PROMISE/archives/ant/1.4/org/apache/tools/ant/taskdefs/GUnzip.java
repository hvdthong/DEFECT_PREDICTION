package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;

import java.io.*;
import java.util.zip.*;

/**
 * Expands a file that has been compressed with the GZIP
 * algorightm. Normally used to compress non-compressed archives such
 * as TAR files.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */

public class GUnzip extends Task {

    private File source;
    private File dest;

    public void setSrc(String src) {
        source = project.resolveFile(src);
    }

    public void setDest(String dest) {
        this.dest = project.resolveFile(dest);
    }

    public void execute() throws BuildException {
        if (source == null) {
            throw new BuildException("No source for gunzip specified", location);
        }

        if (!source.exists()) {
            throw new BuildException("source doesn't exist", location);
        }

        if (source.isDirectory()) {
            throw new BuildException("Cannot expand a directory", location);
        }

        if (dest == null) {
            dest = new File(source.getParent());
        }

        if (dest.isDirectory()) {
            String sourceName = source.getName();
            int len = sourceName.length();
            if (len > 3
                && ".gz".equalsIgnoreCase(sourceName.substring(len-3))) {
                dest = new File(dest, sourceName.substring(0, len-3));
            } else {
                dest = new File(dest, sourceName);
            }
        }

        if (source.lastModified() > dest.lastModified()) {
            log("Expanding "+ source.getAbsolutePath() + " to "
                        + dest.getAbsolutePath());

            FileOutputStream out = null;
            GZIPInputStream zIn = null;
            try {
                out = new FileOutputStream(dest);
                zIn = new GZIPInputStream(new FileInputStream(source));
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
