package org.apache.camel.component.file.remote;

import java.io.IOException;
import java.io.InputStream;

import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;

public class FtpProducer extends RemoteFileProducer<RemoteFileExchange> {
    private static final transient Log LOG = LogFactory.getLog(FtpProducer.class);

    private FtpEndpoint endpoint;
    private FTPClient client;

    public FtpProducer(FtpEndpoint endpoint, FTPClient client) {
        super(endpoint);
        this.endpoint = endpoint;
        this.client = client;
    }

    public void process(Exchange exchange) throws Exception {
        connectIfNecessary();
        try {
            process(endpoint.createExchange(exchange));
        } catch (FTPConnectionClosedException e) {
            LOG.warn("Disconnecting due to exception: " + e.toString());
            disconnect();
            throw e;
        } catch (RuntimeCamelException e) {
            LOG.warn("Caught RuntimeCamelException: " + e.toString());
            LOG.warn("Hoping an explicit disconnect/reconnect will solve the problem");
            disconnect();
            throw e;
        }
    }

    protected void connectIfNecessary() throws IOException {
        if (!client.isConnected()) {
            LOG.warn("FtpProducer's client isn't connected, trying to reconnect...");
            endpoint.connect(client);
            LOG.info("Connected to " + endpoint.getConfiguration());
        }
    }

    public void disconnect() throws IOException {
        LOG.info("FtpProducer's client is being explicitly disconnected");
        endpoint.disconnect(client);
    }

    public void process(RemoteFileExchange exchange) throws Exception {
        InputStream payload = exchange.getIn().getBody(InputStream.class);
        try {
            String fileName = createFileName(exchange.getIn(), endpoint.getConfiguration());

            int lastPathIndex = fileName.lastIndexOf('/');
            if (lastPathIndex != -1) {
                String directory = fileName.substring(0, lastPathIndex);
                if (!buildDirectory(client, directory)) {
                    LOG.warn("Couldn't build directory: " + directory + " (either permissions deny it, or it already exists)");
                }
            }

            final boolean success = client.storeFile(fileName, payload);
            if (!success) {
                throw new RuntimeCamelException("Error sending file: " + fileName);
            }

            RemoteFileConfiguration config = endpoint.getConfiguration();
            LOG.info("Sent: " + fileName + " to " + config.toString().substring(0, config.toString().indexOf(config.getFile())));
        } finally {
            if (payload != null) {
                payload.close();
            }
        }
    }

    @Override
    protected void doStart() throws Exception {
        LOG.info("Starting");
        try {
            connectIfNecessary();
        } catch (IOException e) {
            LOG.warn("Couldn't connect to " + endpoint.getConfiguration());
        }
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        LOG.info("Stopping");
        disconnect();
        super.doStop();
    }

    protected static boolean buildDirectory(FTPClient ftpClient, String dirName) throws IOException {
        boolean atLeastOneSuccess = false;
        final StringBuilder sb = new StringBuilder(dirName.length());
        final String[] dirs = dirName.split("\\/");

        for (String dir : dirs) {
            sb.append(dir).append('/');
            String directory = sb.toString();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Trying to build directory: " + directory);
            }
            final boolean success = ftpClient.makeDirectory(directory);

            if (!atLeastOneSuccess && success) {
                atLeastOneSuccess = true;
            }
        }

        return atLeastOneSuccess;
    }
}
