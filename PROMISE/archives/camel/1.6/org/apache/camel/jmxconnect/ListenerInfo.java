package org.apache.camel.jmxconnect;

import javax.management.NotificationFilter;
import javax.management.NotificationListener;

/**
 * @version $Revision: 689344 $
 */
class ListenerInfo {
    private String id;
    private NotificationListener listener;
    private NotificationFilter filter;
    private Object handback;


    public ListenerInfo(String id, NotificationListener listener, NotificationFilter filter, Object handback) {
        this.id = id;
        this.listener = listener;
        this.filter = filter;
        this.handback = handback;
    }

    /**
     * Is this info a match ?
     *
     * @param l
     * @param f
     * @param handback
     * @return true if a match
     */
    public boolean isMatch(NotificationListener l, NotificationFilter f, Object handback) {
        return listener == listener && filter == filter && handback == handback;
    }

    /**
     * @return Returns the filter.
     */
    public NotificationFilter getFilter() {
        return filter;
    }

    /**
     * @param filter The filter to set.
     */
    public void setFilter(NotificationFilter filter) {
        this.filter = filter;
    }

    /**
     * @return Returns the handback.
     */
    public Object getHandback() {
        return handback;
    }

    /**
     * @param handback The handback to set.
     */
    public void setHandback(Object handback) {
        this.handback = handback;
    }

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return Returns the listener.
     */
    public NotificationListener getListener() {
        return listener;
    }

    /**
     * @param listener The listener to set.
     */
    public void setListener(NotificationListener listener) {
        this.listener = listener;
    }

}
