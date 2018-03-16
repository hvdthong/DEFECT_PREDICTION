package org.apache.tools.ant.types;

import java.util.*;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;

/*
 *
 * @author thomas.haas@softwired-inc.com
 */
public class CommandlineJava implements Cloneable {

    private Commandline vmCommand = new Commandline();
    private Commandline javaCommand = new Commandline();
    private SysProperties sysProperties = new SysProperties();
    private Path classpath = null;
    private String vmVersion;

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
        setVm("java");
        setVmversion(org.apache.tools.ant.Project.getJavaVersion());
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

    public void setClassname(String classname) {
        javaCommand.setExecutable(classname);
    }

    public String getClassname() {
        return javaCommand.getExecutable();
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

    public String[] getCommandline() {
        Path fullClasspath = classpath != null ? classpath.concatSystemClasspath("ignore") : null;
        int size = 
            vmCommand.size() + javaCommand.size() + sysProperties.size();
        if (fullClasspath != null && fullClasspath.size() > 0) {
            size += 2;
        }
        
        String[] result = new String[size];
        System.arraycopy(vmCommand.getCommandline(), 0, 
                         result, 0, vmCommand.size());

        int pos = vmCommand.size();
        if (sysProperties.size() > 0) {
            System.arraycopy(sysProperties.getVariables(), 0,
                             result, pos, sysProperties.size());
            pos += sysProperties.size();
        }
        if (fullClasspath != null && fullClasspath.toString().trim().length() > 0) {
            result[pos++] = "-classpath";
            result[pos++] = fullClasspath.toString();
        }
        System.arraycopy(javaCommand.getCommandline(), 0, 
                         result, pos, javaCommand.size());
        return result;
    }


    public String toString() {
        return Commandline.toString(getCommandline());
    }

    public int size() {
        int size = vmCommand.size() + javaCommand.size();
        if (classpath != null && classpath.size() > 0) {
            size += 2;
        }
        return size;
    }

    public Commandline getJavaCommand() {
        return javaCommand;
    }

    public Commandline getVmCommand() {
        return vmCommand;
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
        if (classpath != null) {
            c.classpath = (Path) classpath.clone();
        }
        c.vmVersion = vmVersion;
        return c;
    }

    /**
     * Clear out the java arguments.
     */
    public void clearJavaArgs() {
        javaCommand.clearArgs();
    }

}
