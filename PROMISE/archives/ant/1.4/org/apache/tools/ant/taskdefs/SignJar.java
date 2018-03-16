package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.FileSet;

/**
 * Sign a archive.
 *
 * @author Peter Donald <a href="mailto:donaldp@apache.org">donaldp@apache.org</a>
 * @author Nick Fortescue <a href="mailto:nick@ox.compsoc.net">nick@ox.compsoc.net</a>
 */
public class SignJar extends Task {

    /**
     * The name of the jar file.
     */
    protected String jar;

    /**
     * The alias of signer.
     */
    protected String alias;

    /**
     * The name of keystore file.
     */
    protected String keystore;
    protected String storepass;
    protected String storetype;
    protected String keypass;
    protected String sigfile;
    protected String signedjar;
    protected boolean verbose;
    protected boolean internalsf;
    protected boolean sectionsonly;

    /**
     * the filesets of the jars to sign
     */
    protected Vector filesets = new Vector();
    /**
     * Whether to assume a jar which has an appropriate .SF file in is already
     * signed.
     */
    protected boolean lazy;

    public void setJar(final String jar) {
        this.jar = jar;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public void setKeystore(final String keystore) {
        this.keystore = keystore;
    }

    public void setStorepass(final String storepass) {
        this.storepass = storepass;
    }

    public void setStoretype(final String storetype) {
        this.storetype = storetype;
    }

    public void setKeypass(final String keypass) {
        this.keypass = keypass;
    }

    public void setSigfile(final String sigfile) {
        this.sigfile = sigfile;
    }

    public void setSignedjar(final String signedjar) {
        this.signedjar = signedjar;
    }

    public void setVerbose(final boolean verbose) {
        this.verbose = verbose;
    }

    public void setInternalsf(final boolean internalsf) {
        this.internalsf = internalsf;
    }

    public void setSectionsonly(final boolean sectionsonly) {
        this.sectionsonly = sectionsonly;
    }

    public void setLazy(final boolean lazy) {
        this.lazy = lazy;
    }

    /**
     * Adds a set of files (nested fileset attribute).
     */
    public void addFileset(final FileSet set) {
        filesets.addElement(set);
    }


    public void execute() throws BuildException {
        if (null == jar && null == filesets) {
            throw new BuildException("jar must be set through jar attribute or nested filesets");
        }
        if( null != jar ) {
            doOneJar(jar, signedjar);
            return;
        } else {

            for (int i=0; i<filesets.size(); i++) {
                FileSet fs = (FileSet) filesets.elementAt(i);
                DirectoryScanner ds = fs.getDirectoryScanner(project);
                String[] jarFiles = ds.getIncludedFiles();
                for(int j=0; j<jarFiles.length; j++) {
                    doOneJar(jarFiles[j], null);
                }
            }
        }
    }

    private void doOneJar(String jarSource, String jarTarget) throws BuildException {
        if (project.getJavaVersion().equals(Project.JAVA_1_1)) {
            throw new BuildException("The signjar task is only available on JDK versions 1.2 or greater");
        }

        if (null == alias) {
            throw new BuildException("alias attribute must be set");
        }

        if (null == storepass) {
            throw new BuildException("storepass attribute must be set");
        }

        if(isUpToDate(jarSource, jarTarget)) return;

        final StringBuffer sb = new StringBuffer();

        final ExecTask cmd = (ExecTask) project.createTask("exec");
        cmd.setExecutable("jarsigner");

        if (null != keystore) {
            cmd.createArg().setValue("-keystore");
            cmd.createArg().setValue(keystore);
        }

        if (null != storepass) {
            cmd.createArg().setValue("-storepass");
            cmd.createArg().setValue(storepass);
        }

        if (null != storetype) {
            cmd.createArg().setValue("-storetype");
            cmd.createArg().setValue(storetype);
        }

        if (null != keypass) {
            cmd.createArg().setValue("-keypass");
            cmd.createArg().setValue(keypass);
        }

        if (null != sigfile) {
            cmd.createArg().setValue("-sigfile");
            cmd.createArg().setValue(sigfile);
        }

        if (null != jarTarget) {
            cmd.createArg().setValue("-signedjar");
            cmd.createArg().setValue(jarTarget);
        }

        if (verbose) {
            cmd.createArg().setValue("-verbose");
        }

        if (internalsf) {
            cmd.createArg().setValue("-internalsf");
        }

        if (sectionsonly) {
            cmd.createArg().setValue("-sectionsonly");
        }

        cmd.createArg().setValue(jarSource);


        cmd.createArg().setValue(alias);

        log("Signing Jar : " + (new File(jarSource)).getAbsolutePath());
        cmd.setFailonerror(true);
        cmd.setTaskName( getTaskName() );
        cmd.execute();
    }

    protected boolean isUpToDate(String jarSource, String jarTarget) {
        if( null == jarSource ) {
            return false;
        }

        if( null != jarTarget ) {

            final File jarFile = new File(jarSource);
            final File signedjarFile = new File(jarTarget);

            if(!jarFile.exists()) return false;
            if(!signedjarFile.exists()) return false;
            if(jarFile.equals(signedjarFile)) return false;
            if(signedjarFile.lastModified() > jarFile.lastModified())
                return true;
        } else {
            if( lazy ) {
                return isSigned(jarSource);
            }
        }

        return false;
    }

    protected boolean isSigned(String jarFilename) {
        final String SIG_START = "META-INF/";
        final String SIG_END = ".SF";

        File file = new File(jarFilename);
        if( !file.exists() ) {
            return false;
        }
        ZipFile jarFile = null;
        try {
            jarFile = new ZipFile(file);
            if(null == alias) {
                Enumeration entries = jarFile.entries();
                while(entries.hasMoreElements()) {
                    String name =  ((ZipEntry)entries.nextElement()).getName();
                    if(name.startsWith(SIG_START) && name.endsWith(SIG_END)) {
                        return true;
                    }
                }
                return false;
            } else {
                return jarFile.getEntry(SIG_START+alias.toUpperCase()+
                                        SIG_END) != null;
            }
        } catch(IOException e) {
            return false;
        } finally {
            if(jarFile != null) {
                try {jarFile.close();} catch(IOException e) {}
            }
        }
    }

}

