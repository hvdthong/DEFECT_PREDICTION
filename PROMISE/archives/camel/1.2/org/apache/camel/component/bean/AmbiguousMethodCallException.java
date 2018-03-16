package org.apache.camel.component.bean;

import java.util.List;

import org.apache.camel.CamelExchangeException;
import org.apache.camel.Exchange;

/**
 * An exception thrown if an attempted method invocation resulted in an ambiguous method
 * such that multiple methods match the inbound message exchange
 *
 * @version $Revision: $
 */
public class AmbiguousMethodCallException extends CamelExchangeException {
    private final List<MethodInfo> methods;

    public AmbiguousMethodCallException(Exchange exchange, List<MethodInfo> methods) {
        super("Ambiguous method invocations possible: " + methods, exchange);
        this.methods = methods;
    }

    /**
     * The ambiguous methods for which a single method could not be chosen
     */
    public List<MethodInfo> getMethods() {
        return methods;
    }
}
