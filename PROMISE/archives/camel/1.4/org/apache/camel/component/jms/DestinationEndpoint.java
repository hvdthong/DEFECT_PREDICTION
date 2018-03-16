package org.apache.camel.component.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

/**
 * An optional interface that a {@link JmsEndpoint} may implement to return the
 * underlying {@link Destination} object
 *
 * @version $Revision: 652240 $
 */
public interface DestinationEndpoint {
    Destination getJmsDestination(Session session) throws JMSException;
}
