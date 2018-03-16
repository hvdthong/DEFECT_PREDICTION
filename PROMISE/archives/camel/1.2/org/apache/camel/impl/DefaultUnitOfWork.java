package org.apache.camel.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.spi.Synchronization;
import org.apache.camel.spi.UnitOfWork;

/**
 * The default implementation of {@link UnitOfWork}
 *
 * @version $Revision: 1.1 $
 */
public class DefaultUnitOfWork implements UnitOfWork {
    private List<Synchronization> synchronizations;
    private List<AsyncCallback> asyncCallbacks;
    private CountDownLatch latch;

    public DefaultUnitOfWork() {
    }

    public synchronized void addSynchronization(Synchronization synchronization) {
        if (synchronizations == null) {
            synchronizations = new ArrayList<Synchronization>();
        }
        synchronizations.add(synchronization);
    }

    public synchronized void removeSynchronization(Synchronization synchronization) {
        if (synchronizations != null) {
            synchronizations.remove(synchronization);
        }
    }

    public void reset() {

    }

    public void done(Exchange exchange) {
        if (synchronizations != null) {
            boolean failed = exchange.isFailed();
            for (Synchronization synchronization : synchronizations) {
                if (failed) {
                    synchronization.onFailure(exchange);
                }
                else {
                    synchronization.onComplete(exchange);
                }
            }
        }
    }

    public boolean isSynchronous() {
        return asyncCallbacks == null || asyncCallbacks.isEmpty();
    }

    /**
     * Register some asynchronous processing step
     */
    /*
    public synchronized AsyncCallback addAsyncStep() {
        AsyncCallback answer = new AsyncCallback() {
            public void done(boolean doneSynchronously) {
                latch.countDown();
            }
        };
        if (latch == null) {
            latch = new CountDownLatch(1);
        }
        else {
        }
        if (asyncCallbacks == null) {
            asyncCallbacks = new ArrayList<AsyncCallback>();
        }
        asyncCallbacks.add(answer);
        return answer;
    }
    */
}
