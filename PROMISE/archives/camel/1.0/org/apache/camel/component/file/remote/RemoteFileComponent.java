package org.apache.camel.component.file.remote;

import org.apache.camel.CamelContext;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.util.IntrospectionSupport;

import java.net.URI;
import java.util.Map;

public class RemoteFileComponent extends DefaultComponent<RemoteFileExchange> {
    private RemoteFileConfiguration configuration;

    public RemoteFileComponent() {
        this.configuration = new RemoteFileConfiguration();
    }

    public RemoteFileComponent(RemoteFileConfiguration configuration) {
        this.configuration = configuration;
    }

    public RemoteFileComponent(CamelContext context) {
        super(context);
        this.configuration = new RemoteFileConfiguration();
    }

    public static RemoteFileComponent remoteFileComponent() {
        return new RemoteFileComponent();
    }

    protected RemoteFileEndpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        RemoteFileConfiguration config = getConfiguration().copy();
        config.configure(new URI(uri));

        final RemoteFileEndpoint endpoint;
        if ("ftp".equals(config.getProtocol())) {
            endpoint = new FtpEndpoint(uri, this, config);
        }
        else if ("sftp".equals(config.getProtocol())) {
            endpoint = new SftpEndpoint(uri, this, config);
        }
        else {
            throw new RuntimeCamelException("Unsupported protocol: " + config.getProtocol());
        }

        IntrospectionSupport.setProperties(endpoint.getConfiguration(), parameters);
        return endpoint;
    }

    public RemoteFileConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RemoteFileConfiguration configuration) {
        this.configuration = configuration;
    }
}
