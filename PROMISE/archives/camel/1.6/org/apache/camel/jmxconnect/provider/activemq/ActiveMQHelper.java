package org.apache.camel.jmxconnect.provider.activemq;

import org.apache.camel.jmxconnect.CamelJmxConnectorSupport;
import org.apache.camel.CamelContext;
import org.apache.activemq.camel.component.ActiveMQComponent;

/**
 * @version $Revision: 689344 $
 */
public class ActiveMQHelper {
    public static String getDefaultEndpointUri() {
        return "activemq:" + CamelJmxConnectorSupport.DEFAULT_DESTINATION_PREFIX;
    }

    public static void configureActiveMQComponent(CamelContext camelContext, String brokerUrl) {
        ActiveMQComponent activemqComponent = camelContext.getComponent("activemq", ActiveMQComponent.class);
        activemqComponent.setBrokerURL(brokerUrl);
    }
}
