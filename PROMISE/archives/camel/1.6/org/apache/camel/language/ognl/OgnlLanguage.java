package org.apache.camel.language.ognl;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.spi.Language;

/**
 *
 * @version $Revision: 630574 $
 */
public class OgnlLanguage implements Language {

    public Predicate<Exchange> createPredicate(String expression) {
        return new OgnlExpression(this, expression, Boolean.class);
    }

    public Expression<Exchange> createExpression(String expression) {
        return new OgnlExpression(this, expression, Object.class);
    }
}
