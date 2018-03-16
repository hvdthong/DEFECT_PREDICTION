package org.apache.camel.spi;

import java.util.List;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;

/**
 * An optional interface an {@link Endpoint} may choose to implement which allows it to
 * expose a way of browsing the exchanges available.
 *
 * @version $Revision: 688279 $
 */
public interface BrowsableEndpoint<T extends Exchange> extends Endpoint<T> {

    /**
     * Return the exchanges available on this endpoint
     *
     * @return the exchanges on this endpoint
     */
    List<Exchange> getExchanges();
}
