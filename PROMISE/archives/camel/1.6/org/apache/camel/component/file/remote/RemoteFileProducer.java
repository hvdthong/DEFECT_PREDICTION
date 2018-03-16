package org.apache.camel.component.file.remote;

import org.apache.camel.Expression;
import org.apache.camel.Message;
import org.apache.camel.component.file.FileComponent;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.language.simple.FileLanguage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class RemoteFileProducer<T extends RemoteFileExchange> extends DefaultProducer<T> {
    protected final transient Log log = LogFactory.getLog(getClass());
    protected RemoteFileEndpoint<T> endpoint;

    protected RemoteFileProducer(RemoteFileEndpoint<T> endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    protected String createFileName(Message message, RemoteFileConfiguration fileConfig) {
        String answer;

        String name = message.getHeader(FileComponent.HEADER_FILE_NAME, String.class);

        Expression expression = endpoint.getConfiguration().getExpression();
        if (name != null) {
            if (name.indexOf("${") > -1) {
                if (log.isDebugEnabled()) {
                    log.debug(FileComponent.HEADER_FILE_NAME + " contains a FileLanguage expression: " + name);
                }
                expression = FileLanguage.file(name);
            }
        }
        if (expression != null) {
            if (log.isDebugEnabled()) {
                log.debug("Filename evaluated as expression: " + expression);
            }
            Object result = expression.evaluate(message.getExchange());
            name = message.getExchange().getContext().getTypeConverter().convertTo(String.class, result);
        }        

        String endpointFile = fileConfig.getFile();
        if (fileConfig.isDirectory()) {
            String baseDir = "";
            if (endpointFile.length() > 0) {
                baseDir = endpointFile + (endpointFile.endsWith("/") ? "" : "/");
            }
            String fileName = (name != null) ? name : endpoint.getGeneratedFileName(message); 
            answer = baseDir + fileName;
        } else {
            answer = endpointFile;
        }

        message.setHeader(FileComponent.HEADER_FILE_NAME_PRODUCED, answer);

        return answer;
    }

    protected String remoteServer() {
        return endpoint.getConfiguration().remoteServerInformation();
    }

    @Override
    protected void doStart() throws Exception {
        log.info("Starting");
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        log.info("Stopping");
        try {
            disconnect();
        } catch (Exception e) {
            log.warn("Exception occured during disconecting from " + remoteServer() + ". "
                     + e.getClass().getCanonicalName() + " message: " + e.getMessage());
        }
        super.doStop();
    }

    protected abstract void connectIfNecessary() throws Exception;

    protected abstract void disconnect() throws Exception;
}
