package org.apache.camel.component.cxf.invoker;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.cxf.Bus;
import org.apache.cxf.binding.Binding;
import org.apache.cxf.endpoint.ClientImpl;
import org.apache.cxf.endpoint.ConduitSelector;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.endpoint.EndpointImpl;
import org.apache.cxf.endpoint.PreexistingConduitSelector;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.ExchangeImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingMessageInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.transport.MessageObserver;

/**
 * Just deal with the PayLoadMessage and RawMessage
 *
 */
public class CxfClient extends ClientImpl {

    private static final Logger LOG = Logger.getLogger(CxfClient.class.getName());

    private Endpoint endpoint;
    
    public CxfClient(Bus b, Endpoint e) {
        super(b, e);
        endpoint = e; 
    }
    
    public Object dispatch(Object params, 
                           Map<String, Object> context,
                           Exchange exchange) throws Exception {
        
        Object retval = null;
        InvokingContext invokingContext = exchange.get(InvokingContext.class);
        assert invokingContext != null;

        BindingOperationInfo inBoundOp = exchange.get(BindingOperationInfo.class);
        
        BindingOperationInfo outBoundOp = null;

        if (inBoundOp != null) {
            BindingInfo bi = getEndpoint().getEndpointInfo().getBinding();
            outBoundOp = bi.getOperation(inBoundOp.getOperationInfo().getName());
            if (outBoundOp != null 
                && inBoundOp.isUnwrapped()) {
                outBoundOp = outBoundOp.getUnwrappedOperation();
            }
        }
        
       
        retval = invokeWithMessageStream(outBoundOp, params, context, invokingContext);
        
        return retval;
        
        
    }

 
    @SuppressWarnings("unchecked")
    public Object invokeWithMessageStream(BindingOperationInfo bi, 
                                          Object param, 
                                          Map<String, Object> context,
                                          InvokingContext invokingContext) throws Exception {

        Object retval = null;

        Map<String, Object> requestContext = null;
        Map<String, Object> responseContext = null;

        if (null != context) {
            requestContext = (Map<String, Object>) context.get(REQUEST_CONTEXT);
            responseContext = (Map<String, Object>) context.get(RESPONSE_CONTEXT);
        }

        Exchange exchange = new ExchangeImpl();
        exchange.put(MessageObserver.class, this);
        exchange.put(InvokingContext.class, invokingContext);
        exchange.put(Bus.class, bus);
        exchange.put(Endpoint.class, getEndpoint());
        exchange.put(BindingInfo.class, getEndpoint().getEndpointInfo().getBinding());
        if (bi != null) {
            exchange.put(BindingOperationInfo.class, bi);
            exchange.put(BindingMessageInfo.class, bi.getInput());            
            exchange.setOneWay(bi.getOperationInfo().isOneWay());
        }

        Message message = prepareMessage(exchange, requestContext, param, invokingContext);
        
        PhaseInterceptorChain chain = setupOutChain(requestContext, message, invokingContext);

        prepareConduitSelector(message);

        chain.doIntercept(message);
                
                
        Exception ex = message.getContent(Exception.class);

        if (ex != null) {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Exception in outgoing chain: " + ex.toString());
            }
            throw ex;
        }

        if (!exchange.isOneWay()) {
            ex = getException(exchange);
    
            if (ex != null) {
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("Exception in incoming chain: " + ex.toString());
                }
                throw ex;
            }
            retval = invokingContext.getResponseObject(exchange, responseContext);  
            
        }

        return retval;
    }

    public void onMessage(Message message) {
        Exchange exchange = message.getExchange();
        
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("call the cxf client on message , exchange is " + exchange);
        }    
        if (exchange.get(InvokingContext.class) == null) {
            super.onMessage(message);
        } else {

            message = getEndpoint().getBinding().createMessage(message);
            message.put(Message.REQUESTOR_ROLE, Boolean.TRUE);
            message.put(Message.INBOUND_MESSAGE, Boolean.TRUE);
                        
            exchange.put(Binding.class, getEndpoint().getBinding());
            BindingOperationInfo bi = exchange.get(BindingOperationInfo.class);        
            if (bi != null) {
                exchange.put(BindingMessageInfo.class, bi.getOutput());
            }
            InvokingContext invokingContext = exchange.get(InvokingContext.class);
            assert invokingContext != null;

            PhaseInterceptorChain chain = invokingContext.getResponseInInterceptorChain(exchange);
            message.setInterceptorChain(chain);
            
            chain.doIntercept(message);

            exchange.setInMessage(message);
        }
    }

    private Message prepareMessage(Exchange exchange, Map<String, Object> requestContext,
            Object param, InvokingContext InvokingContext) {

        Message message = getEndpoint().getBinding().createMessage();
        message.put(Message.REQUESTOR_ROLE, Boolean.TRUE);
        message.put(Message.INBOUND_MESSAGE, Boolean.FALSE);

        if (requestContext != null) {
            message.putAll(requestContext);
        }

        if (param != null) {
            InvokingContext.setRequestOutMessageContent(message, param);
        }

        if (null != requestContext) {
            exchange.putAll(requestContext);
        }

        exchange.setOutMessage(message);
        return message;
    }

    private PhaseInterceptorChain setupOutChain(Map<String, Object> requestContext,
            Message message, InvokingContext invokingContext) {

        if (LOG.isLoggable(Level.FINEST)) {
            LOG.finest("Build an out interceptor chain to send request to server");
        }
        Exchange exchange = message.getExchange();
        PhaseInterceptorChain chain = invokingContext.getRequestOutInterceptorChain(exchange);
        message.setInterceptorChain(chain);
        modifyChain(chain, requestContext);
        chain.setFaultObserver(outFaultObserver);
        return chain;
    }
    
    public Endpoint getEndpoint() {
        return endpoint;
    }
    
    public Bus getBus() {
        return bus;
    }
}

