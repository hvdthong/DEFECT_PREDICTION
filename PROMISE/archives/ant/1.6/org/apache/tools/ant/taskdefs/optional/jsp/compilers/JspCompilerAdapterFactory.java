package org.apache.tools.ant.taskdefs.optional.jsp.compilers;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.optional.jsp.JspNameMangler;
import org.apache.tools.ant.taskdefs.optional.jsp.Jasper41Mangler;


/**
 * Creates the necessary compiler adapter, given basic criteria.
 *
 */
public class JspCompilerAdapterFactory {

    /** This is a singleton -- can't create instances!! */
    private JspCompilerAdapterFactory() {
    }

    /**
     * Based on the parameter passed in, this method creates the necessary
     * factory desired.
     *
     * The current mapping for compiler names are as follows:
     * <ul><li>jasper = jasper compiler (the default)
     * <li><i>a fully quallified classname</i> = the name of a jsp compiler
     * adapter
     * </ul>
     *
     * @param compilerType either the name of the desired compiler, or the
     * full classname of the compiler's adapter.
     * @param task a task to log through.
     * @throws BuildException if the compiler type could not be resolved into
     * a compiler adapter.
     */
    public static JspCompilerAdapter getCompiler(String compilerType, Task task)
        throws BuildException {
        return getCompiler(compilerType, task,
                           task.getProject().createClassLoader(null));
    }

    /**
     * Based on the parameter passed in, this method creates the necessary
     * factory desired.
     *
     * The current mapping for compiler names are as follows:
     * <ul><li>jasper = jasper compiler (the default)
     * <li><i>a fully quallified classname</i> = the name of a jsp compiler
     * adapter
     * </ul>
     *
     * @param compilerType either the name of the desired compiler, or the
     * full classname of the compiler's adapter.
     * @param task a task to log through.
     * @param loader AntClassLoader with which the compiler should be loaded
     * @throws BuildException if the compiler type could not be resolved into
     * a compiler adapter.
     */
    public static JspCompilerAdapter getCompiler(String compilerType, Task task,
                                                 AntClassLoader loader)
        throws BuildException {

        if (compilerType.equalsIgnoreCase("jasper")) {
            return new JasperC(new JspNameMangler());
        }
        if (compilerType.equalsIgnoreCase("jasper41")) {
            return new JasperC(new Jasper41Mangler());
        }
        return resolveClassName(compilerType, loader);
    }

    /**
     * Tries to resolve the given classname into a compiler adapter.
     * Throws a fit if it can't.
     *
     * @param className The fully qualified classname to be created.
     * @param classloader Classloader with which to load the class
     * @throws BuildException This is the fit that is thrown if className
     * isn't an instance of JspCompilerAdapter.
     */
    private static JspCompilerAdapter resolveClassName(String className,
                                                       AntClassLoader classloader)
        throws BuildException {
        try {
            Class c = classloader.findClass(className);
            Object o = c.newInstance();
            return (JspCompilerAdapter) o;
        } catch (ClassNotFoundException cnfe) {
            throw new BuildException(className + " can\'t be found.", cnfe);
        } catch (ClassCastException cce) {
            throw new BuildException(className + " isn\'t the classname of "
                                     + "a compiler adapter.", cce);
        } catch (Throwable t) {
            throw new BuildException(className + " caused an interesting "
                                     + "exception.", t);
        }
    }

}
