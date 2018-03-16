package org.apache.camel.converter;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Message;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;

/**
 * Some useful converters for Camel APIs such as to convert a {@link Predicate} or {@link Expression}
 * to a {@link Processor}
 *
 * @version $Revision: 662301 $
 */
@Converter
public class CamelConverter {

    @Converter
    public Processor toProcessor(final Predicate<Exchange> predicate) {
        return new Processor() {
            public void process(Exchange exchange) throws Exception {
                boolean answer = predicate.matches(exchange);
                Message out = exchange.getOut();
                out.copyFrom(exchange.getIn());
                out.setBody(answer);
            }
        };

    }

    @Converter
    public Processor toProcessor(final Expression<Exchange> expresion) {
        return new Processor() {
            public void process(Exchange exchange) throws Exception {
                Object answer = expresion.evaluate(exchange);
                Message out = exchange.getOut();
                out.copyFrom(exchange.getIn());
                out.setBody(answer);
            }
        };
    }
}
