package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.parsers.*;

import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.types.Commandline.Argument;
import org.xml.sax.*;

/**
 * BorlandGenerateClient is dedicated to the Borland Application Server 4.5
 * This task generates the client jar using as input the ejb jar file.
 * Two mode are available: java mode (default) and fork mode. With the fork mode,
 * it is impossible to add classpath to the commmand line.
 * 
 * @author  <a href="mailto:benoit.moussaud@criltelecom.com">Benoit Moussaud</a>
 *
 */
public class BorlandGenerateClient extends Task
{
    static final String JAVA_MODE = "java";
    static final String FORK_MODE = "fork";

    /** debug the generateclient task */
    boolean debug = false;

    /** hold the ejbjar file name */
    File ejbjarfile = null;

    /** hold the client jar file name */
    File clientjarfile = null;

    /** hold the classpath */
    Path classpath;

    /** hold the mode (java|fork) */
    String mode = JAVA_MODE;

    public void setMode(String s) {
        mode = s;
    }
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setEjbjar(File ejbfile) {
        ejbjarfile = ejbfile;
    }
    
    public void setClientjar(File clientjar) {
        clientjarfile = clientjar;
    }

    public void setClasspath(Path classpath) {
        if (this.classpath == null) {
            this.classpath = classpath;
        }
        else {
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
    

    /**
     * Do the work.
     *
     * The work is actually done by creating a separate JVM to run a java task. 
     *
     * @exception BuildException if someting goes wrong with the build
     */
    public void execute() throws BuildException {
        if ( ejbjarfile == null ||
             ejbjarfile.isDirectory()) {
            throw new BuildException("invalid ejb jar file.");

        if ( clientjarfile == null ||
             clientjarfile.isDirectory()) {
            log("invalid or missing client jar file.",Project.MSG_VERBOSE);
            String ejbjarname = ejbjarfile.getAbsolutePath();
            String clientname = ejbjarname.substring(0,ejbjarname.lastIndexOf("."));
            clientname = clientname + "client.jar";
            clientjarfile = new File(clientname);


        if ( mode == null ) {
            log("mode is null default mode  is java");
            setMode(JAVA_MODE);

        log("client jar file is " + clientjarfile);

        if ( mode.equalsIgnoreCase(FORK_MODE)) {
            executeFork();
        else {            
            executeJava();
    }
    
    /** launch the generate client using java api */
    protected void executeJava() throws BuildException {
        try {
            log("mode : java");

            org.apache.tools.ant.taskdefs.Java execTask = null;                
            execTask = (Java) getProject().createTask("java");
                       
            execTask.setDir(new File("."));
            execTask.setClassname("com.inprise.server.commandline.EJBUtilities");
            execTask.setClasspath(classpath.concatSystemClasspath());

            execTask.setFork(true);
            execTask.createArg().setValue("generateclient");
            if ( debug ) {
                execTask.createArg().setValue("-trace");                

            execTask.createArg().setValue("-short");
            execTask.createArg().setValue("-jarfile");
            execTask.createArg().setValue(ejbjarfile.getAbsolutePath());
            execTask.createArg().setValue("-single");
            execTask.createArg().setValue("-clientjarfile");
            execTask.createArg().setValue(clientjarfile.getAbsolutePath());

            log("Calling EJBUtilities",Project.MSG_VERBOSE);                       
            execTask.execute();        

        }
        catch (Exception e) {
            String msg = "Exception while calling generateclient Details: " + e.toString();
            throw new BuildException(msg, e);
        }
    }

    /** launch the generate client using system api */
    protected  void executeFork() throws BuildException {
        try {
            log("mode : fork");

            org.apache.tools.ant.taskdefs.ExecTask execTask = null;                
            execTask = (ExecTask) getProject().createTask("exec");
                       
            execTask.setDir(new File("."));
            execTask.setExecutable("iastool");
            execTask.createArg().setValue("generateclient");
            if ( debug ){
                execTask.createArg().setValue("-trace");                

            execTask.createArg().setValue("-short");
            execTask.createArg().setValue("-jarfile");
            execTask.createArg().setValue(ejbjarfile.getAbsolutePath());
            execTask.createArg().setValue("-single");
            execTask.createArg().setValue("-clientjarfile");
            execTask.createArg().setValue(clientjarfile.getAbsolutePath());

            log("Calling java2iiop",Project.MSG_VERBOSE);                       
            execTask.execute();        
        }
        catch (Exception e) {
            String msg = "Exception while calling generateclient Details: " + e.toString();
            throw new BuildException(msg, e);
        }

    }
}
