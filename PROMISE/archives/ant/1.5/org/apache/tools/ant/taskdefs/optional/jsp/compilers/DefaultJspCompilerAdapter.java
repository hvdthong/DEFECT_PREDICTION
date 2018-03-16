package org.apache.tools.ant.taskdefs.optional.jsp.compilers;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.optional.jsp.JspC;
import org.apache.tools.ant.types.CommandlineJava;

/**
 * This is the default implementation for the JspCompilerAdapter interface.
 * This is currently very light on the ground since only one compiler type is
 * supported.
 *
 * @author Matthew Watson <a href="mailto:mattw@i3sp.com">mattw@i3sp.com</a>
 */
public abstract class DefaultJspCompilerAdapter
    implements JspCompilerAdapter {

    private static String lSep = System.getProperty("line.separator");

    /**
     * Logs the compilation parameters, adds the files to compile and logs the 
     * &quot;niceSourceList&quot;
     */
    protected void logAndAddFilesToCompile(JspC jspc,
                                           Vector compileList,
                                           CommandlineJava cmd) {
        jspc.log("Compilation " + cmd.describeJavaCommand(),
                 Project.MSG_VERBOSE);

        StringBuffer niceSourceList = new StringBuffer("File");
        if (compileList.size() != 1) {
            niceSourceList.append("s");
        }
        niceSourceList.append(" to be compiled:");

        niceSourceList.append(lSep);

        Enumeration enum = compileList.elements();
        while (enum.hasMoreElements()) {
            String arg = (String) enum.nextElement();
            cmd.createArgument().setValue(arg);
            niceSourceList.append("    " + arg + lSep);
        }

        jspc.log(niceSourceList.toString(), Project.MSG_VERBOSE);
    }

    /**
     * our owner
     */
    protected JspC owner;

    /**
     * set the owner
     */
    public void setJspc(JspC owner) {
        this.owner = owner;
    }

    /** get the owner
     * @return the owner; should never be null
     */
    public JspC getJspc() {
        return owner;
    }

    
    /**
     *  add an argument oneple to the argument list, if the value aint null
     *
     * @param  argument  The argument
     */
    protected void addArg(CommandlineJava cmd, String argument) {
        if (argument != null && argument.length() != 0) {
           cmd.createArgument().setValue(argument);
        }
    }


    /**
     *  add an argument tuple to the argument list, if the value aint null
     *
     * @param  argument  The argument
     * @param  value     the parameter
     */
    protected void addArg(CommandlineJava cmd, String argument, String value) {
        if (value != null) {
            cmd.createArgument().setValue(argument);
            cmd.createArgument().setValue(value);
        }
    }

    /**
     *  add an argument tuple to the arg list, if the file parameter aint null
     *
     * @param  argument  The argument
     * @param  file     the parameter
     */
    protected void addArg(CommandlineJava cmd, String argument, File file) {
        if (file != null) {
            cmd.createArgument().setValue(argument);
            cmd.createArgument().setFile(file);
        }
    }

    /**
     * ask if compiler can sort out its own dependencies
     * @return true if the compiler wants to do its own
     * depends
     */
    public boolean implementsOwnDependencyChecking() {
        return false;
    }

    /**
     * get our project
     * @return owner project data
     */
    public Project getProject() {
        return getJspc().getProject();
    }
}

