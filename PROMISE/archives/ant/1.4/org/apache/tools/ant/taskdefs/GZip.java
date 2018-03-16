package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;

import java.io.*;
import java.util.zip.*;

/**
 * Compresses a file with the GZIP algorightm. Normally used to compress
 * non-compressed archives such as TAR files.
 *
 * @author James Davidson <a href="mailto:duncan@x180.com">duncan@x180.com</a>
 * @author Jon S. Stevens <a href="mailto:jon@clearink.com">jon@clearink.com</a>
 */
 
public class GZip extends Task {

    private File zipFile;
    private File source;
    
    public void setZipfile(File zipFilename) {
        zipFile = zipFilename;
    }

    public void setSrc(File src) {
        source = src;
    }

    public void execute() throws BuildException {
        if (zipFile == null) {
            throw new BuildException("zipfile attribute is required", location);
        }

        if (source == null) {
            throw new BuildException("src attribute is required", location);
        }

        log("Building gzip: " + zipFile.getAbsolutePath());
    
        GZIPOutputStream zOut = null;
        try {
            zOut = new GZIPOutputStream(new FileOutputStream(zipFile));
        
            if (source.isDirectory()) {
                log ("Cannot Gzip a directory!", Project.MSG_ERR);
            } else {
                zipFile(source, zOut);
            }
        } catch (IOException ioe) {
            String msg = "Problem creating gzip " + ioe.getMessage();
            throw new BuildException(msg, ioe, location);
        } finally {
            if (zOut != null) {
                try {
                    zOut.close();
                }
                catch (IOException e) {}
            }
        }
    }
    
    private void zipFile(InputStream in, GZIPOutputStream zOut)
        throws IOException
    {        
        byte[] buffer = new byte[8 * 1024];
        int count = 0;
        do {
            zOut.write(buffer, 0, count);
            count = in.read(buffer, 0, buffer.length);
        } while (count != -1);
    }
    
    private void zipFile(File file, GZIPOutputStream zOut)
        throws IOException
    {
        FileInputStream fIn = new FileInputStream(file);
        try {
            zipFile(fIn, zOut);
        } finally {
            fIn.close();
        }
    }
}
