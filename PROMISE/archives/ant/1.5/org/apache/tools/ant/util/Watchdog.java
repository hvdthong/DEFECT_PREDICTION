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
 * @author <a href="stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author thomas.haas@softwired-inc.com
 * @author <a href="mailto:sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class Watchdog implements Runnable {

    private Vector observers = new Vector(1);
    private long timeout = -1;
    private boolean stopped = false;

    public Watchdog(long timeout) {
        if (timeout < 1) {
            throw new IllegalArgumentException("timeout lesser than 1.");
        }
        this.timeout = timeout;
    }

    public void addTimeoutObserver(TimeoutObserver to) {
        observers.addElement(to);
    }

    public void removeTimeoutObserver(TimeoutObserver to) {
        observers.removeElement(to);
    }

    protected final void fireTimeoutOccured() {
        Enumeration enum = observers.elements();
        while (enum.hasMoreElements()) {
            ((TimeoutObserver) enum.nextElement()).timeoutOccured(this);
        }
    }

    public synchronized void start() {
        stopped = false;
        Thread t = new Thread(this, "WATCHDOG");
        t.setDaemon(true);
        t.start();
    }

    public synchronized void stop() {
        stopped = true;
        notifyAll();
    }

    public synchronized void run() {
        final long until = System.currentTimeMillis() + timeout;
        long now;
        while (!stopped && until > (now = System.currentTimeMillis())) {
            try {
                wait(until - now);
            } catch (InterruptedException e) {}
        }
        if (!stopped) {
            fireTimeoutOccured();
        }
    }

}
