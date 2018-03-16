package org.apache.camel.component.file.remote;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.camel.util.ObjectHelper.isNotNullAndNonEmpty;

public class SftpEndpoint extends RemoteFileEndpoint<RemoteFileExchange> {
    protected final transient Log log = LogFactory.getLog(getClass());
    
    public SftpEndpoint(String uri, RemoteFileComponent remoteFileComponent, RemoteFileConfiguration configuration) {
        super(uri, remoteFileComponent, configuration);
    }

    public SftpEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public SftpProducer createProducer() throws Exception {
        return new SftpProducer(this, createSession());
    }

    public SftpConsumer createConsumer(Processor processor) throws Exception {
        final SftpConsumer consumer = new SftpConsumer(this, processor, createSession());
        configureConsumer(consumer);
        return consumer;
    }

    protected Session createSession() throws JSchException {
        final JSch jsch = new JSch();

        String privateKeyFile = getConfiguration().getPrivateKeyFile();
        if (isNotNullAndNonEmpty(privateKeyFile)) {
            log.debug("Using private keyfile: " + privateKeyFile);
            String privateKeyFilePassphrase = getConfiguration().getPrivateKeyFilePassphrase(); 
            if (isNotNullAndNonEmpty(privateKeyFilePassphrase)) {
                jsch.addIdentity(privateKeyFile, privateKeyFilePassphrase);
            } else {
                jsch.addIdentity(privateKeyFile);
            }
        }

        String knownHostsFile = getConfiguration().getKnownHosts();
        if (isNotNullAndNonEmpty(knownHostsFile)) {
            log.debug("Using knownHosts: " + knownHostsFile);
            jsch.setKnownHosts(knownHostsFile);
        }
      
        final Session session = jsch.getSession(getConfiguration().getUsername(), getConfiguration().getHost(), getConfiguration().getPort());
        session.setUserInfo(new UserInfo() {
            public String getPassphrase() {
                return null;
            }

            public String getPassword() {
                return getConfiguration().getPassword();
            }

            public boolean promptPassword(String string) {
                return true;
            }

            public boolean promptPassphrase(String string) {
                return true;
            }           

            public boolean promptYesNo(String string) {
                log.error(string);
                return false;
            }

            public void showMessage(String string) {
            }
        });
        return session;
    }

    public ChannelSftp createChannelSftp(Session session) throws JSchException {
        final ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        return channel;
    }
}
