package org.apache.camel.builder.script;

import org.apache.camel.CamelContext;
import org.apache.camel.spi.Language;
import org.apache.camel.spi.LanguageResolver;

/**
 * @version $Revision: 630591 $
 */
public class ScriptLanguageResolver implements LanguageResolver {
    public Language resolveLanguage(String name, CamelContext context) {
        return new ScriptLanguage(name);
    }
}
