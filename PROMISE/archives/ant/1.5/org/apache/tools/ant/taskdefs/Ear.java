package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.zip.ZipOutputStream;

import java.io.File;
import java.io.IOException;

/**
 * Creates a EAR archive. Based on WAR task
 *
 * @author Stefan Bodewig
 * @author <a href="mailto:leslie.hughes@rubus.com">Les Hughes</a>
 *
 * @since Ant 1.4
 *
 * @ant.task category="packaging"
 */
public class Ear extends Jar {

    private File deploymentDescriptor;
    private boolean descriptorAdded;
    private static final FileUtils fu = FileUtils.newFileUtils();

    /**
     * Create an Ear task.
     */
    public Ear() {
        super();
        archiveType = "ear";
        emptyBehavior = "create";
    }

    /**
     * @deprecated Use setDestFile(destfile) instead
     */
    public void setEarfile(File earFile) {
        setDestFile(earFile);
    }

    /**
     * File to incorporate as application.xml.
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
        fs.setFullpath("META-INF/application.xml");
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


    protected void initZipOutputStream(ZipOutputStream zOut)
        throws IOException, BuildException {
        if (deploymentDescriptor == null && !isInUpdateMode()) {
            throw new BuildException("appxml attribute is required", getLocation());
        }

        super.initZipOutputStream(zOut);
    }

    /**
     * Overriden from Zip class to deal with application.xml
     */
    protected void zipFile(File file, ZipOutputStream zOut, String vPath, 
                           int mode)
        throws IOException {
        if (vPath.equalsIgnoreCase("META-INF/application.xml"))  {
            if (deploymentDescriptor == null 
                || !fu.fileNameEquals(deploymentDescriptor, file)
                || descriptorAdded) {
                log("Warning: selected " + archiveType
                    + " files include a META-INF/application.xml which will"
                    + " be ignored (please use appxml attribute to "
                    + archiveType + " task)", Project.MSG_WARN);
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
