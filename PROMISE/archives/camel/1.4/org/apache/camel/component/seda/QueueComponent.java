package org.apache.camel.component.seda;

import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * for asynchronous SEDA exchanges on a {@link BlockingQueue} within a CamelContext
 *
 * @deprecated This component has been deprecated; please use the seda: URI format instead of queue: 
 * @version $Revision: 668065 $
 */
public class QueueComponent extends SedaComponent {
    private static final transient Log LOG = LogFactory.getLog(QueueComponent.class);

    public QueueComponent() {
        LOG.warn("This component has been deprecated; please use the seda: URI format instead of queue:");
    }
}
