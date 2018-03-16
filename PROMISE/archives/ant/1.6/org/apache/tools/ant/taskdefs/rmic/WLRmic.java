package org.apache.tools.ant.taskdefs.rmic;

import java.lang.reflect.Method;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Commandline;

/**
 * The implementation of the rmic for WebLogic
 *
 * @since Ant 1.4
 */
public class WLRmic extends DefaultRmicAdapter {

    public boolean execute() throws BuildException {
        getRmic().log("Using WebLogic rmic", Project.MSG_VERBOSE);
        Commandline cmd = setupRmicCommand(new String[] {"-noexit"});

        AntClassLoader loader = null;
        try {
            Class c = null;
            if (getRmic().getClasspath() == null) {
                c = Class.forName("weblogic.rmic");
            } else {
                loader
                    = getRmic().getProject().createClassLoader(getRmic().getClasspath());
                c = Class.forName("weblogic.rmic", true, loader);
            }
            Method doRmic = c.getMethod("main",
                                        new Class [] {String[].class});
            doRmic.invoke(null, new Object[] {cmd.getArguments()});
            return true;
        } catch (ClassNotFoundException ex) {
            throw new BuildException("Cannot use WebLogic rmic, as it is not "
                                     + "available.  A common solution is to "
                                     + "set the environment variable "
                                     + "CLASSPATH.", getRmic().getLocation());
        } catch (Exception ex) {
            if (ex instanceof BuildException) {
                throw (BuildException) ex;
            } else {
                throw new BuildException("Error starting WebLogic rmic: ", ex,
                                         getRmic().getLocation());
            }
        } finally {
            if (loader != null) {
                loader.cleanup();
            }
        }
    }

    /**
     * Get the suffix for the rmic stub classes
     */
    public String getStubClassSuffix() {
        return "_WLStub";
    }

    /**
     * Get the suffix for the rmic skeleton classes
     */
    public String getSkelClassSuffix() {
        return "_WLSkel";
    }
}
