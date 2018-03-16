package org.apache.camel.impl;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.camel.Service;

/**
 * A useful base class which ensures that a service is only initialized once and provides some helper methods for
 * enquiring of its status
 * 
 * @version $Revision: 534063 $
 */
public abstract class ServiceSupport implements Service {
    private AtomicBoolean started = new AtomicBoolean(false);
    private AtomicBoolean stopping = new AtomicBoolean(false);
    private AtomicBoolean stopped = new AtomicBoolean(false);

    public void start() throws Exception {
        if (started.compareAndSet(false, true)) {
            doStart();
        }
    }

    public void stop() throws Exception {
        if (stopped.compareAndSet(false, true)) {
            stopping.set(true);
            try {
                doStop();
            }
            finally {
                stopped.set(true);
                started.set(false);
                stopping.set(false);
            }
        }
    }

    /**
     * @return true if this service has been started
     */
    public boolean isStarted() {
        return started.get();
    }

    /**
     * @return true if this service is in the process of closing
     */
    public boolean isStopping() {
        return stopping.get();
    }


    /**
     * @return true if this service is closed
     */
    public boolean isStopped() {
        return stopped.get();
    }

    protected abstract void doStart() throws Exception;
    
    protected abstract void doStop() throws Exception;

}
