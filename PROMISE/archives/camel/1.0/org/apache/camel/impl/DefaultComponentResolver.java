package org.apache.camel.impl;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Exchange;
import org.apache.camel.spi.ComponentResolver;
import org.apache.camel.util.FactoryFinder;
import org.apache.camel.util.NoFactoryAvailableException;

/**
 * The default implementation of {@link ComponentResolver}
 * which tries to find components by using the URI scheme prefix and searching for a file of the URI
 * scheme name in the <b>META-INF/services/org/apache/camel/component/</b>
 * directory on the classpath.
 *
 * @version $Revision: 530555 $
 */
public class DefaultComponentResolver<E extends Exchange> implements ComponentResolver<E> {
    protected static final FactoryFinder componentFactory = new FactoryFinder("META-INF/services/org/apache/camel/component/");

    public Component<E> resolveComponent(String name, CamelContext context) {
        Class type;
        try {
            type = componentFactory.findClass(name);
        }
        catch (NoFactoryAvailableException e) {
            return null;
        }
        catch (Throwable e) {
            throw new IllegalArgumentException("Invalid URI, no EndpointResolver registered for scheme : " + name, e);
        }
        if (type == null) {
            return null;
        }
        if (Component.class.isAssignableFrom(type)) {
            return (Component<E>)context.getInjector().newInstance(type);
        }
        else {
            throw new IllegalArgumentException("Type is not a Component implementation. Found: " + type.getName());
        }
    }
}
