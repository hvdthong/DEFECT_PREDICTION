package org.apache.camel.language.jxpath;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.spi.Language;

/**
 * provider
 */
public class JXPathLanguage implements Language {

    public Expression<Exchange> createExpression(String expression) {
        return new JXPathExpression(expression, Object.class);
    }

    public Predicate<Exchange> createPredicate(String predicate) {
        return new JXPathExpression(predicate, Boolean.class);
    }

}
