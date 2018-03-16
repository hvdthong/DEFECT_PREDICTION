package org.apache.camel.language.bean;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.spi.Language;
import org.apache.camel.util.ObjectHelper;

/**
 * which uses a simple text notation to invoke methods on beans to evaluate predicates or expressions
 * <p/>
 * The notation is essentially <code>beanName.methodName</code> which is then invoked using the
 * then the method is invoked to evaluate the expression using the
 * {@link Exchange} to the method arguments.
 * <p/>
 * As of Camel 1.5 the bean language also supports invoking a provided bean by
 * its classname or the bean itself.
 *
 * @version $Revision: 688350 $
 */
public class BeanLanguage implements Language {

    /**
     * Creates the expression based on the string syntax.
     *
     * @param expression the string syntax
     * @return the expression
     */
    public static Expression bean(String expression) {
        BeanLanguage language = new BeanLanguage();
        return language.createExpression(expression);
    }

    /**
     * Creates the expression for invoking the bean type.
     *
     * @param beanType  the bean type to invoke
     * @param method optional name of method to invoke for instance to avoid ambiguity
     * @return the expression
     */
    public static Expression bean(Class beanType, String method) {
        Object bean = ObjectHelper.newInstance(beanType);
        return bean(bean, method);
    }

    /**
     * Creates the expression for invoking the bean type.
     *
     * @param bean  the bean to invoke
     * @param method optional name of method to invoke for instance to avoid ambiguity
     * @return the expression
     */
    public static Expression bean(Object bean, String method) {
        BeanLanguage language = new BeanLanguage();
        return language.createExpression(bean, method);
    }

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

    public Expression<Exchange> createExpression(Object bean, String method) {
        ObjectHelper.notNull(bean, "bean");
        return new BeanExpression(bean, method);
    }

}
