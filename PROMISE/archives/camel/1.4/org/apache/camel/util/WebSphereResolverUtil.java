package org.apache.camel.util;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * WebSphere specific resolver util to handle loading annotated resources in JAR files.
 */
public class WebSphereResolverUtil extends ResolverUtil {

    private String resourcePath;

    /**
     * Constructor.
     *
     * @param resourcePath  the fixed resource path to use for fetching camel jars in WebSphere.
     */
    public WebSphereResolverUtil(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    /**
     * Is the classloader from IBM and thus the WebSphere platform?
     *
     * @param loader  the classloader
     * @return  <tt>true</tt> if IBM classloader, <tt>false</tt> otherwise.
     */
    public static boolean isWebSphereClassLoader(ClassLoader loader) {
        return loader.getClass().getName().startsWith("com.ibm");
    }

    /**
     * Overloaded to handle specific problem with getting resources on the IBM WebSphere platform.
     * <p/>
     * WebSphere can <b>not</b> load resources if the resource to load is a folder name, such as a
     * packagename, you have to explicit name a resource that is a file.
     *
     * @param loader  the classloader
     * @param packageName   the packagename for the package to load
     * @return  URL's for the given package
     * @throws IOException is thrown by the classloader
     */
    @Override
    protected Enumeration<URL> getResources(ClassLoader loader, String packageName) throws IOException {
        Enumeration<URL> enumeration = super.getResources(loader, packageName);
        if (!enumeration.hasMoreElements()) {
            LOG.trace("Using WebSphere workaround to load the camel jars with the annotated converters.");
            enumeration = loader.getResources(resourcePath);
        }

        return enumeration;
    }

}
