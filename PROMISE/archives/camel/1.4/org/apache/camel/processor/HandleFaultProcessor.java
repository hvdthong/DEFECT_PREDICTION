package org.apache.camel.processor;

import org.apache.camel.CamelException;
import org.apache.camel.Exchange;
import org.apache.camel.Message;

public class HandleFaultProcessor extends DelegateProcessor {

    @Override
    public void process(Exchange exchange) throws Exception {
        super.process(exchange);
        final Message faultMessage = exchange.getFault(false);
        if (faultMessage != null) {
            final Object faultBody = faultMessage.getBody();
            if (faultBody != null) {
                if (faultBody instanceof Throwable) {
                    exchange.setException((Throwable)faultBody);
                } else {
                    exchange.setException(new CamelException("Message contains fault of type "
                                                             + faultBody.getClass().getName() + ":\n"
                                                             + faultBody));
                }
            }
        }
    }
}
