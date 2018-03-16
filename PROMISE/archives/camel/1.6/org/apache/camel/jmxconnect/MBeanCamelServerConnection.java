package org.apache.camel.jmxconnect;

import org.apache.camel.Endpoint;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

/**
 * @version $Revision: 689344 $
 */
public interface MBeanCamelServerConnection extends MBeanServerConnection {

    /**
     * Add a Notification listener
     *
     * @param listenerId
     * @param name
     * @param replyToEndpoint
     */
    public void addNotificationListener(String listenerId, ObjectName name, Endpoint replyToEndpoint);

    /**
     * Remove a Notification listener
     *
     * @param listenerId
     */
    public void removeNotificationListener(String listenerId);

}
