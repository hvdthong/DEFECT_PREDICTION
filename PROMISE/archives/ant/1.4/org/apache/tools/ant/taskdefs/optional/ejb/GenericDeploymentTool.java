package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;
import java.net.*;

import javax.xml.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

/**
 * A deployment tool which creates generic EJB jars. Generic jars contains
 * only those classes and META-INF entries specified in the EJB 1.1 standard
 *
 * This class is also used as a framework for the creation of vendor specific
 * deployment tools. A number of template methods are provided through which the
 * vendor specific tool can hook into the EJB creation process.
 */
public class GenericDeploymentTool implements EJBDeploymentTool {
    /** Private constants that are used when constructing the standard jarfile */
    protected static final String META_DIR  = "META-INF/";
    protected static final String EJB_DD    = "ejb-jar.xml";

    /**
     * The configuration from the containing task. This config combined with the 
     * settings of the individual attributes here constitues the complete config for
     * this deployment tool.
     */
    private EjbJar.Config config;

    /** Stores a handle to the directory to put the Jar files in */
    private File destDir;
    
    /** The classpath to use with this deployment tool. This is appended to 
        any paths from the ejbjar task itself.*/
    private Path classpath;

    /** Instance variable that stores the suffix for the generated jarfile. */
    private String genericJarSuffix = "-generic.jar";

    /**
     * The task to which this tool belongs. This is used to access services provided
     * by the ant core, such as logging.
     */
    private Task task;
    
    /**
     * The classloader generated from the given classpath to load
     * the super classes and super interfaces.
     */
    private ClassLoader classpathLoader = null;
    
     /**
     * List of files have been loaded into the EJB jar
     */
    private List addedfiles;

    /**
     * Handler used to parse the EJB XML descriptor
     */
    private DescriptorHandler handler;

    /**
     * Setter used to store the value of destination directory prior to execute()
     * being called.
     * @param inDir the destination directory.
     */
    public void setDestdir(File inDir) {
        this.destDir = inDir;
    }

    /**
     * Get the desitination directory.
     */
    protected File getDestDir() {
        return destDir;
    }
    

    /**
     * Set the task which owns this tool
     */
    public void setTask(Task task) {
        this.task = task;
    }
       
    /**
     * Get the task for this tool.
     */
    protected Task getTask() {
        return task;
    }

    /**
     * Get the basename terminator.
     */
    protected EjbJar.Config getConfig() {
        return config;
    }
    
    /**
     * Returns true, if the meta-inf dir is being explicitly set, false otherwise.
     */
    protected boolean usingBaseJarName() {
        return config.baseJarName != null;
    }
    
    /**
     * Setter used to store the suffix for the generated jar file.
     * @param inString the string to use as the suffix.
     */
    public void setGenericJarSuffix(String inString) {
        this.genericJarSuffix = inString;
    }

    /**
     * Add the classpath for the user classes
     */
    public Path createClasspath() {
        if (classpath == null) {
            classpath = new Path(task.getProject());
        }
        return classpath.createPath();
    }

    /**
     * Set the classpath to be used for this compilation.
     */
    public void setClasspath(Path classpath) {
        this.classpath = classpath;
    }

    /**
     * Get the classpath by combining the one from the surrounding task, if any
     * and the one from tis tool.
     */
    protected Path getCombinedClasspath() {
        Path combinedPath = classpath;
        if (config.classpath != null) {
            if (combinedPath == null) {
                combinedPath = config.classpath;
            }
            else {
                combinedPath.append(config.classpath);
            }
        }
        
        return combinedPath;
    }
    
    protected void log(String message, int level) {
        getTask().log(message, level);
    }

    protected Location getLocation() {
        return getTask().getLocation();
    }


    /**
     * Configure this tool for use in the ejbjar task.
     */
    public void configure(EjbJar.Config config) {
        this.config = config;
        
        classpathLoader = null;
    }

    /**
     * Utility method that encapsulates the logic of adding a file entry to
     * a .jar file.  Used by execute() to add entries to the jar file as it is
     * constructed.
     * @param jStream A JarOutputStream into which to write the
     *        jar entry.
     * @param inputFile A File from which to read the
     *        contents the file being added.
     * @param logicalFilename A String representing the name, including
     *        all relevant path information, that should be stored for the entry
     *        being added.
     */
    protected void addFileToJar(JarOutputStream jStream,
                                File inputFile,
                                String logicalFilename)
        throws BuildException {
        FileInputStream iStream = null;
        try {
            if (!addedfiles.contains(logicalFilename)) {
                iStream = new FileInputStream(inputFile);
                ZipEntry zipEntry = new ZipEntry(logicalFilename.replace('\\','/'));
                jStream.putNextEntry(zipEntry);
                   
                byte[] byteBuffer = new byte[2 * 1024];
                int count = 0;
                do {
                    jStream.write(byteBuffer, 0, count);
                    count = iStream.read(byteBuffer, 0, byteBuffer.length);
                } while (count != -1);
                
                addedfiles.add(logicalFilename);
           }       
        }
        catch (IOException ioe) {
            log("WARNING: IOException while adding entry " + 
                logicalFilename + " to jarfile from " + inputFile.getPath() + " " + 
                ioe.getClass().getName() + "-" + ioe.getMessage(), Project.MSG_WARN);
        }
        finally {
            if (iStream != null) {
                try {
                    iStream.close();
                }
                catch (IOException closeException) {}
            }
        }
    }

    protected DescriptorHandler getDescriptorHandler(File srcDir) { 
        DescriptorHandler handler = new DescriptorHandler(getTask(), srcDir); 
        
        registerKnownDTDs(handler);
        
        for (Iterator i = getConfig().dtdLocations.iterator(); i.hasNext();) { 
            EjbJar.DTDLocation dtdLocation = (EjbJar.DTDLocation)i.next(); 
            handler.registerDTD(dtdLocation.getPublicId(), dtdLocation.getLocation()); 
        } 
        return handler; 
    } 
    
    /**
     * Register the locations of all known DTDs.
     *
     * vendor-specific subclasses should override this method to define
     * the vendor-specific locations of the EJB DTDs
     */
    protected void registerKnownDTDs(DescriptorHandler handler) {
    }

    public void processDescriptor(String descriptorFileName, SAXParser saxParser) {

        checkConfiguration(descriptorFileName, saxParser);
                    
        try {
            handler = getDescriptorHandler(config.srcDir);

            Hashtable ejbFiles = parseEjbFiles(descriptorFileName, saxParser);

            addSupportClasses(ejbFiles);

            String baseName = getJarBaseName(descriptorFileName);

            String ddPrefix = getVendorDDPrefix(baseName, descriptorFileName);

            ejbFiles.put(META_DIR + EJB_DD,
                         new File(config.descriptorDir, descriptorFileName));
            
            addVendorFiles(ejbFiles, ddPrefix);

            checkAndAddInherited(ejbFiles);

            if (config.flatDestDir && baseName.length() != 0) {
                int startName = baseName.lastIndexOf(File.separator);
                if (startName == -1) {
                    startName = 0;
                }
                
                int endName   = baseName.length();
                baseName = baseName.substring(startName, endName);
            }
            
            File jarFile = getVendorOutputJarFile(baseName);
            
            
            if (needToRebuild(ejbFiles, jarFile)) {
                log( "building "
                              + jarFile.getName()
                              + " with "
                              + String.valueOf(ejbFiles.size())
                              + " files",
                              Project.MSG_INFO);
    
                String publicId = getPublicId();
                writeJar(baseName, jarFile, ejbFiles, publicId);

            }
            else {
                log(jarFile.toString() + " is up to date.",
                              Project.MSG_VERBOSE);
            }

        }
        catch (SAXException se) {
            String msg = "SAXException while parsing '"
                + descriptorFileName.toString()
                + "'. This probably indicates badly-formed XML."
                + "  Details: "
                + se.getMessage();
            throw new BuildException(msg, se);
        }
        catch (IOException ioe) {
            String msg = "IOException while parsing'"
                + descriptorFileName.toString()
                + "'.  This probably indicates that the descriptor"
                + " doesn't exist. Details: "
                + ioe.getMessage();
            throw new BuildException(msg, ioe);
        }
    }
    
    /**
     * This method is called as the first step in the processDescriptor method
     * to allow vendor-specific subclasses to validate the task configuration
     * prior to processing the descriptor.  If the configuration is invalid,
     * a BuildException should be thrown.
     *
     * @param descriptorFileName String representing the file name of an EJB
     *                           descriptor to be processed
     * @param saxParser          SAXParser which may be used to parse the XML
     *                           descriptor
     * @thows BuildException     Thrown if the configuration is invalid
     */
    protected void checkConfiguration(String descriptorFileName, 
                                    SAXParser saxParser) throws BuildException {

        /* 
         * For the GenericDeploymentTool, do nothing.  Vendor specific 
         * subclasses should throw a BuildException if the configuration is 
         * invalid for their server.
         */
    }

    /**
     * This method returns a list of EJB files found when the specified EJB
     * descriptor is parsed and processed.
     *
     * @param descriptorFileName String representing the file name of an EJB
     *                           descriptor to be processed
     * @param saxParser          SAXParser which may be used to parse the XML
     *                           descriptor
     * @return                   Hashtable of EJB class (and other) files to be
     *                           added to the completed JAR file
     * @throws SAXException      Any SAX exception, possibly wrapping another 
     *                           exception
     * @throws IOException       An IOException from the parser, possibly from a
     *                           the byte stream or character stream 
     */
    protected Hashtable parseEjbFiles(String descriptorFileName, SAXParser saxParser)
                            throws IOException, SAXException {
        FileInputStream descriptorStream = null;
        Hashtable ejbFiles = null;

        try {

            /* Parse the ejb deployment descriptor.  While it may not
             * look like much, we use a SAXParser and an inner class to
             * get hold of all the classfile names for the descriptor.
             */
            descriptorStream = new FileInputStream(new File(config.descriptorDir, descriptorFileName));
            saxParser.parse(new InputSource(descriptorStream), handler);
                            
            ejbFiles = handler.getFiles();

        } finally {
            if (descriptorStream != null) {
                try {
                    descriptorStream.close();
                }
                catch (IOException closeException) {}
            }
        }

        return ejbFiles;
    }

    /**
     * Adds any classes the user specifies using <i>support</i> nested elements
     * to the <code>ejbFiles</code> Hashtable.
     *
     * @param ejbFiles Hashtable of EJB classes (and other) files that will be
     *                 added to the completed JAR file
     */
    protected void addSupportClasses(Hashtable ejbFiles) {
        Project project = task.getProject();
        for (Iterator i = config.supportFileSets.iterator(); i.hasNext();) {
            FileSet supportFileSet = (FileSet)i.next();
            File supportBaseDir = supportFileSet.getDir(project);
            DirectoryScanner supportScanner = supportFileSet.getDirectoryScanner(project);
            supportScanner.scan();
            String[] supportFiles = supportScanner.getIncludedFiles();
            for (int j = 0; j < supportFiles.length; ++j) {
                ejbFiles.put(supportFiles[j], new File(supportBaseDir, supportFiles[j]));
            }
        }            
    }


    /**
     * Using the EJB descriptor file name passed from the <code>ejbjar</code>
     * task, this method returns the "basename" which will be used to name the
     * completed JAR file.
     *
     * @param descriptorFileName String representing the file name of an EJB
     *                           descriptor to be processed
     * @return                   The "basename" which will be used to name the
     *                           completed JAR file
     */
    protected String getJarBaseName(String descriptorFileName) {

        String baseName = "";

        if (config.namingScheme.getValue().equals(EjbJar.NamingScheme.BASEJARNAME)) {
            String canonicalDescriptor = descriptorFileName.replace('\\', '/');
            int index = canonicalDescriptor.lastIndexOf('/');
            if (index != -1) {
                baseName = descriptorFileName.substring(0, index + 1);
            }
            baseName += config.baseJarName;
        } else if (config.namingScheme.getValue().equals(EjbJar.NamingScheme.DESCRIPTOR)) {
            int lastSeparatorIndex = descriptorFileName.lastIndexOf(File.separator);
            int endBaseName = -1;
            if (lastSeparatorIndex != -1) {
                endBaseName = descriptorFileName.indexOf(config.baseNameTerminator, 
                                                            lastSeparatorIndex);
            } else {
                endBaseName = descriptorFileName.indexOf(config.baseNameTerminator);
            }

            if (endBaseName != -1) {
                baseName = descriptorFileName.substring(0, endBaseName);
            }
            baseName = descriptorFileName.substring(0, endBaseName);
        } else if (config.namingScheme.getValue().equals(EjbJar.NamingScheme.DIRECTORY)) {
            int lastSeparatorIndex = descriptorFileName.lastIndexOf(File.separator);
            String dirName = descriptorFileName.substring(0, lastSeparatorIndex);
            int dirSeparatorIndex = dirName.lastIndexOf(File.separator);
            if (dirSeparatorIndex != -1) {
                dirName = dirName.substring(dirSeparatorIndex + 1);
            }
            
            baseName = dirName;
        } else if (config.namingScheme.getValue().equals(EjbJar.NamingScheme.EJB_NAME)) {
            baseName = handler.getEjbName();
        }
        return baseName;
    }

    /**
     * Get the prefix for vendor deployment descriptors.
     *
     * This will contain the path and the start of the descriptor name, 
     * depending on the naming scheme
     */
    public String getVendorDDPrefix(String baseName, String descriptorFileName) {
        String ddPrefix = null;

        if (config.namingScheme.getValue().equals(EjbJar.NamingScheme.DESCRIPTOR)) {
            ddPrefix = baseName + config.baseNameTerminator;
        } else if (config.namingScheme.getValue().equals(EjbJar.NamingScheme.BASEJARNAME) ||
                   config.namingScheme.getValue().equals(EjbJar.NamingScheme.EJB_NAME) ||
                   config.namingScheme.getValue().equals(EjbJar.NamingScheme.DIRECTORY)) {
            String canonicalDescriptor = descriptorFileName.replace('\\', '/');
            int index = canonicalDescriptor.lastIndexOf('/');
            if (index == -1) {
                ddPrefix = "";
            }
            else {
                ddPrefix = descriptorFileName.substring(0, index + 1);
            }
        }
        return ddPrefix;
    }

    /**
     * Add any vendor specific files which should be included in the 
     * EJB Jar.
     */
    protected void addVendorFiles(Hashtable ejbFiles, String ddPrefix) {
    }


    /**
     * Get the vendor specific name of the Jar that will be output. The modification date
     * of this jar will be checked against the dependent bean classes.
     */
    File getVendorOutputJarFile(String baseName) {
        return new File(destDir, baseName + genericJarSuffix);
    }

    /**
     * This method checks the timestamp on each file listed in the <code>
     * ejbFiles</code> and compares them to the timestamp on the <code>jarFile
     * </code>.  If the <code>jarFile</code>'s timestamp is more recent than
     * each EJB file, <code>true</code> is returned.  Otherwise, <code>false
     * </code> is returned.
     *
     * @param ejbFiles Hashtable of EJB classes (and other) files that will be
     *                 added to the completed JAR file
     * @param jarFile  JAR file which will contain all of the EJB classes (and
     *                 other) files
     * @return         boolean indicating whether or not the <code>jarFile</code>
     *                 is up to date
     */
    protected boolean needToRebuild(Hashtable ejbFiles, File jarFile) {
        if (jarFile.exists()) {
            long lastBuild = jarFile.lastModified();
            if (config.manifest != null && config.manifest.exists() &&
                config.manifest.lastModified() > lastBuild) {
                log("Build needed because manifest " + config.manifest + " is out of date",
                    Project.MSG_VERBOSE);
                return true;
            }
                            
            
            Iterator fileIter = ejbFiles.values().iterator();

            while(fileIter.hasNext()) {
                File currentFile = (File) fileIter.next();
                if (lastBuild < currentFile.lastModified()) {
                    log("Build needed because " + currentFile.getPath() + " is out of date",
                        Project.MSG_VERBOSE);
                    return true;                        
                }
            }
            return false;
        }
        
        return true;
    }

    /**
     * Returns the Public ID of the DTD specified in the EJB descriptor.  Not
     * every vendor-specific <code>DeploymentTool</code> will need to reference
     * this value or may want to determine this value in a vendor-specific way.
     *
     * @return         Public ID of the DTD specified in the EJB descriptor.
     */
    protected String getPublicId() {
        return handler.getPublicId();
    }

    /**
     * Method used to encapsulate the writing of the JAR file. Iterates over the
     * filenames/java.io.Files in the Hashtable stored on the instance variable
     * ejbFiles.
     */
    protected void writeJar(String baseName, File jarfile, Hashtable files, 
                            String publicId) throws BuildException{

        JarOutputStream jarStream = null;
        try {
            addedfiles = new ArrayList();

            /* If the jarfile already exists then whack it and recreate it.
             * Should probably think of a more elegant way to handle this
             * so that in case of errors we don't leave people worse off
             * than when we started =)
             */
            if (jarfile.exists()) {
                jarfile.delete();
            }
            jarfile.getParentFile().mkdirs();
            jarfile.createNewFile();
            
            InputStream in = null;
            Manifest manifest = null;
            try {
                if (config.manifest != null) {
                    in = new FileInputStream(config.manifest);
                    if ( in == null ) {
                        throw new BuildException("Could not find manifest file: " + config.manifest, 
                                                  getLocation());
                    }
                }
                else {
                    String defaultManifest = "/org/apache/tools/ant/defaultManifest.mf";
                    in = this.getClass().getResourceAsStream(defaultManifest);
                    if ( in == null ) {
                        throw new BuildException("Could not find default manifest: " + defaultManifest,
                                                  getLocation());
                    }
                }
                            
                manifest = new Manifest(in);
            }
            catch (IOException e) {
                throw new BuildException ("Unable to read manifest", e, getLocation());
            }
            finally {
                if (in != null) {
                    in.close();
                }
            }
            
            
            jarStream = new JarOutputStream(new FileOutputStream(jarfile), manifest);
            jarStream.setMethod(JarOutputStream.DEFLATED);
            
            for (Iterator entryIterator = files.keySet().iterator(); entryIterator.hasNext(); ) {
                String entryName = (String) entryIterator.next();
                File entryFile = (File) files.get(entryName);
                
                log("adding file '" + entryName + "'",
                              Project.MSG_VERBOSE);

                addFileToJar(jarStream, entryFile, entryName);

                InnerClassFilenameFilter flt = new InnerClassFilenameFilter(entryFile.getName());
                File entryDir = entryFile.getParentFile();
                String[] innerfiles = entryDir.list(flt);
                for (int i = 0, n = innerfiles.length; i < n; i++) {
            
                    int entryIndex = entryName.lastIndexOf(entryFile.getName()) -1;
                    if ( entryIndex < 0) {
                        entryName = innerfiles[i];
                    }
                    else {
                        entryName = entryName.substring(0, entryIndex) + File.separatorChar + innerfiles[i];
                    }
                    entryFile = new File(config.srcDir, entryName);
        
                    log("adding innerclass file '" + entryName + "'", 
                            Project.MSG_VERBOSE);
        
                    addFileToJar(jarStream, entryFile, entryName);
        
                }
            }
        }
        catch(IOException ioe) {
            String msg = "IOException while processing ejb-jar file '"
                + jarfile.toString()
                + "'. Details: "
                + ioe.getMessage();
            throw new BuildException(msg, ioe);
        }
        finally {
            if (jarStream != null) {
                try {
                    jarStream.close();
                }
                catch (IOException closeException) {}
            }
        }

    /**
     * Check if a EJB Class Inherits from a Superclass, and if a Remote Interface
     * extends an interface other then javax.ejb.EJBObject directly.  Then add those 
     * classes to the generic-jar so they dont have to added elsewhere.
     *
     */
    protected void checkAndAddInherited(Hashtable checkEntries) throws BuildException
    {
        Hashtable copiedHash = (Hashtable)checkEntries.clone();

        for (Iterator entryIterator = copiedHash.keySet().iterator(); entryIterator.hasNext(); ) 
        {
            String entryName = (String)entryIterator.next();
            File entryFile = (File)copiedHash.get(entryName);

            if (entryName.endsWith(".class"))
            {
                String classname = entryName.substring(0,entryName.lastIndexOf(".class")).replace(File.separatorChar,'.');
                ClassLoader loader = getClassLoaderForBuild();
                try {
                    Class c = loader.loadClass(classname);

                    if (!c.isPrimitive())
                    {
                        {
                            log("looking at interface " + c.getName(),  Project.MSG_VERBOSE);
                            Class[] interfaces = c.getInterfaces();
                            for (int i = 0; i < interfaces.length; i++){
                                log("     implements " + interfaces[i].getName(),  Project.MSG_VERBOSE);
                                addInterface(interfaces[i], checkEntries);
                            }
                        }
                        {
                            log("looking at class " + c.getName(),  Project.MSG_VERBOSE);
                            Class s = c.getSuperclass();
                            addSuperClass(c.getSuperclass(), checkEntries);
                        }
                }
                catch (ClassNotFoundException cnfe) {
                    log("Could not load class " + classname + " for super class check", 
                                  Project.MSG_WARN);
                }                            
                catch (NoClassDefFoundError ncdfe) {
                    log("Could not fully load class " + classname + " for super class check", 
                                  Project.MSG_WARN);
                }                            
    }

    private void addInterface(Class theInterface, Hashtable checkEntries) {
        { 
            File interfaceFile = new File(config.srcDir.getAbsolutePath() 
                                        + File.separatorChar 
                                        + theInterface.getName().replace('.',File.separatorChar)
                                        + ".class"
                                        );
            if (interfaceFile.exists() && interfaceFile.isFile())
            {
                checkEntries.put(theInterface.getName().replace('.',File.separatorChar)+".class",
                                 interfaceFile);
                Class[] superInterfaces = theInterface.getInterfaces();
                for (int i = 0; i < superInterfaces.length; i++) {
                    addInterface(superInterfaces[i], checkEntries);
                }
            }
        }
    }
     
    private void addSuperClass(Class superClass, Hashtable checkEntries) {
    
        if (!superClass.getName().startsWith("java"))
        {
            File superClassFile = new File(config.srcDir.getAbsolutePath() 
                                            + File.separatorChar 
                                            + superClass.getName().replace('.',File.separatorChar)
                                            + ".class");
            if (superClassFile.exists() && superClassFile.isFile())
            {
                checkEntries.put(superClass.getName().replace('.',File.separatorChar) + ".class", 
                                 superClassFile);
                
                Class[] superInterfaces = superClass.getInterfaces();
                for (int i = 0; i < superInterfaces.length; i++) {
                    addInterface(superInterfaces[i], checkEntries);
                }
                
                addSuperClass(superClass.getSuperclass(), checkEntries);
            }               
        }
    }
    
    /**
     * Returns a Classloader object which parses the passed in generic EjbJar classpath.
     * The loader is used to dynamically load classes from javax.ejb.* and the classes 
     * being added to the jar.
     *
     */ 
    protected ClassLoader getClassLoaderForBuild()
    {
        if (classpathLoader != null) {
            return classpathLoader;
        }
        
        Path combinedClasspath = getCombinedClasspath();
        
        if (combinedClasspath == null) {
            classpathLoader = getClass().getClassLoader();
        }
        else {
            classpathLoader = new AntClassLoader(getTask().getProject(), combinedClasspath);
        }
        
        return classpathLoader;
    }
 
    /**
     * Called to validate that the tool parameters have been configured.
     *
     * @throws BuildException If the Deployment Tool's configuration isn't
     *                        valid
     */
    public void validateConfigured() throws BuildException {
        if ((destDir == null) || (!destDir.isDirectory())) {
            String msg = "A valid destination directory must be specified "
                            + "using the \"destdir\" attribute.";
            throw new BuildException(msg, getLocation());
        }
    }
}
