package org.apache.camel.jmxconnect.provider.camel;

import org.apache.camel.jmxconnect.CamelJmxConnector;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorProvider;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

/**
 * @version $Revision: 689344 $
 */
public class ClientProvider implements JMXConnectorProvider {
    public JMXConnector newJMXConnector(JMXServiceURL url, Map environment) throws IOException {
        return new CamelJmxConnector(environment, url);
    }
}
