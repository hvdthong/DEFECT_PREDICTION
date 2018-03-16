package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

/**
 * Define a new task - name and class
 *
 * @author costin@dnt.ro
 */
public class Taskdef extends Task {
    private String name;
    private String value;
    private Path classpath;

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
        if (name==null || value==null ) {
            String msg = "name or classname attributes of taskdef element "
                + "are undefined";
            throw new BuildException(msg);
        }
        try {
            ClassLoader loader = null;
            if (classpath != null) {
                AntClassLoader al = new AntClassLoader(project, classpath,
                                                       false);
                al.addSystemPackageRoot("org.apache.tools.ant");
                loader = al;
            } else {
                loader = this.getClass().getClassLoader();
            }

            Class taskClass = null;
            if (loader != null) {
                taskClass = loader.loadClass(value);
            } else {
                taskClass = Class.forName(value);
            }
            project.addTaskDefinition(name, taskClass);
        } catch (ClassNotFoundException cnfe) {
            String msg = "taskdef class " + value +
                " cannot be found";
            throw new BuildException(msg, cnfe, location);
        } catch (NoClassDefFoundError ncdfe) {
            String msg = "taskdef class " + value +
                " cannot be found";
            throw new BuildException(msg, ncdfe, location);
        }
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
}
