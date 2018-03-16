package org.apache.tools.ant.taskdefs;

import java.io.*;
import java.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.types.*;

/**
 * Will set the given property if the requested resource is available at runtime.
 *
 * @author Stefano Mazzocchi <a href="mailto:stefano@apache.org">stefano@apache.org</a>
 */

public class Available extends Task implements Condition {

    private String property;
    private String classname;
    private File file;
    private Path filepath;
    private String resource;
    private String type;
    private Path classpath;
    private AntClassLoader loader;
    private String value = "true";

    public void setClasspath(Path classpath) {
        createClasspath().append(classpath);
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

    public void setFilepath(Path filepath) {
        createFilepath().append(filepath);
    }
    
    public Path createFilepath() {
        if (this.filepath == null) {
            this.filepath = new Path(project);
        }
        return this.filepath.createPath();
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

    public void setType(String type) {
        this.type = type;
    }

    public void execute() throws BuildException {
        if (property == null) {
            throw new BuildException("property attribute is required", location);
        }

        if (eval()) {
            this.project.setProperty(property, value);
        }
    }
        
    public boolean  eval() throws BuildException {
        if (classname == null && file == null && resource == null) {
            throw new BuildException("At least one of (classname|file|resource) is required", location);
        }

        if (type != null){
            if (!type.equalsIgnoreCase("file") && !type.equalsIgnoreCase("dir")){
                throw new BuildException("Type must be one of either dir or file");
            }
        }

        if (classpath != null) {
            classpath.setProject(project);
            this.loader = new AntClassLoader(project, classpath);
        }

        if ((classname != null) && !checkClass(classname)) {
            log("Unable to load class " + classname + " to set property " + property, Project.MSG_VERBOSE);
            return false;
        }
        
        if ((file != null) && !checkFile()) {
            log("Unable to find " + file + " to set property " + property, Project.MSG_VERBOSE);
            return false;
        }
        
        if ((resource != null) && !checkResource(resource)) {
            log("Unable to load resource " + resource + " to set property " + property, Project.MSG_VERBOSE);
            return false;
        }

        if (loader != null) {
            loader.cleanup();
        }

        return true;
    }

    private boolean checkFile() {
        if (filepath == null) {
            return checkFile(file);
        } else {
            String[] paths = filepath.list();
            for(int i = 0; i < paths.length; ++i) {
                log("Searching " + paths[i], Project.MSG_VERBOSE);
                if(new File(paths[i], file.getName()).isFile()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkFile(File file) {
        if (type != null) {
            if (type.equalsIgnoreCase("dir")) {
                return file.isDirectory();
            } else if (type.equalsIgnoreCase("file")) {
                return file.isFile();
            }
        }
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
