package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.zip.ZipOutputStream;


/**
 * An extension of &lt;jar&gt; to create a WAR archive.
 * Contains special treatment for files that should end up in the
 * <code>WEB-INF/lib</code>, <code>WEB-INF/classes</code> or
 * <code>WEB-INF</code> directories of the Web Application Archive.</p>
 * <p>(The War task is a shortcut for specifying the particular layout of a WAR file.
 * The same thing can be accomplished by using the <i>prefix</i> and <i>fullpath</i>
 * attributes of zipfilesets in a Zip or Jar task.)</p>
 * <p>The extended zipfileset element from the zip task
 * (with attributes <i>prefix</i>, <i>fullpath</i>, and <i>src</i>)
 * is available in the War task.</p>
 *
 * @since Ant 1.2
 *
 * @ant.task category="packaging"
 * @see Jar
 */
public class War extends Jar {

    /**
     * our web.xml deployment descriptor
     */
    private File deploymentDescriptor;

    /**
     * flag set if the descriptor is added
     */
    private boolean needxmlfile = true;
    private File addedWebXmlFile;

    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
    /** path to web.xml file */
    private static final String XML_DESCRIPTOR_PATH = "WEB-INF/web.xml";
    /** lower case version for comparisons */
    private static final String XML_DESCRIPTOR_PATH_LC =
            XML_DESCRIPTOR_PATH.toLowerCase(Locale.ENGLISH);

    /** Constructor for the War Task. */
    public War() {
        super();
        archiveType = "war";
        emptyBehavior = "create";
    }

    /**
     * <i>Deprecated<i> name of the file to create
     * -use <tt>destfile</tt> instead.
     * @param warFile the destination file
     * @deprecated since 1.5.x.
     *             Use setDestFile(File) instead
     * @ant.attribute ignore="true"
     */
    public void setWarfile(File warFile) {
        setDestFile(warFile);
    }

    /**
     * set the deployment descriptor to use (WEB-INF/web.xml);
     * required unless <tt>update=true</tt>
     * @param descr the deployment descriptor file
     */
    public void setWebxml(File descr) {
        deploymentDescriptor = descr;
        if (!deploymentDescriptor.exists()) {
            throw new BuildException("Deployment descriptor: "
                                     + deploymentDescriptor
                                     + " does not exist.");
        }

        ZipFileSet fs = new ZipFileSet();
        fs.setFile(deploymentDescriptor);
        fs.setFullpath(XML_DESCRIPTOR_PATH);
        super.addFileset(fs);
    }


    /**
     * Set the policy on the web.xml file, that is, whether or not it is needed
     * @param needxmlfile whether a web.xml file is needed. Default: true
     */
    public void setNeedxmlfile(boolean needxmlfile) {
        this.needxmlfile = needxmlfile;
    }

    /**
     * add files under WEB-INF/lib/
     * @param fs the zip file set to add
     */

    public void addLib(ZipFileSet fs) {
        fs.setPrefix("WEB-INF/lib/");
        super.addFileset(fs);
    }

    /**
     * add files under WEB-INF/classes
     * @param fs the zip file set to add
     */
    public void addClasses(ZipFileSet fs) {
        fs.setPrefix("WEB-INF/classes/");
        super.addFileset(fs);
    }

    /**
     * files to add under WEB-INF;
     * @param fs the zip file set to add
     */
    public void addWebinf(ZipFileSet fs) {
        fs.setPrefix("WEB-INF/");
        super.addFileset(fs);
    }

    /**
     * override of  parent; validates configuration
     * before initializing the output stream.
     * @param zOut the zip output stream
     * @throws IOException on output error
     * @throws BuildException if invalid configuration
     */
    protected void initZipOutputStream(ZipOutputStream zOut)
        throws IOException, BuildException {
        super.initZipOutputStream(zOut);
    }

    /**
     * Overridden from Zip class to deal with web.xml
     *
     * Here are cases that can arise
     * -not a web.xml file : add
     * -first web.xml : add, remember we added it
     * -same web.xml again: skip
     * -alternate web.xml : warn and skip
     *
     * @param file the file to add to the archive
     * @param zOut the stream to write to
     * @param vPath the name this entry shall have in the archive
     * @param mode the Unix permissions to set.
     * @throws IOException on output error
     */
    protected void zipFile(File file, ZipOutputStream zOut, String vPath,
                           int mode)
        throws IOException {
        String vPathLowerCase = vPath.toLowerCase(Locale.ENGLISH);
        boolean addFile = true;
        if (XML_DESCRIPTOR_PATH_LC.equals(vPathLowerCase)) {
            if (addedWebXmlFile != null) {
                addFile = false;
                if (!FILE_UTILS.fileNameEquals(addedWebXmlFile, file)) {
                    log("Warning: selected " + archiveType
                            + " files include a second " + XML_DESCRIPTOR_PATH
                            + " which will be ignored.\n"
                            + "The duplicate entry is at " + file + '\n'
                            + "The file that will be used is "
                            + addedWebXmlFile,
                            Project.MSG_WARN);
                }
            } else {
                addedWebXmlFile = file;
                addFile = true;
                deploymentDescriptor = file;
            }
        }
        if (addFile) {
            super.zipFile(file, zOut, vPath, mode);
        }
    }


    /**
     * Make sure we don't think we already have a web.xml next time this task
     * gets executed.
     */
    protected void cleanUp() {
        if (addedWebXmlFile == null
            && deploymentDescriptor == null
            && needxmlfile
            && !isInUpdateMode()
            && hasUpdatedFile()) {
            throw new BuildException("No WEB-INF/web.xml file was added.\n"
                    + "If this is your intent, set needxmlfile='false' ");
        }
        addedWebXmlFile = null;
        super.cleanUp();
    }
}
