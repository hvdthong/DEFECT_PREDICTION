package org.apache.camel.component.file.remote;

import java.net.URI;

import org.apache.camel.Expression;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.language.simple.FileLanguage;
import org.apache.commons.net.ftp.FTPClientConfig;

public class RemoteFileConfiguration implements Cloneable {
    private String protocol;
    private String username;
    private String host;
    private int port;
    private String password;
    private String file;
    private boolean binary;
    private boolean directory = true;
    private FTPClientConfig ftpClientConfig;
    private Expression expression;
    private boolean passiveMode;
    private String knownHosts;
    private String privateKeyFile;
    private String privateKeyFilePassphrase;

    public RemoteFileConfiguration() {
    }

    public RemoteFileConfiguration(URI uri) {
        configure(uri);
    }

    public RemoteFileConfiguration copy() {
        try {
            return (RemoteFileConfiguration)clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeCamelException(e);
        }
    }

    public String toString() {
        return remoteServerInformation() + "/" + file;
    }

    /**
     * Returns human readable server information for logging purpose
     */
    public String remoteServerInformation() {
    }

    public void configure(URI uri) {
        setProtocol(uri.getScheme());
        setDefaultPort();
        setUsername(uri.getUserInfo());
        setHost(uri.getHost());
        setPort(uri.getPort());
        setFile(uri.getPath());
    }

    protected void setDefaultPort() {
        if ("ftp".equalsIgnoreCase(protocol)) {
            setPort(21);
        } else if ("sftp".equalsIgnoreCase(protocol)) {
            setPort(22);
        }
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        if (file.startsWith("/")) {
            file = file.substring(1);
        }
        this.file = file;
    }

    public String getKnownHosts() {
        return knownHosts;
    }

    public void setKnownHosts(String knownHosts) {
        this.knownHosts = knownHosts;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
            this.port = port;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isBinary() {
        return binary;
    }

    public void setBinary(boolean binary) {
        this.binary = binary;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    public FTPClientConfig getFtpClientConfig() {
        return ftpClientConfig;
    }

    public void setFtpClientConfig(FTPClientConfig ftpClientConfig) {
        this.ftpClientConfig = ftpClientConfig;
    }


    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    /**
     * Sets the expression based on {@link org.apache.camel.language.simple.FileLanguage}
     */
    public void setExpression(String fileLanguageExpression) {
        this.expression = FileLanguage.file(fileLanguageExpression);
    }

    public boolean isPassiveMode() {
        return passiveMode;
    }

    /**
     * Sets passive mode connections.
     * <br/>
     * Default is active mode connections.
     */
    public void setPassiveMode(boolean passiveMode) {
        this.passiveMode = passiveMode;
    }

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public void setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public String getPrivateKeyFilePassphrase() {
        return privateKeyFilePassphrase;
    }

    public void setPrivateKeyFilePassphrase(String privateKeyFilePassphrase) {
        this.privateKeyFilePassphrase = privateKeyFilePassphrase;
    }
    
}
