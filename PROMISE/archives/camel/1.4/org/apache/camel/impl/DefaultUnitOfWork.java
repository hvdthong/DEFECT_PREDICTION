package org.apache.camel.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.spi.Synchronization;
import org.apache.camel.spi.UnitOfWork;
import org.apache.camel.util.UuidGenerator;

/**
 * The default implementation of {@link UnitOfWork}
 *
 * @version $Revision: 670436 $
 */
public class DefaultUnitOfWork implements UnitOfWork {
    private static final UuidGenerator DEFAULT_ID_GENERATOR = new UuidGenerator();

    private String id;
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
                } else {
                    synchronization.onComplete(exchange);
                }
            }
        }
    }

    public boolean isSynchronous() {
        return asyncCallbacks == null || asyncCallbacks.isEmpty();
    }

    /**
     * Returns the unique ID of this unit of work, lazily creating one if it does not yet have one
     *
     * @return
     */
    public String getId() {
        if (id == null) {
            id = DEFAULT_ID_GENERATOR.generateId();
        }
        return id;
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
