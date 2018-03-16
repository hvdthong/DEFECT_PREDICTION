package org.apache.tools.ant.taskdefs;

import java.io.*;
import java.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

/**
 * Will set the given property if the requested resource is available at runtime.
 *
 * @author Stefano Mazzocchi <a href="mailto:stefano@apache.org">stefano@apache.org</a>
 */

public class Available extends Task {

    private String property;
    private String classname;
    private File file;
    private String resource;
    private Path classpath;
    private AntClassLoader loader;
    private String value = "true";

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

    public void setProperty(String property) {
        this.property = property;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setClassname(String classname) {
        if (!"".equals(classname)) {
            this.classname = classname;
        }
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void execute() throws BuildException {
        if (property == null) {
            throw new BuildException("property attribute is required", location);
        }
        
        if (classname == null && file == null && resource == null) {
            throw new BuildException("At least one of (classname|file|resource) is required", location);
        }

        if (classpath != null) {
            this.loader = new AntClassLoader(project, classpath, false);
        }

        if ((classname != null) && !checkClass(classname)) {
            log("Unable to load class " + classname + " to set property " + property, Project.MSG_VERBOSE);
            return;
        }
        
        if ((file != null) && !checkFile(file)) {
            log("Unable to find file " + file + " to set property " + property, Project.MSG_VERBOSE);
            return;
        }
        
        if ((resource != null) && !checkResource(resource)) {
            log("Unable to load resource " + resource + " to set property " + property, Project.MSG_VERBOSE);
            return;
        }

        this.project.setProperty(property, value);
    }

    private boolean checkFile(File file) {
        return file.exists();
    }

    private boolean checkResource(String resource) {
        if (loader != null) {
            return (loader.getResourceAsStream(resource) != null);
        } else {
            ClassLoader cL = this.getClass().getClassLoader();
            if (cL != null) {
                return (cL.getResourceAsStream(resource) != null);
            } else {
                return 
                    (ClassLoader.getSystemResourceAsStream(resource) != null);
            }
        }
    }

    private boolean checkClass(String classname) {
        try {
            if (loader != null) {
                loader.loadClass(classname);
            } else {
                ClassLoader l = this.getClass().getClassLoader();
                if (l != null) {
                    l.loadClass(classname);
                } else {
                    Class.forName(classname);
                }
            }
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (NoClassDefFoundError e) {
            return false;
        }
    }
}
