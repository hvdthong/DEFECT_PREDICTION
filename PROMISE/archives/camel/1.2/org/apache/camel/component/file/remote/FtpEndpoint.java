package org.apache.camel.component.file.remote;

import java.io.IOException;

import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;

public class FtpEndpoint extends RemoteFileEndpoint<RemoteFileExchange> {
    private static final transient Log LOG = LogFactory.getLog(FtpEndpoint.class);

    public FtpEndpoint(String uri, RemoteFileComponent remoteFileComponent, RemoteFileConfiguration configuration) {
        super(uri, remoteFileComponent, configuration);
    }

    public FtpProducer createProducer() throws Exception {
        return new FtpProducer(this, createFtpClient());
    }

    public FtpConsumer createConsumer(Processor processor) throws Exception {
        final FtpConsumer consumer = new FtpConsumer(this, processor, createFtpClient());
        configureConsumer(consumer);
        return consumer;
    }

    protected FTPClient createFtpClient() throws IOException {
        final FTPClient client = new FTPClient();
        RemoteFileConfiguration config = getConfiguration();
        String host = config.getHost();
        int port = config.getPort();
        LOG.debug("Connecting to host: " + host + " port: " + port);

        client.connect(host, port);
        client.login(config.getUsername(), config.getPassword());
        client.setFileType(config.isBinary() ? FTPClient.BINARY_FILE_TYPE : FTPClient.ASCII_FILE_TYPE);
        return client;
    }
}
