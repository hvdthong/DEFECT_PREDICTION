package org.apache.tools.ant.taskdefs.optional.javah;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.util.JavaEnvUtils;

/**
 * Creates the JavahAdapter based on the user choice and
 * potentially the VM vendor.
 *
 * @since Ant 1.6.3
 */
public class JavahAdapterFactory {

    /**
     * Determines the default choice of adapter based on the VM
     * vendor.
     *
     * @return the default choice of adapter based on the VM
     * vendor
     */
    public static String getDefault() {
        if (JavaEnvUtils.isKaffe()) {
            return Kaffeh.IMPLEMENTATION_NAME;
        }
        return SunJavah.IMPLEMENTATION_NAME;
    }

    /**
     * Creates the JavahAdapter based on the user choice and
     * potentially the VM vendor.
     *
     * @param choice the user choice (if any).
     * @param log a ProjectComponent instance used to access Ant's
     * logging system.
     * @return The adapter to use.
     */
    public static JavahAdapter getAdapter(String choice,
                                          ProjectComponent log)
        throws BuildException {
        if ((JavaEnvUtils.isKaffe() && choice == null)
            || Kaffeh.IMPLEMENTATION_NAME.equals(choice)) {
            return new Kaffeh();
        } else if (SunJavah.IMPLEMENTATION_NAME.equals(choice)) {
            return new SunJavah();
        } else if (choice != null) {
            return resolveClassName(choice);
        }

        return new SunJavah();
    }

    /**
     * Tries to resolve the given classname into a native2ascii adapter.
     * Throws a fit if it can't.
     *
     * @param className The fully qualified classname to be created.
     * @throws BuildException This is the fit that is thrown if className
     * isn't an instance of JavahAdapter.
     */
    private static JavahAdapter resolveClassName(String className)
        throws BuildException {
        try {
            Class c = Class.forName(className);
            Object o = c.newInstance();
            return (JavahAdapter) o;
        } catch (ClassNotFoundException cnfe) {
            throw new BuildException("Can't load " + className, cnfe);
        } catch (ClassCastException cce) {
            throw new BuildException(className 
                                     + " is not a Javah adapter", cce);
        } catch (Throwable t) {
            throw new BuildException(className + " caused an interesting "
                                     + "exception.", t);
        }
    }
}
