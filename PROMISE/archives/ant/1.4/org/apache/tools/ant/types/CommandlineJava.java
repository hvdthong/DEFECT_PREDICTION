package org.apache.tools.ant.types;

import java.util.*;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;

/**
 * A representation of a Java command line that is nothing more
 * than a composite of 2 <tt>Commandline</tt>. 1 for the vm/options and
 * 1 for the classname/arguments. It provides specific methods for
 * a java command line.
 *
 * @author thomas.haas@softwired-inc.com
 * @author <a href="sbailliez@apache.org>Stephane Bailliez</a>
 */
public class CommandlineJava implements Cloneable {

    private Commandline vmCommand = new Commandline();
    private Commandline javaCommand = new Commandline();
    private SysProperties sysProperties = new SysProperties();
    private Path classpath = null;
    private String vmVersion;
    private String maxMemory = null;

    /**
     * Indicate whether it will execute a jar file or not, in this case
     * the first vm option must be a -jar and the 'executable' is a jar file.
     */
     private boolean executeJar  = false;

    /**
     * Specialized Environment class for System properties
     */
    public static class SysProperties extends Environment implements Cloneable {
        Properties sys = null;

        public String[] getVariables() throws BuildException {
            String props[] = super.getVariables();
      
            if (props == null) return null;

            for (int i = 0; i < props.length; i++) {
                props[i] = "-D" + props[i];
            }
            return props;
        }

        public int size() {
            return variables.size();
        }

        public void setSystem() throws BuildException {
            try {
                Properties p = new Properties(sys = System.getProperties());
        
                for (Enumeration e = variables.elements(); e.hasMoreElements(); ) {
                    Environment.Variable v = (Environment.Variable) e.nextElement();
                    p.put(v.getKey(), v.getValue());
                }
                System.setProperties(p);
            } catch (SecurityException e) {
                throw new BuildException("Cannot modify system properties", e);
            }
        }

        public void restoreSystem() throws BuildException {
            if (sys == null)
                throw new BuildException("Unbalanced nesting of SysProperties");

            try {
                System.setProperties(sys);
                sys = null;
            } catch (SecurityException e) {
                throw new BuildException("Cannot modify system properties", e);
            }
        }

        public Object clone() {
            try {
                SysProperties c = (SysProperties) super.clone();
                c.variables = (Vector) variables.clone();
                return c;
            } catch(CloneNotSupportedException e){return null;}
        }

    }

    public CommandlineJava() {
        setVm(getJavaExecutableName());
        setVmversion(Project.getJavaVersion());
    }

    public Commandline.Argument createArgument() {
        return javaCommand.createArgument();
    }

    public Commandline.Argument createVmArgument() {
        return vmCommand.createArgument();
    }

    public void addSysproperty(Environment.Variable sysp) {
        sysProperties.addVariable(sysp);
    }

    public void setVm(String vm) {
        vmCommand.setExecutable(vm);
    }

    public void setVmversion(String value) {
        vmVersion = value;
    }

    /**
     * set a jar file to execute via the -jar option.
     * @param the pathname of the jar to execute
     */
    public void setJar(String jarpathname){
        javaCommand.setExecutable(jarpathname);
        executeJar = true;
    }

    /**
     * @return the pathname of the jar file to run via -jar option
     * or <tt>null</tt> if there is no jar to run.
     * @see #getClassname()
     */
    public String getJar(){
        if (executeJar){
            return javaCommand.getExecutable();
        }
        return null;
    }

    /**
     * set the classname to execute
     * @param classname the fully qualified classname.
     */
    public void setClassname(String classname) {
        javaCommand.setExecutable(classname);
        executeJar = false;
    }

    /**
     * @return the name of the class to run or <tt>null</tt> if there is no class.
     * @see #getJar()
     */
    public String getClassname() {
        if (!executeJar) {
            return javaCommand.getExecutable();
        }
        return null;
    }

    public Path createClasspath(Project p) {
        if (classpath == null) {
            classpath = new Path(p);
        }
        return classpath;
    }

    public String getVmversion() {
        return vmVersion;
    }

    /**
     * get the command line to run a java vm.
     * @return the list of all arguments necessary to run the vm.
     */
    public String[] getCommandline() {
        String[] result = new String[size()];
        int pos = 0;
        String[] vmArgs = getActualVMCommand().getCommandline();
        result[pos++] = vmArgs[0];
        
        if (executeJar){
            result[pos++] = "-jar";
        }
        System.arraycopy(vmArgs, 1, result, pos, vmArgs.length - 1);
        pos += vmArgs.length - 1;
        if (sysProperties.size() > 0) {
            System.arraycopy(sysProperties.getVariables(), 0,
                             result, pos, sysProperties.size());
            pos += sysProperties.size();
        }
        Path fullClasspath = classpath != null ? classpath.concatSystemClasspath("ignore") : null;
        if (fullClasspath != null && fullClasspath.toString().trim().length() > 0) {
            result[pos++] = "-classpath";
            result[pos++] = fullClasspath.toString();
        }
        System.arraycopy(javaCommand.getCommandline(), 0, 
                         result, pos, javaCommand.size());
        return result;
    }

    /**
     * -mx or -Xmx depending on VM version
     */
    public void setMaxmemory(String max){
        this.maxMemory = max;
    }


    public String toString() {
        return Commandline.toString(getCommandline());
    }

    private Commandline getActualVMCommand() {
        Commandline actualVMCommand = (Commandline)vmCommand.clone();
        if (maxMemory != null) {
            if (vmVersion.startsWith("1.1")) {
                actualVMCommand.createArgument().setValue("-mx" + maxMemory);
            } else {
                actualVMCommand.createArgument().setValue("-Xmx" + maxMemory);
            }
        }
        return actualVMCommand;
    }        

    /**
     * The size of the java command line.
     * @return the total number of arguments in the java command line.
     * @see #getCommandline()
     */
    public int size() {
        int size = getActualVMCommand().size() + javaCommand.size() + sysProperties.size();
        Path fullClasspath = classpath != null ? classpath.concatSystemClasspath("ignore") : null;
        if (fullClasspath != null && fullClasspath.toString().trim().length() > 0) {
            size += 2;
        }
        if (executeJar){
            size++ ;
        }
        return size;
    }

    public Commandline getJavaCommand() {
        return javaCommand;
    }

    public Commandline getVmCommand() {
        return getActualVMCommand();
    }

    public Path getClasspath() {
        return classpath;
    }

    public void setSystemProperties() throws BuildException {
        sysProperties.setSystem();
    }

    public void restoreSystemProperties() throws BuildException {
        sysProperties.restoreSystem();
    }

    public SysProperties getSystemProperties() {
        return sysProperties;
    }

    public Object clone() {
        CommandlineJava c = new CommandlineJava();
        c.vmCommand = (Commandline) vmCommand.clone();
        c.javaCommand = (Commandline) javaCommand.clone();
        c.sysProperties = (SysProperties) sysProperties.clone();
        c.maxMemory = maxMemory;
        if (classpath != null) {
            c.classpath = (Path) classpath.clone();
        }
        c.vmVersion = vmVersion;
        c.executeJar = executeJar;
        return c;
    }

    /**
     * Clear out the java arguments.
     */
    public void clearJavaArgs() {
        javaCommand.clearArgs();
    }

    private String getJavaExecutableName() {
        String os = System.getProperty("os.name").toLowerCase();
        boolean dosBased = 
            os.indexOf("windows") >= 0 || os.indexOf("os/2") >= 0;
	String extension =  dosBased? ".exe" : "";

	java.io.File jExecutable = 
            new java.io.File(System.getProperty("java.home") +
                             "/../bin/java" + extension );

	if (jExecutable.exists()) {
	    return jExecutable.getAbsolutePath();
	} else {
	    return "java";
	}
    }
    
}
