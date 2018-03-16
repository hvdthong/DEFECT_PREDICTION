package org.apache.camel.component.file.remote;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.file.FileComponent;
import org.apache.camel.impl.DefaultProducer;

public abstract class RemoteFileProducer<T extends RemoteFileExchange> extends DefaultProducer<T> {

    protected RemoteFileProducer(RemoteFileEndpoint<T> endpoint) {
        super(endpoint);
    }

    protected String createFileName(Message message, RemoteFileConfiguration fileConfig) {
        String answer;
        String endpointFileName = fileConfig.getFile();
        String headerFileName = message.getHeader(FileComponent.HEADER_FILE_NAME, String.class);
        if (fileConfig.isDirectory()) {
            if (headerFileName != null) {
                answer = endpointFileName + "/" + headerFileName;
            } else {
                answer = endpointFileName + "/" + message.getMessageId();
            }
        } else {
            answer = endpointFileName;
        }
        return answer;
    }
}
