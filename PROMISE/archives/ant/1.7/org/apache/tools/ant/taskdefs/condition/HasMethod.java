package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

/**
 * test for a method
 */
public class HasMethod extends ProjectComponent implements Condition {
    private String classname;
    private String method;
    private String field;
    private Path classpath;
    private AntClassLoader loader;
    private boolean ignoreSystemClasses = false;


    /**
     * Set the classpath to be used when searching for classes and resources.
     *
     * @param classpath an Ant Path object containing the search path.
     */
    public void setClasspath(Path classpath) {
        createClasspath().append(classpath);
    }

    /**
     * Classpath to be used when searching for classes and resources.
     *
     * @return an empty Path instance to be configured by Ant.
     */
    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(getProject());
        }
        return this.classpath.createPath();
    }

    /**
     * Set the classpath by reference.
     *
     * @param r a Reference to a Path instance to be used as the classpath
     *          value.
     */
    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    /**
     * Set the classname attribute.
     * @param classname the name of the class to check.
     */
    public void setClassname(String classname) {
        this.classname = classname;
    }

    /**
     * Set the name of the method.
     * @param method the name of the method to check.
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Set the name of the field.
     * @param field the name of the field to check.
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * Set whether to ignore system classes when looking for the class.
     * @param ignoreSystemClasses a <code>boolean</code> value.
     */
    public void setIgnoreSystemClasses(boolean ignoreSystemClasses) {
        this.ignoreSystemClasses = ignoreSystemClasses;
    }

    /**
     * Check if a given class can be loaded.
     */
    private Class loadClass(String classname) {
        try {
            if (ignoreSystemClasses) {
                loader = getProject().createClassLoader(classpath);
                loader.setParentFirst(false);
                loader.addJavaLibraries();
                if (loader != null) {
                    try {
                        return loader.findClass(classname);
                    } catch (SecurityException se) {
                        return null;
                    }
                } else {
                    return null;
                }
            } else if (loader != null) {
                return loader.loadClass(classname);
            } else {
                ClassLoader l = this.getClass().getClassLoader();
                if (l != null) {
                    return Class.forName(classname, true, l);
                } else {
                    return Class.forName(classname);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new BuildException("class \"" + classname + "\" was not found");
        } catch (NoClassDefFoundError e) {
            throw new BuildException("Could not load dependent class \"" + e.getMessage()
                    + "\" for class \"" + classname + "\"");
        }
    }


    /** {@inheritDoc}. */
    public boolean eval() throws BuildException {
        if (classname == null) {
            throw new BuildException("No classname defined");
        }
        Class clazz = loadClass(classname);
        if (method != null) {
            return isMethodFound(clazz);
        }
        if (field != null) {
            return isFieldFound(clazz);
        }
        throw new BuildException("Neither method nor field defined");
    }

    private boolean isFieldFound(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field fieldEntry = fields[i];
            if (fieldEntry.getName().equals(field)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMethodFound(Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method methodEntry = methods[i];
            if (methodEntry.getName().equals(method)) {
                return true;
            }
        }
        return false;
    }

}
