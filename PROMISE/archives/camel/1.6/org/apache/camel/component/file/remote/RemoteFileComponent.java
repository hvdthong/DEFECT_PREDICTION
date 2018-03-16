package org.apache.camel.component.file.remote;

import java.net.URI;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.DefaultComponent;
import org.apache.commons.net.ftp.FTPClientConfig;

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

    public String toString() {
        return "RemoteFileComponent";
    }

    public static RemoteFileComponent remoteFileComponent() {
        return new RemoteFileComponent();
    }

    protected RemoteFileEndpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        RemoteFileConfiguration config = getConfiguration().copy();

        String baseUri = uri;
        if (uri.indexOf("?") != -1) {
            baseUri = uri.substring(0, uri.indexOf("?"));
        }
        config.configure(new URI(baseUri));

        final RemoteFileEndpoint endpoint;
        if ("ftp".equals(config.getProtocol())) {
            endpoint = new FtpEndpoint(uri, this, config);
        } else if ("sftp".equals(config.getProtocol())) {
            endpoint = new SftpEndpoint(uri, this, config);
        } else {
            throw new RuntimeCamelException("Unsupported protocol: " + config.getProtocol());
        }

        configureFTPClientConfig(parameters, endpoint);
        setProperties(endpoint.getConfiguration(), parameters);
        return endpoint;
    }

    private void configureFTPClientConfig(Map parameters, RemoteFileEndpoint endpoint) {
        String ref = getAndRemoveParameter(parameters, "ftpClientConfig", String.class);
        if (ref != null) {
            FTPClientConfig ftpClientConfig = this.getCamelContext().getRegistry().lookup(ref, FTPClientConfig.class);
            if (ftpClientConfig == null) {
                throw new IllegalArgumentException("FTPClientConfig " + ref + " not found in registry.");
            }
            endpoint.getConfiguration().setFtpClientConfig(ftpClientConfig);
        }
    }

    public RemoteFileConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RemoteFileConfiguration configuration) {
        this.configuration = configuration;
    }
}
