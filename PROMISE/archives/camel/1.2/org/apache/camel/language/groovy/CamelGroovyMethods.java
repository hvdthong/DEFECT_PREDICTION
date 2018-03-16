package org.apache.camel.language.groovy;

import groovy.lang.Closure;
import org.apache.camel.Exchange;
import org.apache.camel.impl.ExpressionSupport;
import org.apache.camel.model.ChoiceType;
import org.apache.camel.model.FilterType;
import org.apache.camel.model.ProcessorType;

/**
 * @version $Revision: 1.1 $
 */
public class CamelGroovyMethods {

    public static FilterType filter(ProcessorType self, Closure filter) {
        return self.filter(toExpression(filter));
    }

    public static ChoiceType when(ChoiceType self, Closure filter) {
        return self.when(toExpression(filter));
    }

    public static ExpressionSupport toExpression(final Closure filter) {
        return new ExpressionSupport<Exchange>() {
            protected String assertionFailureMessage(Exchange exchange) {
                return filter.toString();
            }

            public Object evaluate(Exchange exchange) {
                return filter.call(exchange);
            }

            @Override
            public String toString() {
                return "Groovy[" + filter + "]";
            }
        };
    }

}
