package org.apache.camel.component.file.remote;

import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;

import java.util.concurrent.ScheduledExecutorService;

public abstract class RemoteFileConsumer<T extends RemoteFileExchange> extends ScheduledPollConsumer<T> {
    RemoteFileEndpoint<T> endpoint;

    public RemoteFileConsumer(RemoteFileEndpoint<T> endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    public RemoteFileConsumer(RemoteFileEndpoint<T> endpoint, Processor processor, ScheduledExecutorService executor) {
        super(endpoint, processor, executor);
    }
}
