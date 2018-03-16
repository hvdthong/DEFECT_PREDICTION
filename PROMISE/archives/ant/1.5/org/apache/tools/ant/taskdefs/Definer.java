package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

import java.util.Properties;
import java.util.Enumeration;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Base class for Taskdef and Typedef - does all the classpath
 * handling and and class loading.
 *
 * @author Costin Manolache
 * @author <a href="stefan.bodewig@epost.de">Stefan Bodewig</a>
 *
 * @since Ant 1.4
 */
public abstract class Definer extends Task {
    private String name;
    private String value;
    private Path classpath;
    private File file;
    private String resource;
    private boolean reverseLoader = false;
    private String loaderId = null;
    private String classpathId = null;
    
    private static final String REUSE_LOADER_REF = "ant.reuse.loader";
    
    /**
     * @deprecated stop using this attribute
     * @ant.attribute ignore="true"
     */
    public void setReverseLoader(boolean reverseLoader) {
        this.reverseLoader = reverseLoader;
        log("The reverseloader attribute is DEPRECATED. It will be removed", 
            Project.MSG_WARN);
    }
    
    /**
     * Set the classpath to be used when searching for component being defined
     * 
     * @param classpath an Ant Path object containing the classpath.
     */
    public void setClasspath(Path classpath) {
        if (this.classpath == null) {
            this.classpath = classpath;
        } else {
            this.classpath.append(classpath);
        }
    }

    /**
     * Create the classpath to be used when searching for component being defined
     */ 
    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(project);
        }
        return this.classpath.createPath();
    }

    /**
     * reference to a classpath to use when loading the files.
     * To actually share the same loader, set loaderref as well
     */
    public void setClasspathRef(Reference r) {
        classpathId=r.getRefId();
        createClasspath().setRefid(r);
    }

    /**
     * Use the reference to locate the loader. If the loader is not
     * found, taskdef will use the specified classpath and register it
     * with the specified name.
     *     
     * This allow multiple taskdef/typedef to use the same class loader,
     * so they can be used together. It eliminate the need to
     * put them in the CLASSPATH.
     *
     * @since Ant 1.5
     */
    public void setLoaderRef(Reference r) {
        loaderId = r.getRefId();
    }

    
    public void execute() throws BuildException {
        AntClassLoader al = createLoader();

        if (file == null && resource == null) {

            if (name == null || value == null) {
                String msg = "name or classname attributes of "
                    + getTaskName() + " element "
                    + "are undefined";
                throw new BuildException(msg);
            }
            addDefinition(al, name, value);

        } else {

            InputStream is = null;
            try {
                if (name != null || value != null) {
                    String msg = "You must not specify name or value "
                        + "together with file or resource.";
                    throw new BuildException(msg, location);
                }
            
                if (file != null && resource != null) {
                    String msg = "You must not specify both, file and "
                        + "resource.";
                    throw new BuildException(msg, location);
                }
            

                Properties props = new Properties();
                if (file != null) {
                    log("Loading definitions from file " + file, 
                        Project.MSG_VERBOSE);
                    is = new FileInputStream(file);
                    if (is == null) {
                        log("Could not load definitions from file " + file
                            + ". It doesn\'t exist.", Project.MSG_WARN);
                    }
                }    
                if (resource != null) {
                    log("Loading definitions from resource " + resource, 
                        Project.MSG_VERBOSE);
                    is = al.getResourceAsStream(resource);
                    if (is == null) {
                        log("Could not load definitions from resource " 
                            + resource + ". It could not be found.", 
                            Project.MSG_WARN);
                    }
                }

                if (is != null) {
                    props.load(is);
                    Enumeration keys = props.keys();
                    while (keys.hasMoreElements()) {
                        String n = (String) keys.nextElement();
                        String v = props.getProperty(n);
                        addDefinition(al, n, v);
                    }
                }
            } catch (IOException ex) {
                throw new BuildException(ex, location);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {}
                }
            }
        }
    }
    
    /**
     * create the classloader then hand the definition off to the subclass;
     * @throws BuildException when the class wont load for any reason
     */
    private void addDefinition(ClassLoader al, String name, String value)
        throws BuildException {
        try {
            Class c = al.loadClass(value);
            AntClassLoader.initializeClass(c);
            addDefinition(name, c);
        } catch (ClassNotFoundException cnfe) {
            String msg = getTaskName() + " class " + value 
                + " cannot be found";
            throw new BuildException(msg, cnfe, location);
        } catch (NoClassDefFoundError ncdfe) {
            String msg = getTaskName() + " class " + value 
                + " cannot be found";
            throw new BuildException(msg, ncdfe, location);
        }
    }

    /**
     * create a classloader for this definition
     */
    private AntClassLoader createLoader() {
        if (project.getProperty(REUSE_LOADER_REF) != null) {
            if (loaderId == null && classpathId != null) {
                loaderId = "ant.loader." + classpathId;
            }
        }
        
        if (loaderId != null) {
            Object reusedLoader = project.getReference(loaderId);
            if (reusedLoader != null) {
                if (reusedLoader instanceof AntClassLoader) {
                    return (AntClassLoader)reusedLoader;
                }
            }
        }
       
        AntClassLoader al = null;
        if (classpath != null) {
            al = new AntClassLoader(project, classpath, !reverseLoader);
        } else {
            al = new AntClassLoader(project, Path.systemClasspath, 
                                    !reverseLoader);
        }
        al.addSystemPackageRoot("org.apache.tools.ant");


        if (loaderId != null) {
            if (project.getReference(loaderId) == null) {
                project.addReference(loaderId, al);
            }
        }

        return al;
    }

    /**
     * Name of the property file  to load
     * ant name/classname pairs from.
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Name of the property resource to load
     * ant name/classname pairs from.
     */
    public void setResource(String res) {
        this.resource = res;
    }

    /**
     * Name of the property resource to load
     * ant name/classname pairs from.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * what is the classname we are definining? Can be null
     */
    public String getClassname() {
        return value;
    }

    /**
     * the full class name of the object being defined.
     * Required, unless file or resource have
     * been specified.
     */
    public void setClassname(String v) {
        value = v;
    }

    /**
     * this must be implemented by subclasses; it is the callback
     * they will get to add a new definition of their type
     */
    protected abstract void addDefinition(String name, Class c);
}
