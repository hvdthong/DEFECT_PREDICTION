package org.apache.camel.component.mail;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.util.IntrospectionSupport;

import java.util.Map;
import java.net.URI;
import java.net.URL;

/**
 * @version $Revision:520964 $
 */
public class MailComponent extends DefaultComponent<MailExchange> {
    public static final String QUEUE_PREFIX = "queue:";
    public static final String TOPIC_PREFIX = "topic:";
    private MailConfiguration configuration;

    /**
     * Static builder method
     */
    public static MailComponent mailComponent() {
        return new MailComponent();
    }

    /**
     * Static builder method
     */
    public static MailComponent mailComponent(MailConfiguration configuration) {
        return new MailComponent(configuration);
    }

    public MailComponent() {
        this.configuration = new MailConfiguration();
    }

    public MailComponent(MailConfiguration configuration) {
        this.configuration = configuration;
    }

    public MailComponent(CamelContext context) {
        super(context);
        this.configuration = new MailConfiguration();
    }

    @Override
    protected Endpoint<MailExchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {

        MailConfiguration config = getConfiguration().copy();
        config.configure(new URI(uri));

        MailEndpoint endpoint = new MailEndpoint(uri, this, config);

        IntrospectionSupport.setProperties(endpoint.getConfiguration(), parameters);
        return endpoint;
    }

    public MailConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Sets the Mail configuration
     *
     * @param configuration the configuration to use by default for endpoints
     */
    public void setConfiguration(MailConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * A strategy method allowing the URI destination to be translated into the actual Mail destination name
     * (say by looking up in JNDI or something)
     */
    protected String convertPathToActualDestination(String path) {
        return path;
    }
}
