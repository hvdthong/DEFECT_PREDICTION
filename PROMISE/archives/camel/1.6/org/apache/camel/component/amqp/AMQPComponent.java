package org.apache.camel.component.amqp;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.qpid.client.AMQConnectionFactory;
import org.apache.qpid.url.URLSyntaxException;

/**
 * @version $Revision: 640731 $
 */
public class AMQPComponent extends JmsComponent {

    public AMQPComponent() {
        init();
    }

    public AMQPComponent(JmsConfiguration configuration) {
        super(configuration);
    }

    public AMQPComponent(CamelContext context) {
        super(context);
        init();
    }


    public AMQPComponent(AMQConnectionFactory connectionFactory) {
        setConnectionFactory(connectionFactory);
    }

    public static Component amqpComponent(String uri) throws URLSyntaxException {
        AMQConnectionFactory connectionFactory = new AMQConnectionFactory(uri);
        return new AMQPComponent(connectionFactory);
    }

    /**
     * Lets install the default connection factory
     */
    private void init() {
        AMQConnectionFactory connectionFactory = new AMQConnectionFactory();
        setConnectionFactory(connectionFactory);
    }

}
