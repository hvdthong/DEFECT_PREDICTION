package org.apache.tools.ant.taskdefs.rmic;

import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.util.*;

import java.io.File;
import java.util.Random;
import java.util.Vector;

/**
 * This is the default implementation for the RmicAdapter interface.
 * Currently, this is a cut-and-paste of the original rmic task and
 * DefaultCopmpilerAdapter.
 *
 * @author duncan@x180.com
 * @author ludovic.claude@websitewatchers.co.uk
 * @author David Maclean <a href="mailto:david@cm.co.za">david@cm.co.za</a>
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 * @author Takashi Okamoto <tokamoto@rd.nttdata.co.jp>
 */
public abstract class DefaultRmicAdapter implements RmicAdapter {

    private Rmic attributes;
    private FileNameMapper mapper;

    public DefaultRmicAdapter() {
    }

    public void setRmic( Rmic attributes ) {
        this.attributes = attributes;
        mapper = new RmicFileNameMapper();
    }

    public Rmic getRmic() {
        return attributes;
    }

    protected String getStubClassSuffix() {
        return "_Stub";
    }        

    protected String getSkelClassSuffix() {
        return "_Skel";
    }        

    protected String getTieClassSuffix() {
        return "_Tie";
    }        

    /**
     * This implementation maps *.class to *getStubClassSuffix().class and - if
     * stubversion is not 1.2 - to *getSkelClassSuffix().class.
     */
    public FileNameMapper getMapper() {
        return mapper;
    }

    /**
     * The CLASSPATH this rmic process will use.
     */
    public Path getClasspath() {
        return getCompileClasspath();
    }

    /**
     * Builds the compilation classpath.
     */
    protected Path getCompileClasspath() {
        Path classpath = new Path(attributes.getProject());
        classpath.setLocation(attributes.getBase());


        if (attributes.getClasspath() == null) {
            if ( attributes.getIncludeantruntime() ) {
                classpath.addExisting(Path.systemClasspath);
            }
        } else {
            if ( attributes.getIncludeantruntime() ) {
                classpath.addExisting(attributes.getClasspath().concatSystemClasspath("last"));
            } else {
                classpath.addExisting(attributes.getClasspath().concatSystemClasspath("ignore"));
            }
        }

        if (attributes.getIncludejavaruntime()) {

            if (System.getProperty("java.vendor").toLowerCase().indexOf("microsoft") >= 0) {
                FileSet msZipFiles = new FileSet();
                msZipFiles.setDir(new File(System.getProperty("java.home") + File.separator + "Packages"));
                msZipFiles.setIncludes("*.ZIP");
                classpath.addFileset(msZipFiles);
            } else if (Project.getJavaVersion() == Project.JAVA_1_1) {
                classpath.addExisting(new Path(null,
                                                System.getProperty("java.home")
                                                + File.separator + "lib"
                                                + File.separator 
                                                + "classes.zip"));
            } else if(System.getProperty("java.vm.name").equals("Kaffe")) {
                FileSet kaffeJarFiles = new FileSet();
                kaffeJarFiles.setDir(new File(System.getProperty("java.home") 
                                              + File.separator + "share"
                                              + File.separator + "kaffe"));
                
                kaffeJarFiles.setIncludes("*.jar");
                classpath.addFileset(kaffeJarFiles);
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
     * setup rmic argument for rmic.
     */
    protected Commandline setupRmicCommand() {
        return setupRmicCommand(null);
    }

    /**
     * setup rmic argument for rmic.
     *
     * @param options additional parameters needed by a specific
     *                implementation.
     */
    protected Commandline setupRmicCommand(String[] options) {
        Commandline cmd = new Commandline();

        if (options != null) {
            for (int i=0; i<options.length; i++) {
                cmd.createArgument().setValue(options[i]);
            }
        }

        Path classpath = getCompileClasspath();

        cmd.createArgument().setValue("-d");
        cmd.createArgument().setFile(attributes.getBase());

        if (attributes.getExtdirs() != null) {
            if (Project.getJavaVersion().startsWith("1.1")) {
                /*
                 * XXX - This doesn't mix very well with build.systemclasspath,
                 */
                addExtdirsToClasspath(classpath);
            } else {
                cmd.createArgument().setValue("-extdirs");
                cmd.createArgument().setPath(attributes.getExtdirs());
            }
        }

        cmd.createArgument().setValue("-classpath");
        cmd.createArgument().setPath(classpath);

        String stubVersion = attributes.getStubVersion();
        if (null != stubVersion) {
            if ("1.1".equals(stubVersion))
                cmd.createArgument().setValue("-v1.1");
            else if ("1.2".equals(stubVersion))
                cmd.createArgument().setValue("-v1.2");
            else
                cmd.createArgument().setValue("-vcompat");
        }

        if (null != attributes.getSourceBase()) {
            cmd.createArgument().setValue("-keepgenerated");
        }

        if( attributes.getIiop() ) {
            attributes.log("IIOP has been turned on.", Project.MSG_INFO);
            cmd.createArgument().setValue("-iiop");
            if( attributes.getIiopopts() != null ) {
                attributes.log("IIOP Options: " + attributes.getIiopopts(),
                               Project.MSG_INFO );
                cmd.createArgument().setValue(attributes.getIiopopts());
            }
        }

        if( attributes.getIdl() )  {
            cmd.createArgument().setValue("-idl");
            attributes.log("IDL has been turned on.", Project.MSG_INFO);
            if( attributes.getIdlopts() != null ) {
                cmd.createArgument().setValue(attributes.getIdlopts());
                attributes.log("IDL Options: " + attributes.getIdlopts(),
                               Project.MSG_INFO );
            }
        }

        if( attributes.getDebug())  {
            cmd.createArgument().setValue("-g");
        }

        logAndAddFilesToCompile(cmd);
        return cmd;
     }

    /**
     * Logs the compilation parameters, adds the files to compile and logs the 
     * &qout;niceSourceList&quot;
     */
    protected void logAndAddFilesToCompile(Commandline cmd) {
        Vector compileList = attributes.getCompileList();

        attributes.log("Compilation args: " + cmd.toString(),
                       Project.MSG_VERBOSE);

        StringBuffer niceSourceList = new StringBuffer("File");
        if (compileList.size() != 1) {
            niceSourceList.append("s");
        }
        niceSourceList.append(" to be compiled:");

        for (int i=0; i < compileList.size(); i++) {
            String arg = (String)compileList.elementAt(i);
            cmd.createArgument().setValue(arg);
            niceSourceList.append("    " + arg);
        }

        attributes.log(niceSourceList.toString(), Project.MSG_VERBOSE);
    }

    /**
     * Emulation of extdirs feature in java >= 1.2.
     * This method adds all files in the given
     * directories (but not in sub-directories!) to the classpath,
     * so that you don't have to specify them all one by one.
     * @param classpath - Path to append files to
     */
    protected void addExtdirsToClasspath(Path classpath) {
        Path extdirs = attributes.getExtdirs();
        if (extdirs == null) {
            String extProp = System.getProperty("java.ext.dirs");
            if (extProp != null) {
                extdirs = new Path(attributes.getProject(), extProp);
            } else {
                return;
            }
        }

        String[] dirs = extdirs.list();
        for (int i=0; i<dirs.length; i++) {
            if (!dirs[i].endsWith(File.separator)) {
                dirs[i] += File.separator;
            }
            File dir = attributes.getProject().resolveFile(dirs[i]);
            FileSet fs = new FileSet();
            fs.setDir(dir);
            fs.setIncludes("*");
            classpath.addFileset(fs);
        }
    }

    private final static Random rand = new Random();

    /**
     * Mapper that possibly returns two file names, *_Stub and *_Skel.
     */
    private class RmicFileNameMapper implements FileNameMapper {

        RmicFileNameMapper() {}

        /**
         * Empty implementation.
         */
        public void setFrom(String s) {}
        /**
         * Empty implementation.
         */
        public void setTo(String s) {}

        public String[] mapFileName(String name) {
            if (name == null
                || !name.endsWith(".class")
                || name.endsWith(getStubClassSuffix()+".class") 
                || name.endsWith(getSkelClassSuffix()+".class") 
                || name.endsWith(getTieClassSuffix()+".class")) {
                return null;
            }

            String base = name.substring(0, name.indexOf(".class"));
            String classname = base.replace(File.separatorChar, '.');
            if (attributes.getVerify() &&
                !attributes.isValidRmiRemote(classname)) {
                return null;
            }

            /*
             * fallback in case we have trouble loading the class or
             * don't know how to handle it (there is no easy way to
             * know what IDL mode would generate.
             *
             * This is supposed to make Ant always recompile the
             * class, as a file of that name should not exist.
             */
            String[] target = new String[] {name+".tmp."+rand.nextLong()};

            if (!attributes.getIiop() && !attributes.getIdl()) {
                if ("1.2".equals(attributes.getStubVersion())) {
                    target = new String[] {
                        base + getStubClassSuffix() + ".class"
                    };
                } else {
                    target = new String[] {
                        base + getStubClassSuffix() + ".class",
                        base + getSkelClassSuffix() + ".class",
                    };
                }
            } else if (!attributes.getIdl()) {
                int lastSlash = base.lastIndexOf(File.separatorChar);

                String dirname = "";
                /*
                 * I know, this is not necessary, but I prefer it explicit (SB)
                 */
                int index = -1;
                if (lastSlash == -1) {
                    index = 0;
                } else {
                    index = lastSlash + 1;
                    dirname = base.substring(0, index);
                }

                String filename = base.substring(index);

                try {
                    Class c = attributes.getLoader().loadClass(classname);

                    if (c.isInterface()) {
                        target = new String[] {
                            dirname + "_" + filename + getStubClassSuffix() 
                            + ".class"
                        };
                    } else {
                        /*
                         * stub is derived from implementation, 
                         * tie from interface name.
                         */
                        Class interf = attributes.getRemoteInterface(c);
                        String iName = interf.getName();
                        String iDir = "";
                        int iIndex = -1;
                        int lastDot = iName.lastIndexOf(".");
                        if (lastDot == -1) {
                            iIndex = 0;
                        } else {
                            iIndex = lastDot + 1;
                            iDir = iName.substring(0, iIndex);
                            iDir = iDir.replace('.', File.separatorChar);
                        }
                        
                        target = new String[] {
                            dirname + "_" + filename + getTieClassSuffix() 
                            + ".class",
                            iDir + "_" + iName.substring(iIndex) 
                            + getStubClassSuffix() + ".class"
                        };
                    }
                } catch (ClassNotFoundException e) {
                    attributes.log("Unable to verify class " + classname
                                   + ". It could not be found.", 
                                   Project.MSG_WARN);
                } catch (NoClassDefFoundError e) {
                    attributes.log("Unable to verify class " + classname
                                   + ". It is not defined.", Project.MSG_WARN);
                } catch (Throwable t) {
                    attributes.log("Unable to verify class " + classname
                                   + ". Loading caused Exception: "
                                   + t.getMessage(), Project.MSG_WARN);
                }
            }
            return target;
        }
    }

}
