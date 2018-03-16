package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.filters.StringInputStream;
import org.apache.tools.ant.filters.util.ChainReaderHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * Load a file's contents as Ant properties.
 *
 * @author Magesh Umasankar
 * @since Ant 1.5
 * @ant.task category="utility"
 */
public final class LoadProperties extends Task {

    /**
     * Source file
     */
    private File srcFile = null;

    /**
     * Holds filterchains
     */
    private final Vector filterChains = new Vector();

    /**
     * Sets the file to load.
     *
     * @param srcFile The new SrcFile value
     */
    public final void setSrcFile(final File srcFile) {
        this.srcFile = srcFile;
    }

    /**
     * read in a source file's contents and load them up as Ant properties
     *
     * @exception BuildException if something goes wrong with the build
     */
    public final void execute() throws BuildException {
        if (srcFile == null) {
            throw new BuildException("Source file not defined.");
        }

        if (!srcFile.exists()) {
            throw new BuildException("Source file does not exist.");
        }

        if (!srcFile.isFile()) {
            throw new BuildException("Source file is not a file.");
        }

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        Reader instream = null;

        try {
            final long len = srcFile.length();
            final int size = (int) len;

            fis = new FileInputStream(srcFile);
            bis = new BufferedInputStream(fis);
            instream = new InputStreamReader(bis);

            ChainReaderHelper crh = new ChainReaderHelper();
            crh.setBufferSize(size);
            crh.setPrimaryReader(instream);
            crh.setFilterChains(filterChains);
            crh.setProject(project);
            instream = crh.getAssembledReader();

            String text = crh.readFully(instream);

            if (text != null) {
                if (!text.endsWith("\n")) {
                    text = text + "\n";
                }

                final StringInputStream sis = new StringInputStream(text);
                final Properties props = new Properties();
                props.load(sis);
                final Enumeration e = props.keys();
                while (e.hasMoreElements()) {
                    final String key = (String) e.nextElement();
                    final String value = props.getProperty(key);
                    if (key != null && value != null
                            && value.trim().length() > 0) {
                        project.setNewProperty(key, value);
                    }
                }
                sis.close();
            }

        } catch (final IOException ioe) {
            final String message = "Unable to load file: " + ioe.toString();
            throw new BuildException(message, ioe, location);
        } catch (final BuildException be) {
            throw be;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ioex) {
            }
        }
    }

    /**
     * Adds a FilterChain.
     */
    public final void addFilterChain(FilterChain filter) {
        filterChains.addElement(filter);
    }

}
