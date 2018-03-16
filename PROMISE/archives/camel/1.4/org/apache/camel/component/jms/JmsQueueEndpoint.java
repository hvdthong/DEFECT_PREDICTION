package org.apache.camel.component.jms;

import java.util.Collections;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.spi.BrowsableEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsOperations;

/**
 * An endpoint for a JMS Queue which is also browsable
 *
 * @version $Revision: 665824 $
 */
public class JmsQueueEndpoint extends JmsEndpoint implements BrowsableEndpoint<JmsExchange> {
    private static final transient Log LOG = LogFactory.getLog(JmsQueueEndpoint.class);

    private int maximumBrowseSize = -1;
    private final QueueBrowseStrategy queueBrowseStrategy;

    public JmsQueueEndpoint(String uri, JmsComponent component, String destination,
            JmsConfiguration configuration) {
        this(uri, component, destination, configuration, null);
    }

    public JmsQueueEndpoint(String uri, JmsComponent component, String destination,
            JmsConfiguration configuration, QueueBrowseStrategy queueBrowseStrategy) {
        super(uri, component, destination, false, configuration);
        if (queueBrowseStrategy == null) {
            this.queueBrowseStrategy = createQueueBrowseStrategy();
        } else {
            this.queueBrowseStrategy = queueBrowseStrategy;
        }
    }

    public JmsQueueEndpoint(String endpointUri, String destination, QueueBrowseStrategy queueBrowseStrategy) {
        super(endpointUri, destination, false);
        if (queueBrowseStrategy == null) {
            this.queueBrowseStrategy = createQueueBrowseStrategy();
        } else {
            this.queueBrowseStrategy = queueBrowseStrategy;
        }
    }

    public JmsQueueEndpoint(String endpointUri, String destination) {
        super(endpointUri, destination, false);
        queueBrowseStrategy = createQueueBrowseStrategy();
    }

    public int getMaximumBrowseSize() {
        return maximumBrowseSize;
    }

    /**
     * If a number is set > 0 then this limits the number of messages that are
     * returned when browsing the queue
     */
    public void setMaximumBrowseSize(int maximumBrowseSize) {
        this.maximumBrowseSize = maximumBrowseSize;
    }

    public List<Exchange> getExchanges() {
        if (queueBrowseStrategy == null) {
            return Collections.EMPTY_LIST;
        }
        String queue = getDestination();
        JmsOperations template = getConfiguration().createInOnlyTemplate(this, false, queue);
        return queueBrowseStrategy.browse(template, queue, this);
    }

    protected static QueueBrowseStrategy createQueueBrowseStrategy() {
        QueueBrowseStrategy answer = null;
        try {
            answer = JmsComponent.tryCreateDefaultQueueBrowseStrategy();
        } catch (Throwable e) {
            LOG.debug("Caught exception trying to create default QueueBrowseStrategy. "
                      + "This could be due to spring 2.0.x on classpath? Cause: " + e, e);
        }
        if (answer == null) {
            LOG.warn("Cannot browse queues as no QueueBrowseStrategy specified. Are you using Spring 2.0.x by any chance? If you upgrade to 2.5.x or later then queue browsing is supported");
        }
        return answer;
    }

}
