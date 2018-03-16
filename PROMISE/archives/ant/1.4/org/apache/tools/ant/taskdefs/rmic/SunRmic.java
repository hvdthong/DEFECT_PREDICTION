package org.apache.tools.ant.taskdefs.rmic;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.types.Commandline;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * The implementation of the rmic for SUN's JDK.
 *
 * @author Takashi Okamoto <tokamoto@rd.nttdata.co.jp>
 */
public class SunRmic extends DefaultRmicAdapter {

    public boolean execute() throws BuildException {
        getRmic().log("Using SUN rmic compiler", Project.MSG_VERBOSE);
        Commandline cmd = setupRmicCommand();

        LogOutputStream logstr = new LogOutputStream(getRmic(), Project.MSG_WARN);

        try {
            Class c = Class.forName("sun.rmi.rmic.Main");
            Constructor cons = c.getConstructor(new Class[] 
                { OutputStream.class, String.class });
            Object rmic = cons.newInstance(new Object[] { logstr, "rmic" });

            Method doRmic = c.getMethod("compile", 
                                        new Class [] { String[].class });
            Boolean ok = (Boolean)doRmic.invoke(rmic, 
                                                (new Object[] {cmd.getArguments()} ));
            return ok.booleanValue();
        } catch (ClassNotFoundException ex) {
            throw new BuildException("Cannot use SUN rmic, as it is not available"+
                                     " A common solution is to set the environment variable"+
                                     " JAVA_HOME or CLASSPATH.", getRmic().getLocation() );
        }
        catch (Exception ex) {
            if (ex instanceof BuildException) {
                throw (BuildException) ex;
            } else {
                throw new BuildException("Error starting SUN rmic: ", ex, getRmic().getLocation());
            }
        } finally {
            try {
                logstr.close();
            } catch (IOException e) {
                throw new BuildException(e);
            }
        }
    }
}
