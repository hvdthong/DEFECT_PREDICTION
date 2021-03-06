package org.apache.tools.ant.taskdefs.compilers;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.JavaEnvUtils;

/**
 * Creates the necessary compiler adapter, given basic criteria.
 *
 * @author <a href="mailto:jayglanville@home.com">J D Glanville</a>
 * @since Ant 1.3
 */
public class CompilerAdapterFactory {

    /** This is a singleton -- can't create instances!! */
    private CompilerAdapterFactory() {
    }

    /**
     * Based on the parameter passed in, this method creates the necessary
     * factory desired.
     *
     * The current mapping for compiler names are as follows:
     * <ul><li>jikes = jikes compiler
     * <li>classic, javac1.1, javac1.2 = the standard compiler from JDK
     * 1.1/1.2
     * <li>modern, javac1.3, javac1.4 = the compiler of JDK 1.3+
     * <li>jvc, microsoft = the command line compiler from Microsoft's SDK
     * for Java / Visual J++
     * <li>kjc = the kopi compiler</li>
     * <li>gcj = the gcj compiler from gcc</li>
     * <li>sj, symantec = the Symantec Java compiler</li>
     * <li><i>a fully quallified classname</i> = the name of a compiler
     * adapter
     * </ul>
     *
     * @param compilerType either the name of the desired compiler, or the
     * full classname of the compiler's adapter.
     * @param task a task to log through.
     * @throws BuildException if the compiler type could not be resolved into
     * a compiler adapter.
     */
    public static CompilerAdapter getCompiler(String compilerType, Task task) 
        throws BuildException {
            boolean isClassicCompilerSupported = true;
            if (JavaEnvUtils.isJavaVersion(JavaEnvUtils.JAVA_1_4)) {
                isClassicCompilerSupported = false;
            }

            if (compilerType.equalsIgnoreCase("jikes")) {
                return new Jikes();
            }
            if (compilerType.equalsIgnoreCase("extJavac")) {
                return new JavacExternal();
            }       
            if (compilerType.equalsIgnoreCase("classic") ||
                compilerType.equalsIgnoreCase("javac1.1") ||
                compilerType.equalsIgnoreCase("javac1.2")) {
                if (isClassicCompilerSupported) {
                    return new Javac12();
                } else {
                    task.log("This version of java does "
                                             + "not support the classic "
                                             + "compiler; upgrading to modern",
                                             Project.MSG_WARN);
                    compilerType="modern";
                }
            }
            if (compilerType.equalsIgnoreCase("modern") ||
                compilerType.equalsIgnoreCase("javac1.3") ||
                compilerType.equalsIgnoreCase("javac1.4")) {
                if (doesModernCompilerExist()) {
                    return new Javac13();
                } else {
                    if (isClassicCompilerSupported) {
                        task.log("Modern compiler not found - looking for "
                                 + "classic compiler", Project.MSG_WARN);
                        return new Javac12();
                    } else {
                        throw new BuildException("Unable to find a javac " 
                                                 + "compiler;\n"
                                                 + "com.sun.tools.javac.Main "
                                                 + "is not on the " 
                                                 + "classpath.\n"
                                                 + "Perhaps JAVA_HOME does not"
                                                 + " point to the JDK");
                    }
                }
            }

            if (compilerType.equalsIgnoreCase("jvc") ||
                compilerType.equalsIgnoreCase("microsoft")) {
                return new Jvc();
            }
            if (compilerType.equalsIgnoreCase("kjc")) {
                return new Kjc();
            }
            if (compilerType.equalsIgnoreCase("gcj")) {
                return new Gcj();
            }
            if (compilerType.equalsIgnoreCase("sj") ||
                compilerType.equalsIgnoreCase("symantec")) {
                return new Sj();
            }
            return resolveClassName(compilerType);
        }

    /**
     * query for the Modern compiler existing
     * @return true iff classic os on the classpath
     */ 
    private static boolean doesModernCompilerExist() {
        try {
            Class.forName("com.sun.tools.javac.Main");
            return true;
        } catch (ClassNotFoundException cnfe) {
            return false;
        }
    }
    
    /**
     * Tries to resolve the given classname into a compiler adapter.
     * Throws a fit if it can't.
     *
     * @param className The fully qualified classname to be created.
     * @throws BuildException This is the fit that is thrown if className
     * isn't an instance of CompilerAdapter.
     */
    private static CompilerAdapter resolveClassName(String className)
        throws BuildException {
        try {
            Class c = Class.forName(className);
            Object o = c.newInstance();
            return (CompilerAdapter) o;
        } catch (ClassNotFoundException cnfe) {
            throw new BuildException("Compiler Adapter '"+className 
                    + "' can\'t be found.", cnfe);
        } catch (ClassCastException cce) {
            throw new BuildException(className + " isn\'t the classname of "
                    + "a compiler adapter.", cce);
        } catch (Throwable t) {
            throw new BuildException("Compiler Adapter "+className 
                    + " caused an interesting exception.", t);
        }
    }

}
