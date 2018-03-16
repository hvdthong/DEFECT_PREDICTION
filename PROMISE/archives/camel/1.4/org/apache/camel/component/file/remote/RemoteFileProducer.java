package org.apache.camel.component.file.remote;

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
            String baseDir = "";
            if (endpointFileName.length() > 0) {
                baseDir = endpointFileName + (endpointFileName.endsWith("/") ? "" : "/");
            }
            String fileName = (headerFileName != null) ? headerFileName : message.getMessageId();
            answer = baseDir + fileName;
        } else {
            answer = endpointFileName;
        }
        return answer;
    }

    protected abstract void connectIfNecessary() throws Exception;

    protected abstract void disconnect() throws Exception;
}
