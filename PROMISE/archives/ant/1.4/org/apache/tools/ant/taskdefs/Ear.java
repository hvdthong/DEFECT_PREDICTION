package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.zip.*;

import java.io.*;
import java.util.Vector;

/**
 * Creates a EAR archive. Based on WAR task
 * 
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 * @author <a href="mailto:leslie.hughes@rubus.com">Les Hughes</a> 
 */
public class Ear extends Jar {

    private File deploymentDescriptor;
    private boolean descriptorAdded;    

    public Ear() {
        super();
        archiveType = "ear";
        emptyBehavior = "create";
    }

    public void setEarfile(File earFile) {
        super.setZipfile(earFile);
    }
    
    public void setAppxml(File descr) {
        deploymentDescriptor = descr; 
        if (!deploymentDescriptor.exists())
            throw new BuildException("Deployment descriptor: " + deploymentDescriptor + " does not exist.");

        ZipFileSet fs = new ZipFileSet();
        fs.setDir(new File(deploymentDescriptor.getParent()));
        fs.setIncludes(deploymentDescriptor.getName());
        fs.setFullpath("META-INF/application.xml");
        super.addFileset(fs);
    }


    public void addArchives(ZipFileSet fs) {
        log("addArchives called",Project.MSG_DEBUG);
        fs.setPrefix("/");
        super.addFileset(fs);
    }


    protected void initZipOutputStream(ZipOutputStream zOut)
        throws IOException, BuildException
    {
        if (deploymentDescriptor == null) {
            throw new BuildException("appxml attribute is required", location);
        }
        
        super.initZipOutputStream(zOut);
    }

    protected void zipFile(File file, ZipOutputStream zOut, String vPath)
        throws IOException
    {
        if (vPath.equalsIgnoreCase("META-INF/aplication.xml"))  {
            if (deploymentDescriptor == null || !deploymentDescriptor.equals(file) || descriptorAdded) {
                log("Warning: selected "+archiveType+" files include a META-INF/application.xml which will be ignored " +
                    "(please use appxml attribute to "+archiveType+" task)", Project.MSG_WARN);
            } else {
                super.zipFile(file, zOut, vPath);
                descriptorAdded = true;
            }
        } else {
            super.zipFile(file, zOut, vPath);
        }
    }

    /**
     * Make sure we don't think we already have a web.xml next time this task
     * gets executed.
     */
    protected void cleanUp() {
        descriptorAdded = false;
        super.cleanUp();
    }
}
