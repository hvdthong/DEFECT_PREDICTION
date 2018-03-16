package org.apache.camel.component.file.remote;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultExchange;

import java.io.ByteArrayOutputStream;

public class RemoteFileExchange<T extends RemoteFileBinding> extends DefaultExchange {
    private T binding;

    public RemoteFileExchange(CamelContext context, T binding) {
        super(context);
        this.binding = binding;
    }

    public RemoteFileExchange(CamelContext context, T binding, String host, String fullFileName, ByteArrayOutputStream outputStream) {
        this(context, binding);
        setIn(new RemoteFileMessage(host, fullFileName, outputStream));
    }

    public T getBinding() {
        return binding;
    }

    public void setBinding(T binding) {
        this.binding = binding;
    }
}
