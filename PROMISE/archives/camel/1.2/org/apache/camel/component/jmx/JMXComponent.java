package org.apache.camel.component.jmx;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

import javax.management.MBeanServer;
import java.util.Map;

/**
 * for monitoring jmx attributes
 * 
 * @version $Revision: 523772 $
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
