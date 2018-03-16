package org.apache.camel.component.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TemporaryQueue;

import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.SessionCallback;

/**
 * for working with a {@link TemporaryQueue}
 *
 * @version $Revision: 655516 $
 */
public class JmsTemporaryQueueEndpoint extends JmsQueueEndpoint implements DestinationEndpoint {
    private Destination jmsDestination;

    public JmsTemporaryQueueEndpoint(String uri, JmsComponent component, String destination, JmsConfiguration configuration) {
        super(uri, component, destination, configuration);
    }

    public JmsTemporaryQueueEndpoint(String uri, JmsComponent component, String destination, JmsConfiguration configuration, QueueBrowseStrategy queueBrowseStrategy) {
        super(uri, component, destination, configuration, queueBrowseStrategy);
    }

    public JmsTemporaryQueueEndpoint(String endpointUri, String destination) {
        super(endpointUri, destination);
    }

    /**
     * This endpoint is a singleton so that the temporary destination instances are shared across all
     * producers and consumers of the same endpoint URI
     *
     * @return true
     */
    public boolean isSingleton() {
        return true;
    }

    public synchronized Destination getJmsDestination(Session session) throws JMSException {
        if (jmsDestination == null) {
            jmsDestination = createJmsDestination(session);
        }
        return jmsDestination;
    }

    protected Destination createJmsDestination(Session session) throws JMSException {
        return session.createTemporaryQueue();
    }
}
