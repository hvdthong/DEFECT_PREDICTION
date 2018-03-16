package org.apache.camel.component.jmx;

import java.util.Map;

import javax.management.MBeanServer;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * for monitoring JMX attributes
 *
 * @version $Revision: 659782 $
 */
public class JMXComponent extends DefaultComponent<JMXExchange> {
    private MBeanServer mbeanServer;

    public JMXComponent() {
    }

    public JMXComponent(CamelContext context) {
        super(context);
    }

    protected Endpoint<JMXExchange> createEndpoint(String uri, String remaining, Map parameters)
        throws Exception {

        JMXEndpoint result = new JMXEndpoint(remaining, this);
        setProperties(result, parameters);
        result.setMbeanServer(getMbeanServer());
        return result;
    }

    public MBeanServer getMbeanServer() {
        return mbeanServer;
    }

    public void setMbeanServer(MBeanServer mbeanServer) {
        this.mbeanServer = mbeanServer;
    }
}
