package org.apache.camel.component.file.remote;

import java.io.ByteArrayOutputStream;

import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.ScheduledPollEndpoint;

public abstract class RemoteFileEndpoint<T extends RemoteFileExchange> extends ScheduledPollEndpoint<T> {
    private RemoteFileBinding binding;
    private RemoteFileConfiguration configuration;

    public RemoteFileEndpoint(String uri, RemoteFileComponent component, RemoteFileConfiguration configuration) {
        super(uri, component);
        this.configuration = configuration;
    }

    protected RemoteFileEndpoint(String endpointUri, RemoteFileConfiguration configuration) {
        super(endpointUri);
        this.configuration = configuration;
    }

    protected RemoteFileEndpoint(String endpointUri) {
        this(endpointUri, new RemoteFileConfiguration());
    }

    protected RemoteFileBinding createRemoteFileBinding() {
        return new RemoteFileBinding();
    }

    public T createExchange() {
        return (T) new RemoteFileExchange(getCamelContext(), getExchangePattern(), getBinding());
    }

    public T createExchange(ExchangePattern pattern) {
        return (T) new RemoteFileExchange(getCamelContext(), pattern, getBinding());
    }

    public T createExchange(String fullFileName, ByteArrayOutputStream outputStream) {
        return (T) new RemoteFileExchange(getCamelContext(), getExchangePattern(), getBinding(), getConfiguration().getHost(), fullFileName, outputStream);
    }

    public RemoteFileBinding getBinding() {
        if (binding == null) {
            binding = createRemoteFileBinding();
        }
        return binding;
    }

    public void setBinding(RemoteFileBinding binding) {
        this.binding = binding;
    }

    public boolean isSingleton() {
        return true;
    }

    public RemoteFileConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RemoteFileConfiguration configuration) {
        this.configuration = configuration;
    }
}
