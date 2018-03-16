package org.apache.camel.processor.resequencer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A timer task that notifies handlers about scheduled timeouts.
 * 
 * @see Timer
 * @see TimerTask
 * 
 * @author Martin Krasser
 * 
 * @version $Revision: 697732 $
 */
public class Timeout extends TimerTask {
    
    private TimeoutHandler timeoutHandler;
    
    private Timer timer;
    
    private long timeout;
    
    /**
     * Creates a new timeout task using the given {@link Timer} instance and
     * timeout value. The task is not scheduled immediately. It will be
     * scheduled by calling this task's {@link #schedule()} method.
     * 
     * @param timer
     *            a timer
     * @param timeout
     *            a timeout value.
     */
    public Timeout(Timer timer, long timeout) {
        this.timeout = timeout;
        this.timer = timer;
    }

    /**
     * Returns the timeout handler that has been registered for notification.
     * 
     * @return the timeout handler.
     */
    public TimeoutHandler getTimeoutHandlers() {
        return timeoutHandler;
    }
    
    /**
     * Sets a timeout handler for receiving timeout notifications.
     * 
     * @param timeoutHandler
     *            a timeout handler.
     */
    public void setTimeoutHandler(TimeoutHandler timeoutHandler) {
        this.timeoutHandler = timeoutHandler;
    }
    
    /**
     * Schedules this timeout task.
     */
    public void schedule() {
        timer.schedule(this, timeout);
    }

    /**
     * Notifies the timeout handler about the scheduled timeout.
     */
    @Override
    public void run() {
        timeoutHandler.timeout(this);
    }

}
