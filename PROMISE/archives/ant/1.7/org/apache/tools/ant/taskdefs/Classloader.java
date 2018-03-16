package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.MagicNames;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Path;

import java.io.File;

/**
 * EXPERIMENTAL
 * Create or modifies ClassLoader. The required pathRef parameter
 * will be used to add classpath elements.
 *
 * The classpath is a regular path. Currently only file components are
 * supported (future extensions may allow URLs).
 *
 * You can modify the core loader by not specifying any name or using
 * "ant.coreLoader". (the core loader is used to load system ant
 * tasks and for taskdefs that don't specify an explicit path).
 *
 * Taskdef and typedef can use the loader you create if the name follows
 * the "ant.loader.NAME" pattern. NAME will be used as a pathref when
 * calling taskdef.
 *
 * This tasks will not modify the core loader if "build.sysclasspath=only"
 *
 * The typical use is:
 * <pre>
 *  &lt;path id="ant.deps" &gt;
 *     &lt;fileset dir="myDir" &gt;
 *        &lt;include name="junit.jar, bsf.jar, js.jar, etc"/&gt;
 *     &lt;/fileset&gt;
 *  &lt;/path&gt;
 *
 *  &lt;classloader pathRef="ant.deps" /&gt;
 *
 * </pre>
 *
 */
public class Classloader extends Task {
    /** @see MagicNames#SYSTEM_LOADER_REF */
    public static final String SYSTEM_LOADER_REF = MagicNames.SYSTEM_LOADER_REF;

    private String name = null;
    private Path classpath;
    private boolean reset = false;
    private boolean parentFirst = true;
    private String parentName = null;

    /**
     * Default constructor
     */
    public Classloader() {
    }

    /** Name of the loader. If none, the default loader will be modified
     *
     * @param name the name of this loader
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Reset the classloader, if it already exists. A new loader will
     * be created and all the references to the old one will be removed.
     * (it is not possible to remove paths from a loader). The new
     * path will be used.
     *
     * @param b true if the loader is to be reset.
     */
    public void setReset(boolean b) {
        this.reset = b;
    }

    /**
     * Set reverse attribute.
     * @param b if true reverse the normal classloader lookup.
     */
    public void setReverse(boolean b) {
        this.parentFirst = !b;
    }

    /**
     * Set reverse attribute.
     * @param b if true reverse the normal classloader lookup.
     */
    public void setParentFirst(boolean b) {
        this.parentFirst = b;
    }

    /**
     * Set the name of the parent.
     * @param name the parent name.
     */
    public void setParentName(String name) {
        this.parentName = name;
    }


    /** Specify which path will be used. If the loader already exists
     *  and is an AntClassLoader (or any other loader we can extend),
     *  the path will be added to the loader.
     * @param pathRef a reference to a path.
     * @throws BuildException if there is a problem.
     */
    public void setClasspathRef(Reference pathRef) throws BuildException {
        classpath = (Path) pathRef.getReferencedObject(getProject());
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
     * Create a classpath.
     * @return a path for configuration.
     */
    public Path createClasspath() {
        if (this.classpath == null) {
            this.classpath = new Path(null);
        }
        return this.classpath.createPath();
    }


    /**
     * do the classloader manipulation.
     */
    public void execute() {
        try {
            if ("only".equals(getProject().getProperty("build.sysclasspath"))
                && (name == null || SYSTEM_LOADER_REF.equals(name))) {
                log("Changing the system loader is disabled "
                    + "by build.sysclasspath=only", Project.MSG_WARN);
                return;
            }

            String loaderName = (name == null) ? SYSTEM_LOADER_REF : name;

            Object obj = getProject().getReference(loaderName);
            if (reset) {
            }

            if (obj != null && !(obj instanceof AntClassLoader)) {
                log("Referenced object is not an AntClassLoader",
                        Project.MSG_ERR);
                return;
            }

            AntClassLoader acl = (AntClassLoader) obj;

            if (acl == null) {
                Object parent = null;
                if (parentName != null) {
                    parent = getProject().getReference(parentName);
                    if (!(parent instanceof ClassLoader)) {
                        parent = null;
                    }
                }
                if (parent == null) {
                    parent = this.getClass().getClassLoader();
                }

                if (name == null) {
                }
                getProject().log("Setting parent loader " + name + " "
                    + parent + " " + parentFirst, Project.MSG_DEBUG);

                acl = new AntClassLoader((ClassLoader) parent,
                         getProject(), classpath, parentFirst);

                getProject().addReference(loaderName, acl);

                if (name == null) {
                    acl.addLoaderPackageRoot("org.apache.tools.ant.taskdefs.optional");
                    getProject().setCoreLoader(acl);
                }
            }
            if (classpath != null) {
                String[] list = classpath.list();
                for (int i = 0; i < list.length; i++) {
                    File f = new File(list[i]);
                    if (f.exists()) {
                        acl.addPathElement(f.getAbsolutePath());
                        log("Adding to class loader " +  acl + " " + f.getAbsolutePath(),
                                Project.MSG_DEBUG);
                    }
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
