package org.apache.tools.ant;

import java.lang.reflect.*;
import java.util.*;
import java.util.zip.*;
import java.io.*;
import java.net.*;
import org.apache.tools.ant.types.Path;

/**
 * Used to load classes within ant with a different claspath from that used to start ant.
 * Note that it is possible to force a class into this loader even when that class is on the
 * system classpath by using the forceLoadClass method. Any subsequent classes loaded by that
 * class will then use this loader rather than the system class loader.
 *
 * @author <a href="mailto:conor@cortexebusiness.com.au">Conor MacNeill</a>
 * @author <a href="mailto:Jesse.Glick@netbeans.com">Jesse Glick</a>
 */
public class AntClassLoader extends ClassLoader implements BuildListener {

    /**
     * An enumeration of all resources of a given name found within the
     * classpath of this class loader. This enumeration is used by the
     * {@link #findResources(String) findResources} method, which is in
     * turn used by the
     * {@link ClassLoader#getResources ClassLoader.getResources} method.
     *
     * @see AntClassLoader#findResources(String)
     * @see java.lang.ClassLoader#getResources(String)
     * @author <a href="mailto:hermand@alumni.grinnell.edu">David A. Herman</a>
     */
    private class ResourceEnumeration implements Enumeration {

        /**
         * The name of the resource being searched for.
         */
        private String resourceName;

        /**
         * The index of the next classpath element to search.
         */
        private int pathElementsIndex;

        /**
         * The URL of the next resource to return in the enumeration. If this
         * field is <code>null</code> then the enumeration has been completed,
         * i.e., there are no more elements to return.
         */
        private URL nextResource;

        /**
         * Construct a new enumeration of resources of the given name found
         * within this class loader's classpath.
         *
         * @param name the name of the resource to search for.
         */
        ResourceEnumeration(String name) {
            this.resourceName = name;
            this.pathElementsIndex = 0;
            findNextResource();
        }

        /**
         * Indicates whether there are more elements in the enumeration to
         * return.
         *
         * @return <code>true</code> if there are more elements in the
         *         enumeration; <code>false</code> otherwise.
         */
        public boolean hasMoreElements() {
            return (this.nextResource != null);
        }

        /**
         * Returns the next resource in the enumeration.
         *
         * @return the next resource in the enumeration.
         */
        public Object nextElement() {
            URL ret = this.nextResource;
            findNextResource();
            return ret;
        }

        /**
         * Locates the next resource of the correct name in the classpath and
         * sets <code>nextResource</code> to the URL of that resource. If no
         * more resources can be found, <code>nextResource</code> is set to
         * <code>null</code>.
         */
        private void findNextResource() {
            URL url = null;
            while ((pathElementsIndex < pathComponents.size()) &&
                   (url == null)) {
                try {                       
                    File pathComponent 
                        = (File)pathComponents.elementAt(pathElementsIndex);
                    url = getResourceURL(pathComponent, this.resourceName);
                    pathElementsIndex++;
                }
                catch (BuildException e) {
                }
            }
            this.nextResource = url;
        }
    }

    /**
     * The size of buffers to be used in this classloader.
     */
    static private final int BUFFER_SIZE = 8192;
    
    /**
     * The components of the classpath that the classloader searches for classes
     */
    Vector pathComponents  = new Vector();
    
    /**
     * The project to which this class loader belongs.
     */
    private Project project;

    /**
     * Indicates whether the parent class loader should be 
     * consulted before trying to load with this class loader. 
     */
    private boolean parentFirst = true;

    /**
     * These are the package roots that are to be loaded by the parent class loader
     * regardless of whether the parent class loader is being searched first or not.
     */
    private Vector systemPackages = new Vector();
    
    /**
     * These are the package roots that are to be loaded by this class loader
     * regardless of whether the parent class loader is being searched first or not.
     */
    private Vector loaderPackages = new Vector();
    
    /**
     * This flag indicates that the classloader will ignore the base
     * classloader if it can't find a class.
     */
    private boolean ignoreBase = false;

    /** 
     * The parent class loader, if one is given or can be determined
     */
    private ClassLoader parent = null;

    /**
     * A hashtable of zip files opened by the classloader
     */
    private Hashtable zipFiles = new Hashtable();     
    
    /**
     * The context loader saved when setting the thread's current context loader.
     */
    private ClassLoader savedContextLoader = null;
    private boolean isContextLoaderSaved = false;
    
    private static Method getProtectionDomain = null;
    private static Method defineClassProtectionDomain = null;
    private static Method getContextClassLoader = null;
    private static Method setContextClassLoader = null;
    static {
        try {
            getProtectionDomain = Class.class.getMethod("getProtectionDomain", new Class[0]);
            Class protectionDomain = Class.forName("java.security.ProtectionDomain");
            Class[] args = new Class[] {String.class, byte[].class, Integer.TYPE, Integer.TYPE, protectionDomain};
            defineClassProtectionDomain = ClassLoader.class.getDeclaredMethod("defineClass", args);
            
            getContextClassLoader = Thread.class.getMethod("getContextClassLoader", new Class[0]);
            args = new Class[] {ClassLoader.class};
            setContextClassLoader = Thread.class.getMethod("setContextClassLoader", args);
        }
        catch (Exception e) {}
    }

    
    /**
     * Create a classloader for the given project using the classpath given.
     *
     * @param project the project to which this classloader is to belong.
     * @param classpath the classpath to use to load the classes.  This
     *                is combined with the system classpath in a manner
     *                determined by the value of ${build.sysclasspath}
     */
    public AntClassLoader(Project project, Path classpath) {
        parent = AntClassLoader.class.getClassLoader();
        this.project = project;
        project.addBuildListener(this);
        if (classpath != null) {
            Path actualClasspath = classpath.concatSystemClasspath("ignore");
            String[] pathElements = actualClasspath.list();
            for (int i = 0; i < pathElements.length; ++i) {
                try {
                    addPathElement((String)pathElements[i]);
                }
                catch (BuildException e) {
                }
            }
        }
    }
    
    /**
     * Create a classloader for the given project using the classpath given.
     *
     * @param parent the parent classloader to which unsatisfied loading attempts
     *               are delgated
     * @param project the project to which this classloader is to belong.
     * @param classpath the classpath to use to load the classes.
     * @param parentFirst if true indicates that the parent classloader should be consulted
     *                    before trying to load the a class through this loader.
     */
    public AntClassLoader(ClassLoader parent, Project project, Path classpath, 
                          boolean parentFirst) {
        this(project, classpath);
        if (parent != null) {
            this.parent = parent;
        }
        this.parentFirst = parentFirst;
        addSystemPackageRoot("java");
        addSystemPackageRoot("javax");
    }


    /**
     * Create a classloader for the given project using the classpath given.
     *
     * @param project the project to which this classloader is to belong.
     * @param classpath the classpath to use to load the classes.
     * @param parentFirst if true indicates that the parent classloader should be consulted
     *                    before trying to load the a class through this loader.
     */
    public AntClassLoader(Project project, Path classpath, boolean parentFirst) {
        this(null, project, classpath, parentFirst);
    }

    /**
     * Create an empty class loader. The classloader should be configured with path elements
     * to specify where the loader is to look for classes.
     *
     * @param parent the parent classloader to which unsatisfied loading attempts
     *               are delgated
     * @param parentFirst if true indicates that the parent classloader should be consulted
     *                    before trying to load the a class through this loader.
     */
    public AntClassLoader(ClassLoader parent, boolean parentFirst) {
        if (parent != null) {
            this.parent = parent;
        }
        else {
            parent = AntClassLoader.class.getClassLoader();
        }
        project = null;
        this.parentFirst = parentFirst;
    }
    
    /**
     * Log a message through the project object if one has been provided.
     *
     * @param message the message to log
     * @param priority the logging priority of the message
     */
    protected void log(String message, int priority) {
        if (project != null) {
            project.log(message, priority);
        }
    }

    /**
     * Set the current thread's context loader to this classloader, storing the current
     * loader value for later resetting
     */
    public void setThreadContextLoader() {
        if (isContextLoaderSaved) {
            throw new BuildException("Context loader has not been reset");
        }
        if (getContextClassLoader != null && setContextClassLoader != null) {
            try {
                savedContextLoader 
                    = (ClassLoader)getContextClassLoader.invoke(Thread.currentThread(), new Object[0]);
                Object[] args = new Object[] {this};
                setContextClassLoader.invoke(Thread.currentThread(), args);
                isContextLoaderSaved = true;
            }
            catch (InvocationTargetException ite) {
                Throwable t = ite.getTargetException();
                throw new BuildException(t.toString());
            }
            catch (Exception e) {
                throw new BuildException(e.toString());
            }
        }
    }
        
    /**
     * Reset the current thread's context loader to its original value
     */
    public void resetThreadContextLoader() {
        if (isContextLoaderSaved &&
                getContextClassLoader != null && setContextClassLoader != null) {
            try {
                Object[] args = new Object[] {savedContextLoader};
                setContextClassLoader.invoke(Thread.currentThread(), args);
                savedContextLoader = null;
                isContextLoaderSaved = false;
            }
            catch (InvocationTargetException ite) {
                Throwable t = ite.getTargetException();
                throw new BuildException(t.toString());
            }
            catch (Exception e) {
                throw new BuildException(e.toString());
            }
        }
    }
        
    
    /**
     * Add an element to the classpath to be searched
     *
     */
    public void addPathElement(String pathElement) throws BuildException {
        File pathComponent 
            = project != null ? project.resolveFile(pathElement)
                              : new File(pathElement);
        pathComponents.addElement(pathComponent);
    }
        
    /**
     * Set this classloader to run in isolated mode. In isolated mode, classes not
     * found on the given classpath will not be referred to the base class loader
     * but will cause a classNotFoundException.
     */
    public void setIsolated(boolean isolated) {
        ignoreBase = isolated;
    }

    /**
     * Force initialization of a class in a JDK 1.1 compatible, albeit hacky 
     * way 
     */
    static public void initializeClass(Class theClass) {
        try {
            theClass.newInstance();
        }
        catch (Throwable t) {
        }
    }
    
    /**
     * Add a package root to the list of packages which must be loaded on the 
     * parent loader.
     *
     * All subpackages are also included.
     *
     * @param packageRoot the root of all packages to be included.
     */
    public void addSystemPackageRoot(String packageRoot) {
        systemPackages.addElement(packageRoot + ".");
    }
    
    /**
     * Add a package root to the list of packages which must be loaded using
     * this loader.
     *
     * All subpackages are also included.
     *
     * @param packageRoot the root of akll packages to be included.
     */
    public void addLoaderPackageRoot(String packageRoot) {
        loaderPackages.addElement(packageRoot + ".");
    }
    


    /**
     * Load a class through this class loader even if that class is available on the
     * parent classpath.
     *
     * This ensures that any classes which are loaded by the returned class will use this
     * classloader.
     *
     * @param classname the classname to be loaded.
     * 
     * @return the required Class object
     *
     * @throws ClassNotFoundException if the requested class does not exist on
     * this loader's classpath.
     */
    public Class forceLoadClass(String classname) throws ClassNotFoundException {
        log("force loading " + classname, Project.MSG_DEBUG);
        
        Class theClass = findLoadedClass(classname);

        if (theClass == null) {
            theClass = findClass(classname);
        }
        
        return theClass;
    }

    /**
     * Load a class through this class loader but defer to the parent class loader
     *
     * This ensures that instances of the returned class will be compatible with instances which
     * which have already been loaded on the parent loader.
     *
     * @param classname the classname to be loaded.
     * 
     * @return the required Class object
     *
     * @throws ClassNotFoundException if the requested class does not exist on
     * this loader's classpath.
     */
    public Class forceLoadSystemClass(String classname) throws ClassNotFoundException {
        log("force system loading " + classname, Project.MSG_DEBUG);
        
        Class theClass = findLoadedClass(classname);

        if (theClass == null) {
            theClass = findBaseClass(classname);
        }
        
        return theClass;
    }

    /**
     * Get a stream to read the requested resource name.
     *
     * @param name the name of the resource for which a stream is required.
     *
     * @return a stream to the required resource or null if the resource cannot be
     * found on the loader's classpath.
     */
    public InputStream getResourceAsStream(String name) {

        InputStream resourceStream = null;
        if (isParentFirst(name)) {
            resourceStream = loadBaseResource(name);
            if (resourceStream != null) {
                log("ResourceStream for " + name
                    + " loaded from parent loader", Project.MSG_DEBUG);

            } else {
                resourceStream = loadResource(name);
                if (resourceStream != null) {
                    log("ResourceStream for " + name
                        + " loaded from ant loader", Project.MSG_DEBUG);
                }
            }
        }
        else {
            resourceStream = loadResource(name);
            if (resourceStream != null) {
                log("ResourceStream for " + name
                    + " loaded from ant loader", Project.MSG_DEBUG);

            } else {
                resourceStream = loadBaseResource(name);
                if (resourceStream != null) {
                    log("ResourceStream for " + name
                        + " loaded from parent loader", Project.MSG_DEBUG);
                }
            }
        }
            
        if (resourceStream == null) {
            log("Couldn't load ResourceStream for " + name, 
                Project.MSG_DEBUG);
        }

        return resourceStream;
    }
    
    
    
    /**
     * Get a stream to read the requested resource name from this loader.
     *
     * @param name the name of the resource for which a stream is required.
     *
     * @return a stream to the required resource or null if the resource cannot be
     * found on the loader's classpath.
     */
    private InputStream loadResource(String name) {
        InputStream stream = null;
 
        for (Enumeration e = pathComponents.elements(); e.hasMoreElements() && stream == null; ) {
            File pathComponent = (File)e.nextElement();
            stream = getResourceStream(pathComponent, name);
        }
        return stream;
    }

    /**
     * Find a system resource (which should be loaded from the parent classloader).
     */
    private InputStream loadBaseResource(String name) {
        if (parent == null) {
            return getSystemResourceAsStream(name);
        }
        else {
            return parent.getResourceAsStream(name);
        }
    }

    /**
     * Get an inputstream to a given resource in the given file which may
     * either be a directory or a zip file.
     *
     * @param file the file (directory or jar) in which to search for the resource.
     * @param resourceName the name of the resource for which a stream is required.
     *
     * @return a stream to the required resource or null if the resource cannot be
     * found in the given file object
     */
    private InputStream getResourceStream(File file, String resourceName) {
        try {
            if (!file.exists()) {
                return null;
            }
            
            if (file.isDirectory()) {
                File resource = new File(file, resourceName); 
                
                if (resource.exists()) {   
                    return new FileInputStream(resource);
                }
            }
            else {
                ZipFile zipFile = (ZipFile)zipFiles.get(file);
                if (zipFile == null) {
                    zipFile = new ZipFile(file);
                    zipFiles.put(file, zipFile);                    
                }
                ZipEntry entry = zipFile.getEntry(resourceName);
                if (entry != null) {
                    return zipFile.getInputStream(entry);
                }
            }
        }
        catch (Exception e) {
            log("Ignoring Exception " + e.getClass().getName() + ": " + e.getMessage() + 
                " reading resource " + resourceName + " from " + file, Project.MSG_VERBOSE);  
        }
        
        return null;   
    }

    private boolean isParentFirst(String resourceName) {
        boolean useParentFirst = parentFirst; 

        for (Enumeration e = systemPackages.elements(); e.hasMoreElements();) {
            String packageName = (String)e.nextElement();
            if (resourceName.startsWith(packageName)) {
                useParentFirst = true;
                break;
            }
        }

        for (Enumeration e = loaderPackages.elements(); e.hasMoreElements();) {
            String packageName = (String)e.nextElement();
            if (resourceName.startsWith(packageName)) {
                useParentFirst = false;
                break;
            }
        }
        
        return useParentFirst;
    }

    /**
     * Finds the resource with the given name. A resource is 
     * some data (images, audio, text, etc)
     * that can be accessed by class
     * code in a way that is independent of the location of the code.
     *
     * @param name the name of the resource for which a stream is required.
     *
     * @return a URL for reading the resource, or null if the resource 
     *         could not be found or the caller
     * doesn't have adequate privileges to get the resource.
     */
    public URL getResource(String name) {
        URL url = null;
        if (isParentFirst(name)) {
            url = (parent == null) ? super.getResource(name) : parent.getResource(name);
        }

        if (url != null) {
            log("Resource " + name + " loaded from parent loader", 
                Project.MSG_DEBUG);

        } else {
            for (Enumeration e = pathComponents.elements(); e.hasMoreElements() && url == null; ) {
                File pathComponent = (File)e.nextElement();
                url = getResourceURL(pathComponent, name);
                if (url != null) {
                    log("Resource " + name 
                        + " loaded from ant loader", 
                        Project.MSG_DEBUG);
                }
            }
        }
        
        if (url == null && !isParentFirst(name)) {
            
            url = (parent == null) ? super.getResource(name) : parent.getResource(name);
            if (url != null) {
                log("Resource " + name + " loaded from parent loader", 
                    Project.MSG_DEBUG);
            }
        }

        if (url == null) {
            log("Couldn't load Resource " + name, Project.MSG_DEBUG);
        }

        return url;
    }

    /**
     * Returns an enumeration of URLs representing all the resources with the
     * given name by searching the class loader's classpath.
     *
     * @param name the resource name.
     * @return an enumeration of URLs for the resources.
     * @throws IOException if I/O errors occurs (can't happen)
     */
    protected Enumeration findResources(String name) throws IOException {
        return new ResourceEnumeration(name);
    }

    /**
     * Get an inputstream to a given resource in the given file which may
     * either be a directory or a zip file.
     *
     * @param file the file (directory or jar) in which to search for 
     *             the resource.
     * @param resourceName the name of the resource for which a stream 
     *                     is required.
     *
     * @return a stream to the required resource or null if the 
     *         resource cannot be found in the given file object
     */
    private URL getResourceURL(File file, String resourceName) {
        try {
            if (!file.exists()) {
                return null;
            }

            if (file.isDirectory()) {
                File resource = new File(file, resourceName);

                if (resource.exists()) {
                    try {
                        return new URL("file:"+resource.toString());
                    } catch (MalformedURLException ex) {
                        return null;
                    }
                }
            }
            else {
                ZipFile zipFile = (ZipFile)zipFiles.get(file);
                if (zipFile == null) {
                    zipFile = new ZipFile(file);
                    zipFiles.put(file, zipFile);                    
                }

                ZipEntry entry = zipFile.getEntry(resourceName);
                if (entry != null) {
                    try {
                        return new URL("jar:file:"+file.toString()+"!/"+entry);
                    } catch (MalformedURLException ex) {
                        return null;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Load a class with this class loader.
     *
     * This method will load a class. 
     *
     * This class attempts to load the class firstly using the parent class loader. For
     * JDK 1.1 compatability, this uses the findSystemClass method.
     *
     * @param classname the name of the class to be loaded.
     * @param resolve true if all classes upon which this class depends are to be loaded.
     * 
     * @return the required Class object
     *
     * @throws ClassNotFoundException if the requested class does not exist on
     * the system classpath or this loader's classpath.
     */
    protected Class loadClass(String classname, boolean resolve) throws ClassNotFoundException {

        Class theClass = findLoadedClass(classname);
        if (theClass != null) {
            return theClass;
        }

        if (isParentFirst(classname)) {
            try {
                theClass = findBaseClass(classname);
                log("Class " + classname + " loaded from parent loader", Project.MSG_DEBUG);
            }
            catch (ClassNotFoundException cnfe) {
                theClass = findClass(classname);
                log("Class " + classname + " loaded from ant loader", Project.MSG_DEBUG);
            }
        }
        else {
            try {
                theClass = findClass(classname);
                log("Class " + classname + " loaded from ant loader", Project.MSG_DEBUG);
            }
            catch (ClassNotFoundException cnfe) {
                if (ignoreBase) {
                    throw cnfe;
                }
                theClass = findBaseClass(classname);
                log("Class " + classname + " loaded from parent loader", Project.MSG_DEBUG);
            }
        }
            
        if (resolve) {
            resolveClass(theClass);
        }

        return theClass;
    }

    /**
     * Convert the class dot notation to a filesystem equivalent for
     * searching purposes.
     *
     * @param classname the class name in dot format (ie java.lang.Integer)
     *
     * @return the classname in filesystem format (ie java/lang/Integer.class)
     */
    private String getClassFilename(String classname) {
        return classname.replace('.', '/') + ".class";
    }

    /**
     * Read a class definition from a stream.
     *
     * @param stream the stream from which the class is to be read.
     * @param classname the class name of the class in the stream.
     *
     * @return the Class object read from the stream.
     *
     * @throws IOException if there is a problem reading the class from the
     * stream.
     */
    private Class getClassFromStream(InputStream stream, String classname) 
                throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bytesRead = -1;
        byte[] buffer = new byte[BUFFER_SIZE];
        
        while ((bytesRead = stream.read(buffer, 0, BUFFER_SIZE)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        
        byte[] classData = baos.toByteArray();

        if (defineClassProtectionDomain != null) {
            try {
                Object domain = getProtectionDomain.invoke(Project.class, new Object[0]);
                Object[] args = new Object[] {classname, classData, new Integer(0), new Integer(classData.length), domain};
                return (Class)defineClassProtectionDomain.invoke(this, args);
            }
            catch (InvocationTargetException ite) {
                Throwable t = ite.getTargetException();
                if (t instanceof ClassFormatError) {
                    throw (ClassFormatError)t;
                }
                else if (t instanceof NoClassDefFoundError) {
                    throw (NoClassDefFoundError)t;
                }
                else {
                    throw new IOException(t.toString());
                }
            }
            catch (Exception e) {
                throw new IOException(e.toString());
            }
        }
        else {
            return defineClass(classname, classData, 0, classData.length); 
        }
    }

    /**
     * Search for and load a class on the classpath of this class loader.
     *
     * @param name the classname to be loaded.
     * 
     * @return the required Class object
     *
     * @throws ClassNotFoundException if the requested class does not exist on
     * this loader's classpath.
     */
    public Class findClass(String name) throws ClassNotFoundException {
        log("Finding class " + name, Project.MSG_DEBUG);

        return findClassInComponents(name);
    }


    /**
     * Find a class on the given classpath.
     */
    private Class findClassInComponents(String name) throws ClassNotFoundException {
        InputStream stream = null;
        String classFilename = getClassFilename(name);
        try {
            for (Enumeration e = pathComponents.elements(); e.hasMoreElements(); ) {
                File pathComponent = (File)e.nextElement();
                try {
                    stream = getResourceStream(pathComponent, classFilename);
                    if (stream != null) {
                        return getClassFromStream(stream, name);
                    }
                }
                catch (IOException ioe) {
                    log("Exception reading component " + pathComponent , Project.MSG_VERBOSE);
                }
            }
            
            throw new ClassNotFoundException(name);
        }
        finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            }
            catch (IOException e) {}
        }
    }

    /**
     * Find a system class (which should be loaded from the same classloader as the Ant core).
     */
    private Class findBaseClass(String name) throws ClassNotFoundException {
        if (parent == null) {
            return findSystemClass(name);
        }
        else {
            return parent.loadClass(name);
        }
    }

    public void cleanup() {
        pathComponents = null;
        project = null;
        for (Enumeration e = zipFiles.elements(); e.hasMoreElements(); ) {
            ZipFile zipFile = (ZipFile)e.nextElement();
            try {
                zipFile.close();
            }
            catch (IOException ioe) {
            }
        }
        zipFiles = new Hashtable();
    }
    
    public void buildStarted(BuildEvent event) {
    }

    public void buildFinished(BuildEvent event) {
        cleanup();
    }

    public void targetStarted(BuildEvent event) {
    }

    public void targetFinished(BuildEvent event) {
    }

    public void taskStarted(BuildEvent event) {
    }

    public void taskFinished(BuildEvent event) {
    }

    public void messageLogged(BuildEvent event) {
    }
}
