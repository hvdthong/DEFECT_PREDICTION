package org.apache.camel.spi;

import org.apache.camel.Exchange;

/**
 * An object representing the unit of work processing an {@link Exchange}
 * which allows the use of {@link Synchronization} hooks. This object might map one-to-one with
 * a transaction in JPA or Spring; or might not.
 *
 * @version $Revision: 688279 $
 */
public interface UnitOfWork {

    /**
     * Adds a synchronization hook
     *
     * @param synchronization  the hook
     */
    void addSynchronization(Synchronization synchronization);

    /**
     * Removes a synchronization hook
     *
     * @param synchronization  the hook
     */
    void removeSynchronization(Synchronization synchronization);

    /**
     * Invoked when this unit of work has been completed, whether it has failed or completed
     *
     * @param exchange the current exchange
     */
    void done(Exchange exchange);

    /**
     * Returns the unique ID of this unit of work, lazily creating one if it does not yet have one
     * 
     * @return the unique ID
     */
    String getId();
}
