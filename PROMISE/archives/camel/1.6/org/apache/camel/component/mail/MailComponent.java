package org.apache.camel.component.mail;

import java.net.URI;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.HeaderFilterStrategyAware;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.DefaultHeaderFilterStrategy;
import org.apache.camel.spi.HeaderFilterStrategy;
import org.apache.camel.util.ObjectHelper;

/**
 * Component for JavaMail.
 *
 * @version $Revision:520964 $
 */
public class MailComponent extends DefaultComponent<MailExchange> implements HeaderFilterStrategyAware {
    private MailConfiguration configuration;
    private HeaderFilterStrategy headerFilterStrategy = new DefaultHeaderFilterStrategy();

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

    /**
     * Static builder method
     *
     * @deprecated will be removed in Camel 2.0
     */
    public static MailComponent mailComponent() {
        return new MailComponent();
    }

    /**
     * Static builder method
     *
     * @deprecated will be removed in Camel 2.0
     */
    public static MailComponent mailComponent(MailConfiguration configuration) {
        return new MailComponent(configuration);
    }

    @Override
    protected Endpoint<MailExchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        URI url = new URI(uri);
        if ("nntp".equalsIgnoreCase(url.getScheme())) {
            throw new UnsupportedOperationException("nntp protocol is not supported");
        }

        ObjectHelper.notNull(configuration, "configuration");
        MailConfiguration config = configuration.copy();

        config.configure(url);

        MailEndpoint endpoint = new MailEndpoint(uri, this, config);
        setProperties(endpoint.getConfiguration(), parameters);

        ObjectHelper.notEmpty(config.getHost(), "host");
        ObjectHelper.notEmpty(config.getProtocol(), "protocol");
        if (config.getPort() <= 0) {
            throw new IllegalArgumentException("port mut be specified");
        }

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

    public HeaderFilterStrategy getHeaderFilterStrategy() {
        return headerFilterStrategy;
    }

    public void setHeaderFilterStrategy(HeaderFilterStrategy strategy) {
        headerFilterStrategy = strategy;
    }
}
