package org.apache.commons.logging.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.LogFactory;

/**
 * This class is capable of receiving notifications about the undeployment of
 * a webapp, and responds by ensuring that commons-logging releases all
 * memory associated with the undeployed webapp.
 * <p>
 * In general, the WeakHashtable support added in commons-logging release 1.1
 * ensures that logging classes do not hold references that prevent an
 * undeployed webapp's memory from being garbage-collected even when multiple
 * copies of commons-logging are deployed via multiple classloaders (a
 * situation that earlier versions had problems with). However there are
 * some rare cases where the WeakHashtable approach does not work; in these
 * situations specifying this class as a listener for the web application will
 * ensure that all references held by commons-logging are fully released.
 * <p>
 * To use this class, configure the webapp deployment descriptor to call
 * this class on webapp undeploy; the contextDestroyed method will tell
 * every accessible LogFactory class that the entry in its map for the
 * current webapp's context classloader should be cleared.
 *
 * @version $Id: ServletContextCleaner.java 1432580 2013-01-13 10:41:05Z tn $
 * @since 1.1
 */
public class ServletContextCleaner implements ServletContextListener {

    private static final Class[] RELEASE_SIGNATURE = {ClassLoader.class};

    /**
     * Invoked when a webapp is undeployed, this tells the LogFactory
     * class to release any logging information related to the current
     * contextClassloader.
     */
    public void contextDestroyed(ServletContextEvent sce) {
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();

        Object[] params = new Object[1];
        params[0] = tccl;

        ClassLoader loader = tccl;
        while (loader != null) {
            try {
                Class logFactoryClass = loader.loadClass("org.apache.commons.logging.LogFactory");
                Method releaseMethod = logFactoryClass.getMethod("release", RELEASE_SIGNATURE);
                releaseMethod.invoke(null, params);
                loader = logFactoryClass.getClassLoader().getParent();
            } catch(ClassNotFoundException ex) {
                loader = null;
            } catch(NoSuchMethodException ex) {
                System.err.println("LogFactory instance found which does not support release method!");
                loader = null;
            } catch(IllegalAccessException ex) {
                System.err.println("LogFactory instance found which is not accessable!");
                loader = null;
            } catch(InvocationTargetException ex) {
                System.err.println("LogFactory instance release method failed!");
                loader = null;
            }
        }

        LogFactory.release(tccl);
    }

    /**
     * Invoked when a webapp is deployed. Nothing needs to be done here.
     */
    public void contextInitialized(ServletContextEvent sce) {
    }
}
