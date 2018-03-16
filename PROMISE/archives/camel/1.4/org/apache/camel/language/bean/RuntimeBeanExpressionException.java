package org.apache.camel.language.bean;

import org.apache.camel.Exchange;
import org.apache.camel.RuntimeExpressionException;

/**
 * Exception thrown if invocation of bean failed.
 *
 * @version $Revision: 659760 $
 */
public class RuntimeBeanExpressionException extends RuntimeExpressionException {
    private final Exchange exchange;
    private final String bean;
    private final String method;

    public RuntimeBeanExpressionException(Exchange exchange, String bean, String method, Throwable e) {
        super("Failed to invoke method: " + method + " on " + bean + " due to: " + e, e);
        this.exchange = exchange;
        this.bean = bean;
        this.method = method;
    }

    public String getBean() {
        return bean;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public String getMethod() {
        return method;
    }
}
