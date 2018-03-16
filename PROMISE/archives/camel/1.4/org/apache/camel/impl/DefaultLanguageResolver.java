package org.apache.camel.impl;

import org.apache.camel.CamelContext;
import org.apache.camel.NoSuchLanguageException;
import org.apache.camel.spi.Language;
import org.apache.camel.spi.LanguageResolver;
import org.apache.camel.util.FactoryFinder;
import org.apache.camel.util.NoFactoryAvailableException;

/**
 * Default language resolver that looks for language factories in <b>META-INF/services/org/apache/camel/language/</b> and
 * language resolvers in <b>META-INF/services/org/apache/camel/language/resolver/</b>.
 *
 * @version $Revision: 659798 $
 */
public class DefaultLanguageResolver implements LanguageResolver {
    protected static final FactoryFinder LANGUAGE_FACTORY = new FactoryFinder("META-INF/services/org/apache/camel/language/");
    protected static final FactoryFinder LANGUAGE_RESOLVER = new FactoryFinder("META-INF/services/org/apache/camel/language/resolver/");

    public Language resolveLanguage(String name, CamelContext context) {
        Class type = null;
        try {
            type = LANGUAGE_FACTORY.findClass(name);
        } catch (NoFactoryAvailableException e) {
        } catch (Throwable e) {
            throw new IllegalArgumentException("Invalid URI, no Language registered for scheme : " + name, e);
        }
        if (type != null) {
            if (Language.class.isAssignableFrom(type)) {
                return (Language)context.getInjector().newInstance(type);
            } else {
                throw new IllegalArgumentException("Type is not a Language implementation. Found: " + type.getName());
            }
        }
        return noSpecificLanguageFound(name, context);
    }

    protected Language noSpecificLanguageFound(String name, CamelContext context) {
        Class type = null;
        try {
            type = LANGUAGE_RESOLVER.findClass("default");
        } catch (NoFactoryAvailableException e) {
        } catch (Throwable e) {
            throw new IllegalArgumentException("Invalid URI, no Language registered for scheme : " + name, e);
        }
        if (type != null) {
            if (LanguageResolver.class.isAssignableFrom(type)) {
                LanguageResolver resolver = (LanguageResolver)context.getInjector().newInstance(type);
                return resolver.resolveLanguage(name, context);
            } else {
                throw new IllegalArgumentException("Type is not a LanguageResolver implementation. Found: " + type.getName());
            }
        }
        throw new NoSuchLanguageException(name);
    }
}
