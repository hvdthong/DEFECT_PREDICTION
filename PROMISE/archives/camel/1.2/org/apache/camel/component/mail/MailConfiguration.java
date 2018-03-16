package org.apache.camel.component.mail;

import java.net.URI;
import java.util.Properties;

import javax.mail.Session;

import org.apache.camel.RuntimeCamelException;

/**
 * Represents the configuration data for communicating over email
 * 
 * @version $Revision: 532790 $
 */
public class MailConfiguration implements Cloneable {
    private String defaultEncoding;
    private String host;
    private Properties javaMailProperties;
    private String password;
    private String protocol;
    private Session session;
    private String username;
    private int port = -1;
    private String destination;
    private String from = "camel@localhost";
    private boolean deleteProcessedMessages = true;
    private String folderName = "INBOX";

    public MailConfiguration() {
    }

    /**
     * Returns a copy of this configuration
     */
    public MailConfiguration copy() {
        try {
            return (MailConfiguration)clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeCamelException(e);
        }
    }

    public void configure(URI uri) {
        String value = uri.getHost();
        if (value != null) {
            setHost(value);
        }

        String scheme = uri.getScheme();
        if (scheme != null) {
            setProtocol(scheme);
        }
        String userInfo = uri.getUserInfo();
        if (userInfo != null) {
            setUsername(userInfo);
        }
        int port = uri.getPort();
        if (port >= 0) {
            setPort(port);
        }


        String fragment = uri.getFragment();
        if (fragment == null || fragment.length() == 0) {
            fragment = userInfo + "@" + host;
        } else {
            setFolderName(fragment);
        }
        setDestination(fragment);
    }

    public JavaMailConnection createJavaMailConnection(MailEndpoint mailEndpoint) {
        JavaMailConnection answer = new JavaMailConnection();
        if (defaultEncoding != null) {
            answer.setDefaultEncoding(defaultEncoding);
        }
        if (host != null) {
            answer.setHost(host);
        }
        if (javaMailProperties != null) {
            answer.setJavaMailProperties(javaMailProperties);
        }
        if (port >= 0) {
            answer.setPort(port);
        }
        if (password != null) {
            answer.setPassword(password);
        }
        if (protocol != null) {
            answer.setProtocol(protocol);
        }
        if (session != null) {
            answer.setSession(session);
        }
        if (username != null) {
            answer.setUsername(username);
        }
        return answer;
    }


    public String getDefaultEncoding() {
        return defaultEncoding;
    }

    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Properties getJavaMailProperties() {
        return javaMailProperties;
    }

    public void setJavaMailProperties(Properties javaMailProperties) {
        this.javaMailProperties = javaMailProperties;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isDeleteProcessedMessages() {
        return deleteProcessedMessages;
    }

    public void setDeleteProcessedMessages(boolean deleteProcessedMessages) {
        this.deleteProcessedMessages = deleteProcessedMessages;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
