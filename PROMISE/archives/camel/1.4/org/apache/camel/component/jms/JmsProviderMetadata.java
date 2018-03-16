package org.apache.camel.component.jms;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;

import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.SessionCallback;

/**
 * A class which represents some metadata about the underlying JMS provider
 * so that we can properly bridge JMS providers such as for dealing with temporary destinations.
 *
 * @version $Revision: 652240 $
 */
public class JmsProviderMetadata {
    private Class<? extends TemporaryQueue> temporaryQueueType;
    private Class<? extends TemporaryTopic> temporaryTopicType;

    /**
     * Lazily loads the temporary queue type if one has not been explicitly configured
     * via calling the {@link #setTemporaryQueueType(Class)}
     */
    public Class<? extends TemporaryQueue> getTemporaryQueueType(JmsOperations template) {
        Class<? extends TemporaryQueue> answer = getTemporaryQueueType();
        if (answer == null) {
            loadTemporaryDestinationTypes(template);
            answer = getTemporaryQueueType();
        }
        return answer;
    }

    /**
     * Lazily loads the temporary topic type if one has not been explicitly configured
     * via calling the {@link #setTemporaryTopicType(Class)}
     */
    public Class<? extends TemporaryTopic> getTemporaryTopicType(JmsOperations template) {
        Class<? extends TemporaryTopic> answer = getTemporaryTopicType();
        if (answer == null) {
            loadTemporaryDestinationTypes(template);
            answer = getTemporaryTopicType();
        }
        return answer;
    }


    public Class<? extends TemporaryQueue> getTemporaryQueueType() {
        return temporaryQueueType;
    }

    public void setTemporaryQueueType(Class<? extends TemporaryQueue> temporaryQueueType) {
        this.temporaryQueueType = temporaryQueueType;
    }

    public Class<? extends TemporaryTopic> getTemporaryTopicType() {
        return temporaryTopicType;
    }

    public void setTemporaryTopicType(Class<? extends TemporaryTopic> temporaryTopicType) {
        this.temporaryTopicType = temporaryTopicType;
    }

    protected void loadTemporaryDestinationTypes(JmsOperations template) {
        if (template == null) {
            throw new IllegalArgumentException("No JmsTemplate supplied!");
        }
        template.execute(new SessionCallback() {
            public Object doInJms(Session session) throws JMSException {
                TemporaryQueue queue = session.createTemporaryQueue();
                setTemporaryQueueType(queue.getClass());

                TemporaryTopic topic = session.createTemporaryTopic();
                setTemporaryTopicType(topic.getClass());

                queue.delete();
                topic.delete();
                return null;
            }
        });
    }
}
