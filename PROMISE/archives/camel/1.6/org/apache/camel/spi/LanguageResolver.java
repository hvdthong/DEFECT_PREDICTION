package org.apache.camel.spi;

import org.apache.camel.CamelContext;

/**
 * A pluggable strategy for resolving different languages in a loosely coupled manner
 * 
 * @version $Revision: 688279 $
 */
public interface LanguageResolver {

    /**
     * Resolves the given language.
     *
     * @param name    the name of the langauge
     * @param context the camel context
     * @return the resolved language
     */
    Language resolveLanguage(String name, CamelContext context);
}
