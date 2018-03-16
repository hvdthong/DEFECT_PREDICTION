package org.apache.camel.component.bean;

import java.util.Collection;

import org.apache.camel.CamelExchangeException;
import org.apache.camel.Exchange;

/**
 * An exception thrown if an attempted method invocation resulted in an ambiguous method
 * such that multiple methods match the inbound message exchange
 *
 * @version $Revision: 640438 $
 */
public class AmbiguousMethodCallException extends CamelExchangeException {
    private final Collection<MethodInfo> methods;

    public AmbiguousMethodCallException(Exchange exchange, Collection<MethodInfo> methods) {
        super("Ambiguous method invocations possible: " + methods, exchange);
        this.methods = methods;
    }

    /**
     * The ambiguous methods for which a single method could not be chosen
     */
    public Collection<MethodInfo> getMethods() {
        return methods;
    }
}
