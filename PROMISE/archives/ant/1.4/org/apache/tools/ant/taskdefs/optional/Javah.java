package org.apache.tools.ant.taskdefs.optional;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.types.*;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.io.*;
import java.util.*;

/**
 * Task to generate JNI header files using javah. This task can take the following
 * arguments:
 * <ul>
 * <li>classname - the fully-qualified name of a class</li>
 * <li>outputFile - Concatenates the resulting header or source files for all
 *     the classes listed into this file</li>
 * <li>destdir - Sets the directory where javah saves the header files or the
 *     stub files</li>
 * <li>classpath</li>
 * <li>bootclasspath</li>
 * <li>force - Specifies that output files should always be written
       (JDK1.2 only)</li>
 * <li>old - Specifies that old JDK1.0-style header files should be generated
 *     (otherwise output file contain JNI-style native method
 *      function prototypes) (JDK1.2 only)</li>
 * <li>stubs - generate C declarations from the Java object file (used with old)</li>
 * <li>verbose - causes javah to print a message to stdout concerning the status
 *     of the generated files</li>
 * <li>extdirs - Override location of installed extensions</li>
 * </ul>
 * Of these arguments, either <b>outputFile</b> or <b>destdir</b> is required,
 * but not both. More than one classname may be specified, using a comma-separated
 * list or by using <code>&lt;class name="xxx"&gt;</code> elements within the task.
 * <p>
 * When this task executes, it will generate C header and source files that
 * are needed to implement native methods.
 *
 * @author Rick Beton <a href="mailto:richard.beton@physics.org">richard.beton@physics.org</a>
 */

public class Javah extends Task {

    private static final String FAIL_MSG = "Compile failed, messages should have been provided.";

    private Vector classes = new Vector(2);
    private String cls;
    private File destDir;
    private Path classpath = null;
    private File outputFile = null;
    private boolean verbose = false;
    private boolean force   = false;
    private boolean old     = false;
    private boolean stubs   = false;
    private Path bootclasspath;
    private static String lSep = System.getProperty("line.separator");

    public void setClass(String cls) {
        this.cls = cls;
    }

    public ClassArgument createClass() {
        ClassArgument ga = new ClassArgument();
        classes.addElement(ga);
        return ga;
    }

    public class ClassArgument {
        private String name;

        public ClassArgument() {
        }

        public void setName(String name) {
            this.name = name;
            log("ClassArgument.name="+name);
        }

        public String getName() {
            return name;
        }
    }

    /**
     * Set the destination directory into which the Java source
     * files should be compiled.
     */
    public void setDestdir(File destDir) {
        this.destDir = destDir;
    }

    public void setClasspath(Path src) {
        if (classpath == null) {
            classpath = src;
        } else {
            classpath.append(src);
        }
    }
    public Path createClasspath() {
        if (classpath == null) {
            classpath = new Path(project);
        }
        return classpath.createPath();
    }

    /**
     * Adds a reference to a CLASSPATH defined elsewhere.
     */
    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    public void setBootclasspath(Path src) {
        if (bootclasspath == null) {
            bootclasspath = src;
        } else {
            bootclasspath.append(src);
        }
    }
    public Path createBootclasspath() {
        if (bootclasspath == null) {
            bootclasspath = new Path(project);
        }
        return bootclasspath.createPath();
    }

    /**
     * Adds a reference to a CLASSPATH defined elsewhere.
     */
    public void setBootClasspathRef(Reference r) {
        createBootclasspath().setRefid(r);
    }



    /**
     * Set the output file name.
     */
    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    /**
     * Set the force-write flag.
     */
    public void setForce(boolean force) {
        this.force = force;
    }

    /**
     * Set the old flag.
     */
    public void setOld(boolean old) {
        this.old = old;
    }

    /**
     * Set the stubs flag.
     */
    public void setStubs(boolean stubs) {
        this.stubs = stubs;
    }

    /**
     * Set the verbose flag.
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Executes the task.
     */
    public void execute() throws BuildException {

        if ((cls == null) && (classes.size() == 0)) {
            throw new BuildException("class attribute must be set!", location);
        }

        if ((cls != null) && (classes.size() > 0)) {
            throw new BuildException("set class attribute or class element, not both.", location);
        }

        if (destDir != null) {
            if (!destDir.isDirectory()) {
                throw new BuildException("destination directory \"" + destDir + "\" does not exist or is not a directory", location);
            }
            if (outputFile != null) {
                throw new BuildException("destdir and outputFile are mutually exclusive", location);
            }
        }

        if (classpath == null) {
            classpath = Path.systemClasspath;
        }

        String compiler = project.getProperty("build.compiler");
        if (compiler == null) {
            if (Project.getJavaVersion() != Project.JAVA_1_1 &&
                Project.getJavaVersion() != Project.JAVA_1_2) {
                compiler = "modern";
            } else {
                compiler = "classic";
            }
        }

        doClassicCompile();
    }


    /**
     * Peforms a compile using the classic compiler that shipped with
     * JDK 1.1 and 1.2.
     */

    private void doClassicCompile() throws BuildException {
        Commandline cmd = setupJavahCommand();

        /*
        sun.tools.javac.Main compiler =
                new sun.tools.javac.Main(new LogOutputStream(this, Project.MSG_WARN), "javac");

        if (!compiler.compile(cmd.getArguments())) {
            throw new BuildException("Compile failed");
        }
        */
        try {
            com.sun.tools.javah.Main main = new com.sun.tools.javah.Main( cmd.getArguments() );
            main.run();
        }
        catch (Exception ex) {
            if (ex instanceof BuildException) {
                throw (BuildException) ex;
            } else {
                throw new BuildException("Error starting javah: ", ex, location);
            }
        }
    }

    /**
     * Does the command line argument processing common to classic and
     * modern.
     */
    private Commandline setupJavahCommand() {
        Commandline cmd = new Commandline();

        if (destDir != null) {
            cmd.createArgument().setValue("-d");
            cmd.createArgument().setFile(destDir);
        }

        if (outputFile != null) {
            cmd.createArgument().setValue("-o");
            cmd.createArgument().setFile(outputFile);
        }

        if (classpath != null) {
            cmd.createArgument().setValue("-classpath");
            cmd.createArgument().setPath(classpath);
        }

        if (Project.getJavaVersion().startsWith("1.1")) {
            if (verbose) {
                cmd.createArgument().setValue("-v");
            }
        } else {
            if (verbose) {
                cmd.createArgument().setValue("-verbose");
            }
            if (old) {
                cmd.createArgument().setValue("-old");
            }
            if (force) {
                cmd.createArgument().setValue("-force");
            }
        }

        if (stubs) {
            if (!old) {
                throw new BuildException("stubs only available in old mode.", location);
            }
            cmd.createArgument().setValue("-stubs");
        }
        if (bootclasspath != null) {
            cmd.createArgument().setValue("-bootclasspath");
            cmd.createArgument().setPath(bootclasspath);
        }

        logAndAddFilesToCompile(cmd);
        return cmd;
    }

    /**
     * Logs the compilation parameters, adds the files to compile and logs the
     * &qout;niceSourceList&quot;
     */
    protected void logAndAddFilesToCompile(Commandline cmd) {
        int n = 0;
        log("Compilation args: " + cmd.toString(),
            Project.MSG_VERBOSE);

        StringBuffer niceClassList = new StringBuffer();
        if (cls != null) {
            StringTokenizer tok = new StringTokenizer(cls, ",", false);
            while (tok.hasMoreTokens()) {
                String aClass = tok.nextToken().trim();
                cmd.createArgument().setValue(aClass);
                niceClassList.append("    " + aClass + lSep);
                n++;
            }
        }

        Enumeration enum = classes.elements();
        while (enum.hasMoreElements()) {
            ClassArgument arg = (ClassArgument)enum.nextElement();
            String aClass = arg.getName();
            cmd.createArgument().setValue(aClass);
            niceClassList.append("    " + aClass + lSep);
            n++;
        }

        StringBuffer prefix = new StringBuffer("Class");
        if (n > 1) {
            prefix.append("es");
        }
        prefix.append(" to be compiled:");
        prefix.append(lSep);

        log(prefix.toString() + niceClassList.toString(), Project.MSG_VERBOSE);
    }
}

