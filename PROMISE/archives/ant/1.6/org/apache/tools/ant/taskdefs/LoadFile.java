package org.apache.tools.ant.taskdefs;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.filters.util.ChainReaderHelper;
import org.apache.tools.ant.types.FilterChain;

/**
 * Load a file into a property
 *
 * @since Ant 1.5
 * @ant.task category="utility"
 */
public class LoadFile extends Task {

    /**
     * source file, usually null
     */
    private File srcFile = null;

    /**
     * what to do when it goes pear-shaped
     */
    private boolean failOnError = true;

    /**
     * Encoding to use for filenames, defaults to the platform's default
     * encoding.
     */
    private String encoding = null;

    /**
     * name of property
     */
    private String property = null;

    /**
     * Holds FilterChains
     */
    private final Vector filterChains = new Vector();

    /**
     * Encoding to use for input, defaults to the platform's default
     * encoding. <p>
     *
     * For a list of possible values see
     * </a>.</p>
     *
     * @param encoding The new Encoding value
     */

    public final void setEncoding(final String encoding) {
        this.encoding = encoding;
    }


    /**
     * Property name to save to.
     *
     * @param property The new Property value
     */
    public final void setProperty(final String property) {
        this.property = property;
    }


    /**
     * Sets the file to load.
     *
     * @param srcFile The new SrcFile value
     */
    public final void setSrcFile(final File srcFile) {
        this.srcFile = srcFile;
    }


    /**
     * If true, fail on load error.
     *
     * @param fail The new Failonerror value
     */
    public final void setFailonerror(final boolean fail) {
        failOnError = fail;
    }


    /**
     * read in a source file to a property
     *
     * @exception BuildException if something goes wrong with the build
     */
    public final void execute()
        throws BuildException {
        if (srcFile == null) {
            throw new BuildException("source file not defined");
        }
        if (property == null) {
            throw new BuildException("output property not defined");
        }
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        Reader instream = null;
        log("loading " + srcFile + " into property " + property,
            Project.MSG_VERBOSE);
        try {
            final long len = srcFile.length();
            log("file size = " + len, Project.MSG_DEBUG);
            final int size = (int) len;
            fis = new FileInputStream(srcFile);
            bis = new BufferedInputStream(fis);
            if (encoding == null) {
                instream = new InputStreamReader(bis);
            } else {
                instream = new InputStreamReader(bis, encoding);
            }

            String text = "";
            if (size != 0) {
                ChainReaderHelper crh = new ChainReaderHelper();
                crh.setBufferSize(size);
                crh.setPrimaryReader(instream);
                crh.setFilterChains(filterChains);
                crh.setProject(getProject());
                instream = crh.getAssembledReader();

                text = crh.readFully(instream);
            }

            if (text != null) {
                if (text.length() > 0) {
                    getProject().setNewProperty(property, text);
                    log("loaded " + text.length() + " characters",
                        Project.MSG_VERBOSE);
                    log(property + " := " + text, Project.MSG_DEBUG);
                }
            }

        } catch (final IOException ioe) {
            final String message = "Unable to load file: " + ioe.toString();
            if (failOnError) {
                throw new BuildException(message, ioe, getLocation());
            } else {
                log(message, Project.MSG_ERR);
            }
        } catch (final BuildException be) {
            if (failOnError) {
                throw be;
            } else {
                log(be.getMessage(), Project.MSG_ERR);
            }
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
     * Add the FilterChain element.
     * @param filter the filter to add
     */
    public final void addFilterChain(FilterChain filter) {
        filterChains.addElement(filter);
    }

}
