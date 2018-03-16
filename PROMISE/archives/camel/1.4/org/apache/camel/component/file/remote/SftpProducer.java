package org.apache.camel.component.file.remote;


import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class SftpProducer extends RemoteFileProducer<RemoteFileExchange> {
    private static final transient Log LOG = LogFactory.getLog(SftpProducer.class);

    private SftpEndpoint endpoint;
    private ChannelSftp channel;
    private Session session;

    public SftpProducer(SftpEndpoint endpoint, Session session) {
        super(endpoint);
        this.endpoint = endpoint;
        this.session = session;
    }

    protected void connectIfNecessary() throws JSchException {
        if (channel == null || !channel.isConnected()) {
            if (session == null || !session.isConnected()) {
                LOG.info("Session isn't connected, trying to recreate and connect...");
                session = endpoint.createSession();
                session.connect();
            }
            LOG.info("Channel isn't connected, trying to recreate and connect...");
            channel = endpoint.createChannelSftp(session);
            channel.connect();
            LOG.info("Connected to " + endpoint.getConfiguration().toString());
        }
    }

    protected void disconnect() throws JSchException {
        if (session != null) {
            LOG.info("Session is being explicitly disconnected");
            session.disconnect();
        }
        if (channel != null) {
            LOG.info("Channel is being explicitly disconnected");
            channel.disconnect();
        }
    }

    public void process(Exchange exchange) throws Exception {
        connectIfNecessary();
        try {
            process(endpoint.createExchange(exchange));
        } catch (JSchException e) {
            LOG.warn("Disconnecting due to exception: " + e.toString());
            disconnect();
            throw e;
        } catch (SftpException e) {
            LOG.warn("Caught SftpException:" + e.toString());
            LOG.warn("Doing nothing for now, need to determine an appropriate action");
            throw e;
        }
    }

    public void process(RemoteFileExchange exchange) throws Exception {
        InputStream payload = exchange.getIn().getBody(InputStream.class);
        try {
            String fileName = createFileName(exchange.getIn(), endpoint.getConfiguration());

            int lastPathIndex = fileName.lastIndexOf('/');
            if (lastPathIndex != -1) {
                String directory = fileName.substring(0, lastPathIndex);
                boolean success = buildDirectory(channel, directory);
                if (!success) {
                    LOG.warn("Couldn't build directory: " + directory + " (either permissions deny it, or it already exists)");
                }
            }

            channel.put(payload, fileName);
            LOG.info("Sent: " + fileName + " to " + endpoint.getConfiguration());
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
        } catch (JSchException e) {
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

    protected static boolean buildDirectory(ChannelSftp sftpClient, String dirName)
        throws IOException, SftpException {

        boolean atLeastOneSuccess = false;
        final StringBuilder sb = new StringBuilder(dirName.length());
        final String[] dirs = dirName.split("\\/");
        for (String dir : dirs) {
            sb.append(dir).append('/');
            String directory = sb.toString();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Trying to build directory: " + directory);
            }
            sftpClient.mkdir(directory);
            if (!atLeastOneSuccess) {
                atLeastOneSuccess = true;
            }
        }

        return atLeastOneSuccess;
    }

}
