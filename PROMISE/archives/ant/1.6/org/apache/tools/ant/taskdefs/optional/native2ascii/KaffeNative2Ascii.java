package org.apache.tools.ant.taskdefs.optional.native2ascii;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.taskdefs.ExecuteJava;
import org.apache.tools.ant.taskdefs.optional.Native2Ascii;
import org.apache.tools.ant.types.Commandline;

/**
 * Adapter to kaffe.tools.native2ascii.Native2Ascii.
 *
 * @since Ant 1.6.3
 */
public final class KaffeNative2Ascii extends DefaultNative2Ascii {

    private static final String[] N2A_CLASSNAMES = new String[] {
        "gnu.classpath.tools.native2ascii.Native2Ascii",
        "kaffe.tools.native2ascii.Native2Ascii",
    };

    /**
     * Identifies this adapter.
     */
    public static final String IMPLEMENTATION_NAME = "kaffe";

    protected void setup(Commandline cmd, Native2Ascii args)
        throws BuildException {
        if (args.getReverse()) {
            throw new BuildException("-reverse is not supported by Kaffe");
        }
        super.setup(cmd, args);
    }

    protected boolean run(Commandline cmd, ProjectComponent log)
        throws BuildException {
        ExecuteJava ej = new ExecuteJava();
        Class c = getN2aClass();
        if (c == null) {
            throw new BuildException("Couldn't load Kaffe's Native2Ascii"
                                     + " class");
        }

        cmd.setExecutable(c.getName());
        ej.setJavaCommand(cmd);
        ej.execute(log.getProject());
        return true;
    }

    /**
     * tries to load Kaffe Native2Ascii and falls back to the older
     * class name if necessary.
     *
     * @return null if neither class can get loaded.
     */
    private static Class getN2aClass() {
        for (int i = 0; i < N2A_CLASSNAMES.length; i++) {
            try {
                return Class.forName(N2A_CLASSNAMES[i]);
            } catch (ClassNotFoundException cnfe) {
            }
        }
        return null;
    }

}
