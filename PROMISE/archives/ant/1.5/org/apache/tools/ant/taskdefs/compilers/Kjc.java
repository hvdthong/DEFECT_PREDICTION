package org.apache.tools.ant.taskdefs.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;



import java.lang.reflect.Method;

/**
 * The implementation of the Java compiler for KJC.
 * This is primarily a cut-and-paste from Jikes.java and
 * DefaultCompilerAdapter.
 *
 * @author <a href="mailto:tora@debian.org">Takashi Okamoto</a> 
 * @since Ant 1.4
 */
public class Kjc extends DefaultCompilerAdapter {

    /**
     * Run the compilation.
     *
     * @exception BuildException if the compilation has problems.
     */
    public boolean execute() throws BuildException {
        attributes.log("Using kjc compiler", Project.MSG_VERBOSE);
        Commandline cmd = setupKjcCommand();

        try {
            Class c = Class.forName("at.dms.kjc.Main");

            Method compile = c.getMethod("compile",
                                         new Class [] { String [].class });
            Boolean ok = 
                (Boolean) compile.invoke(null, 
                                        new Object[] {cmd.getArguments()});
            return ok.booleanValue();
        } catch (ClassNotFoundException ex) {
            throw new BuildException("Cannot use kjc compiler, as it is not "
                                     + "available. A common solution is to "
                                     + "set the environment variable CLASSPATH "
                                     + "to your kjc archive (kjc.jar).", 
                                     location);
        } catch (Exception ex) {
            if (ex instanceof BuildException) {
                throw (BuildException) ex;
            } else {
                throw new BuildException("Error starting kjc compiler: ", 
                                         ex, location);
            }
        }
    }

    /**
     * setup kjc command arguments.
     */
    protected Commandline setupKjcCommand() {
        Commandline cmd = new Commandline();

        Path classpath = getCompileClasspath();

        if (deprecation == true) {
            cmd.createArgument().setValue("-deprecation");
        }

        if (destDir != null) {
            cmd.createArgument().setValue("-d");
            cmd.createArgument().setFile(destDir);
        }

        cmd.createArgument().setValue("-classpath");

        Path cp = new Path(project);

        if (bootclasspath != null) {
            cp.append(bootclasspath);
        }
        
        if (extdirs != null) {
            cp.addExtdirs(extdirs);
        }
        
        cp.append(classpath);
        if (compileSourcepath != null) {
            cp.append(compileSourcepath);
        } else {
            cp.append(src);
        }        

        cmd.createArgument().setPath(cp);
        
        if (encoding != null) {
            cmd.createArgument().setValue("-encoding");
            cmd.createArgument().setValue(encoding);
        }
        
        if (debug) {
            cmd.createArgument().setValue("-g");
        }
        
        if (optimize) {
            cmd.createArgument().setValue("-O2");
        }

        if (verbose) {
            cmd.createArgument().setValue("-verbose");
        }

        addCurrentCompilerArgs(cmd);

        logAndAddFilesToCompile(cmd);
        return cmd;
    }
}


