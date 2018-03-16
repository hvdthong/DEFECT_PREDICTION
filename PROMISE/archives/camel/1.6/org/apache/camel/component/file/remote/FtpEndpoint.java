package org.apache.camel.component.file.remote;

import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FtpEndpoint extends RemoteFileEndpoint<RemoteFileExchange> {
    private static final transient Log LOG = LogFactory.getLog(FtpEndpoint.class);

    public FtpEndpoint(String uri, RemoteFileComponent remoteFileComponent, RemoteFileConfiguration configuration) {
        super(uri, remoteFileComponent, configuration);
    }

    public FtpEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public FtpProducer createProducer() throws Exception {
        return new FtpProducer(this, FtpUtils.createNewFtpClient());
    }

    public FtpConsumer createConsumer(Processor processor) throws Exception {
        final FtpConsumer consumer = new FtpConsumer(this, processor, FtpUtils.createNewFtpClient());
        configureConsumer(consumer);
        return consumer;
    }

}
