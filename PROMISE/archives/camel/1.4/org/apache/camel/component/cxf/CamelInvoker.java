package org.apache.camel.component.cxf;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.camel.ExchangePattern;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.MethodDispatcher;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageContentsList;
import org.apache.cxf.service.Service;
import org.apache.cxf.service.invoker.Invoker;
import org.apache.cxf.service.model.BindingMessageInfo;
import org.apache.cxf.service.model.BindingOperationInfo;

public class CamelInvoker implements Invoker, MessageInvoker {
    private static final Logger LOG = LogUtils.getL7dLogger(CamelInvoker.class);
    private CxfConsumer cxfConsumer;

    public CamelInvoker(CxfConsumer consumer) {
        cxfConsumer = consumer;
    }

    /**
    * This method is called when the incoming message is to
    * be passed into the Camel processor. The return value is the response
    * from the processor
    */
    public void invoke(Exchange exchange) {
        Message inMessage = exchange.getInMessage();

        CxfEndpoint endpoint = cxfConsumer.getEndpoint();
        CxfExchange cxfExchange = endpoint.createExchange(inMessage);

        BindingOperationInfo bop = exchange.get(BindingOperationInfo.class);

        if (bop != null && bop.getOperationInfo().isOneWay()) {
            cxfExchange.setPattern(ExchangePattern.InOnly);
        } else {
            cxfExchange.setPattern(ExchangePattern.InOut);
        }
        try {
            cxfConsumer.getProcessor().process(cxfExchange);
        } catch (Exception ex) {
            throw new Fault(ex);
        }

        copybackExchange(cxfExchange, exchange);

        Message outMessage = exchange.getOutMessage();
        outMessage.put(Message.INBOUND_MESSAGE, Boolean.FALSE);
        BindingOperationInfo boi = exchange.get(BindingOperationInfo.class);

        if (boi != null) {
            exchange.put(BindingMessageInfo.class, boi.getOutput());
        }
    }


    public void copybackExchange(CxfExchange result, Exchange exchange) {
        final Endpoint endpoint = exchange.get(Endpoint.class);
        Message outMessage = null;
        if (result.isFailed()) {
            CxfMessage fault = result.getFault();
            outMessage = exchange.getInFaultMessage();
            if (outMessage == null) {
                outMessage = endpoint.getBinding().createMessage();
                outMessage.setExchange(exchange);
                exchange.setInFaultMessage(outMessage);
            }
            Exception ex = (Exception) fault.getBody();
            outMessage.setContent(Exception.class, ex);
        } else {
            outMessage = result.getOutMessage();
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("Get the response outMessage " + outMessage);
            }
            org.apache.camel.Message camelMessage = result.getOut();
            CxfBinding.copyMessage(camelMessage, outMessage);
        }
        exchange.setOutMessage(outMessage);
    }

    @SuppressWarnings("unchecked")
    public void updateContext(Map<String, Object> from, Map<String, Object> to) {
        if (to != null && from != null) {
            for (Iterator iter = from.entrySet().iterator(); iter.hasNext();) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String)entry.getKey();

                if (!(Message.INBOUND_MESSAGE.equals(key)
                      || Message.REQUESTOR_ROLE.equals(key)
                      || Message.PROTOCOL_HEADERS.equals(key))) {
                    to.put(key, entry.getValue());
                }
            }
        }
    }

    /**
     * This method is called when the incoming pojo or WebServiceProvider invocation is called
     * from the service invocation interceptor. The return value is the response
     * from the processor
     */
    public Object invoke(Exchange exchange, Object o) {
        CxfEndpoint endpoint = cxfConsumer.getEndpoint();

        Object params = null;
        if (o instanceof List) {
            params = CastUtils.cast((List<?>)o);
        } else if (o != null) {
            params = new MessageContentsList(o);
        }

        CxfExchange cxfExchange = endpoint.createExchange(exchange.getInMessage());

        BindingOperationInfo bop = exchange.get(BindingOperationInfo.class);
        MethodDispatcher md = (MethodDispatcher)
            exchange.get(Service.class).get(MethodDispatcher.class.getName());
        Method m = md.getMethod(bop);

        if (bop != null && bop.getOperationInfo().isOneWay()) {
            cxfExchange.setPattern(ExchangePattern.InOnly);
        } else {
            cxfExchange.setPattern(ExchangePattern.InOut);
        }
        if (bop != null && bop.getName() != null) {
            cxfExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, bop.getName().getNamespaceURI());
            cxfExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, bop.getName().getLocalPart());
        } else {
            cxfExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, m.getName());
        }
        cxfExchange.getIn().setBody(params);
        try {
            cxfConsumer.getProcessor().process(cxfExchange);
        } catch (Exception ex) {
            throw new Fault(ex);
        }

        Object result = null;
        if (cxfExchange.isFailed()) {
            Exception ex = (Exception)cxfExchange.getFault().getBody();
            if (ex instanceof Fault) {
                throw (Fault)ex;
            } else {
                throw new Fault(ex);
            }
        } else {
            result = cxfExchange.getOut().getBody();
            if (result != null) {
                if (result instanceof MessageContentsList || result instanceof List || result.getClass().isArray()) {
                    return result;
                    MessageContentsList resList = new MessageContentsList();
                    resList.add(result);
                    return resList;
                }
            }
        }
        return result;
    }

}
