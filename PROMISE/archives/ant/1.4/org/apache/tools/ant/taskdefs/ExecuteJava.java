package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ExitException;
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
     *
     * @deprecated manage output at the task level
     */
    public void setOutput(PrintStream out) {
    }

    public void execute(Project project) throws BuildException{
        final String classname = javaCommand.getExecutable();
        final Object[] argument = { javaCommand.getArguments() };

        AntClassLoader loader = null; 
        try {
            if (sysProperties != null) {
                sysProperties.setSystem();
            }

            final Class[] param = { Class.forName("[Ljava.lang.String;") };
            Class target = null;
            if (classpath == null) {
                target = Class.forName(classname);
            } else {
                loader = new AntClassLoader(project.getCoreLoader(), project, classpath, false);
                loader.setIsolated(true);
                loader.setThreadContextLoader();
                target = loader.forceLoadClass(classname);
                AntClassLoader.initializeClass(target);
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
            else {
                throw (SecurityException)t;
            }
        } catch (Exception e) {
            throw new BuildException(e);
        } finally {
            if (loader != null) {
                loader.resetThreadContextLoader();
                loader.cleanup();
            }
            if (sysProperties != null) {
                sysProperties.restoreSystem();
            }
        }
    }
}
