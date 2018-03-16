package org.apache.camel.component.xquery;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.spi.Language;

/**
 * @version $Revision: 641680 $
 */
public class XQueryLanguage implements Language {

    public Predicate<Exchange> createPredicate(String expression) {
        return XQueryBuilder.xquery(expression);
    }

    public Expression<Exchange> createExpression(String expression) {
        return XQueryBuilder.xquery(expression);
    }
}
