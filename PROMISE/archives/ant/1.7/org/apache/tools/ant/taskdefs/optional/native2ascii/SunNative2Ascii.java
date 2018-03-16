package org.apache.tools.ant.taskdefs.optional.native2ascii;

import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.taskdefs.optional.Native2Ascii;
import org.apache.tools.ant.types.Commandline;

/**
 * Adapter to sun.tools.native2ascii.Main.
 *
 * @since Ant 1.6.3
 */
public final class SunNative2Ascii extends DefaultNative2Ascii {

    /**
     * Identifies this adapter.
     */
    public static final String IMPLEMENTATION_NAME = "sun";

    /** {@inheritDoc} */
    protected void setup(Commandline cmd, Native2Ascii args)
        throws BuildException {
        if (args.getReverse()) {
            cmd.createArgument().setValue("-reverse");
        }
        super.setup(cmd, args);
    }

    /** {@inheritDoc} */
    protected boolean run(Commandline cmd, ProjectComponent log)
        throws BuildException {
        try {
            Class n2aMain = Class.forName("sun.tools.native2ascii.Main");
            Class[] param = new Class[] {String[].class};
            Method convert = n2aMain.getMethod("convert", param);
            if (convert == null) {
                throw new BuildException("Could not find convert() method in "
                                         + "sun.tools.native2ascii.Main");
            }
            Object o = n2aMain.newInstance();
            return ((Boolean) convert.invoke(o,
                                             new Object[] {cmd.getArguments()})
                    ).booleanValue();
        } catch (BuildException ex) {
            throw ex;
        } catch (Exception ex) {
           throw new BuildException("Error starting Sun's native2ascii: ", ex);
        }
    }
}
