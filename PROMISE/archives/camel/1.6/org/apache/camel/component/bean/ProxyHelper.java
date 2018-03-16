package org.apache.camel.component.bean;

import java.lang.reflect.Proxy;

import org.apache.camel.Endpoint;
import org.apache.camel.Producer;

/**
 * A helper class for creating proxies which delegate to Camel
 *
 * @version $Revision: 701059 $
 */
public final class ProxyHelper {

    /**
     * Utility classes should not have a public constructor.
     */
    private ProxyHelper() {
    }


    /**
     * Creates a Proxy which sends PojoExchange to the endpoint.
     */
    @SuppressWarnings("unchecked")
    public static Object createProxyObject(Endpoint endpoint, Producer producer, ClassLoader classLoader, Class[] interfaces, MethodInfoCache methodCache) {
        return Proxy.newProxyInstance(classLoader, interfaces.clone(), new CamelInvocationHandler(endpoint, producer, methodCache));
    }


    /**
     * Creates a Proxy which sends PojoExchange to the endpoint.
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Endpoint endpoint, ClassLoader cl, Class[] interfaces, MethodInfoCache methodCache) throws Exception {
        return (T) createProxyObject(endpoint, endpoint.createProducer(), cl, interfaces, methodCache);
    }

    /**
     * Creates a Proxy which sends PojoExchange to the endpoint.
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Endpoint endpoint, ClassLoader cl, Class<T>... interfaceClasses) throws Exception {
        return (T) createProxy(endpoint, cl, interfaceClasses, createMethodInfoCache(endpoint));
    }


    /**
     * Creates a Proxy which sends PojoExchange to the endpoint.
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Endpoint endpoint, Class<T>... interfaceClasses) throws Exception {
        return (T) createProxy(endpoint, getClassLoader(interfaceClasses), interfaceClasses);
    }

    /**
     * Creates a Proxy which sends PojoExchange to the endpoint.
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Endpoint endpoint, Producer producer, Class<T>... interfaceClasses) throws Exception {
        return (T) createProxyObject(endpoint, producer, getClassLoader(interfaceClasses), interfaceClasses, createMethodInfoCache(endpoint));
    }


    /**
     * Returns the class loader of the first interface or throws {@link IllegalArgumentException} if there are no interfaces specified
     *
     * @return the class loader
     */
    protected static ClassLoader getClassLoader(Class... interfaces) {
        if (interfaces == null || interfaces.length < 1) {
            throw new IllegalArgumentException("You must provide at least 1 interface class.");
        }
        return interfaces[0].getClassLoader();
    }


    protected static MethodInfoCache createMethodInfoCache(Endpoint endpoint) {
        return new MethodInfoCache(endpoint.getCamelContext());
    }

}
