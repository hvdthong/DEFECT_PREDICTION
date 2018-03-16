package org.apache.camel.builder.xml;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathVariableResolver;
import java.util.HashMap;
import java.util.Map;

/**
 * A variable resolver for XPath expressions which support properties on the messge, exchange as well
 * as making system properties and environment properties available.
 *
 * @version $Revision: 521692 $
 */
public class MessageVariableResolver implements XPathVariableResolver {

    private static final transient Log log = LogFactory.getLog(MessageVariableResolver.class);

    private Exchange exchange;
    private Map<String, Object> variables = new HashMap<String, Object>();

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public Object resolveVariable(QName name) {
        String uri = name.getNamespaceURI();
        String localPart = name.getLocalPart();
        Object answer = null;

        if (uri == null || uri.length() == 0) {
            answer = variables.get(localPart);
            if (answer == null) {
                Message message = exchange.getIn();
                if (message != null) {
                    answer = message.getHeader(localPart);
                }
                if (answer == null) {
                    answer = exchange.getProperty(localPart);
                }
            }
        }
        else if (uri.equals(SYSTEM_PROPERTIES_NAMESPACE)) {
            try {
                answer = System.getProperty(localPart);
            }
            catch (Exception e) {
                log.debug("Security exception evaluating system property: " + localPart + ". Reason: " + e, e);
            }
        }
        else if (uri.equals(ENVIRONMENT_VARIABLES)) {
            answer = System.getenv().get(localPart);
        }
        else if (uri.equals(EXCHANGE_PROPERTY)) {
            answer = exchange.getProperty(localPart);
        }
        else if (uri.equals(IN_HEADER)) {
            answer = exchange.getIn().getHeader(localPart);
        }
        else if (uri.equals(OUT_HEADER)) {
            answer = exchange.getOut().getHeader(localPart);
        }

        return answer;
    }

    public void addVariable(String localPart, Object value) {
        variables.put(localPart, value);
    }
}
