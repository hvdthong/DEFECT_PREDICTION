package org.apache.camel.component.jmx;

import javax.management.Notification;
import javax.management.NotificationListener;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;

/**
 * Creates an JMXExchange after getting a JMX Notification
 * 
 * @version $Revision: 660275 $
 */
public class JMXConsumer extends DefaultConsumer implements NotificationListener {

    private JMXEndpoint jmxEndpoint;

    public JMXConsumer(JMXEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.jmxEndpoint = endpoint;
    }

    public void handleNotification(Notification notification, Object handback) {
        try {
            getProcessor().process(jmxEndpoint.createExchange(notification));
        } catch (Throwable e) {
            handleException(e);
        }
    }
}
