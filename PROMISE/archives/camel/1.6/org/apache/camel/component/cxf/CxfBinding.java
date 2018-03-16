package org.apache.camel.component.cxf;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;

import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.camel.component.cxf.util.CxfHeaderHelper;
import org.apache.camel.spi.HeaderFilterStrategy;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.Message;

/**
 * The binding/mapping of Camel messages to Apache CXF and back again
 *
 * @version $Revision: 743243 $
 */
public final class CxfBinding {
    private CxfBinding() {
    }
    public static Object extractBodyFromCxf(CxfExchange exchange, Message message) {
        return getBody(message);
    }

    protected static Object getBody(Message message) {
        Set<Class<?>> contentFormats = message.getContentFormats();
        if (contentFormats != null) {
            for (Class<?> contentFormat : contentFormats) {
                Object answer = message.getContent(contentFormat);
                if (answer != null) {
                    return answer;
                }
            }
        }
        return null;
    }

    /**
     * @deprecated please use {@link #createCxfMessage(HeaderFilterStrategy, CxfExchange)} instead
     */
    public static Message createCxfMessage(CxfExchange exchange) {
        return CxfBinding.createCxfMessage(new CxfHeaderFilterStrategy(), exchange);
    }

    public static Message createCxfMessage(HeaderFilterStrategy strategy, CxfExchange exchange) {

        Message answer = exchange.getInMessage();
        CxfMessage in = exchange.getIn();

        try {
            List body = in.getBody(List.class);
            answer.setContent(List.class, body);
            CxfHeaderHelper.propagateCamelToCxf(strategy, in.getHeaders(), answer);
        } catch (NoTypeConversionAvailableException ex) {
            try {
                InputStream body = in.getBody(InputStream.class);
                answer.setContent(InputStream.class, body);
            } catch (NoTypeConversionAvailableException ex2) {
            }
        }

        Map<String, Object> requestContext = CastUtils.cast((Map)answer.get(Client.REQUEST_CONTEXT));
        if (requestContext == null) {
            requestContext = new HashMap<String, Object>();
        }
        if (exchange.getExchange() != null) {
            requestContext.putAll(exchange.getExchange());
        }
        if (exchange.getProperties() != null) {
            requestContext.putAll(exchange.getProperties());
        }
        answer.put(Client.REQUEST_CONTEXT, requestContext);

        return answer;
    }

    /**
     * @deprecated please use {@link #storeCxfResponse(HeaderFilterStrategy, CxfExchange, Message)} instead.
     */
    public static void storeCxfResponse(CxfExchange exchange, Message response) {
        CxfBinding.storeCxfResponse(new CxfHeaderFilterStrategy(), exchange, response);
    }

    public static void storeCxfResponse(HeaderFilterStrategy strategy, CxfExchange exchange,
            Message response) {
        CxfMessage out = exchange.getOut();
        if (response != null) {
            CxfHeaderHelper.propagateCxfToCamel(strategy, response, out.getHeaders());
            out.setMessage(response);
            DataFormat dataFormat = (DataFormat) exchange.getProperty(CxfExchange.DATA_FORMAT);
            if (dataFormat.equals(DataFormat.MESSAGE)) {
                out.setBody(response.getContent(InputStream.class));
            }
            if (dataFormat.equals(DataFormat.PAYLOAD)) {
                out.setBody(response);
            }
        }
    }

    /**
     * @deprecated Please use {@link #copyMessage(HeaderFilterStrategy, org.apache.camel.Message, Message)} instead.
     */
    public static void copyMessage(org.apache.camel.Message camelMessage, org.apache.cxf.message.Message cxfMessage) {
        CxfBinding.copyMessage(new CxfHeaderFilterStrategy(), camelMessage, cxfMessage);
    }

    public static void copyMessage(HeaderFilterStrategy strategy,
            org.apache.camel.Message camelMessage, org.apache.cxf.message.Message cxfMessage) {

        CxfHeaderHelper.propagateCamelToCxf(strategy, camelMessage.getHeaders(), cxfMessage);
        try {
            InputStream is = camelMessage.getBody(InputStream.class);
            if (is != null) {
                cxfMessage.setContent(InputStream.class, is);
            }
        } catch (NoTypeConversionAvailableException ex) {
            Object result = camelMessage.getBody();
            if (result instanceof InputStream) {
                cxfMessage.setContent(InputStream.class, result);
            } else {
                cxfMessage.setContent(result.getClass(), result);
            }
        }
    }

    public static void storeCXfResponseContext(Message response, Map<String, Object> context) {
        if (context != null) {
            MessageContext messageContext = new WrappedMessageContext(context, null, Scope.HANDLER);
            response.put(Client.RESPONSE_CONTEXT, messageContext);
            Object value = context.get(Message.RESPONSE_CODE);
            if (value != null) {
                response.put(Message.RESPONSE_CODE, value);
            }
        }
    }

    public static void storeCxfResponse(CxfExchange exchange, Object response) {
        CxfMessage out = exchange.getOut();
        if (response != null) {
            out.setBody(response);
        }
    }

    public static void storeCxfFault(CxfExchange exchange, Message message) {
        CxfMessage fault = exchange.getFault();
        if (fault != null) {
            fault.setBody(getBody(message));
        }
    }


    public static Map<String, Object> propogateContext(Message message, Map<String, Object> context) {
        Map<String, Object> requestContext = CastUtils.cast((Map)message.get(Client.REQUEST_CONTEXT));
        Map<String, Object> responseContext = CastUtils.cast((Map)message.get(Client.RESPONSE_CONTEXT));
        if (requestContext != null) {
            Map<String, Object> realMap = new HashMap<String, Object>();
            WrappedMessageContext ctx = new WrappedMessageContext(realMap,
                                                                  null,
                                                                  Scope.APPLICATION);
            ctx.putAll(requestContext);
            requestContext = realMap;

        }

        if (responseContext == null) {
            responseContext = new HashMap<String, Object>();
        } else {
            responseContext.clear();
        }
        context.put(Client.REQUEST_CONTEXT, requestContext);
        context.put(Client.RESPONSE_CONTEXT, responseContext);
        return responseContext;

    }

}
