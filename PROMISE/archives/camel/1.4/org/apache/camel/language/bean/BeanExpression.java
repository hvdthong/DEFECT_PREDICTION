package org.apache.camel.language.bean;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.bean.BeanProcessor;
import org.apache.camel.component.bean.RegistryBean;
import org.apache.camel.impl.ExpressionSupport;

/**
 * Evaluates an expression using a bean method invocation
 *
 * @version $Revision: 675833 $
 */
public class BeanExpression<E extends Exchange> extends ExpressionSupport<E> {
    private String beanName;
    private String method;

    public BeanExpression(String beanName, String method) {
        this.beanName = beanName;
        this.method = method;
    }

    @Override
    public String toString() {
        return "BeanExpression[bean: " + beanName + " method: " + method + "]";
    }

    protected String assertionFailureMessage(E exchange) {
        return "bean: " + beanName + " method: " + method;
    }

    public Object evaluate(E exchange) {
        BeanProcessor processor = new BeanProcessor(new RegistryBean(exchange.getContext(), beanName));
        if (method != null) {
            processor.setMethod(method);
        }
        try {
            Exchange newExchange = exchange.copy();
            if (!newExchange.getPattern().isOutCapable()) {
                newExchange.setPattern(ExchangePattern.InOut);
            }
            processor.process(newExchange);
            return newExchange.getOut(true).getBody();
        } catch (Exception e) {
            throw new RuntimeBeanExpressionException(exchange, beanName, method, e);
        }
    }
}
