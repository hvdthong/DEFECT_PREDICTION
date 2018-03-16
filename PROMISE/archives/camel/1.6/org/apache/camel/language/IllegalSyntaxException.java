package org.apache.camel.language;

import org.apache.camel.RuntimeCamelException;
import org.apache.camel.spi.Language;

/**
 * An exception thrown if some illegal syntax is rejected by a specific language
 *
 * @version $Revision: 687103 $
 */
public class IllegalSyntaxException extends RuntimeCamelException {
    private final Language language;
    private final String expression;

    public IllegalSyntaxException(Language language, String expression) {
        this(language, expression, null);
    }

    public IllegalSyntaxException(Language language, String expression, Throwable cause) {
        super("Illegal syntax for language: " + language + ". Expression: " + expression, cause);
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
