package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.ZipFileSet;

import java.io.*;
import java.util.Vector;
import java.util.zip.*;

/**
 * Creates a WAR archive.
 * 
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 */
public class War extends Jar {

    private File deploymentDescriptor;
    private boolean descriptorAdded;    

    public War() {
        super();
        archiveType = "war";
        emptyBehavior = "create";
    }

    public void setWarfile(File warFile) {
        super.setZipfile(warFile);
    }
    
    public void setWebxml(File descr) {
        deploymentDescriptor = descr; 
        if (!deploymentDescriptor.exists())
            throw new BuildException("Deployment descriptor: " + deploymentDescriptor + " does not exist.");

        ZipFileSet fs = new ZipFileSet();
        fs.setDir(new File(deploymentDescriptor.getParent()));
        fs.setIncludes(deploymentDescriptor.getName());
        fs.setFullpath("WEB-INF/web.xml");
        super.addFileset(fs);
    }

    public void addLib(ZipFileSet fs) {
        fs.setPrefix("WEB-INF/lib/");
        super.addFileset(fs);
    }

    public void addClasses(ZipFileSet fs) {
        fs.setPrefix("WEB-INF/classes/");
        super.addFileset(fs);
    }

    public void addWebinf(ZipFileSet fs) {
        fs.setPrefix("WEB-INF/");
        super.addFileset(fs);
    }

    protected void initZipOutputStream(ZipOutputStream zOut)
        throws IOException, BuildException
    {
        if (deploymentDescriptor == null) {
            throw new BuildException("webxml attribute is required", location);
        }
        
        super.initZipOutputStream(zOut);
    }

    protected void zipFile(File file, ZipOutputStream zOut, String vPath)
        throws IOException
    {
        if (vPath.equalsIgnoreCase("WEB-INF/web.xml"))  {
            if (deploymentDescriptor == null || !deploymentDescriptor.equals(file) || descriptorAdded) {
                log("Warning: selected "+archiveType+" files include a WEB-INF/web.xml which will be ignored " +
                    "(please use webxml attribute to "+archiveType+" task)", Project.MSG_WARN);
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
