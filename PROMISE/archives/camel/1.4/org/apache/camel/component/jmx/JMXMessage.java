package org.apache.camel.component.jmx;

import javax.management.Notification;
import org.apache.camel.impl.DefaultMessage;

/**
 * A {@link org.apache.camel.Message Message} for a JMX Notification
 * 
 * @version $Revision: 642753 $
 */
public class JMXMessage extends DefaultMessage {

    private Notification notification;

    public JMXMessage() {
        this(null);
    }

    public JMXMessage(Notification notification) {
        this.notification = notification;
    }

    @Override
    public String toString() {
        return "JMXMessage: " + notification;
    }

    @Override
    public JMXExchange getExchange() {
        return (JMXExchange)super.getExchange();
    }

    @Override
    public JMXMessage newInstance() {
        return new JMXMessage();
    }

    public Notification getNotification() {
        return notification;
    }
}
