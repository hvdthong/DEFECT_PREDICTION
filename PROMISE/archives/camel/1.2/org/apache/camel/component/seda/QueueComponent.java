package org.apache.camel.component.seda;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.BlockingQueue;

/**
 * for asynchronous SEDA exchanges on a {@link BlockingQueue} within a CamelContext
 *
 * @version $Revision: 519973 $
 */
public class QueueComponent extends SedaComponent {
    private static final transient Log LOG = LogFactory.getLog(QueueComponent.class);

    public QueueComponent() {
        LOG.warn("This component has been deprecated; please use the seda: URI format instead of queue:");
    }
}
