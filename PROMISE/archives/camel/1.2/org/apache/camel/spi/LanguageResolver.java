package org.apache.camel.spi;

import org.apache.camel.CamelContext;

/**
 * A pluggable strategy for resolving different languages in a loosely coupled manner
 * 
 * @version $Revision: 1.1 $
 */
public interface LanguageResolver {
    Language resolveLanguage(String name, CamelContext context);
}
