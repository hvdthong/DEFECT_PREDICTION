package org.apache.camel.osgi;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.spi.ComponentResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.SynchronousBundleListener;
import org.springframework.osgi.util.BundleDelegatingClassLoader;

public class OsgiComponentResolver implements ComponentResolver {
    
    private static final transient Log LOG = LogFactory.getLog(OsgiComponentResolver.class);

    protected Class getComponent(String name) throws Exception {
        return Activator.getComponent(name);       
    }

    public Component resolveComponent(String name, CamelContext context) throws Exception {
        Object bean = null;
        try {
            bean = context.getRegistry().lookup(name);
            if (bean != null && LOG.isDebugEnabled()) {
                LOG.debug("Found component: " + name + " in registry: " + bean);
            }
        } catch (Exception e) {
            LOG.debug("Ignored error looking up bean: " + name + ". Error: " + e);
        }
        if (bean != null) {
            if (bean instanceof Component) {
                return (Component)bean;
            }
        }
        Class type = null;
        try {
            type = getComponent(name);
        } catch (Throwable e) {
            throw new IllegalArgumentException("Invalid URI, no Component registered for scheme : " + name, e);
        }
        if (type == null) {
            return null;
        }
        if (Component.class.isAssignableFrom(type)) {
            return (Component)context.getInjector().newInstance(type);
        } else {
            throw new IllegalArgumentException("Type is not a Component implementation. Found: "
                                               + type.getName());
        }
    }

}
