package org.apache.tools.ant.taskdefs.optional.native2ascii;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.util.ClasspathUtils;
import org.apache.tools.ant.util.JavaEnvUtils;

/**
 * Creates the Native2AsciiAdapter based on the user choice and
 * potentially the VM vendor.
 *
 * @since Ant 1.6.3
 */
public class Native2AsciiAdapterFactory {

    /**
     * Determines the default choice of adapter based on the VM
     * vendor.
     *
     * @return the default choice of adapter based on the VM
     * vendor
     */
    public static String getDefault() {
        if (JavaEnvUtils.isKaffe()) {
            return KaffeNative2Ascii.IMPLEMENTATION_NAME;
        }
        return SunNative2Ascii.IMPLEMENTATION_NAME;
    }

    /**
     * Creates the Native2AsciiAdapter based on the user choice and *
     * potentially the VM vendor.
     *
     * @param choice the user choice (if any).
     * @param log a ProjectComponent instance used to access Ant's
     * logging system.
     * @return The adapter to use.
     * @throws BuildException if there was a problem.
     */
    public static Native2AsciiAdapter getAdapter(String choice,
                                                 ProjectComponent log)
        throws BuildException {
        if ((JavaEnvUtils.isKaffe() && choice == null)
            || KaffeNative2Ascii.IMPLEMENTATION_NAME.equals(choice)) {
            return new KaffeNative2Ascii();
        } else if (SunNative2Ascii.IMPLEMENTATION_NAME.equals(choice)) {
            return new SunNative2Ascii();
        } else if (choice != null) {
            return resolveClassName(choice);
        }

        return new SunNative2Ascii();
    }

    /**
     * Tries to resolve the given classname into a native2ascii adapter.
     * Throws a fit if it can't.
     *
     * @param className The fully qualified classname to be created.
     * @throws BuildException This is the fit that is thrown if className
     * isn't an instance of Native2AsciiAdapter.
     */
    private static Native2AsciiAdapter resolveClassName(String className)
        throws BuildException {
        return (Native2AsciiAdapter) ClasspathUtils.newInstance(className,
            Native2AsciiAdapterFactory.class.getClassLoader(),
            Native2AsciiAdapter.class);
    }
}
