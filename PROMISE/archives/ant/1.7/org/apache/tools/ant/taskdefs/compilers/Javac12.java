package org.apache.tools.ant.taskdefs.compilers;

import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.JavaEnvUtils;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.types.Commandline;

/**
 * The implementation of the javac compiler for JDK 1.2
 * This is primarily a cut-and-paste from the original javac task before it
 * was refactored.
 *
 * @since Ant 1.3
 */
public class Javac12 extends DefaultCompilerAdapter {
    protected static final String CLASSIC_COMPILER_CLASSNAME = "sun.tools.javac.Main";

    /**
     * Run the compilation.
     * @return true if the compiler ran with a zero exit result (ok)
     * @exception BuildException if the compilation has problems.
     */
    public boolean execute() throws BuildException {
        attributes.log("Using classic compiler", Project.MSG_VERBOSE);
        Commandline cmd = setupJavacCommand(true);

        OutputStream logstr = new LogOutputStream(attributes, Project.MSG_WARN);
        try {
            Class c = Class.forName(CLASSIC_COMPILER_CLASSNAME);
            Constructor cons =
                c.getConstructor(new Class[] {OutputStream.class,
                                              String.class});
            Object compiler
                = cons.newInstance(new Object[] {logstr, "javac"});

            Method compile = c.getMethod("compile",
                                         new Class [] {String[].class});
            Boolean ok =
                (Boolean) compile.invoke(compiler,
                                        new Object[] {cmd.getArguments()});
            return ok.booleanValue();
        } catch (ClassNotFoundException ex) {
            throw new BuildException("Cannot use classic compiler , as it is "
                                        + "not available. \n"
                                        + " A common solution is "
                                        + "to set the environment variable"
                                        + " JAVA_HOME to your jdk directory.\n"
                                        + "It is currently set to \""
                                        + JavaEnvUtils.getJavaHome()
                                        + "\"",
                                        location);
        } catch (Exception ex) {
            if (ex instanceof BuildException) {
                throw (BuildException) ex;
            } else {
                throw new BuildException("Error starting classic compiler: ",
                                         ex, location);
            }
        } finally {
            FileUtils.close(logstr);
        }
    }
}
