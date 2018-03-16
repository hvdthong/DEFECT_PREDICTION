package org.apache.camel.jmxconnect;


import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.management.Notification;
import javax.management.NotificationListener;
import java.util.Map;

/**
 * @version $Revision: 689344 $
 */
class ServerListenerInfo implements NotificationListener {
    private static final Log log = LogFactory.getLog(ServerListenerInfo.class);
    private final String id;
    private final Map holder;
    private final ProducerTemplate template;
    private final Endpoint replyToEndpoint;

    ServerListenerInfo(String id, Map holder, ProducerTemplate template, Endpoint replyToEndpoint) {
        this.id = id;
        this.holder = holder;
        this.template = template;
        this.replyToEndpoint = replyToEndpoint;
    }

    /**
     * NotificationListener implementation
     *
     * @param notification
     * @param handback
     */
    public void handleNotification(Notification notification, Object handback) {
        System.out.println("Should be sending notification: " + notification);
        if (replyToEndpoint == null) {
            log.warn("No replyToDestination for replies to be received so cannot send notification: " + notification);
        } else {
            template.sendBody(replyToEndpoint, notification);
        }
    }

    /**
     * close the info if the connection times out
     * <p/>
     * TODO we should auto-detect that id has timed out and then remove this subscription
     */
    public void close() {
        holder.remove(id);
    }

    /**
     * @return Returns the holder.
     */
    public Map getHolder() {
        return holder;
    }


    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

}
