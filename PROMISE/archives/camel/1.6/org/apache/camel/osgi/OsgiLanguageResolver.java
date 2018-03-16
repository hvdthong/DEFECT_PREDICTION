package org.apache.camel.osgi;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.spi.Language;
import org.apache.camel.spi.LanguageResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OsgiLanguageResolver implements LanguageResolver {
    private static final transient Log LOG = LogFactory.getLog(OsgiLanguageResolver.class);

    public Language resolveLanguage(String name, CamelContext context) {
        Object bean = null;
        try {
            bean = context.getRegistry().lookup(name);
            if (bean != null && LOG.isDebugEnabled()) {
                LOG.debug("Found language: " + name + " in registry: " + bean);
            }
        } catch (Exception e) {
            LOG.debug("Ignored error looking up bean: " + name + ". Error: " + e);
        }
        if (bean != null) {
            if (bean instanceof Language) {
                return (Language)bean;
            }
        }
        Class type = null;
        try {
            type = getLanaguage(name);
        } catch (Throwable e) {
            throw new IllegalArgumentException("Invalid URI, no Language registered for scheme : " + name, e);
        }
        if (type == null) {
            return null;
        }
        if (Language.class.isAssignableFrom(type)) {
            return (Language)context.getInjector().newInstance(type);
        } else {
            throw new IllegalArgumentException("Type is not a Lanaguage implementation. Found: "
                                               + type.getName());
        }
    }

    protected Class getLanaguage(String name) throws Exception {
        return Activator.getLanguage(name);
    }

}
