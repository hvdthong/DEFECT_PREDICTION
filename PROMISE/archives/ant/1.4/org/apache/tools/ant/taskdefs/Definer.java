package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

import java.util.*;
import java.io.*;

/**
 * Base class for Taskdef and Typedef - does all the classpath
 * handling and and class loading.
 *
 * @author Costin Manolache
 * @author <a href="stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public abstract class Definer extends Task {
    private String name;
    private String value;
    private Path classpath;
    private File file;
    private String resource;
    private boolean reverseLoader = false;

    public void setReverseLoader(boolean reverseLoader) {
        this.reverseLoader = reverseLoader;
        log("The reverseloader attribute is DEPRECATED. It will be removed", Project.MSG_WARN);
    }
    
    public void setClasspath(Path classpath) {
        if (this.classpath == null) {
            this.classpath = classpath;
        } else {
            this.classpath.append(classpath);
        }
    }

    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(project);
        }
        return this.classpath.createPath();
    }

    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    public void execute() throws BuildException {
        AntClassLoader al=createLoader();

        if (file==null && resource==null ) {

            if ( name==null || value==null  ) {
                String msg = "name or classname attributes of "
                    + getTaskName() + " element "
                    + "are undefined";
                throw new BuildException(msg);
            }
            addDefinition( al, name, value );

        } else {

            try {
                if (name != null || value != null) {
                    String msg = "You must not specify name or value "
                        + "together with file or resource.";
                    throw new BuildException(msg, location);
                }
            
                if (file != null && resource != null) {
                    String msg = "You must not specify both, file and resource.";
                    throw new BuildException(msg, location);
                }
            

                Properties props=new Properties();
                InputStream is=null;
                if( file != null ) {
                    log("Loading definitions from file " + file, 
                        Project.MSG_VERBOSE);
                    is=new FileInputStream( file );
                    if (is == null) {
                        log("Could not load definitions from file " + file
                            + ". It doesn\'t exist.", Project.MSG_WARN);
                    }
                }    
                if( resource!=null ) {
                    log("Loading definitions from resource " + resource, 
                        Project.MSG_VERBOSE);
                    is=al.getResourceAsStream( resource );
                    if (is == null) {
                        log("Could not load definitions from resource " 
                            + resource + ". It could not be found.", 
                            Project.MSG_WARN);
                    }
                }

                if( is!=null ) {
                    props.load( is );
                    Enumeration keys=props.keys();
                    while( keys.hasMoreElements() ) {
                        String n=(String)keys.nextElement();
                        String v=props.getProperty( n );
                        addDefinition( al, n, v );
                    }
                }
            } catch( IOException ex ) {
                throw new BuildException(ex, location);
            }
        }
    }
    
    private void addDefinition( ClassLoader al, String name, String value ) {
        try {
            Class c = al.loadClass(value);
            AntClassLoader.initializeClass(c);
            addDefinition(name, c);
        } catch (ClassNotFoundException cnfe) {
            String msg = getTaskName()+" class " + value +
                " cannot be found";
            throw new BuildException(msg, cnfe, location);
        } catch (NoClassDefFoundError ncdfe) {
            String msg = getTaskName()+" class " + value +
                " cannot be found";
            throw new BuildException(msg, ncdfe, location);
        }
    }


    private AntClassLoader createLoader() {
        AntClassLoader al = null;
        if (classpath != null) {
            al = new AntClassLoader(project, classpath, !reverseLoader);
        } else {
            al = new AntClassLoader(project, Path.systemClasspath, !reverseLoader);
        }
        al.addSystemPackageRoot("org.apache.tools.ant");
        return al;
    }

    public void setFile( File file ) {
        this.file=file;
    }

    public void setResource( String res ) {
        this.resource=res;
    }

    public void setName( String name) {
        this.name = name;
    }

    public String getClassname() {
        return value;
    }

    public void setClassname(String v) {
        value = v;
    }

    protected abstract void addDefinition(String name, Class c);
}
