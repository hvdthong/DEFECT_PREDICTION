package org.apache.camel.jmxconnect;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.util.UuidGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.management.*;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @version $Revision: 689344 $
 */
public class MBeanCamelServerConnectionClient extends MBeanServerConnectionDelegate implements Processor {
    private static final Log log = LogFactory.getLog(MBeanCamelServerConnectionClient.class);
    private MBeanCamelServerConnection serverConnection;
    private Endpoint replyToEndpoint;
    private List listeners = new CopyOnWriteArrayList();
    private UuidGenerator idGenerator = new UuidGenerator();
    private NotificationBroadcasterSupport localNotifier = new NotificationBroadcasterSupport();

    public MBeanCamelServerConnectionClient(MBeanCamelServerConnection serverConnection) {
        super(serverConnection);
        this.serverConnection = serverConnection;
    }

    /**
     * Add a notification listener
     */
    public void addNotificationListener(ObjectName name, NotificationListener listener, NotificationFilter filter,
                                        Object handback) {
        String id = generateId();
        ListenerInfo info = new ListenerInfo(id, listener, filter, handback);
        listeners.add(info);
        localNotifier.addNotificationListener(listener, filter, handback);

        if (replyToEndpoint == null) {
            log.error("no replyToDestination for replies to be received!");
        }
        serverConnection.addNotificationListener(id, name, replyToEndpoint);
    }

    public String generateId() {
        return idGenerator.generateId();
    }

    /**
     * Remove a Notification Listener
     */
    public void removeNotificationListener(ObjectName name, NotificationListener listener)
            throws ListenerNotFoundException {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            ListenerInfo li = (ListenerInfo) i.next();
            if (li.getListener() == listener) {
                listeners.remove(i);
                serverConnection.removeNotificationListener(li.getId());
                break;
            }
        }
        localNotifier.removeNotificationListener(listener);
    }

    /**
     * Remove a Notification Listener
     */
    public void removeNotificationListener(ObjectName name, NotificationListener listener, NotificationFilter filter,
                                           Object handback) throws ListenerNotFoundException {
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            ListenerInfo li = (ListenerInfo) i.next();
            if (li.getListener() == listener && li.getFilter() == filter && li.getHandback() == handback) {
                listeners.remove(i);
                serverConnection.removeNotificationListener(li.getId());
            }
        }
        localNotifier.removeNotificationListener(listener, filter, handback);
    }


    public void process(Exchange exchange) throws Exception {
        Notification notification = exchange.getIn().getBody(Notification.class);
        if (notification != null) {
            localNotifier.sendNotification(notification);
        } else {
            log.warn("Received message which is not a Notification: " + exchange);
        }
    }
}
