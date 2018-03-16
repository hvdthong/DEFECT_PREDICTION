package org.apache.xml.dtm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * This class is based on the FactoryFinder classes in the JAXP subpackages
 * in the xml-commons project (xml-apis.jar)
 *
 * This copy of FactoryFinder is for the DTMManager.  It caches the class
 * name after it is found the first time, if the System.property is not set.
 * If the System.property is set, then it is always used.
 * 
 * It does not use context class loaders, but we will probably need to add
 * this support in the future.  Question: If we use context class loaders, can
 * we still cache the class (do we need to also cache the class loader for
 * comparison purposes)?
 * 
 * @author Edwin Goei, Ilene Seelemann
 */
class FactoryFinder {
    /** Controls debugging output to stderr */
    private static boolean debug;
    
   /**
    * Avoid reading all the files when the findFactory
    * method is called the second time (cache the result of
    * finding the default impl).
    */
   private static String foundFactory = null;
   

    static {
        try {
            String val =
                SecuritySupport.getInstance().getSystemProperty("jaxp.debug");
            debug = val != null && (! "false".equals(val));
        } catch (SecurityException se) {
            debug = false;
        }
    }

    /**
     * Main entry point.  Finds and creates a new instance of a concrete
     * factory implementation in the specified order as stated in the JAXP
     * spec.  This code attempts to find a factory implementation in
     * serveral locations.  If one fails, the next one is tried.  To be
     * more robust, this occurs even if a SecurityException is thrown, but
     * perhaps it may be better to propogate the SecurityException instead,
     * so SecurityException-s are not masked.
     *
     * @return A new instance of the concrete factory class, never null
     *
     * @param factoryId
     *        Name of the factory to find, same as a property name
     *
     * @param fallbackClassName
     *        Implementation class name, if nothing else is found.  Use
     *        null to mean not to use a fallback.
     *
     * @throws FactoryFinder.ConfigurationError
     *         If a factory instance cannot be returned
     *
     * Package private so this code can be shared.
     */
    static Object find(String factoryId, String fallbackClassName)
        throws ConfigurationError
    {
        SecuritySupport ss = SecuritySupport.getInstance();
        ClassLoader cl = FactoryFinder.class.getClassLoader();
        dPrint("find factoryId=" + factoryId);

        try {
            String systemProp = ss.getSystemProperty(factoryId);
            if (systemProp != null) {
                dPrint("found system property, value=" + systemProp);
                
                return newInstance(systemProp, cl, true);
            }
            
        } catch (SecurityException se) {
        }

   
        synchronized (FactoryFinder.class) {            
   
            if (foundFactory == null) {
           
               Properties xalanProperties = null;
                try {
                   String javah = ss.getSystemProperty("java.home");
                   String configFile = javah + File.separator +
                        "lib" + File.separator + "xalan.properties";

                   File f = new File(configFile);
                   FileInputStream fis = ss.getFileInputStream(f);
                   xalanProperties = new Properties();
                   xalanProperties.load(fis);
                   fis.close();
                   
               } catch (Exception x) {
               }
               
               if (xalanProperties != null) {            
                   foundFactory = xalanProperties.getProperty(factoryId);
                   if (foundFactory != null) {
                       dPrint("found in xalan.properties, value=" + foundFactory);
                   }
                } else {    
                    findJarServiceProvider(factoryId);
        
                    if (foundFactory == null) {
                        if (fallbackClassName == null) {
                            throw new ConfigurationError(
                            "Provider for " + factoryId + " cannot be found", null);
                        }

                        dPrint("using fallback, value=" + fallbackClassName);
                        foundFactory = fallbackClassName;        
                    }
               }   
            }               
        }
            
        return newInstance(foundFactory, cl, true);
    }

    private static void dPrint(String msg) {
        if (debug) {
            System.err.println("JAXP: " + msg);
        }
    }

    /**
     * Create an instance of a class using the specified ClassLoader and
     * optionally fall back to the current ClassLoader if not found.
     *
     * @param className Name of the concrete class corresponding to the
     * service provider
     *
     * @param cl ClassLoader to use to load the class, null means to use
     * the bootstrap ClassLoader
     *
     * @param doFallback true if the current ClassLoader should be tried as
     * a fallback if the class is not found using cl
     */
    private static Object newInstance(String className, ClassLoader cl,
                                      boolean doFallback)
        throws ConfigurationError
    {

        try {
            Class providerClass;
            if (cl == null) {
                providerClass = Class.forName(className);
            } else {
                try {
                    providerClass = cl.loadClass(className);
                } catch (ClassNotFoundException x) {
                    if (doFallback) {
                        cl = FactoryFinder.class.getClassLoader();
                        providerClass = cl.loadClass(className);
                    } else {
                        throw x;
                    }
                }
            }
            Object instance = providerClass.newInstance();
            dPrint("created new instance of " + providerClass +
                   " using ClassLoader: " + cl);
            return instance;
        } catch (ClassNotFoundException x) {
            throw new ConfigurationError(
                "Provider " + className + " not found", x);
        } catch (Exception x) {
            throw new ConfigurationError(
                "Provider " + className + " could not be instantiated: " + x,
                x);
        }
    }

    /*
     * Try to find provider using Jar Service Provider Mechanism
     *
     * @return instance of provider class if found or null
     */
    private static String findJarServiceProvider(String factoryId)
        throws ConfigurationError
    {
        SecuritySupport ss = SecuritySupport.getInstance();
        String serviceId = "META-INF/services/" + factoryId;
        InputStream is = null;
        ClassLoader cl = FactoryFinder.class.getClassLoader();
        is = ss.getResourceAsStream(cl, serviceId);

        if (is == null) {
            return null;
        }

        dPrint("found jar resource=" + serviceId +
               " using ClassLoader: " + cl);

        BufferedReader rd;
        try {
            rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            rd = new BufferedReader(new InputStreamReader(is));
        }
        
        String factoryClassName = null;
        try {
            factoryClassName = rd.readLine();
            rd.close();
        } catch (IOException x) {
            return null;
        }

        if (factoryClassName != null &&
            ! "".equals(factoryClassName)) {
            dPrint("found in resource, value="
                   + factoryClassName);

            return factoryClassName;
        }

        return null;
    }

    static class ConfigurationError extends Error {
        private Exception exception;

        /**
         * Construct a new instance with the specified detail string and
         * exception.
         */
        ConfigurationError(String msg, Exception x) {
            super(msg);
            this.exception = x;
        }

        Exception getException() {
            return exception;
        }
    }
}
