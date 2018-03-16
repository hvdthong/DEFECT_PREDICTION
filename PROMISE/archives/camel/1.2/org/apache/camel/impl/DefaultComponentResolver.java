package org.apache.camel.impl;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Exchange;
import org.apache.camel.spi.ComponentResolver;
import org.apache.camel.util.FactoryFinder;
import org.apache.camel.util.NoFactoryAvailableException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The default implementation of {@link ComponentResolver} which tries to find
 * components by using the URI scheme prefix and searching for a file of the URI
 * scheme name in the <b>META-INF/services/org/apache/camel/component/</b>
 * directory on the classpath.
 *
 * @version $Revision: 572600 $
 */
public class DefaultComponentResolver<E extends Exchange> implements ComponentResolver<E> {
    private static final transient Log LOG = LogFactory.getLog(DefaultComponentResolver.class);
    protected static final FactoryFinder COMPONENT_FACTORY =
            new FactoryFinder("META-INF/services/org/apache/camel/component/");

    public Component<E> resolveComponent(String name, CamelContext context) {
        Object bean = null;
        try {
            bean = context.getRegistry().lookup(name);
            if (bean != null && LOG.isDebugEnabled()) {
                LOG.debug("Found component: " + name + " in registry: " + bean);
            }
        }
        catch (Exception e) {
            LOG.debug("Ignored error looking up bean: " + name + ". Error: " + e);
        }
        if (bean != null) {
            if (bean instanceof Component) {
                return (Component) bean;
            }
            else {
                throw new IllegalArgumentException("Bean with name: " + name + " in registry is not a Component: " + bean);
            }
        }
        Class type;
        try {
            type = COMPONENT_FACTORY.findClass(name);
        }
        catch (NoFactoryAvailableException e) {
            return null;
        }
        catch (Throwable e) {
            throw new IllegalArgumentException("Invalid URI, no Component registered for scheme : "
                    + name, e);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Found component: " + name + " via type: " + type.getName() + " via " + COMPONENT_FACTORY.getPath() + name);
        }
        if (type == null) {
            return null;
        }
        if (Component.class.isAssignableFrom(type)) {
            return (Component<E>) context.getInjector().newInstance(type);
        }
        else {
            throw new IllegalArgumentException("Type is not a Component implementation. Found: "
                    + type.getName());
        }
    }
}
