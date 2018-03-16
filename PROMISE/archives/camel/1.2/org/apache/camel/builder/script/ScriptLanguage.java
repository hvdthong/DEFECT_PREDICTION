package org.apache.camel.builder.script;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.spi.Language;

/**
 * @version $Revision: $
 */
public class ScriptLanguage implements Language {
    private final String language;

    public ScriptLanguage(String language) {
        this.language = language;
    }

    public Predicate<Exchange> createPredicate(String expression) {
        return new ScriptBuilder(language, expression);
    }

    public Expression<Exchange> createExpression(String expression) {
        return new ScriptBuilder(language, expression);
    }
}
