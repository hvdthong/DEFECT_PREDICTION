package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.Commandline;

/**
 * Sign a archive.
 * 
 * @author Peter Donald <a href="mailto:donaldp@apache.org">donaldp@apache.org</a>
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

    public void execute() throws BuildException {
        if (project.getJavaVersion().equals(Project.JAVA_1_1)) {
            throw new BuildException("The signjar task is only available on JDK versions 1.2 or greater");
        } 

        if (null == jar) {
            throw new BuildException("jar attribute must be set");
        } 

        if (null == alias) {
            throw new BuildException("alias attribute must be set");
        } 

        if (null == storepass) {
            throw new BuildException("storepass attribute must be set");
        } 

        if(isUpToDate()) return;

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

        if (null != signedjar) {
            cmd.createArg().setValue("-signedjar");
            cmd.createArg().setValue(signedjar);
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

        cmd.createArg().setValue(jar);
        

        cmd.createArg().setValue(alias);

        log("Signing Jar : " + (new File(jar)).getAbsolutePath());
        cmd.setFailonerror(true);
        cmd.setTaskName( getTaskName() );
        cmd.execute();
    }

    protected boolean isUpToDate() {

        if( null != jar && null != signedjar ) {

            final File jarFile = new File(jar);
            final File signedjarFile = new File(signedjar);
            
            if(!jarFile.exists()) return false;
            if(!signedjarFile.exists()) return false;
            if(jarFile.equals(signedjarFile)) return false;
            if(signedjarFile.lastModified() > jarFile.lastModified())
                return true;
        }

        return false;
    }
}

