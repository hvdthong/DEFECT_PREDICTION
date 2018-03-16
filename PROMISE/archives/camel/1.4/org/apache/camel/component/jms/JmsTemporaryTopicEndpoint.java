package org.apache.camel.component.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TemporaryTopic;

import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.SessionCallback;

/**
 * for working with a {@link TemporaryTopic}
 *
 * @version $Revision: 655516 $
 */
public class JmsTemporaryTopicEndpoint extends JmsEndpoint implements DestinationEndpoint {
    private Destination jmsDestination;

    public JmsTemporaryTopicEndpoint(String uri, JmsComponent component, String destination, JmsConfiguration configuration) {
        super(uri, component, destination, true, configuration);
    }

    public JmsTemporaryTopicEndpoint(String endpointUri, String destination) {
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
        return session.createTemporaryTopic();
    }


}
