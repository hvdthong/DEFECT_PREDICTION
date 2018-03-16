package org.apache.tools.ant.taskdefs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.zip.ZipOutputStream;


/**
 * Creates a JAR archive.
 *
 * @author James Davidson <a href="mailto:duncan@x180.com">duncan@x180.com</a>
 * @author Brian Deitte
 *         <a href="mailto:bdeitte@macromedia.com">bdeitte@macromedia.com</a>
 *
 * @since Ant 1.1
 *
 * @ant.task category="packaging"
 */
public class Jar extends Zip {
    /** The index file name. */
    private static final String INDEX_NAME = "META-INF/INDEX.LIST";

    /** The mainfest file name. */
    private static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";

    /** merged manifests added through addConfiguredManifest */
    private Manifest configuredManifest;
    /** shadow of the above if upToDate check alters the value */
    private Manifest savedConfiguredManifest;

    /**  merged manifests added through filesets */
    private Manifest filesetManifest;

    /** 
     * Manifest of original archive, will be set to null if not in
     * update mode.
     */
    private Manifest originalManifest;

    /**
     *  whether to merge fileset manifests;
     *  value is true if filesetmanifest is 'merge' or 'mergewithoutmain'
     */
    private FilesetManifestConfig filesetManifestConfig;

    /**
     *  Whether to create manifest file on finalizeOutputStream?
     */
    private boolean manifestOnFinalize = true;

    /**
     * whether to merge the main section of fileset manifests;
     * value is true if filesetmanifest is 'merge'
     */
    private boolean mergeManifestsMain = true;

    /** the manifest specified by the 'manifest' attribute **/
    private Manifest manifest;

    /** The encoding to use when reading in a manifest file */
    private String manifestEncoding;
    
    /**
     * The file found from the 'manifest' attribute.  This can be
     * either the location of a manifest, or the name of a jar added
     * through a fileset.  If its the name of an added jar, the
     * manifest is looked for in META-INF/MANIFEST.MF
     */
    private File manifestFile;

    /** jar index is JDK 1.3+ only */
    private boolean index = false;

    /** 
     * whether to really create the archive in createEmptyZip, will
     * get set in getResourcesToAdd.
     */
    private boolean createEmpty = false;

    /** constructor */
    public Jar() {
        super();
        archiveType = "jar";
        emptyBehavior = "create";
        setEncoding("UTF8");
    }

    /**
     * @ant.attribute ignore="true"
     */
    public void setWhenempty(WhenEmpty we) {
        log("JARs are never empty, they contain at least a manifest file",
            Project.MSG_WARN);
    }

    /**
     * @deprecated Use setDestFile(File) instead
     */
    public void setJarfile(File jarFile) {
        setDestFile(jarFile);
    }

    /**
     * Set whether or not to create an index list for classes.
     * This may speed up classloading in some cases.
     */
    public void setIndex(boolean flag){
        index = flag;
    }

    /**
     * Set whether or not to create an index list for classes.
     * This may speed up classloading in some cases.
     */
    public void setManifestEncoding(String manifestEncoding) {
        this.manifestEncoding = manifestEncoding;
    }

    /**
     * Allows the manifest for the archive file to be provided inline
     * in the build file rather than in an external file.
     *
     * @param newManifest
     * @throws ManifestException
     */
    public void addConfiguredManifest(Manifest newManifest)
        throws ManifestException {
        if (configuredManifest == null) {
            configuredManifest = newManifest;
        } else {
            configuredManifest.merge(newManifest);
        }
        savedConfiguredManifest = configuredManifest;
    }

    /**
     * The manifest file to use. This can be either the location of a manifest,
     * or the name of a jar added through a fileset. If its the name of an added
     * jar, the task expects the manifest to be in the jar at META-INF/MANIFEST.MF.
     *
     * @param manifestFile the manifest file to use.
     */
    public void setManifest(File manifestFile) {
        if (!manifestFile.exists()) {
            throw new BuildException("Manifest file: " + manifestFile +
                                     " does not exist.", getLocation());
        }

        this.manifestFile = manifestFile;
    }

    private Manifest getManifest(File manifestFile) {

        Manifest newManifest = null;
        FileInputStream fis = null;
        InputStreamReader isr = null;
        try {
            fis = new FileInputStream(manifestFile);
            if (manifestEncoding == null) {
                isr = new InputStreamReader(fis);
            } else {
                isr = new InputStreamReader(fis, manifestEncoding);
            }
            newManifest = getManifest(isr);
        } catch (UnsupportedEncodingException e) {
            throw new BuildException("Unsupported encoding while reading manifest: "
                                     + e.getMessage(), e);
        } catch (IOException e) {
            throw new BuildException("Unable to read manifest file: "
                                     + manifestFile
                                     + " (" + e.getMessage() + ")", e);
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                }
            }
        }
        return newManifest;
    }

    /**
     * @return null if jarFile doesn't contain a manifest, the
     * manifest otherwise.
     * @since Ant 1.5.2
     */
    private Manifest getManifestFromJar(File jarFile) throws IOException {
        ZipFile zf = null;
        try {
            zf = new ZipFile(jarFile);
            
            Enumeration enum = zf.entries();
            while (enum.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) enum.nextElement();
                if (ze.getName().equalsIgnoreCase(MANIFEST_NAME)) {
                    InputStreamReader isr =
                        new InputStreamReader(zf.getInputStream(ze), "UTF-8");
                    return getManifest(isr);
                }
            }
            return null;
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private Manifest getManifest(Reader r) {

        Manifest newManifest = null;
        try {
            newManifest = new Manifest(r);
        } catch (ManifestException e) {
            log("Manifest is invalid: " + e.getMessage(), Project.MSG_ERR);
            throw new BuildException("Invalid Manifest: " + manifestFile,
                                     e, getLocation());
        } catch (IOException e) {
            throw new BuildException("Unable to read manifest file"
                                     + " (" + e.getMessage() + ")", e);
        }
        return newManifest;
    }

    /**
     * Behavior when a Manifest is found in a zipfileset or zipgroupfileset file.
     * Valid values are "skip", "merge", and "mergewithoutmain".
     * "merge" will merge all of manifests together, and merge this into any
     * other specified manifests.
     * "mergewithoutmain" merges everything but the Main section of the manifests.
     * Default value is "skip".
     *
     * Note: if this attribute's value is not "skip", the created jar will not
     * be readable by using java.util.jar.JarInputStream
     *
     * @param config setting for found manifest behavior.
     */
    /*
    public void setFilesetmanifest(FilesetManifestConfig config) {
        filesetManifestConfig = config;
        mergeManifestsMain = "merge".equals(config.getValue());
    }
    */

    /**
     * Adds a zipfileset to include in the META-INF directory.
     *
     * @param fs zipfileset to add
     */
    public void addMetainf(ZipFileSet fs) {
        fs.setPrefix("META-INF/");
        super.addFileset(fs);
    }

    protected void initZipOutputStream(ZipOutputStream zOut)
        throws IOException, BuildException {
        if (filesetManifestConfig == null
            || filesetManifestConfig.getValue().equals("skip")) {
            manifestOnFinalize = false;
            Manifest jarManifest = createManifest();
            writeManifest(zOut, jarManifest);
        }
    }

    private Manifest createManifest()
        throws BuildException {
        try {
            Manifest finalManifest = Manifest.getDefaultManifest();

            if (manifest == null) {
                if (manifestFile != null) {
                    manifest = getManifest(manifestFile);
                }
            }

            /*
             * Precedence: manifestFile wins over inline manifest,
             * over manifests read from the filesets over the original
             * manifest.
             *
             * merge with null argument is a no-op
             */

            if (isInUpdateMode()) {
                finalManifest.merge(originalManifest);
            }
            finalManifest.merge(filesetManifest);
            finalManifest.merge(configuredManifest);
            finalManifest.merge(manifest, !mergeManifestsMain);

            return finalManifest;

        } catch (ManifestException e) {
            log("Manifest is invalid: " + e.getMessage(), Project.MSG_ERR);
            throw new BuildException("Invalid Manifest", e, getLocation());
        }
    }

    private void writeManifest(ZipOutputStream zOut, Manifest manifest)
         throws IOException {
        for (Enumeration e = manifest.getWarnings();
             e.hasMoreElements();) {
            log("Manifest warning: " + (String) e.nextElement(),
                Project.MSG_WARN);
        }

        zipDir(null, zOut, "META-INF/", ZipFileSet.DEFAULT_DIR_MODE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(baos, "UTF-8");
        PrintWriter writer = new PrintWriter(osw);
        manifest.write(writer);
        writer.flush();

        ByteArrayInputStream bais =
            new ByteArrayInputStream(baos.toByteArray());
        super.zipFile(bais, zOut, MANIFEST_NAME,
                      System.currentTimeMillis(), null,
                      ZipFileSet.DEFAULT_FILE_MODE);
        super.initZipOutputStream(zOut);
    }

    protected void finalizeZipOutputStream(ZipOutputStream zOut)
            throws IOException, BuildException {
        if (manifestOnFinalize) {
            Manifest jarManifest = createManifest();
            writeManifest(zOut, jarManifest);
        }

        if (index) {
            createIndexList(zOut);
        }
    }

    /**
     * Create the index list to speed up classloading.
     * This is a JDK 1.3+ specific feature and is enabled by default. See
     * the JAR index specification</a> for more details.
     *
     * @param zOut the zip stream representing the jar being built.
     * @throws IOException thrown if there is an error while creating the
     * index and adding it to the zip stream.
     */
    private void createIndexList(ZipOutputStream zOut) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(baos,
                                                                    "UTF8"));

        writer.println("JarIndex-Version: 1.0");
        writer.println();

        writer.println(zipFile.getName());

        Enumeration enum = addedDirs.keys();
        while (enum.hasMoreElements()) {
            String dir = (String) enum.nextElement();

            dir = dir.replace('\\', '/');
            int pos = dir.lastIndexOf('/');
            if (pos != -1){
                dir = dir.substring(0, pos);
            }

            if (dir.startsWith("META-INF")) {
                continue;
            }
            writer.println(dir);
        }

        writer.flush();
        ByteArrayInputStream bais =
            new ByteArrayInputStream(baos.toByteArray());
        super.zipFile(bais, zOut, INDEX_NAME, System.currentTimeMillis(), null,
                      ZipFileSet.DEFAULT_FILE_MODE);
    }

    /**
     * Overriden from Zip class to deal with manifests
     */
    protected void zipFile(InputStream is, ZipOutputStream zOut, String vPath,
                           long lastModified, File fromArchive, int mode)
        throws IOException {
        if (MANIFEST_NAME.equalsIgnoreCase(vPath))  {
            filesetManifest(fromArchive, is);
        } else {
            super.zipFile(is, zOut, vPath, lastModified, fromArchive, mode);
        }
    }

    private void filesetManifest(File file, InputStream is) throws IOException {
        if (manifestFile != null && manifestFile.equals(file)) {
            log("Found manifest " + file, Project.MSG_VERBOSE);
            try {
                if (is != null) {
                    InputStreamReader isr;
                    if (manifestEncoding == null) {
                        isr = new InputStreamReader(is);
                    } else {
                        isr = new InputStreamReader(is, manifestEncoding);
                    }
                    manifest = getManifest(isr);
                } else {
                    manifest = getManifest(file);
                }
            } catch (UnsupportedEncodingException e) {
                throw new BuildException("Unsupported encoding while reading " 
                    + "manifest: " + e.getMessage(), e);
            }
        } else if (filesetManifestConfig != null &&
                   !filesetManifestConfig.getValue().equals("skip")) {
            log("Found manifest to merge in file " + file,
                Project.MSG_VERBOSE);

            try {
                Manifest newManifest = null;
                if (is != null) {
                    InputStreamReader isr;
                    if (manifestEncoding == null) {
                        isr = new InputStreamReader(is);
                    } else {
                        isr = new InputStreamReader(is, manifestEncoding);
                    }
                    newManifest = getManifest(isr);
                } else {
                    newManifest = getManifest(file);
                }

                if (filesetManifest == null) {
                    filesetManifest = newManifest;
                } else {
                    filesetManifest.merge(newManifest);
                }
            } catch (UnsupportedEncodingException e) {
                throw new BuildException("Unsupported encoding while reading " 
                    + "manifest: " + e.getMessage(), e);
            } catch (ManifestException e) {
                log("Manifest in file " + file + " is invalid: "
                    + e.getMessage(), Project.MSG_ERR);
                throw new BuildException("Invalid Manifest", e, getLocation());
            }
        } else {


        }
    }

    /**
     * Collect the resources that are newer than the corresponding
     * entries (or missing) in the original archive.
     *
     * <p>If we are going to recreate the archive instead of updating
     * it, all resources should be considered as new, if a single one
     * is.  Because of this, subclasses overriding this method must
     * call <code>super.getResourcesToAdd</code> and indicate with the
     * third arg if they already know that the archive is
     * out-of-date.</p>
     *
     * @param filesets The filesets to grab resources from
     * @param zipFile intended archive file (may or may not exist)
     * @param needsUpdate whether we already know that the archive is
     * out-of-date.  Subclasses overriding this method are supposed to
     * set this value correctly in their call to
     * super.getResourcesToAdd.
     * @return an array of resources to add for each fileset passed in as well
     *         as a flag that indicates whether the archive is uptodate.
     *
     * @exception BuildException if it likes
     */
    protected ArchiveState getResourcesToAdd(FileSet[] filesets,
                                             File zipFile,
                                             boolean needsUpdate)
        throws BuildException {

        if (zipFile.exists()) {

            try {
                originalManifest = getManifestFromJar(zipFile);
                if (originalManifest == null) {
                    log("Updating jar since the current jar has no manifest",
                        Project.MSG_VERBOSE);
                    needsUpdate = true;
                } else {
                    Manifest mf = createManifest();
                    if (!mf.equals(originalManifest)) {
                        log("Updating jar since jar manifest has changed", 
                            Project.MSG_VERBOSE);
                        needsUpdate = true;
                    }
                }
            } catch (Throwable t) {
                log("error while reading original manifest: " + t.getMessage(),
                    Project.MSG_WARN);
                needsUpdate = true;
            }

        } else {
            needsUpdate = true;
        }

        createEmpty = needsUpdate;
        return super.getResourcesToAdd(filesets, zipFile, needsUpdate);
    }

    protected boolean createEmptyZip(File zipFile) throws BuildException {
        if (!createEmpty) {
            return true;
        }
        
        ZipOutputStream zOut = null;
        try {
            log("Building MANIFEST-only jar: " 
                + getDestFile().getAbsolutePath());
            zOut = new ZipOutputStream(new FileOutputStream(getDestFile()));

            zOut.setEncoding(getEncoding());
            if (isCompress()) {
                zOut.setMethod(ZipOutputStream.DEFLATED);
            } else {
                zOut.setMethod(ZipOutputStream.STORED);
            }
            initZipOutputStream(zOut);
            finalizeZipOutputStream(zOut);
        } catch (IOException ioe) {
            throw new BuildException("Could not create almost empty JAR archive"
                                     + " (" + ioe.getMessage() + ")", ioe,
                                     getLocation());
        } finally {
            try {
                if (zOut != null) {
                    zOut.close();
                }
            } catch (IOException ex) {
            }
            createEmpty = false;
        }
        return true;
    }

    /**
     * Make sure we don't think we already have a MANIFEST next time this task
     * gets executed.
     *
     * @see Zip#cleanUp
     */
    protected void cleanUp() {
        super.cleanUp();

        manifest = null;
        configuredManifest = savedConfiguredManifest;
        filesetManifest = null;
    }

    /**
     * reset to default values.
     *
     * @see Zip#reset
     *
     * @since 1.44, Ant 1.5
     */
    public void reset() {
        super.reset();
        configuredManifest = null;
        filesetManifestConfig = null;
        mergeManifestsMain = false;
        manifestFile = null;
        index = false;
    }

    public static class FilesetManifestConfig extends EnumeratedAttribute {
        public String[] getValues() {
            return new String[] {"skip", "merge", "mergewithoutmain"};
        }
    }
}
