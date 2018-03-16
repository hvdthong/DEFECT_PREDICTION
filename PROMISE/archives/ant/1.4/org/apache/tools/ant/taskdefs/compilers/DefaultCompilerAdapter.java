package org.apache.tools.ant.taskdefs.compilers;

import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.*;

import java.io.*;
import java.util.Random;

/**
 * This is the default implementation for the CompilerAdapter interface.
 * Currently, this is a cut-and-paste of the original javac task.
 *
 * @author James Davidson <a href="mailto:duncan@x180.com">duncan@x180.com</a>
 * @author Robin Green <a href="mailto:greenrd@hotmail.com">greenrd@hotmail.com</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author <a href="mailto:jayglanville@home.com">J D Glanville</a>
 */
public abstract class DefaultCompilerAdapter implements CompilerAdapter {

    /* jdg - TODO - all these attributes are currently protected, but they
     * should probably be private in the near future.
     */

    protected Path src;
    protected File destDir;
    protected String encoding;
    protected boolean debug = false;
    protected boolean optimize = false;
    protected boolean deprecation = false;
    protected boolean depend = false;
    protected boolean verbose = false;
    protected String target;
    protected Path bootclasspath;
    protected Path extdirs;
    protected Path compileClasspath;
    protected Project project;
    protected Location location;
    protected boolean includeAntRuntime;
    protected boolean includeJavaRuntime;
    protected String memoryInitialSize;
    protected String memoryMaximumSize;

    protected File[] compileList;
    protected static String lSep = System.getProperty("line.separator");
    protected Javac attributes;

    public void setJavac( Javac attributes ) {
        this.attributes = attributes;
        src = attributes.getSrcdir();
        destDir = attributes.getDestdir();
        encoding = attributes.getEncoding();
        debug = attributes.getDebug();
        optimize = attributes.getOptimize();
        deprecation = attributes.getDeprecation();
        depend = attributes.getDepend();
        verbose = attributes.getVerbose();
        target = attributes.getTarget();
        bootclasspath = attributes.getBootclasspath();
        extdirs = attributes.getExtdirs();
        compileList = attributes.getFileList();
        compileClasspath = attributes.getClasspath();
        project = attributes.getProject();
        location = attributes.getLocation();
        includeAntRuntime = attributes.getIncludeantruntime();
        includeJavaRuntime = attributes.getIncludejavaruntime();
        memoryInitialSize = attributes.getMemoryInitialSize();
        memoryMaximumSize = attributes.getMemoryMaximumSize();
    }

    public Javac getJavac() {
        return attributes;
    }

    /**
     * Builds the compilation classpath.
     *
     */
    protected Path getCompileClasspath() {
        Path classpath = new Path(project);


        if (destDir != null) {
            classpath.setLocation(destDir);
        }


        if (compileClasspath == null) {
            if ( includeAntRuntime ) {
                classpath.addExisting(Path.systemClasspath);
            }
        } else {
            if ( includeAntRuntime ) {
                classpath.addExisting(compileClasspath.concatSystemClasspath("last"));
            } else {
                classpath.addExisting(compileClasspath.concatSystemClasspath("ignore"));
            }
        }

        if (includeJavaRuntime) {

            if (System.getProperty("java.vendor").toLowerCase().indexOf("microsoft") >= 0) {
                FileSet msZipFiles = new FileSet();
                msZipFiles.setDir(new File(System.getProperty("java.home") + File.separator + "Packages"));
                msZipFiles.setIncludes("*.ZIP");
                classpath.addFileset(msZipFiles);
            }
            else if (Project.getJavaVersion() == Project.JAVA_1_1) {
                classpath.addExisting(new Path(null,
                                                System.getProperty("java.home")
                                                + File.separator + "lib"
                                                + File.separator
                                                + "classes.zip"));
            } else {
                classpath.addExisting(new Path(null,
                                                System.getProperty("java.home")
                                                + File.separator + "lib"
                                                + File.separator + "rt.jar"));
                classpath.addExisting(new Path(null,
                                                System.getProperty("java.home")
                                                + File.separator +"jre"
                                                + File.separator + "lib"
                                                + File.separator + "rt.jar"));

                classpath.addExisting(new Path(null,
                                               System.getProperty("java.home")
                                               + File.separator + ".."
                                               + File.separator + "Classes"
                                               + File.separator + "classes.jar"));
                classpath.addExisting(new Path(null,
                                               System.getProperty("java.home")
                                               + File.separator + ".."
                                               + File.separator + "Classes"
                                               + File.separator + "ui.jar"));
            }
        }

        return classpath;
    }

    /**
     * Does the command line argument processing common to classic and
     * modern.  Doesn't add the files to compile.
     */
    protected Commandline setupJavacCommandlineSwitches(Commandline cmd) {
        Path classpath = getCompileClasspath();

        boolean usingJava1_1 = Project.getJavaVersion().equals(Project.JAVA_1_1);
        String memoryParameterPrefix = usingJava1_1 ? "-J-" : "-J-X";
        if (memoryInitialSize != null) {
            if (!attributes.isForkedJavac()) {
                attributes.log("Since fork is false, ignoring memoryInitialSize setting.",
                               Project.MSG_WARN);
            } else {
                cmd.createArgument().setValue(memoryParameterPrefix+"ms"+memoryInitialSize);
            }
        }

        if (memoryMaximumSize != null) {
            if (!attributes.isForkedJavac()) {
                attributes.log("Since fork is false, ignoring memoryMaximumSize setting.",
                               Project.MSG_WARN);
            } else {
                cmd.createArgument().setValue(memoryParameterPrefix+"mx"+memoryMaximumSize);
            }
        }

        if (attributes.getNowarn()) {
            cmd.createArgument().setValue("-nowarn");
        }

        if (deprecation == true) {
            cmd.createArgument().setValue("-deprecation");
        }

        if (destDir != null) {
            cmd.createArgument().setValue("-d");
            cmd.createArgument().setFile(destDir);
        }

        cmd.createArgument().setValue("-classpath");

        if (Project.getJavaVersion().startsWith("1.1")) {
            Path cp = new Path(project);
            /*
             * XXX - This doesn't mix very well with build.systemclasspath,
             */
            if (bootclasspath != null) {
                cp.append(bootclasspath);
            }
            if (extdirs != null) {
                addExtdirsToClasspath(cp);
            }
            cp.append(classpath);
            cp.append(src);
            cmd.createArgument().setPath(cp);
        } else {
            cmd.createArgument().setPath(classpath);
            cmd.createArgument().setValue("-sourcepath");
            cmd.createArgument().setPath(src);
            if (target != null) {
                cmd.createArgument().setValue("-target");
                cmd.createArgument().setValue(target);
            }
            if (bootclasspath != null) {
                cmd.createArgument().setValue("-bootclasspath");
                cmd.createArgument().setPath(bootclasspath);
            }
            if (extdirs != null) {
                cmd.createArgument().setValue("-extdirs");
                cmd.createArgument().setPath(extdirs);
            }
        }

        if (encoding != null) {
            cmd.createArgument().setValue("-encoding");
            cmd.createArgument().setValue(encoding);
        }
        if (debug) {
            cmd.createArgument().setValue("-g");
        } else if (Project.getJavaVersion() != Project.JAVA_1_0 &&
                   Project.getJavaVersion() != Project.JAVA_1_1) {
            cmd.createArgument().setValue("-g:none");
        }
        if (optimize) {
            cmd.createArgument().setValue("-O");
        }

        if (depend) {
            if (Project.getJavaVersion().startsWith("1.1")) {
                cmd.createArgument().setValue("-depend");
            } else if (Project.getJavaVersion().startsWith("1.2")) {
                cmd.createArgument().setValue("-Xdepend");
            } else {
                attributes.log("depend attribute is not supported by the modern compiler",
                    Project.MSG_WARN);
            }
        }

        if (verbose) {
            cmd.createArgument().setValue("-verbose");
        }
        return cmd;
    }

    /**
     * Does the command line argument processing common to classic and
     * modern and adds the files to compile as well.
     */
    protected Commandline setupModernJavacCommand() {
        Commandline cmd = new Commandline();
        setupJavacCommandlineSwitches(cmd);

        if (attributes.getSource() != null) {
            cmd.createArgument().setValue("-source");
            cmd.createArgument().setValue(attributes.getSource());
        }
        
        logAndAddFilesToCompile(cmd);
        return cmd;
    }

    /**
     * Does the command line argument processing common to classic and
     * modern and adds the files to compile as well.
     */
    protected Commandline setupJavacCommand() {
        Commandline cmd = new Commandline();
        setupJavacCommandlineSwitches(cmd);
        logAndAddFilesToCompile(cmd);
        return cmd;
    }

    /**
     * Logs the compilation parameters, adds the files to compile and logs the
     * &qout;niceSourceList&quot;
     */
    protected void logAndAddFilesToCompile(Commandline cmd) {
        attributes.log("Compilation args: " + cmd.toString(),
            Project.MSG_VERBOSE);

        StringBuffer niceSourceList = new StringBuffer("File");
        if (compileList.length != 1) {
            niceSourceList.append("s");
        }
        niceSourceList.append(" to be compiled:");

        niceSourceList.append(lSep);

        for (int i=0; i < compileList.length; i++) {
            String arg = compileList[i].getAbsolutePath();
            cmd.createArgument().setValue(arg);
            niceSourceList.append("    " + arg + lSep);
        }

        attributes.log(niceSourceList.toString(), Project.MSG_VERBOSE);
    }

    /**
     * Do the compile with the specified arguments.
     * @param args - arguments to pass to process on command line
     * @param firstFileName - index of the first source file in args
     */
    protected int executeExternalCompile(String[] args, int firstFileName) {
        String[] commandArray = null;
        File tmpFile = null;

        try {
            /*
             * Many system have been reported to get into trouble with
             * long command lines - no, not only Windows ;-).
             *
             * POSIX seems to define a lower limit of 4k, so use a temporary
             * file if the total length of the command line exceeds this limit.
             */
            if (Commandline.toString(args).length() > 4096) {
                PrintWriter out = null;
                try {
                    tmpFile = new File("jikes"+(new Random(System.currentTimeMillis())).nextLong());
                    out = new PrintWriter(new FileWriter(tmpFile));
                    for (int i = firstFileName; i < args.length; i++) {
                        out.println(args[i]);
                    }
                    out.flush();
                    commandArray = new String[firstFileName+1];
                    System.arraycopy(args, 0, commandArray, 0, firstFileName);
                    commandArray[firstFileName] = "@" + tmpFile.getAbsolutePath();
                } catch (IOException e) {
                    throw new BuildException("Error creating temporary file", e, location);
                } finally {
                    if (out != null) {
                        try {out.close();} catch (Throwable t) {}
                    }
                }
            } else {
                commandArray = args;
            }

            try {
                Execute exe = new Execute(new LogStreamHandler(attributes,
                                                               Project.MSG_INFO,
                                                               Project.MSG_WARN));
                exe.setAntRun(project);
                exe.setWorkingDirectory(project.getBaseDir());
                exe.setCommandline(commandArray);
                exe.execute();
                return exe.getExitValue();
            } catch (IOException e) {
                throw new BuildException("Error running " + args[0]
                        + " compiler", e, location);
            }
        } finally {
            if (tmpFile != null) {
                tmpFile.delete();
            }
        }
    }

    /**
     * Emulation of extdirs feature in java >= 1.2.
     * This method adds all files in the given
     * directories (but not in sub-directories!) to the classpath,
     * so that you don't have to specify them all one by one.
     * @param classpath - Path to append files to
     */
    protected void addExtdirsToClasspath(Path classpath) {
        if (extdirs == null) {
            String extProp = System.getProperty("java.ext.dirs");
            if (extProp != null) {
                extdirs = new Path(project, extProp);
            } else {
                return;
            }
        }

        String[] dirs = extdirs.list();
        for (int i=0; i<dirs.length; i++) {
            if (!dirs[i].endsWith(File.separator)) {
                dirs[i] += File.separator;
            }
            File dir = project.resolveFile(dirs[i]);
            FileSet fs = new FileSet();
            fs.setDir(dir);
            fs.setIncludes("*");
            classpath.addFileset(fs);
        }
    }

}

