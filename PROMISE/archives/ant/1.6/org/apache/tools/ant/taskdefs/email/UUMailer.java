package org.apache.tools.ant.taskdefs.email;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.tools.ant.BuildException;
import sun.misc.UUEncoder;

/**
 * An emailer that uuencodes attachments.
 *
 * @since Ant 1.5
 */
class UUMailer extends PlainMailer {
    protected void attach(File file, PrintStream out)
         throws IOException {
        if (!file.exists() || !file.canRead()) {
            throw new BuildException("File \"" + file.getName()
                 + "\" does not exist or is not "
                 + "readable.");
        }

        FileInputStream finstr = new FileInputStream(file);

        try {
            BufferedInputStream in = new BufferedInputStream(finstr);
            UUEncoder encoder = new UUEncoder(file.getName());

            encoder.encode(in, out);

        } finally {
            finstr.close();
        }
    }
}

