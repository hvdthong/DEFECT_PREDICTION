package org.apache.tools.ant.taskdefs.optional.metamata;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;

import java.io.*;
import java.util.*;

/**
 * Simple Metamata MParse task based on the original written by
 * <a href="mailto:thomas.haas@softwired-inc.com">Thomas Haas</a>
 * 
 * This version was written for Metamata 2.0 available at
 *
 * @author <a href="mailto:sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class MParse extends Task {

    private Path classpath = null;
    private Path sourcepath = null;
    private File metahome = null;
    private File target = null;
    private boolean verbose = false;
    private boolean debugparser = false;
    private boolean debugscanner = false;    
    private boolean cleanup = false;
    private CommandlineJava cmdl = new CommandlineJava();
    private File optionsFile = null;

    /** location of metamata dev environment */
    public void setMetamatahome(File metamatahome) {
        this.metahome = metamatahome;
    }

    /** the .jj file to process */
    public void setTarget(File target) {
        this.target = target;
    }

    /** create a classpath entry */
    public Path createClasspath() {
        if (classpath == null) {
            classpath = new Path(project);
        }        
        return classpath;
    }

    /** creates a sourcepath entry */
    public Path createSourcepath() {
        if (sourcepath == null) {
            sourcepath = new Path(project);
        }        
        return sourcepath;
    }

    /** set verbose mode */
    public void setVerbose(boolean flag){
        verbose = flag;
    }

    /** set scanner debug mode */
    public void setDebugscanner(boolean flag){
        debugscanner = flag;
    }

    /** set parser debug mode */
    public void setDebugparser(boolean flag){
        debugparser = flag;
    }

    /** set the hack to cleanup the temp file */
    public void setCleanup(boolean value) {
        cleanup = value;
    }

    /** Creates a nested jvmarg element. */
    public Commandline.Argument createJvmarg() {
        return cmdl.createVmArgument();
    }

    /**  -mx or -Xmx depending on VM version */
    public void setMaxmemory(String max){
        if (Project.getJavaVersion().startsWith("1.1")) {
            createJvmarg().setValue("-mx" + max);
        } else {
            createJvmarg().setValue("-Xmx" + max);
        }
    }

    public MParse() {
        cmdl.setVm("java");
        cmdl.setClassname("com.metamata.jj.MParse");
    }

   
    /** execute the command line */
    public void execute() throws BuildException {
        try {
            setUp();
            ExecuteStreamHandler handler = createStreamHandler();
            _execute(handler);
        } finally {
            cleanUp();
        }
    }
    
    /** return the default stream handler for this task */
    protected ExecuteStreamHandler createStreamHandler(){
        return new LogStreamHandler(this, Project.MSG_INFO, Project.MSG_INFO);
    }
    
    /**
     * check the options and build the command line
     */
    protected void setUp() throws BuildException {
        checkOptions();

        File[] jars = getMetamataLibs();
        final Path classPath = cmdl.createClasspath(project);
        for (int i = 0; i < jars.length; i++){
            classPath.createPathElement().setLocation(jars[i]);
        }

        final Commandline.Argument vmArgs = cmdl.createVmArgument();
        vmArgs.setValue("-Dmetamata.home=" + metahome.getAbsolutePath() );


        String[] options = getOptions();
        optionsFile = createTmpFile();
        generateOptionsFile(optionsFile, options);
        Commandline.Argument args = cmdl.createArgument();
        args.setLine("-arguments " + optionsFile.getAbsolutePath());
    }    

    
    /** execute the process with a specific handler */
    protected void _execute(ExecuteStreamHandler handler) throws BuildException {
        String pathname = target.getAbsolutePath();
        int pos = pathname.length() - ".jj".length();
        pathname = pathname.substring(0, pos) + ".java";
        File javaFile = new File(pathname);
        if (javaFile.exists() && target.lastModified() < javaFile.lastModified()) {
            project.log("Target is already build - skipping (" + target + ")");
            return;
        }
        
        final Execute process = new Execute(handler);
        log(cmdl.toString(), Project.MSG_VERBOSE);
        process.setCommandline(cmdl.getCommandline());
        try {
            if (process.execute() != 0) {
                throw new BuildException("Metamata task failed.");
            }
        } catch (IOException e){
            throw new BuildException("Failed to launch Metamata task: " + e);
        }
    }    
    
    /** clean up all the mess that we did with temporary objects */
    protected void cleanUp(){
        if (optionsFile != null){
            optionsFile.delete();
            optionsFile = null;
        }
        if (cleanup) {
            String name = target.getName();
            int pos = name.length() - ".jj".length();
            name = "__jj" + name.substring(0, pos) + ".sunjj";
            final File sunjj = new File(target.getParent(), name);
            if (sunjj.exists()) {
                project.log("Removing stale file: " + sunjj.getName());
                sunjj.delete();
            }
        }
    }
    
    /**
     * return an array of files containing the path to the needed
     * libraries to run metamata. The file are not checked for
     * existence. You should do this yourself if needed or simply let the
     * forked process do it for you.
     * @return array of jars/zips needed to run metamata.
     */
    protected File[] getMetamataLibs(){
        Vector files = new Vector();
        files.addElement( new File(metahome, "lib/metamata.jar") );
        files.addElement( new File(metahome, "bin/lib/JavaCC.zip") );
        
        File[] array = new File[ files.size() ];
        files.copyInto(array);
        return array;
    }
    
    
    /**
     * validate options set and resolve files and paths
     * @throws BuildException thrown if an option has an incorrect state.
     */
    protected void checkOptions() throws BuildException {       
        if (metahome == null || !metahome.exists()){
            throw new BuildException("'metamatahome' must point to Metamata home directory.");
        }
        metahome = project.resolveFile(metahome.getPath());
        
        File[] jars = getMetamataLibs();
        for (int i = 0; i < jars.length; i++){
            if (!jars[i].exists()){
                throw new BuildException( jars[i] + " does not exist. Check your metamata installation.");
            }           
        }
        
        if (target == null || !target.isFile() || !target.getName().endsWith(".jj") ) {
            throw new BuildException("Invalid target: " + target);
        }
        target = project.resolveFile(target.getPath());
    }    
    
    /**
     * return all options of the command line as string elements
     * @param an array of options corresponding to the setted options.
     */
    protected String[] getOptions(){
        Vector options = new Vector();
        if (verbose){
            options.addElement("-verbose");
        }
        if (debugscanner){
            options.addElement("-ds");
        }
        if (debugparser){
            options.addElement("-dp");
        }
        if (classpath != null){
            options.addElement("-classpath");
            options.addElement(classpath.toString());
        }
        if (sourcepath != null){
            options.addElement("-sourcepath");
            options.addElement(sourcepath.toString());
        }
        options.addElement(target.getAbsolutePath());
        
        String[] array = new String[options.size()];
        options.copyInto(array);
        return array;
    }
    
    /**
     * write all options to a file with one option / line
     * @param tofile the file to write the options to.
     * @param options the array of options element to write to the file.
     * @throws BuildException thrown if there is a problem while writing
     * to the file.
     */
    protected void generateOptionsFile(File tofile, String[] options) throws BuildException {
        FileWriter fw = null;
        try {
            fw = new FileWriter(tofile);
            PrintWriter pw = new PrintWriter(fw);
            for (int i = 0; i < options.length; i++){
                pw.println( options[i] );
            }
            pw.flush();
        } catch (IOException e){
            throw new BuildException("Error while writing options file " + tofile, e);
        } finally {
            if (fw != null){
                try {
                    fw.close();
                } catch (IOException ignored){}
            }
        }
    }
    
    /** create a temporary file in the current directory */
    protected final static File createTmpFile(){
        final long rand = (new Random(System.currentTimeMillis())).nextLong();
        File file = new File("metamata" + rand + ".tmp");
        return file;
    }

}
