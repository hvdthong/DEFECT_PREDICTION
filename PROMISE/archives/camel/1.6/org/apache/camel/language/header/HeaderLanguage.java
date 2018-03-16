package org.apache.camel.language.header;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.spi.Language;

/**
 * A language for header expressions.
 */
public class HeaderLanguage implements Language {

    public static Expression<Exchange> header(String headerName) {        
        return ExpressionBuilder.headerExpression(headerName);
    }

    public Predicate<Exchange> createPredicate(String expression) {
        return PredicateBuilder.toPredicate(createExpression(expression));
    }

    public Expression<Exchange> createExpression(String expression) {
        return HeaderLanguage.header(expression);
    }
}
