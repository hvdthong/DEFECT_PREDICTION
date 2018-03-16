package org.apache.tools.ant.util;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Generalization of <code>ExecuteWatchdog</code>
 *
 * @since Ant 1.5
 *
 * @see org.apache.tools.ant.taskdefs.ExecuteWatchdog
 *
 */
public class Watchdog implements Runnable {

    private Vector observers = new Vector(1);
    private long timeout = -1;
    private boolean stopped = false;

    /**
     * Constructor for Watchdog.
     * @param timeout the timeout to use in milliseconds (must be >= 1).
     */
    public Watchdog(long timeout) {
        if (timeout < 1) {
            throw new IllegalArgumentException("timeout less than 1.");
        }
        this.timeout = timeout;
    }

    /**
     * Add a timeout observer.
     * @param to the timeout observer to add.
     */
    public void addTimeoutObserver(TimeoutObserver to) {
        observers.addElement(to);
    }

    /**
     * Remove a timeout observer.
     * @param to the timeout observer to remove.
     */
    public void removeTimeoutObserver(TimeoutObserver to) {
        observers.removeElement(to);
    }

    /**
     * Inform the observers that a timeout has occured.
     */
    protected final void fireTimeoutOccured() {
        Enumeration e = observers.elements();
        while (e.hasMoreElements()) {
            ((TimeoutObserver) e.nextElement()).timeoutOccured(this);
        }
    }

    /**
     * Start the watch dog.
     */
    public synchronized void start() {
        stopped = false;
        Thread t = new Thread(this, "WATCHDOG");
        t.setDaemon(true);
        t.start();
    }

    /**
     * Stop the watch dog.
     */
    public synchronized void stop() {
        stopped = true;
        notifyAll();
    }

    /**
     * The run method of the watch dog thread.
     * This simply does a wait for the timeout time, and
     * if the stop flag has not been set when the wait has returned or
     * has been interrupted, the watch dog listeners are informed.
     */
    public synchronized void run() {
        final long until = System.currentTimeMillis() + timeout;
        long now;
        while (!stopped && until > (now = System.currentTimeMillis())) {
            try {
                wait(until - now);
            } catch (InterruptedException e) {
            }
        }
        if (!stopped) {
            fireTimeoutOccured();
        }
    }

}
