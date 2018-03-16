package org.apache.camel.component.cxf;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.jaxws.support.ContextPropertiesMapping;
import org.apache.cxf.message.Message;

/**
 * The binding/mapping of Camel messages to Apache CXF and back again
 *
 * @version $Revision: 676805 $
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

    public static Message createCxfMessage(CxfExchange exchange) {
        Message answer = exchange.getInMessage();
        CxfMessage in = exchange.getIn();
        Object body = in.getBody(List.class);
        if (body instanceof List) {
            answer.setContent(List.class, body);
            answer.put(CxfConstants.OPERATION_NAME, (String)in.getHeader(CxfConstants.OPERATION_NAME));
            answer.put(CxfConstants.OPERATION_NAMESPACE, (String)in.getHeader(CxfConstants.OPERATION_NAMESPACE));
        } else {
            body = in.getBody(InputStream.class);
            if (body instanceof InputStream) {
                answer.setContent(InputStream.class, body);
            }
        }
        return answer;
    }

    public static void storeCxfResponse(CxfExchange exchange, Message response) {
        CxfMessage out = exchange.getOut();
        if (response != null) {
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

    public static void copyMessage(org.apache.camel.Message camelMessage, org.apache.cxf.message.Message cxfMessage) {
        InputStream is = camelMessage.getBody(InputStream.class);
        if (is != null) {
            cxfMessage.setContent(InputStream.class, is);
        } else {
            Object result = camelMessage.getBody();
            if (result != null) {
                if (result instanceof InputStream) {
                    cxfMessage.setContent(InputStream.class, result);
                } else {
                    cxfMessage.setContent(result.getClass(), result);
                }
            }
        }
    }

    public static void storeCXfResponseContext(Message response, Map<String, Object> context) {
        if (context != null) {
            ContextPropertiesMapping.mapResponsefromCxf2Jaxws(context);
            response.put(Client.RESPONSE_CONTEXT, context);
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
            ContextPropertiesMapping.mapRequestfromJaxws2Cxf(requestContext);
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
