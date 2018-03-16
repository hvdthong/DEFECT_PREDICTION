package org.apache.camel.component.file.remote;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import org.apache.camel.Processor;

public class SftpEndpoint extends RemoteFileEndpoint<RemoteFileExchange> {
    public SftpEndpoint(String uri, RemoteFileComponent remoteFileComponent, RemoteFileConfiguration configuration) {
        super(uri, remoteFileComponent, configuration);
    }

    public SftpProducer createProducer() throws Exception {
        return new SftpProducer(this, createChannelSftp());
    }

    public SftpConsumer createConsumer(Processor processor) throws Exception {
        final SftpConsumer consumer = new SftpConsumer(this, processor, createChannelSftp());
        configureConsumer(consumer);
        return consumer;
    }

    protected ChannelSftp createChannelSftp() throws JSchException {
        final JSch jsch = new JSch();
        final Session session = jsch.getSession(getConfiguration().getUsername(), getConfiguration().getHost());
        session.setUserInfo(new UserInfo() {
            public String getPassphrase() {
                return null;
            }

            public String getPassword() {
                return SftpEndpoint.this.getConfiguration().getPassword();
            }

            public boolean promptPassword(String string) {
                return true;
            }

            public boolean promptPassphrase(String string) {
                return true;
            }

            public boolean promptYesNo(String string) {
                return true;
            }

            public void showMessage(String string) {
            }
        });
        session.connect();
        final ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
        return channel;
    }
}
