package org.apache.camel.jmxconnect;

import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version $Revision: 689344 $
 */
public class MBeanCamelServerConnectionImpl extends MBeanServerConnectionDelegate implements MBeanCamelServerConnection {
    private static final Log log = LogFactory.getLog(MBeanCamelServerConnectionImpl.class);
    private Map notificationListeners = new ConcurrentHashMap();
    private final ProducerTemplate template;

    public MBeanCamelServerConnectionImpl(MBeanServerConnection connection, ProducerTemplate template) {
        super(connection);
        this.template = template;
    }

    /**
     * Add a Notification listener
     *
     * @param listenerId
     * @param name
     * @param replyToEndpoint
     */
    public void addNotificationListener(String listenerId, ObjectName name, Endpoint replyToEndpoint) {
        try {
            ServerListenerInfo info = new ServerListenerInfo(listenerId, notificationListeners, template, replyToEndpoint);
            notificationListeners.put(listenerId, info);
            connection.addNotificationListener(name, info, null, null);
        } catch (Exception e) {
            log.error("Failed to addNotificationListener ", e);
        }

    }

    /**
     * Remove a Notification listener
     *
     * @param listenerId
     */
    public void removeNotificationListener(String listenerId) {
        ServerListenerInfo info = (ServerListenerInfo) notificationListeners.remove(listenerId);
        if (info != null) {
            info.close();
        }
    }

}
