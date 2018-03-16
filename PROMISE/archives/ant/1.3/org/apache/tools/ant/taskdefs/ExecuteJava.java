package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.Path;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.*;

/*
 *
 * @author thomas.haas@softwired-inc.com
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class ExecuteJava {

    private Commandline javaCommand = null;
    private Path classpath = null;
    private CommandlineJava.SysProperties sysProperties = null;
    private PrintStream out;

    public void setJavaCommand(Commandline javaCommand) {
        this.javaCommand = javaCommand;
    }

    public void setClasspath(Path p) {
        classpath = p;
    }

    public void setSystemProperties(CommandlineJava.SysProperties s) {
        sysProperties = s;
    }

    /**
     * All output (System.out as well as System.err) will be written
     * to this Stream.
     */
    public void setOutput(PrintStream out) {
        this.out = out;
    }

    public void execute(Project project) throws BuildException{
        PrintStream sOut = System.out;
        PrintStream sErr = System.err;

        final String classname = javaCommand.getExecutable();
        final Object[] argument = { javaCommand.getArguments() };
        try {
            if (sysProperties != null) {
                sysProperties.setSystem();
            }

            if (out != null) {
                System.setErr(out);
                System.setOut(out);
            }

            final Class[] param = { Class.forName("[Ljava.lang.String;") };
            Class target = null;
            if (classpath == null) {
                target = Class.forName(classname);
            } else {
                AntClassLoader loader = new AntClassLoader(project, classpath, false);
                loader.setIsolated(true);
                target = loader.forceLoadClass(classname);
            }
            final Method main = target.getMethod("main", param);
            main.invoke(null, argument);

        } catch (NullPointerException e) {
            throw new BuildException("Could not find main() method in " + classname);
        } catch (ClassNotFoundException e) {
            throw new BuildException("Could not find " + classname + ". Make sure you have it in your classpath");
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            if (!(t instanceof SecurityException)) {
                throw new BuildException(t);
            }
        } catch (Exception e) {
            throw new BuildException(e);
        } finally {
            if (sysProperties != null) {
                sysProperties.restoreSystem();
            }
            if (out != null) {
                System.setOut(sOut);
                System.setErr(sErr);
                out.close();
            }
        }
    }
}
