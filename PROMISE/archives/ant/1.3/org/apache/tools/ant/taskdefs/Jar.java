package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.ZipFileSet;

import java.io.*;
import java.util.zip.*;

/**
 * Creates a JAR archive.
 * 
 * @author James Davidson <a href="mailto:duncan@x180.com">duncan@x180.com</a>
 */

public class Jar extends Zip {

    private File manifest;    
    private boolean manifestAdded;    

    public Jar() {
        super();
        archiveType = "jar";
        emptyBehavior = "create";
    }

    public void setJarfile(File jarFile) {
        super.setZipfile(jarFile);
    }

    public void setManifest(File manifestFile) {
        manifest = manifestFile;
        if (!manifest.exists())
            throw new BuildException("Manifest file: " + manifest + " does not exist.");

        ZipFileSet fs = new ZipFileSet();
        fs.setDir(new File(manifest.getParent()));
        fs.setIncludes(manifest.getName());
        fs.setFullpath("META-INF/MANIFEST.MF");
        super.addFileset(fs);
    }


    protected void initZipOutputStream(ZipOutputStream zOut)
        throws IOException, BuildException
    {
        if (manifest == null) {
            String s = "/org/apache/tools/ant/defaultManifest.mf";
            InputStream in = this.getClass().getResourceAsStream(s);
            if ( in == null )
                throw new BuildException ( "Could not find: " + s );
            zipDir(null, zOut, "META-INF/");
            zipFile(in, zOut, "META-INF/MANIFEST.MF", System.currentTimeMillis());
        }

        super.initZipOutputStream(zOut);
    }

    protected void zipFile(File file, ZipOutputStream zOut, String vPath)
        throws IOException
    {
        if (vPath.equalsIgnoreCase("META-INF/MANIFEST.MF"))  {
            if (manifest == null || !manifest.equals(file) || manifestAdded) {
                log("Warning: selected "+archiveType+" files include a META-INF/MANIFEST.MF which will be ignored " +
                    "(please use manifest attribute to "+archiveType+" task)", Project.MSG_WARN);
            } else {
                super.zipFile(file, zOut, vPath);
                manifestAdded = true;
            }
        } else {
            super.zipFile(file, zOut, vPath);
        }
    }

    /**
     * Make sure we don't think we already have a MANIFEST next time this task
     * gets executed.
     */
    protected void cleanUp() {
        manifestAdded = false;
        super.cleanUp();
    }
}
