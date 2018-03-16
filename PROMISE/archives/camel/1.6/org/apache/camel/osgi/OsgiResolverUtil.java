package org.apache.camel.osgi;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.apache.camel.util.ResolverUtil;
import org.apache.camel.util.ResolverUtil.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.springframework.osgi.util.BundleDelegatingClassLoader;

public class OsgiResolverUtil extends ResolverUtil {
    private Bundle bundle;
    
    public OsgiResolverUtil(BundleContext context) {
        bundle = context.getBundle();
    }
    
    /**
     * Returns the classloaders that will be used for scanning for classes. 
     * Here we just add BundleDelegatingClassLoader here
     *
     * @return the ClassLoader instances that will be used to scan for classes
     */
    public Set<ClassLoader> getClassLoaders() {
        Set<ClassLoader> classLoaders = super.getClassLoaders();
        ClassLoader osgiLoader = BundleDelegatingClassLoader.createBundleClassLoaderFor(bundle);
        classLoaders.add(osgiLoader);
        return classLoaders;
    }
    
    /**
     * Scans for classes starting at the package provided and descending into
     * subpackages. Each class is offered up to the Test as it is discovered,
     * and if the Test returns true the class is retained. Accumulated classes
     * can be fetched by calling {@link #getClasses()}.
     *
     * @param test        an instance of {@link Test} that will be used to filter
     *                    classes
     * @param packageName the name of the package from which to start scanning
     *                    for classes, e.g. {@code net.sourceforge.stripes}
     */
    public void find(Test test, String packageName) {
        packageName = packageName.replace('.', '/');

        Set<ClassLoader> set = getClassLoaders();

        ClassLoader osgiClassLoader = getOsgiClassLoader(set);

        if (osgiClassLoader != null) {
            LOG.debug("Using only osgi bundle classloader");
            findInOsgiClassLoader(test, packageName, osgiClassLoader);
        } else {
            LOG.debug("Using only regular classloaders");
            for (ClassLoader classLoader : set) {
                if (!isOsgiClassloader(classLoader)) {
                    find(test, packageName, classLoader);
                }
            }
        }
    }

    
    private void findInOsgiClassLoader(Test test, String packageName, ClassLoader osgiClassLoader) {
        try {
            Method mth = osgiClassLoader.getClass().getMethod("getBundle", new Class[]{});
            if (mth != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Loading from osgi buindle using classloader: " + osgiClassLoader);
                }
                loadImplementationsInBundle(test, packageName, osgiClassLoader, mth);
                return;
            }
        } catch (NoSuchMethodException e) {
            LOG.warn("It's not an osgi bundle classloader: " + osgiClassLoader);
            return;
        }
        
    }

    /**
     * Gets the osgi classloader if any in the given set
     */
    private static ClassLoader getOsgiClassLoader(Set<ClassLoader> set) {
        for (ClassLoader loader : set) {
            if (isOsgiClassloader(loader)) {
                return loader;
            }
        }
        return null;
    }

    /**
     * Is it an osgi classloader
     */
    private static boolean isOsgiClassloader(ClassLoader loader) {
        try {
            Method mth = loader.getClass().getMethod("getBundle", new Class[]{});
            if (mth != null) {
                return true;
            }
        } catch (NoSuchMethodException e) {
        }
        return false;
    }
    
    private void loadImplementationsInBundle(Test test, String packageName, ClassLoader loader, Method mth) {
        Set<String> urls = OsgiUtil.getImplementationsInBundle(test, packageName, loader, mth);
        if (urls != null) {
            for (String url : urls) {
                addIfMatching(test, url);
            }
        }
    }

    private static final class OsgiUtil {
        private OsgiUtil() {
        }
        static Set<String> getImplementationsInBundle(Test test, String packageName, ClassLoader loader, Method mth) {
            try {
                org.osgi.framework.Bundle bundle = (org.osgi.framework.Bundle) mth.invoke(loader);
                org.osgi.framework.Bundle[] bundles = bundle.getBundleContext().getBundles();
                Set<String> urls = new HashSet<String>();
                for (org.osgi.framework.Bundle bd : bundles) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("Searching in bundle:" + bd);
                    }
                    Enumeration<URL> paths = bd.findEntries("/" + packageName, "*.class", true);
                    while (paths != null && paths.hasMoreElements()) {
                        URL path = paths.nextElement();
                        String pathString = path.getPath();
                        pathString.indexOf(packageName);
                        urls.add(pathString.substring(pathString.indexOf(packageName)));
                    }
                }
                return urls;
            } catch (Throwable t) {
                LOG.error("Could not search osgi bundles for classes matching criteria: " + test
                          + "due to an Exception: " + t.getMessage());
                return null;
            }
        }
    }

}
