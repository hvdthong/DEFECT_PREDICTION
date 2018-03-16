package org.apache.camel.component.file.remote;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;

import java.io.IOException;
import java.io.InputStream;

public class SftpProducer extends RemoteFileProducer<RemoteFileExchange> {
    SftpEndpoint endpoint;
    private final ChannelSftp channel;

    public SftpProducer(SftpEndpoint endpoint, ChannelSftp channelSftp) {
        super(endpoint);
        this.endpoint = endpoint;
        this.channel = channelSftp;
    }

    public void process(Exchange exchange) throws Exception {
        process(endpoint.toExchangeType(exchange));
    }

    public void process(RemoteFileExchange exchange) throws Exception {
        final String fileName;
        InputStream payload = exchange.getIn().getBody(InputStream.class);
        final String endpointFile = endpoint.getConfiguration().getFile();
        channel.cd(endpointFile);
        if (endpointFile == null) {
            throw new NullPointerException("Null Endpoint File");
        }
        else {
            if (endpoint.getConfiguration().isDirectory()) {
                fileName = endpointFile + "/" + exchange.getIn().getMessageId();
            }
            else {
                fileName = endpointFile;
            }
        }
        buildDirectory(channel, fileName.substring(0, fileName.lastIndexOf('/')));
        try {
            channel.put(payload, fileName);
        }
        catch (SftpException e) {
            throw new RuntimeCamelException("error sending file", e);
        }
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        channel.disconnect();
        super.doStop();
    }

    protected static boolean buildDirectory(ChannelSftp sftpClient, String dirName) throws IOException {
        boolean atLeastOneSuccess = false;
        final StringBuilder sb = new StringBuilder(dirName.length());
        final String[] dirs = dirName.split("\\/");
        for (String dir : dirs) {
            sb.append('/').append(dir);
            try {
                sftpClient.mkdir(sb.toString());
                if (!atLeastOneSuccess) {
                    atLeastOneSuccess = true;
                }
            }
            catch (SftpException e) {
            }
        }
        return atLeastOneSuccess;
    }
}
