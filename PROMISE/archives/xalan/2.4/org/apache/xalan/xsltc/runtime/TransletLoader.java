package org.apache.xalan.xsltc.runtime;

import java.lang.Class;
import java.lang.ClassLoader;
import java.lang.Thread;


/**
 * This class is intended used when the default Class.forName() method fails.
 * This method will fail if XSLTC is installed in a jar-file under the
 * $JAVA_HOME/jre/lib/ext directory. This is because the extensions class
 * loader is used instead of the bootstrap class loader, and that the
 * extensions class loader does not load classes for the default class path.
 * But, if the extensions class loader is being used, then we know two things:
 *  (1) XSLTC is running on Java 1.2 or later (when extensions were introduced)
 *  (2) XSLTC has access to the ClassLoader.getSystemClassLoader() method
 * This class takes advantage of this and uses a privileged call to this
 * method to get a reference to the bootstrap class loader. It then uses this
 * class loader to load the desired class.
 *
 * Note that this class should only be _instanciated_ if Class.forName() fails.
 * And, YES, I do mean _instanciated_, and not called. By instanciating this
 * class on Java 1.1 you'll get a NoSuchMethodException.
 */
final public class TransletLoader {
    

    /**
     * Create a translet loader.
     * Get a handle to the system class loader
     */
    public TransletLoader() {
	ClassLoader loader = Thread.currentThread().getContextClassLoader();

	final String loaderName = loader.getClass().getName();
	if (loaderName.equals("sun.misc.Launcher$ExtClassLoader")) {
	    loader = ClassLoader.getSystemClassLoader();
	}
	_loader = loader;
    }

    /**
     * Loads a Class definition, but does not run static initializers
     */
    public Class loadClass(String name) throws ClassNotFoundException {
	return(Class.forName(name, false, _loader));
    }

    /**
     * Loads a Class definition and runs static initializers.
     */
    public Class loadTranslet(String name) throws ClassNotFoundException {
	return(Class.forName(name, true, _loader));
    }
}
