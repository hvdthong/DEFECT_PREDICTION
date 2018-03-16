package org.apache.camel.language.constant;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.spi.Language;

/**
 * A language for constant expressions.
 */
public class ConstantLanguage implements Language {

    public static Expression<Exchange> constant(Object value) {        
        return ExpressionBuilder.constantExpression(value);
    }

    public Predicate<Exchange> createPredicate(String expression) {
        return PredicateBuilder.toPredicate(createExpression(expression));
    }

    public Expression<Exchange> createExpression(String expression) {
        return ConstantLanguage.constant(expression);
    }
}
