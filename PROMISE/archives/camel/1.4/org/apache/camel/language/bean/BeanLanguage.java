package org.apache.camel.language.bean;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.spi.Language;
import org.apache.camel.util.ObjectHelper;

/**
 * which uses a simple text notation to invoke methods on beans to evaluate predicates or expressions<p/>
 * <p/>
 * The notation is essentially <code>beanName.methodName</code> which is then invoked using the
 * then the method is invoked to evaluate the expression using the
 * {@link Exchange} to the method arguments.
 *
 * @version $Revision: 630591 $
 */
public class BeanLanguage implements Language {
    public Predicate<Exchange> createPredicate(String expression) {
        return PredicateBuilder.toPredicate(createExpression(expression));
    }

    public Expression<Exchange> createExpression(String expression) {
        ObjectHelper.notNull(expression, "expression");

        int idx = expression.lastIndexOf('.');
        String beanName = expression;
        String method = null;
        if (idx > 0) {
            beanName = expression.substring(0, idx);
            method = expression.substring(idx + 1);
        }
        return new BeanExpression(beanName, method);
    }
}
