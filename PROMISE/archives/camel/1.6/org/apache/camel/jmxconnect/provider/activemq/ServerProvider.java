package org.apache.camel.jmxconnect.provider.activemq;


import org.apache.camel.jmxconnect.CamelJmxConnectorServer;
import org.apache.camel.jmxconnect.CamelJmxConnectorSupport;
import org.apache.camel.CamelContext;

import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerProvider;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

/**
 * @version $Revision: 689344 $
 */
public class ServerProvider implements JMXConnectorServerProvider {

    public JMXConnectorServer newJMXConnectorServer(JMXServiceURL url, Map environment, MBeanServer server) throws IOException {
        String brokerUrl = CamelJmxConnectorSupport.getEndpointUri(url, "activemq");
        CamelJmxConnectorServer answer = new CamelJmxConnectorServer(url, ActiveMQHelper.getDefaultEndpointUri(), environment, server);

        CamelContext camelContext = answer.getCamelContext();
        ActiveMQHelper.configureActiveMQComponent(camelContext, brokerUrl);
        return answer;
    }
}
