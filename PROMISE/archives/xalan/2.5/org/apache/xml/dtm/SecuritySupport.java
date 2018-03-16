package org.apache.xml.dtm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * This class is duplicated for each JAXP subpackage so keep it in sync.
 * It is package private and therefore is not exposed as part of the JAXP
 * API.
 *
 * Base class with security related methods that work on JDK 1.1.
 */
class SecuritySupport {

    /*
     * Make this of type Object so that the verifier won't try to
     * prove its type, thus possibly trying to load the SecuritySupport12
     * class.
     */
    private static final Object securitySupport;

    static {
	SecuritySupport ss = null;
	try {
	    Class c = Class.forName("java.security.AccessController");
	    /*
	    c = Class.forName("javax.mail.SecuritySupport12");
	    Constructor cons = c.getConstructor(new Class[] { });
	    ss = (SecuritySupport)cons.newInstance(new Object[] { });
	    */
	    /*
	     * Unfortunately, we can't load the class using reflection
	     * because the class is package private.  And the class has
	     * to be package private so the APIs aren't exposed to other
	     * code that could use them to circumvent security.  Thus,
	     * we accept the risk that the direct reference might fail
	     * on some JDK 1.1 JVMs, even though we would never execute
	     * this code in such a case.  Sigh...
	     */
	    ss = new SecuritySupport12();
	} catch (Exception ex) {
	} finally {
	    if (ss == null)
		ss = new SecuritySupport();
	    securitySupport = ss;
	}
    }

    /**
     * Return an appropriate instance of this class, depending on whether
     * we're on a JDK 1.1 or J2SE 1.2 (or later) system.
     */
    public static SecuritySupport getInstance() {
	return (SecuritySupport)securitySupport;
    }

    public ClassLoader getContextClassLoader() {
	return null;
    }

    public String getSystemProperty(String propName) {
        return System.getProperty(propName);
    }

    public FileInputStream getFileInputStream(File file)
        throws FileNotFoundException
    {
        return new FileInputStream(file);
    }

    public InputStream getResourceAsStream(ClassLoader cl, String name) {
        InputStream ris;
        if (cl == null) {
            ris = ClassLoader.getSystemResourceAsStream(name);
        } else {
            ris = cl.getResourceAsStream(name);
        }
        return ris;
    }
}
