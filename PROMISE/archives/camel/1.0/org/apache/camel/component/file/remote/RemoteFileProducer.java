package org.apache.camel.component.file.remote;

import org.apache.camel.impl.DefaultProducer;

public abstract class RemoteFileProducer<T extends RemoteFileExchange> extends DefaultProducer<T> {
    protected RemoteFileProducer(RemoteFileEndpoint<T> endpoint) {
        super(endpoint);
    }
}
