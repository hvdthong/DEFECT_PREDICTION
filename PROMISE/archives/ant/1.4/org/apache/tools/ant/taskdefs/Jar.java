package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.zip.*;

import java.io.*;
import java.util.Enumeration;

/**
 * Creates a JAR archive.
 * 
 * @author James Davidson <a href="mailto:duncan@x180.com">duncan@x180.com</a>
 */
public class Jar extends Zip {

    private File manifestFile;
    private Manifest manifest;
    private Manifest execManifest;  
    
    /** true if a manifest has been specified in the task */
    private boolean buildFileManifest = false;
    
    public Jar() {
        super();
        archiveType = "jar";
        emptyBehavior = "create";
        setEncoding("UTF8");
    }

    public void setJarfile(File jarFile) {
        super.setZipfile(jarFile);
    }

    public void addConfiguredManifest(Manifest newManifest) throws ManifestException {
        if (manifest == null) {
            manifest = getDefaultManifest();
        }
        manifest.merge(newManifest);
        buildFileManifest = true;
    }
    
    public void setManifest(File manifestFile) {
        if (!manifestFile.exists()) {
            throw new BuildException("Manifest file: " + manifestFile + " does not exist.", 
                                     getLocation());
        }

        this.manifestFile = manifestFile;
        
        InputStream is = null;
        try {
            is = new FileInputStream(manifestFile);
            Manifest newManifest = new Manifest(is);
            if (manifest == null) {
                manifest = getDefaultManifest();
            }
            manifest.merge(newManifest);
        }
        catch (ManifestException e) {
            log("Manifest is invalid: " + e.getMessage(), Project.MSG_ERR);
            throw new BuildException("Invalid Manifest: " + manifestFile, e, getLocation());
        }
        catch (IOException e) {
            throw new BuildException("Unable to read manifest file: " + manifestFile, e);
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                }
            }
        }
    }

    public void addMetainf(ZipFileSet fs) {
        fs.setPrefix("META-INF/");
        super.addFileset(fs);
    }

    protected void initZipOutputStream(ZipOutputStream zOut)
        throws IOException, BuildException
    {
        try {
            execManifest = getDefaultManifest();

            if (manifest != null) {
                execManifest.merge(manifest);
            }
            for (Enumeration e = execManifest.getWarnings(); e.hasMoreElements(); ) {
                log("Manifest warning: " + (String)e.nextElement(), Project.MSG_WARN);
            }
        
            zipDir(null, zOut, "META-INF/");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(baos);
            execManifest.write(writer);
            writer.flush();
        
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            super.zipFile(bais, zOut, "META-INF/MANIFEST.MF", System.currentTimeMillis());
            super.initZipOutputStream(zOut);
        }
        catch (ManifestException e) {
            log("Manifest is invalid: " + e.getMessage(), Project.MSG_ERR);
            throw new BuildException("Invalid Manifest", e, getLocation());
        }
    }
        
    private Manifest getDefaultManifest() {
        try {
            String s = "/org/apache/tools/ant/defaultManifest.mf";
            InputStream in = this.getClass().getResourceAsStream(s);
            if (in == null) {
                throw new BuildException("Could not find default manifest: " + s);
            }
            return new Manifest(in);
        }
        catch (ManifestException e) {
            throw new BuildException("Default manifest is invalid !!");
        }
        catch (IOException e) {
            throw new BuildException("Unable to read default manifest", e);
        }
    }   
    
    /**
     * Handle situation when we encounter a manifest file
     *
     * If we haven't been given one, we use this one.
     *
     * If we have, we merge the manifest in, provided it is a new file
     * and not the old one from the JAR we are updating
     */
    private void zipManifestEntry(InputStream is) throws IOException {
        try {
            if (execManifest == null) {
                execManifest = new Manifest(is);
            }
            else if (isAddingNewFiles()) {
                execManifest.merge(new Manifest(is));
            }
        }
        catch (ManifestException e) {
            log("Manifest is invalid: " + e.getMessage(), Project.MSG_ERR);
            throw new BuildException("Invalid Manifest", e, getLocation());
        }
    }
    
    protected void zipFile(File file, ZipOutputStream zOut, String vPath)
        throws IOException
    {
        if (vPath.equalsIgnoreCase("META-INF/MANIFEST.MF"))  {
            log("Warning: selected "+archiveType+" files include a META-INF/MANIFEST.MF which will be ignored " +
                "(please use manifest attribute to "+archiveType+" task)", Project.MSG_WARN);
        } else {
            super.zipFile(file, zOut, vPath);
        }

    }

    protected void zipFile(InputStream is, ZipOutputStream zOut, String vPath, long lastModified)
        throws IOException
    {
        if (vPath.equalsIgnoreCase("META-INF/MANIFEST.MF"))  {
            try {
                zipManifestEntry(is);
            }
            catch (IOException e) {
                throw new BuildException("Unable to read manifest file: ", e);
            }
        } else {
            super.zipFile(is, zOut, vPath, lastModified);
        }
    }

    /**
     * Check whether the archive is up-to-date; 
     * @param scanners list of prepared scanners containing files to archive
     * @param zipFile intended archive file (may or may not exist)
     * @return true if nothing need be done (may have done something already); false if
     *         archive creation should proceed
     * @exception BuildException if it likes
     */
    protected boolean isUpToDate(FileScanner[] scanners, File zipFile) throws BuildException {
        if (buildFileManifest || manifestFile == null) {
            java.util.zip.ZipFile theZipFile = null;
            try {
                theZipFile = new java.util.zip.ZipFile(zipFile);
                java.util.zip.ZipEntry entry = theZipFile.getEntry("META-INF/MANIFEST.MF");
                if (entry == null) {
                    log("Updating jar since the current jar has no manifest", Project.MSG_VERBOSE);
                    return false;
                }
                Manifest currentManifest = new Manifest(theZipFile.getInputStream(entry));
                if (manifest == null) {
                    manifest = getDefaultManifest();
                }
                if (!currentManifest.equals(manifest)) {
                    log("Updating jar since jar manifest has changed", Project.MSG_VERBOSE);
                    return false;
                }
            }
            catch (Exception e) {
                log("Updating jar since cannot read current jar manifest: " + e.getClass().getName() + e.getMessage(), 
                    Project.MSG_VERBOSE);
                return false;
            }
            finally {
                if (theZipFile != null) {
                    try {
                        theZipFile.close();
                    }
                    catch (IOException e) {
                    }
                }
            }
        }
        else if (manifestFile.lastModified() > zipFile.lastModified()) {
            return false;
        }
        return super.isUpToDate(scanners, zipFile);
    }
        
    protected boolean createEmptyZip(File zipFile) {
        return false;
    }
    
    /**
     * Make sure we don't think we already have a MANIFEST next time this task
     * gets executed.
     */
    protected void cleanUp() {
        super.cleanUp();
    }
}
