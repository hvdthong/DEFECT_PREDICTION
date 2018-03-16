package org.apache.camel.jmxconnect.provider.camel;


import org.apache.camel.jmxconnect.CamelJmxConnectorServer;

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
        return new CamelJmxConnectorServer(url, environment, server);
    }
}
