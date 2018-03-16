package org.apache.camel.processor;

import java.io.Serializable;

/**
 * The base policy used when a fixed delay is needed.
 * <p/>
 * This policy is used by
 *
 * The default values is:
 * <ul>
 *   <li>delay = 1000L</li>
 * </ul>
 * <p/>
 *
 * @version $Revision: 693940 $
 */
public class DelayPolicy implements Cloneable, Serializable {

    protected long delay = 1000L;

    public DelayPolicy() {
    }

    @Override
    public String toString() {
        return "DelayPolicy[delay=" + delay + "]";
    }

    public DelayPolicy copy() {
        try {
            return (DelayPolicy)clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Could not clone: " + e, e);
        }
    }


    /**
     * Sets the delay in milliseconds
     */
    public DelayPolicy delay(long delay) {
        setDelay(delay);
        return this;
    }

    public long getDelay() {
        return delay;
    }

    /**
     * Sets the delay in milliseconds
     */
    public void setDelay(long delay) {
        this.delay = delay;
    }


}
