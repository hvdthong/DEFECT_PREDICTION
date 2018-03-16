package org.apache.tools.ant.taskdefs.optional;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Java;

/**
 * Executes the Apache Stylebook documentation generator.
 * Unlike the commandline version of this tool, all three arguments
 * are required to run stylebook.
 * <p>
 * Being extended from &lt;Java&gt;, all the parent's attributes
 * and options are available. Do not set any apart from the <tt>classpath</tt>
 * as they are not guaranteed to be there in future.
 * @todo stop extending from Java.
 * @deprecated since 1.7.
 *             This task is considered unsupported by the Ant developers
 */
public class StyleBook extends Java {
    protected File m_targetDirectory;
    protected File m_skinDirectory;
    protected String m_loaderConfig;
    protected File m_book;


    /**
     * Constructor
     */
    public StyleBook() {
        setClassname("org.apache.stylebook.StyleBook");
        setFork(true);
        setFailonerror(true);
    }

    /**
     * Set the book xml file that the documentation generation starts from;
     * required.
     * @param book the source file
     */

    public void setBook(final File book) {
        m_book = book;
    }


    /**
     * Set the directory that contains the stylebook skin;
     * required.
     * @param skinDirectory the location of the stylebook skin
     */
    public void setSkinDirectory(final File skinDirectory) {
        m_skinDirectory = skinDirectory;
    }


    /**
     * Set the destination directory where the documentation is generated;
     * required.
     * @param targetDirectory the document output directory
     */
    public void setTargetDirectory(final File targetDirectory) {
        m_targetDirectory = targetDirectory;
    }

    /**
     * A loader configuration to send to stylebook; optional.
     * @param loaderConfig the configuration to use.
     */
    public void setLoaderConfig(final String loaderConfig) {
        m_loaderConfig = loaderConfig;
    }


    /**
     * call the program
     * @throws BuildException if there is a problem.
     */
    public void execute()
         throws BuildException {

        if (null == m_targetDirectory) {
            throw new BuildException("TargetDirectory attribute not set.");
        }

        if (null == m_skinDirectory) {
            throw new BuildException("SkinDirectory attribute not set.");
        }

        if (null == m_book) {
            throw new BuildException("book attribute not set.");
        }

        createArg().setValue("targetDirectory=" + m_targetDirectory);
        createArg().setValue(m_book.toString());
        createArg().setValue(m_skinDirectory.toString());
        if (null != m_loaderConfig) {
            createArg().setValue("loaderConfig=" + m_loaderConfig);
        }

        super.execute();
    }
}
