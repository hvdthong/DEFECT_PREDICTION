package org.apache.tools.ant.taskdefs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Manifest.Section;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.ant.types.spi.Service;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.zip.JarMarker;
import org.apache.tools.zip.ZipExtraField;
import org.apache.tools.zip.ZipOutputStream;

/**
 * Creates a JAR archive.
 *
 * @since Ant 1.1
 *
 * @ant.task category="packaging"
 */
public class Jar extends Zip {
    /** The index file name. */
    private static final String INDEX_NAME = "META-INF/INDEX.LIST";

    /** The manifest file name. */
    private static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";

    /**
     * List of all known SPI Services
     */
    private List serviceList = new ArrayList();

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

    /**
     * Stores all files that are in the root of the archive (i.e. that
     * have a name that doesn't contain a slash) so they can get
     * listed in the index.
     *
     * Will not be filled unless the user has asked for an index.
     *
     * @since Ant 1.6
     */
    private Vector rootEntries;

    /**
     * Path containing jars that shall be indexed in addition to this archive.
     *
     * @since Ant 1.6.2
     */
    private Path indexJars;

    /**
     * Strict mode for checking rules of the JAR-Specification.
     */
    private StrictMode strict = new StrictMode("ignore");


    /**
     * Extra fields needed to make Solaris recognize the archive as a jar file.
     *
     * @since Ant 1.6.3
     */
    private static final ZipExtraField[] JAR_MARKER = new ZipExtraField[] {
        JarMarker.getInstance()
    };

    /** constructor */
    public Jar() {
        super();
        archiveType = "jar";
        emptyBehavior = "create";
        setEncoding("UTF8");
        rootEntries = new Vector();
    }

    /**
     * Not used for jar files.
     * @param we not used
     * @ant.attribute ignore="true"
     */
    public void setWhenempty(WhenEmpty we) {
        log("JARs are never empty, they contain at least a manifest file",
            Project.MSG_WARN);
    }

    /**
     * Indicates if a jar file should be created when it would only contain a
     * manifest file.
     * Possible values are: <code>fail</code> (throw an exception
     * and halt the build); <code>skip</code> (do not create
     * any archive, but issue a warning); <code>create</code>
     * (make an archive with only a manifest file).
     * Default is <code>create</code>;
     * @param we a <code>WhenEmpty</code> enumerated value
     */
    public void setWhenmanifestonly(WhenEmpty we) {
        emptyBehavior = we.getValue();
    }

    /**
     * Activate the strict mode. When set to <i>true</i> a BuildException
     * will be thrown if the Jar-Packaging specification was broken.
     * @param strict New value of the strict mode.
     * @since Ant 1.7.1
     */
    public void setStrict(StrictMode strict) {
        this.strict = strict;
    }

    /**
     * Set the destination file.
     * @param jarFile the destination file
     * @deprecated since 1.5.x.
     *             Use setDestFile(File) instead.
     */
    public void setJarfile(File jarFile) {
        setDestFile(jarFile);
    }

    /**
     * Set whether or not to create an index list for classes.
     * This may speed up classloading in some cases.
     * @param flag a <code>boolean</code> value
     */
    public void setIndex(boolean flag) {
        index = flag;
    }

    /**
     * The character encoding to use in the manifest file.
     *
     * @param manifestEncoding the character encoding
     */
    public void setManifestEncoding(String manifestEncoding) {
        this.manifestEncoding = manifestEncoding;
    }

    /**
     * Allows the manifest for the archive file to be provided inline
     * in the build file rather than in an external file.
     *
     * @param newManifest an embedded manifest element
     * @throws ManifestException on error
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
            throw new BuildException("Manifest file: " + manifestFile
                                     + " does not exist.", getLocation());
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
            FileUtils.close(isr);
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

            Enumeration e = zf.entries();
            while (e.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) e.nextElement();
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
    public void setFilesetmanifest(FilesetManifestConfig config) {
        filesetManifestConfig = config;
        mergeManifestsMain = "merge".equals(config.getValue());

        if (filesetManifestConfig != null
            && !filesetManifestConfig.getValue().equals("skip")) {

            doubleFilePass = true;
        }
    }

    /**
     * Adds a zipfileset to include in the META-INF directory.
     *
     * @param fs zipfileset to add
     */
    public void addMetainf(ZipFileSet fs) {
        fs.setPrefix("META-INF/");
        super.addFileset(fs);
    }

    /**
     * Add a path to index jars.
     * @param p a path
     * @since Ant 1.6.2
     */
    public void addConfiguredIndexJars(Path p) {
        if (indexJars == null) {
            indexJars = new Path(getProject());
        }
        indexJars.append(p);
    }

    /**
     * A nested SPI service element.
     * @param service the nested element.
     * @since Ant 1.7
     */
    public void addConfiguredService(Service service) {
        service.check();
        serviceList.add(service);
    }

    /**
     * Write SPI Information to JAR
     */
    private void writeServices(ZipOutputStream zOut) throws IOException {
        Iterator serviceIterator;
        Service service;

        serviceIterator = serviceList.iterator();
        while (serviceIterator.hasNext()) {
           service = (Service) serviceIterator.next();
           super.zipFile(service.getAsStream(), zOut,
                         "META-INF/services/" + service.getType(),
                         System.currentTimeMillis(), null,
                         ZipFileSet.DEFAULT_FILE_MODE);
        }
    }


    /**
     * Initialize the zip output stream.
     * @param zOut the zip output stream
     * @throws IOException on I/O errors
     * @throws BuildException on other errors
     */
    protected void initZipOutputStream(ZipOutputStream zOut)
        throws IOException, BuildException {

        if (!skipWriting) {
            Manifest jarManifest = createManifest();
            writeManifest(zOut, jarManifest);
            writeServices(zOut);
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
            log("Manifest warning: " + e.nextElement(),
                Project.MSG_WARN);
        }

        zipDir(null, zOut, "META-INF/", ZipFileSet.DEFAULT_DIR_MODE,
               JAR_MARKER);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(baos, Manifest.JAR_ENCODING);
        PrintWriter writer = new PrintWriter(osw);
        manifest.write(writer);
        writer.close();

        ByteArrayInputStream bais =
            new ByteArrayInputStream(baos.toByteArray());
        super.zipFile(bais, zOut, MANIFEST_NAME,
                      System.currentTimeMillis(), null,
                      ZipFileSet.DEFAULT_FILE_MODE);
        super.initZipOutputStream(zOut);
    }

    /**
     * Finalize the zip output stream.
     * This creates an index list if the index attribute is true.
     * @param zOut the zip output stream
     * @throws IOException on I/O errors
     * @throws BuildException on other errors
     */
    protected void finalizeZipOutputStream(ZipOutputStream zOut)
        throws IOException, BuildException {

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

        writeIndexLikeList(new ArrayList(addedDirs.keySet()),
                           rootEntries, writer);
        writer.println();

        if (indexJars != null) {
            Manifest mf = createManifest();
            Manifest.Attribute classpath =
                mf.getMainSection().getAttribute(Manifest.ATTRIBUTE_CLASSPATH);
            String[] cpEntries = null;
            if (classpath != null && classpath.getValue() != null) {
                StringTokenizer tok = new StringTokenizer(classpath.getValue(),
                                                          " ");
                cpEntries = new String[tok.countTokens()];
                int c = 0;
                while (tok.hasMoreTokens()) {
                    cpEntries[c++] = tok.nextToken();
                }
            }
            String[] indexJarEntries = indexJars.list();
            for (int i = 0; i < indexJarEntries.length; i++) {
                String name = findJarName(indexJarEntries[i], cpEntries);
                if (name != null) {
                    ArrayList dirs = new ArrayList();
                    ArrayList files = new ArrayList();
                    grabFilesAndDirs(indexJarEntries[i], dirs, files);
                    if (dirs.size() + files.size() > 0) {
                        writer.println(name);
                        writeIndexLikeList(dirs, files, writer);
                        writer.println();
                    }
                }
            }
        }

        writer.close();
        ByteArrayInputStream bais =
            new ByteArrayInputStream(baos.toByteArray());
        super.zipFile(bais, zOut, INDEX_NAME, System.currentTimeMillis(), null,
                      ZipFileSet.DEFAULT_FILE_MODE);
    }

    /**
     * Overridden from Zip class to deal with manifests and index lists.
     * @param is the input stream
     * @param zOut the zip output stream
     * @param vPath the name this entry shall have in the archive
     * @param lastModified last modification time for the entry.
     * @param fromArchive the original archive we are copying this
     *                    entry from, will be null if we are not copying from an archive.
     * @param mode the Unix permissions to set.
     * @throws IOException on error
     */
    protected void zipFile(InputStream is, ZipOutputStream zOut, String vPath,
                           long lastModified, File fromArchive, int mode)
        throws IOException {
        if (MANIFEST_NAME.equalsIgnoreCase(vPath))  {
            if (!doubleFilePass || skipWriting) {
                filesetManifest(fromArchive, is);
            }
        } else if (INDEX_NAME.equalsIgnoreCase(vPath) && index) {
            log("Warning: selected " + archiveType
                + " files include a META-INF/INDEX.LIST which will"
                + " be replaced by a newly generated one.", Project.MSG_WARN);
        } else {
            if (index && vPath.indexOf("/") == -1) {
                rootEntries.addElement(vPath);
            }
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
        } else if (filesetManifestConfig != null
                    && !filesetManifestConfig.getValue().equals("skip")) {
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
     * @param rcs The resource collections to grab resources from
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
    protected ArchiveState getResourcesToAdd(ResourceCollection[] rcs,
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
                log("error while reading original manifest in file: "
                    + zipFile.toString() + t.getMessage(),
                    Project.MSG_WARN);
                needsUpdate = true;
            }

        } else {
            needsUpdate = true;
        }

        createEmpty = needsUpdate;
        return super.getResourcesToAdd(rcs, zipFile, needsUpdate);
    }

    /**
     * Create an empty jar file.
     * @param zipFile the file to create
     * @return true for historic reasons
     * @throws BuildException on error
     */
    protected boolean createEmptyZip(File zipFile) throws BuildException {
        if (!createEmpty) {
            return true;
        }

        if (emptyBehavior.equals("skip")) {
                log("Warning: skipping " + archiveType + " archive "
                    + zipFile + " because no files were included.",
                    Project.MSG_WARN);
                return true;
        } else if (emptyBehavior.equals("fail")) {
            throw new BuildException("Cannot create " + archiveType
                                     + " archive " + zipFile
                                     + ": no files were included.",
                                     getLocation());
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
            FileUtils.close(zOut);
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
        checkJarSpec();

        if (!doubleFilePass || !skipWriting) {
            manifest = null;
            configuredManifest = savedConfiguredManifest;
            filesetManifest = null;
            originalManifest = null;
        }
        rootEntries.removeAllElements();
    }

    /**
     * Check against packaging spec
     */
    private void checkJarSpec() {
        String br = System.getProperty("line.separator");
        StringBuffer message = new StringBuffer();
        Section mainSection = (configuredManifest == null)
                            ? null
                            : configuredManifest.getMainSection();

        if (mainSection == null) {
            message.append("No Implementation-Title set.");
            message.append("No Implementation-Version set.");
            message.append("No Implementation-Vendor set.");
        } else {
            if (mainSection.getAttribute("Implementation-Title") == null) {
                message.append("No Implementation-Title set.");
            }
            if (mainSection.getAttribute("Implementation-Version") == null) {
                message.append("No Implementation-Version set.");
            }
            if (mainSection.getAttribute("Implementation-Vendor") == null) {
                message.append("No Implementation-Vendor set.");
            }
        }

        if (message.length() > 0) {
            message.append(br);
            message.append("Location: ").append(getLocation());
            message.append(br);
            if (strict.getValue().equalsIgnoreCase("fail")) {
                throw new BuildException(message.toString(), getLocation());
            } else {
                log(message.toString(), strict.getLogLevel());
            }
        }
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
        emptyBehavior = "create";
        configuredManifest = null;
        filesetManifestConfig = null;
        mergeManifestsMain = false;
        manifestFile = null;
        index = false;
    }

    /**
     * The manifest config enumerated type.
     */
    public static class FilesetManifestConfig extends EnumeratedAttribute {
        /**
         * Get the list of valid strings.
         * @return the list of values - "skip", "merge" and "mergewithoutmain"
         */
        public String[] getValues() {
            return new String[] {"skip", "merge", "mergewithoutmain"};
        }
    }

    /**
     * Writes the directory entries from the first and the filenames
     * from the second list to the given writer, one entry per line.
     *
     * @param dirs a list of directories
     * @param files a list of files
     * @param writer the writer to write to
     * @throws IOException on error
     * @since Ant 1.6.2
     */
    protected final void writeIndexLikeList(List dirs, List files,
                                            PrintWriter writer)
        throws IOException {
        Collections.sort(dirs);
        Collections.sort(files);
        Iterator iter = dirs.iterator();
        while (iter.hasNext()) {
            String dir = (String) iter.next();

            dir = dir.replace('\\', '/');
            if (dir.startsWith("./")) {
                dir = dir.substring(2);
            }
            while (dir.startsWith("/")) {
                dir = dir.substring(1);
            }
            int pos = dir.lastIndexOf('/');
            if (pos != -1) {
                dir = dir.substring(0, pos);
            }

            if (dir.startsWith("META-INF")) {
                continue;
            }
            writer.println(dir);
        }

        iter = files.iterator();
        while (iter.hasNext()) {
            writer.println(iter.next());
        }
    }

    /**
     * try to guess the name of the given file.
     *
     * <p>If this jar has a classpath attribute in its manifest, we
     * can assume that it will only require an index of jars listed
     * there.  try to find which classpath entry is most likely the
     * one the given file name points to.</p>
     *
     * <p>In the absence of a classpath attribute, assume the other
     * files will be placed inside the same directory as this jar and
     * use their basename.</p>
     *
     * <p>if there is a classpath and the given file doesn't match any
     * of its entries, return null.</p>
     *
     * @param fileName the name to look for
     * @param classpath the classpath to look in (may be null)
     * @return the matching entry, or null if the file is not found
     * @since Ant 1.6.2
     */
    protected static String findJarName(String fileName,
                                              String[] classpath) {
        if (classpath == null) {
            return (new File(fileName)).getName();
        }
        fileName = fileName.replace(File.separatorChar, '/');
        TreeMap matches = new TreeMap(new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (o1 instanceof String && o2 instanceof String) {
                        return ((String) o2).length()
                            - ((String) o1).length();
                    }
                    return 0;
                }
            });

        for (int i = 0; i < classpath.length; i++) {
            if (fileName.endsWith(classpath[i])) {
                matches.put(classpath[i], classpath[i]);
            } else {
                int slash = classpath[i].indexOf("/");
                String candidate = classpath[i];
                while (slash > -1) {
                    candidate = candidate.substring(slash + 1);
                    if (fileName.endsWith(candidate)) {
                        matches.put(candidate, classpath[i]);
                        break;
                    }
                    slash = candidate.indexOf("/");
                }
            }
        }

        return matches.size() == 0
            ? null : (String) matches.get(matches.firstKey());
    }

    /**
     * Grab lists of all root-level files and all directories
     * contained in the given archive.
     * @param file the zip file to examine
     * @param dirs where to place the directories found
     * @param files where to place the files found
     * @since Ant 1.7
     * @throws IOException on error
     */
    protected static void grabFilesAndDirs(String file, List dirs,
                                                 List files)
        throws IOException {
        org.apache.tools.zip.ZipFile zf = null;
        try {
            zf = new org.apache.tools.zip.ZipFile(file, "utf-8");
            Enumeration entries = zf.getEntries();
            HashSet dirSet = new HashSet();
            while (entries.hasMoreElements()) {
                org.apache.tools.zip.ZipEntry ze =
                    (org.apache.tools.zip.ZipEntry) entries.nextElement();
                String name = ze.getName();
                if (!name.startsWith("META-INF/")) {
                    if (ze.isDirectory()) {
                        dirSet.add(name);
                    } else if (name.indexOf("/") == -1) {
                        files.add(name);
                    } else {
                        dirSet.add(name.substring(0,
                                                  name.lastIndexOf("/") + 1));
                    }
                }
            }
            dirs.addAll(dirSet);
        } finally {
            if (zf != null) {
                zf.close();
            }
        }
    }

    /** The strict enumerated type. */
    public static class StrictMode extends EnumeratedAttribute {
        /** Public no arg constructor. */
        public StrictMode() {
        }
        /**
         * Constructor with an arg.
         * @param value the enumerated value as a string.
         */
        public StrictMode(String value) {
            setValue(value);
        }
        /**
         * Get List of valid strings.
         * @return the list of values.
         */
        public String[] getValues() {
            return new String[]{"fail", "warn", "ignore"};
        }
        /**
         * @return The log level according to the strict mode.
         */
        public int getLogLevel() {
            return (getValue().equals("ignore")) ? Project.MSG_VERBOSE : Project.MSG_WARN;
        }
    }
}
