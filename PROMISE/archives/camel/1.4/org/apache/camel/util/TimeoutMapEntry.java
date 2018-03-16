package org.apache.camel.util;

import java.util.Map;

/**
 * Represents an entry in a {@link TimeoutMap}
 *
 * @version $Revision: 640438 $
 */
public class TimeoutMapEntry implements Comparable, Map.Entry {
    private Object key;
    private Object value;
    private long timeout;
    private long expireTime;

    public TimeoutMapEntry(Object id, Object handler, long timeout) {
        this.key = id;
        this.value = handler;
        this.timeout = timeout;
    }

    public Object getKey() {
        return key;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public Object getValue() {
        return value;
    }

    public Object setValue(Object value) {
        Object oldValue = value;
        this.value = value;
        return oldValue;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int compareTo(Object that) {
        if (this == that) {
            return 0;
        }
        if (that instanceof TimeoutMapEntry) {
            return compareTo((TimeoutMapEntry) that);
        }
        return 1;
    }

    public int compareTo(TimeoutMapEntry that) {
        long diff = this.expireTime - that.expireTime;
        if (diff > 0) {
            return 1;
        } else if (diff < 0) {
            return -1;
        }
        return this.key.hashCode() - that.key.hashCode();
    }

    public String toString() {
        return "Entry for key: " + key;
    }
}
