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
 * Creates a EAR archive. Based on WAR task
 *
 * @since Ant 1.4
 *
 * @ant.task category="packaging"
 */
public class Ear extends Jar {
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    private File deploymentDescriptor;
    private boolean descriptorAdded;
    private static final String XML_DESCRIPTOR_PATH = "META-INF/application.xml";

    /**
     * Create an Ear task.
     */
    public Ear() {
        super();
        archiveType = "ear";
        emptyBehavior = "create";
    }

    /**
     * Set the destination file.
     * @param earFile the destination file
     * @deprecated since 1.5.x.
     *             Use setDestFile(destfile) instead.
     */
    public void setEarfile(File earFile) {
        setDestFile(earFile);
    }

    /**
     * File to incorporate as application.xml.
     * @param descr the descriptor file
     */
    public void setAppxml(File descr) {
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
     * Adds zipfileset.
     *
     * @param fs zipfileset to add
     */
    public void addArchives(ZipFileSet fs) {
        fs.setPrefix("/");
        super.addFileset(fs);
    }


    /**
     * Initialize the output stream.
     * @param zOut the zip output stream.
     * @throws IOException on I/O errors
     * @throws BuildException on other errors
     */
    protected void initZipOutputStream(ZipOutputStream zOut)
        throws IOException, BuildException {
        if (deploymentDescriptor == null && !isInUpdateMode()) {
            throw new BuildException("appxml attribute is required", getLocation());
        }

        super.initZipOutputStream(zOut);
    }

    /**
     * Overridden from Zip class to deal with application.xml
     * @param file the file to add to the archive
     * @param zOut the stream to write to
     * @param vPath the name this entry shall have in the archive
     * @param mode the Unix permissions to set.
     * @throws IOException on error
     */
    protected void zipFile(File file, ZipOutputStream zOut, String vPath,
                           int mode)
        throws IOException {
        String vPathLowerCase = vPath.toLowerCase(Locale.ENGLISH);
        if (XML_DESCRIPTOR_PATH.equals(vPathLowerCase))  {
            if (deploymentDescriptor != null
                || !FILE_UTILS.fileNameEquals(deploymentDescriptor, file)
                || descriptorAdded) {
                log("Warning: selected " + archiveType
                    + " files include a " + XML_DESCRIPTOR_PATH + " which will"
                    + " be ignored (please use appxml attribute to "
                    + archiveType + " task)",
                        Project.MSG_WARN);
            } else {
                super.zipFile(file, zOut, vPath, mode);
                descriptorAdded = true;
            }
        } else {
            super.zipFile(file, zOut, vPath, mode);
        }
    }

    /**
     * Make sure we don't think we already have a application.xml next
     * time this task gets executed.
     */
    protected void cleanUp() {
        descriptorAdded = false;
        super.cleanUp();
    }
}
