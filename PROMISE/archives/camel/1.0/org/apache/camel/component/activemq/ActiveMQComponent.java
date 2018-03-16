package org.apache.camel.component.activemq;

import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;

/**
 *
 * @version $Revision: 1.1 $
 */
public class ActiveMQComponent extends JmsComponent {
    /**
     *
     * @return the created component
     */
    public static ActiveMQComponent activeMQComponent() {
        return new ActiveMQComponent();
    }

    /**
     *
     * @param brokerURL the URL to connect to
     * @return the created component
     */
    public static ActiveMQComponent activeMQComponent(String brokerURL) {
        ActiveMQComponent answer = new ActiveMQComponent();
        answer.getConfiguration().setBrokerURL(brokerURL);
        return answer;
    }

    public ActiveMQComponent() {
    }

    public ActiveMQComponent(CamelContext context) {
        super(context);
    }

    public ActiveMQComponent(ActiveMQConfiguration configuration) {
        super(configuration);
    }

    @Override
    public ActiveMQConfiguration getConfiguration() {
        return (ActiveMQConfiguration) super.getConfiguration();
    }

    public void setBrokerURL(String brokerURL) {
        getConfiguration().setBrokerURL(brokerURL);
    }


    @Override
    protected JmsConfiguration createConfiguration() {
        return new ActiveMQConfiguration();
    }
}
