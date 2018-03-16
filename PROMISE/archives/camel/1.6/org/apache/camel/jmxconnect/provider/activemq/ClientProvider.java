package org.apache.camel.jmxconnect.provider.activemq;

import org.apache.camel.jmxconnect.CamelJmxConnector;
import org.apache.camel.jmxconnect.CamelJmxConnectorSupport;
import org.apache.camel.CamelContext;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorProvider;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Map;

/**
 * @version $Revision: 689344 $
 */
public class ClientProvider implements JMXConnectorProvider {
    public JMXConnector newJMXConnector(JMXServiceURL url, Map environment) throws IOException {
        String brokerUrl = CamelJmxConnectorSupport.getEndpointUri(url, "activemq");
        CamelJmxConnector answer = new CamelJmxConnector(environment, ActiveMQHelper.getDefaultEndpointUri());

        CamelContext camelContext = answer.getCamelContext();
        ActiveMQHelper.configureActiveMQComponent(camelContext, brokerUrl);
        return answer;
    }

}
