package org.apache.camel.processor;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.CamelException;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.util.AsyncProcessorHelper;

public class HandleFaultProcessor extends DelegateProcessor implements AsyncProcessor {
    
    @Override
    public String toString() {
        return "HandleFaultProcessor(" + processor + ")";
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        AsyncProcessorHelper.process(this, exchange);
    }

    public boolean process(final Exchange exchange, final AsyncCallback callback) {
        if (processor == null) {
            callback.done(true);
            return true;
        }      

        if (processor instanceof AsyncProcessor) {
            return ((AsyncProcessor)processor).process(exchange, new AsyncCallback() {
                
                public void done(boolean doneSynchronously) {
                    Message faultMessage = exchange.getFault(false);
                    if (faultMessage != null) {
                        final Object faultBody = faultMessage.getBody();
                        if (faultBody != null) {
                            if (faultBody instanceof Throwable) {
                                exchange.setException((Throwable)faultBody);
                            } else {
                                if (exchange.getException() == null) {                                
                                    exchange.setException(new CamelException("Message contains fault of type "
                                        + faultBody.getClass().getName() + ":\n" + faultBody));
                                }
                            }
                        }
                    }
                    callback.done(doneSynchronously);
                }                
            });
        }
        
        try {
            processor.process(exchange);
        } catch (Throwable e) {
            exchange.setException(e);
        }
        
        final Message faultMessage = exchange.getFault(false);
        if (faultMessage != null) {
            final Object faultBody = faultMessage.getBody();
            if (faultBody != null) {
                if (faultBody instanceof Throwable) {
                    exchange.setException((Throwable)faultBody);
                } else {
                    if (exchange.getException() == null) {
                        exchange.setException(new CamelException("Message contains fault of type "
                            + faultBody.getClass().getName() + ":\n" + faultBody));
                    }
                }
            }
        }
        callback.done(true);
        return true;
    }
}
