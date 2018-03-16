package org.apache.camel.component.file.remote;

import java.io.OutputStream;

public class RemoteFileBinding {
    public Object extractBodyFromOutputStream(RemoteFileExchange exchange, OutputStream outputStream) {
        return outputStream;
    }
}
