package org.apache.camel.component.activemq;

import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsConfiguration;

import javax.jms.ConnectionFactory;

/**
 * @version $Revision: 1.1 $
 */
public class ActiveMQConfiguration extends JmsConfiguration {
    private String brokerURL = ActiveMQConnectionFactory.DEFAULT_BROKER_URL;

    public ActiveMQConfiguration() {
    }

    public String getBrokerURL() {
        return brokerURL;
    }

    /**
     * Sets the broker URL to use to connect to ActiveMQ using the
     *
     * @param brokerURL the URL of the broker.
     */
    public void setBrokerURL(String brokerURL) {
        this.brokerURL = brokerURL;
    }

    @Override
    public ActiveMQConnectionFactory getListenerConnectionFactory() {
        return (ActiveMQConnectionFactory) super.getListenerConnectionFactory();
    }

    @Override
    public void setListenerConnectionFactory(ConnectionFactory listenerConnectionFactory) {
        if (listenerConnectionFactory instanceof ActiveMQConnectionFactory) {
            super.setListenerConnectionFactory(listenerConnectionFactory);
        }
        else {
            throw new IllegalArgumentException("ConnectionFactory " + listenerConnectionFactory
                    + " is not an instanceof " + ActiveMQConnectionFactory.class.getName());
        }
    }

    @Override
    protected ConnectionFactory createListenerConnectionFactory() {
        ActiveMQConnectionFactory answer = new ActiveMQConnectionFactory();
        answer.setBrokerURL(getBrokerURL());
        return answer;
    }

    @Override
    protected ConnectionFactory createTemplateConnectionFactory() {
        return new PooledConnectionFactory(getListenerConnectionFactory());
    }
}
