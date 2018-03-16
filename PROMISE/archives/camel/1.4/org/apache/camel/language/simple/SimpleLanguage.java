package org.apache.camel.language.simple;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.language.IllegalSyntaxException;
import org.apache.camel.spi.Language;
import org.apache.camel.util.ObjectHelper;

/**
 * which maps simple property style notations to access headers and bodies.
 * Examples of supported expressions are <p/>
 * <ul>
 * <li>in.header.foo or header.foo to access an inbound header called 'foo'</li>
 * <li>in.body or body to access the inbound body</li>
 * <li>out.header.foo to access an outbound header called 'foo'</li>
 * <li>out.body to access the inbound body</li>
 * <li>property.foo to access the exchange property called 'foo'</li>
 * <li>sys.foo to access the system property called 'foo'</li>
 * </ul>
 *
 * @version $Revision: 659197 $
 */
public class SimpleLanguage implements Language {

    public static Expression simple(String expression) {
        SimpleLanguage language = new SimpleLanguage();
        return language.createExpression(expression);
    }

    public Predicate<Exchange> createPredicate(String expression) {
        return PredicateBuilder.toPredicate(createExpression(expression));
    }

    public Expression<Exchange> createExpression(String expression) {
        if (expression.indexOf("${") >= 0) {
            return createComplexExpression(expression);
        }
        return createSimpleExpression(expression);
    }

    protected Expression<Exchange> createComplexExpression(String expression) {
        List<Expression> results = new ArrayList<Expression>();

        int pivot = 0;
        int size = expression.length();
        while (pivot < size) {
            int idx = expression.indexOf("${", pivot);
            if (idx < 0) {
                results.add(createConstantExpression(expression, pivot, size));
                break;
            } else {
                if (pivot < idx) {
                    results.add(createConstantExpression(expression, pivot, idx));
                }
                pivot = idx + 2;
                int endIdx = expression.indexOf("}", pivot);
                if (endIdx < 0) {
                    throw new IllegalArgumentException("Expecting } but found end of string for simple expression: " + expression);
                }
                String simpleText = expression.substring(pivot, endIdx);

                Expression simpleExpression = createSimpleExpression(simpleText);
                results.add(simpleExpression);
                pivot = endIdx + 1;
            }
        }
        return ExpressionBuilder.concatExpression(results, expression);
    }

    protected Expression createConstantExpression(String expression, int start, int end) {
        return ExpressionBuilder.constantExpression(expression.substring(start, end));
    }

    protected Expression<Exchange> createSimpleExpression(String expression) {
        if (ObjectHelper.isEqualToAny(expression, "body", "in.body")) {
            return ExpressionBuilder.bodyExpression();
        } else if (ObjectHelper.equal(expression, "out.body")) {
            return ExpressionBuilder.outBodyExpression();
        }

        String remainder = ifStartsWithReturnRemainder("in.header.", expression);
        if (remainder == null) {
            remainder = ifStartsWithReturnRemainder("header.", expression);
        }
        if (remainder == null) {
            remainder = ifStartsWithReturnRemainder("headers.", expression);
        }
        if (remainder == null) {
            remainder = ifStartsWithReturnRemainder("in.headers.", expression);
        }
        if (remainder != null) {
            return ExpressionBuilder.headerExpression(remainder);
        }

        remainder = ifStartsWithReturnRemainder("out.header.", expression);
        if (remainder == null) {
            remainder = ifStartsWithReturnRemainder("out.headers.", expression);
        }
        if (remainder != null) {
            return ExpressionBuilder.outHeaderExpression(remainder);
        }

        remainder = ifStartsWithReturnRemainder("property.", expression);
        if (remainder != null) {
            return ExpressionBuilder.propertyExpression(remainder);
        }

        remainder = ifStartsWithReturnRemainder("sys.", expression);
        if (remainder != null) {
            return ExpressionBuilder.propertyExpression(remainder);
        }
        throw new IllegalSyntaxException(this, expression);
    }

    protected String ifStartsWithReturnRemainder(String prefix, String text) {
        if (text.startsWith(prefix)) {
            String remainder = text.substring(prefix.length());
            if (remainder.length() > 0) {
                return remainder;
            }
        }
        return null;
    }
}
