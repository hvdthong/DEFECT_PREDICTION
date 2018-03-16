package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.util.Vector;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.rmic.RmicAdapter;
import org.apache.tools.ant.taskdefs.rmic.RmicAdapterFactory;
import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.SourceFileScanner;
import org.apache.tools.ant.util.facade.FacadeTaskHelper;

/**
 * Runs the rmic compiler against classes.</p>
 * <p>Rmic can be run on a single class (as specified with the classname
 * attribute) or a number of classes at once (all classes below base that
 * are neither _Stub nor _Skel classes).  If you want to rmic a single
 * class and this class is a class nested into another class, you have to
 * specify the classname in the form <code>Outer$$Inner</code> instead of
 * <code>Outer.Inner</code>.</p>
 * <p>It is possible to refine the set of files that are being rmiced. This can be
 * done with the <i>includes</i>, <i>includesfile</i>, <i>excludes</i>, 
 * <i>excludesfile</i> and <i>defaultexcludes</i>
 * attributes. With the <i>includes</i> or <i>includesfile</i> attribute you specify the files you want to
 * have included by using patterns. The <i>exclude</i> or <i>excludesfile</i> attribute is used to specify
 * the files you want to have excluded. This is also done with patterns. And
 * finally with the <i>defaultexcludes</i> attribute, you can specify whether you
 * want to use default exclusions or not. See the section on 
 * directory based tasks</a>, on how the
 * inclusion/exclusion of files works, and how to write patterns.</p>
 * <p>This task forms an implicit FileSet and
 * supports all attributes of <code>&lt;fileset&gt;</code>
 * (<code>dir</code> becomes <code>base</code>) as well as the nested
 * <code>&lt;include&gt;</code>, <code>&lt;exclude&gt;</code> and
 * <code>&lt;patternset&gt;</code> elements.</p>
 * <p>It is possible to use different compilers. This can be selected
 * with the &quot;build.rmic&quot; property or the <code>compiler</code>
 * attribute. <a name="compilervalues">There are three choices</a>:</p>
 * <ul>
 *   <li>sun (the standard compiler of the JDK)</li>
 *   <li>kaffe (the standard compiler of 
 *   <li>weblogic</li>
 * </ul>
 * 
 * project contains a compiler implementation for this task as well,
 * please consult miniRMI's documentation to learn how to use it.</p>
 *
 * @author duncan@x180.com
 * @author ludovic.claude@websitewatchers.co.uk
 * @author David Maclean <a href="mailto:david@cm.co.za">david@cm.co.za</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 * @author Takashi Okamoto tokamoto@rd.nttdata.co.jp
 *
 * @since Ant 1.1
 *
 * @ant.task category="java"
 */

public class Rmic extends MatchingTask {

    private static final String FAIL_MSG 
        = "Rmic failed; see the compiler error output for details.";

    private File baseDir;
    private String classname;
    private File sourceBase;
    private String stubVersion;
    private Path compileClasspath;
    private Path extdirs;
    private boolean verify = false;
    private boolean filtering = false;

    private boolean iiop = false;
    private String  iiopopts;
    private boolean idl  = false;
    private String  idlopts;
    private boolean debug  = false;
    private boolean includeAntRuntime = true;
    private boolean includeJavaRuntime = false;

    private Vector compileList = new Vector();

    private ClassLoader loader = null;

    private FileUtils fileUtils = FileUtils.newFileUtils();

    private FacadeTaskHelper facade;

    public Rmic() {
        try {
            Class.forName("kaffe.rmi.rmic.RMIC");
            facade = new FacadeTaskHelper("kaffe");
        } catch (ClassNotFoundException cnfe) {
            facade = new FacadeTaskHelper("sun");
        }
    }

    /** 
     * Sets the location to store the compiled files; required 
     */
    public void setBase(File base) {
        this.baseDir = base;
    }

    /** 
     * Gets the base directory to output generated class. 
     */
     
    public File getBase() {
        return this.baseDir;
    }

    /** 
     * Sets the the class to run <code>rmic</code> against;
     * optional
     */
    public void setClassname(String classname) {
        this.classname = classname;
    }

    /**
     * Gets the class name to compile. 
     */
    public String getClassname() {
        return classname;
    }

    /**
     * optional directory to save generated source files to.
     */
    public void setSourceBase(File sourceBase) {
        this.sourceBase = sourceBase;
    }

    /**
     * Gets the source dirs to find the source java files. 
     */
    public File getSourceBase() {
        return sourceBase;
    }

    /**
     * Specify the JDK version for the generated stub code.
     * Specify &quot;1.1&quot; to pass the &quot;-v1.1&quot; option to rmic.</td>
     */
    public void setStubVersion(String stubVersion) {
        this.stubVersion = stubVersion;
    }

    public String getStubVersion() {
        return stubVersion;
    }

    /**
     * indicates whether token filtering should take place;
     * optional, default=false
     */
    public void setFiltering(boolean filter) {
        filtering = filter;
    }

    public boolean getFiltering() {
        return filtering;
    }

    /**
     * generate debug info (passes -g to rmic);
     * optional, defaults to false
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Gets the debug flag. 
     */
    public boolean getDebug() {
        return debug;
    }

    /**
     * Set the classpath to be used for this compilation.
     */
    public void setClasspath(Path classpath) {
        if (compileClasspath == null) {
            compileClasspath = classpath;
        } else {
            compileClasspath.append(classpath);
        }
    }

    /**
     * Creates a nested classpath element.
     */
    public Path createClasspath() {
        if (compileClasspath == null) {
            compileClasspath = new Path(getProject());
        }
        return compileClasspath.createPath();
    }

    /**
     * Adds to the classpath a reference to 
     * a &lt;path&gt; defined elsewhere.
     */
    public void setClasspathRef(Reference r) {
        createClasspath().setRefid(r);
    }

    /**
     * Gets the classpath. 
     */
    public Path getClasspath() {
        return compileClasspath; 
    }

    /**
     * Flag to enable verification so that the classes 
     * found by the directory match are
     * checked to see if they implement java.rmi.Remote.
     * Optional; his defaults to false if not set.  
     */
     
    public void setVerify(boolean verify) {
        this.verify = verify;
    }

    /** Get verify flag. */
    public boolean getVerify() {
        return verify;
    }

    /**
     * Indicates that IIOP compatible stubs should
     * be generated; optional, defaults to false 
     * if not set.  
     */
    public void setIiop(boolean iiop) {
        this.iiop = iiop;
    }

    /** 
     * Gets iiop flags. 
     */
    public boolean getIiop() {
        return iiop;
    }

    /**
     * Set additional arguments for iiop 
     */
    public void setIiopopts(String iiopopts) {
        this.iiopopts = iiopopts;
    }

    /**
     * Gets additional arguments for iiop. 
     */
    public String getIiopopts() {
        return iiopopts;
    }

    /**
     * Indicates that IDL output should be 
     * generated.  This defaults to false 
     * if not set.  
     */
    public void setIdl(boolean idl) {
        this.idl = idl;
    }

    /**
     * Gets IDL flags. 
     */
    public boolean getIdl() {
        return idl;
    }

    /**
     * pass additional arguments for idl compile 
     */
    public void setIdlopts(String idlopts) {
        this.idlopts = idlopts;
    }

    /**
     * Gets additional arguments for idl compile. 
     */
    public String getIdlopts() {
        return idlopts;
    }

    /**
     * Gets file list to compile. 
     */
    public Vector getFileList() {
        return compileList;
    }

    /**
     * Sets whether or not to include ant's own classpath in this task's 
     * classpath.
     * Optional; default is <code>true</code>.
     */
    public void setIncludeantruntime(boolean include) {
        includeAntRuntime = include;
    }

    /**
     * Gets whether or not the ant classpath is to be included in the
     * task's classpath.
     */
    public boolean getIncludeantruntime() {
        return includeAntRuntime;
    }

    /**
     * task's classpath.
     * Enables or disables including the default run-time
     * libraries from the executing VM; optional,
     * defaults to false     
     */
    public void setIncludejavaruntime(boolean include) {
        includeJavaRuntime = include;
    }

    /**
     * Gets whether or not the java runtime should be included in this
     * task's classpath.
     */
    public boolean getIncludejavaruntime() {
        return includeJavaRuntime;
    }

    /**
     * Sets the extension directories that will be used during the
     * compilation; optional.
     */
    public void setExtdirs(Path extdirs) {
        if (this.extdirs == null) {
            this.extdirs = extdirs;
        } else {
            this.extdirs.append(extdirs);
        }
    }

    /**
     * Maybe creates a nested extdirs element.
     */
    public Path createExtdirs() {
        if (extdirs == null) {
            extdirs = new Path(getProject());
        }
        return extdirs.createPath();
    }

    /**
     * Gets the extension directories that will be used during the
     * compilation.
     */
    public Path getExtdirs() {
        return extdirs;
    }

    public Vector getCompileList() {
        return compileList;
    }

    /**
     * Sets the compiler implementation to use; optional,
     * defaults to the value of the <code>build.rmic</code> property,
     * or failing that, default compiler for the current VM
     * @since Ant 1.5
     */
    public void setCompiler(String compiler) {
        facade.setImplementation(compiler);
    }

    /**
     * get the name of the current compiler
     * @since Ant 1.5
     */
    public String getCompiler() {
        facade.setMagicValue(getProject().getProperty("build.rmic"));
        return facade.getImplementation();
    }

    /**
     * Adds an implementation specific command line argument.
     * @since Ant 1.5
     */
    public ImplementationSpecificArgument createCompilerArg() {
        ImplementationSpecificArgument arg =
            new ImplementationSpecificArgument();
        facade.addImplementationArgument(arg);
        return arg;
    }

    /**
     * Get the additional implementation specific command line arguments.
     * @return array of command line arguments, guaranteed to be non-null.
     * @since Ant 1.5
     */
    public String[] getCurrentCompilerArgs() {
        getCompiler();
        return facade.getArgs();
    }

    /**
     * execute by creating an instance of an implementation
     * class and getting to do the work
     */
    public void execute() throws BuildException {
        if (baseDir == null) {
            throw new BuildException("base attribute must be set!", getLocation());
        }
        if (!baseDir.exists()) {
            throw new BuildException("base does not exist!", getLocation());
        }

        if (verify) {
            log("Verify has been turned on.", Project.MSG_VERBOSE);
        }

        RmicAdapter adapter = RmicAdapterFactory.getRmic(getCompiler(), this);
            
        adapter.setRmic(this);

        Path classpath = adapter.getClasspath();
        loader = new AntClassLoader(getProject(), classpath);

        try {
            if (classname == null) {
                DirectoryScanner ds = this.getDirectoryScanner(baseDir);
                String[] files = ds.getIncludedFiles();
                scanDir(baseDir, files, adapter.getMapper());
            } else {
                scanDir(baseDir, 
                        new String[] {classname.replace('.', 
                                                        File.separatorChar)
                                          + ".class"},
                        adapter.getMapper());
            }
            
            int fileCount = compileList.size();
            if (fileCount > 0) {
                log("RMI Compiling " + fileCount +
                    " class" + (fileCount > 1 ? "es" : "") + " to " + baseDir, 
                    Project.MSG_INFO);
                
                if (!adapter.execute()) {
                    throw new BuildException(FAIL_MSG, getLocation());
                }
            }
            
            /* 
             * Move the generated source file to the base directory.  If
             * base directory and sourcebase are the same, the generated
             * sources are already in place.
             */
            if (null != sourceBase && !baseDir.equals(sourceBase) 
                && fileCount > 0) {
                if (idl) {
                    log("Cannot determine sourcefiles in idl mode, ", 
                        Project.MSG_WARN);
                    log("sourcebase attribute will be ignored.", 
                        Project.MSG_WARN);
                } else {
                    for (int j = 0; j < fileCount; j++) {
                        moveGeneratedFile(baseDir, sourceBase,
                                          (String) compileList.elementAt(j),
                                          adapter);
                    }
                }
            }
        } finally {
            compileList.removeAllElements();
        }
    }

    /**
     * Move the generated source file(s) to the base directory
     *
     * @throws org.apache.tools.ant.BuildException When error
     * copying/removing files.
     */
    private void moveGeneratedFile (File baseDir, File sourceBaseFile,
                                    String classname,
                                    RmicAdapter adapter)
        throws BuildException {

        String classFileName = 
            classname.replace('.', File.separatorChar) + ".class";
        String[] generatedFiles = 
            adapter.getMapper().mapFileName(classFileName);

        for (int i = 0; i < generatedFiles.length; i++) {
            final String generatedFile = generatedFiles[i];
            if (!generatedFile.endsWith(".class")) {
                continue;
            }

            final int pos = generatedFile.length() - ".class".length();
            String sourceFileName =
                generatedFile.substring(0, pos) + ".java";

            File oldFile = new File(baseDir, sourceFileName);
            if (!oldFile.exists()) {
                continue;
            }

            File newFile = new File(sourceBaseFile, sourceFileName);
            try {
                if (filtering) {
                    fileUtils.copyFile(oldFile, newFile, 
                        new FilterSetCollection(getProject()
                                                .getGlobalFilterSet()));
                } else {
                    fileUtils.copyFile(oldFile, newFile);
                }
                oldFile.delete();
            } catch (IOException ioe) {
                String msg = "Failed to copy " + oldFile + " to " +
                    newFile + " due to " + ioe.getMessage();
                throw new BuildException(msg, ioe, getLocation());
            }
        }
    }

    /**
     * Scans the directory looking for class files to be compiled.
     * The result is returned in the class variable compileList.
     */
    protected void scanDir(File baseDir, String[] files,
                           FileNameMapper mapper) {

        String[] newFiles = files;
        if (idl) {
            log("will leave uptodate test to rmic implementation in idl mode.",
                Project.MSG_VERBOSE);
        } else if (iiop 
                   && iiopopts != null && iiopopts.indexOf("-always") > -1) {
            log("no uptodate test as -always option has been specified",
                Project.MSG_VERBOSE);
        } else {
            SourceFileScanner sfs = new SourceFileScanner(this);
            newFiles = sfs.restrict(files, baseDir, baseDir, mapper);
        }

        for (int i = 0; i < newFiles.length; i++) {
            String classname = newFiles[i].replace(File.separatorChar, '.');
            classname = classname.substring(0, classname.lastIndexOf(".class"));
            compileList.addElement(classname);
        }
    }

    /**
     * Load named class and test whether it can be rmic'ed
     */
    public boolean isValidRmiRemote(String classname) {
        try {
            Class testClass = loader.loadClass(classname);
            if (testClass.isInterface() && !iiop && !idl) {
                return false;
            }
            return isValidRmiRemote(testClass);
        } catch (ClassNotFoundException e) {
            log("Unable to verify class " + classname + 
                ". It could not be found.", Project.MSG_WARN);
        } catch (NoClassDefFoundError e) {
            log("Unable to verify class " + classname + 
                ". It is not defined.", Project.MSG_WARN);
        } catch (Throwable t) {
            log("Unable to verify class " + classname + 
                ". Loading caused Exception: " +
                t.getMessage(), Project.MSG_WARN);
        }
        return false;
    }

    /**
     * Returns the topmost interface that extends Remote for a given
     * class - if one exists.
     */
    public Class getRemoteInterface(Class testClass) {
        if (Remote.class.isAssignableFrom(testClass)) {
            Class [] interfaces = testClass.getInterfaces();
            if (interfaces != null) {
                for (int i = 0; i < interfaces.length; i++) {
                    if (Remote.class.isAssignableFrom(interfaces[i])) {
                        return interfaces[i];
                    }
                }
            }
        }
        return null;
    }

    /**
     * Check to see if the class or (super)interfaces implement
     * java.rmi.Remote.
     */
    private boolean isValidRmiRemote (Class testClass) {
        return getRemoteInterface(testClass) != null;
    }

    /**
     * Classloader for the user-specified classpath.
     */
    public ClassLoader getLoader() {
        return loader;
    }

    /**
     * Adds an "compiler" attribute to Commandline$Attribute used to
     * filter command line attributes based on the current
     * implementation.
     */
    public class ImplementationSpecificArgument extends 
        org.apache.tools.ant.util.facade.ImplementationSpecificArgument {

        /**
         * Only pass the specified argument if the 
         * chosen compiler implementation matches the 
         * value of this attribute. Legal values are
         * the same as those in the above list of
         * valid compilers.)
         */
        public void setCompiler(String impl) {
            super.setImplementation(impl);
        }
    }

}

