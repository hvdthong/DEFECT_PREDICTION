package org.apache.camel.component.cxf.feature;

import java.util.concurrent.Executor;

import org.apache.camel.component.cxf.MessageInvoker;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.Service;

/**
 * This interceptor just works for invoking the MessageInvoker implementor
 *
 */
public class MessageInvokerInterceptor extends AbstractPhaseInterceptor<Message> {


    public MessageInvokerInterceptor() {
        super(Phase.INVOKE);
    }

    public void handleMessage(final Message message) {
        final Exchange exchange = message.getExchange();
        final Endpoint endpoint = exchange.get(Endpoint.class);
        final Service service = endpoint.getService();
        final MessageInvoker invoker = (MessageInvoker)service.getInvoker();

        Runnable invocation = new Runnable() {

            public void run() {
                Exchange runableEx = message.getExchange();
                invoker.invoke(runableEx);
                if (!exchange.isOneWay()) {
                    Endpoint ep = exchange.get(Endpoint.class);
                    Message outMessage = runableEx.getOutMessage();
                    copyJaxwsProperties(message, outMessage);
                    if (outMessage == null) {
                        outMessage = ep.getBinding().createMessage();
                        exchange.setOutMessage(outMessage);
                    }
                }
            }

        };

        Executor executor = getExecutor(endpoint);
        if (exchange.get(Executor.class) == executor) {
            invocation.run();
        } else {
            exchange.put(Executor.class, executor);
            executor.execute(invocation);
        }
    }


    /**
     * Get the Executor for this invocation.
     * @param endpoint
     * @return
     */
    private Executor getExecutor(final Endpoint endpoint) {
        return endpoint.getService().getExecutor();
    }

    private void copyJaxwsProperties(Message inMsg, Message outMsg) {
        outMsg.put(Message.WSDL_OPERATION, inMsg.get(Message.WSDL_OPERATION));
        outMsg.put(Message.WSDL_SERVICE, inMsg.get(Message.WSDL_SERVICE));
        outMsg.put(Message.WSDL_INTERFACE, inMsg.get(Message.WSDL_INTERFACE));
        outMsg.put(Message.WSDL_PORT, inMsg.get(Message.WSDL_PORT));
        outMsg.put(Message.WSDL_DESCRIPTION, inMsg.get(Message.WSDL_DESCRIPTION));
    }

}



