package org.apache.camel.language.juel;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.spi.Language;

/**
 *
 * @version $Revision: $
 */
public class JuelLanguage implements Language {
    public Predicate<Exchange> createPredicate(String expression) {
        return new JuelExpression(expression, Boolean.class);
    }

    public Expression<Exchange> createExpression(String expression) {
        return new JuelExpression(expression, Object.class);
    }
}
