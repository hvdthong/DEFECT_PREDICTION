package org.apache.camel.language;

import org.apache.camel.RuntimeCamelException;
import org.apache.camel.spi.Language;

/**
 * An exception thrown if some illegal syntax is rejected by a specific language
 *
 * @version $Revision: $
 */
public class IllegalSyntaxException extends RuntimeCamelException {
    private final Language language;
    private final String expression;

    public IllegalSyntaxException(Language language, String expression) {
        super("Illegal syntax for language: " + language + ". Expression: " + expression);
        this.language = language;
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public Language getLanguage() {
        return language;
    }
}
