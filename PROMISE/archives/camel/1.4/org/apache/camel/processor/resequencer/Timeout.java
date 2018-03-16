package org.apache.camel.processor.resequencer;

import java.util.LinkedList;
import java.util.List;
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
 * @version $Revision
 */
public class Timeout extends TimerTask {
    
    private List<TimeoutHandler> timeoutHandlers;
    
    private Timer timer;
    
    private long timeout;
    
    /**
     * Creates a new timeout task using the given {@link Timer} instance a timeout value. The
     * task is not scheduled immediately. It will be scheduled by calling this
     * task's {@link #schedule()} method.
     * 
     * @param timer
     * @param timeout
     */
    public Timeout(Timer timer, long timeout) {
        this.timeoutHandlers = new LinkedList<TimeoutHandler>();
        this.timeout = timeout;
        this.timer = timer;
    }

    /**
     * Returns the list of timeout handlers that have been registered for
     * notification.
     * 
     * @return the list of timeout handlers
     */
    public List<TimeoutHandler> getTimeoutHandlers() {
        return timeoutHandlers;
    }
    
    /**
     * Appends a new timeout handler at the end of the timeout handler list.
     * 
     * @param handler a timeout handler.
     */
    public void addTimeoutHandler(TimeoutHandler handler) {
        timeoutHandlers.add(handler);
    }
    
    /**
     * inserts a new timeout handler at the beginning of the timeout handler
     * list.
     * 
     * @param handler a timeout handler.
     */
    public void addTimeoutHandlerFirst(TimeoutHandler handler) {
        timeoutHandlers.add(0, handler);
    }
    
    /**
     * Removes all timeout handlers from the timeout handler list. 
     */
    public void clearTimeoutHandlers() {
        this.timeoutHandlers.clear();
    }
    
    /**
     * Schedules this timeout task.
     */
    public void schedule() {
        timer.schedule(this, timeout);
    }

    /**
     * Notifies all timeout handlers about the scheduled timeout.
     */
    @Override
    public void run() {
        for (TimeoutHandler observer : timeoutHandlers) {
            observer.timeout(this);
        }
    }

}
