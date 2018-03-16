package org.apache.ivy.util.url;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.apache.ivy.util.Credentials;
import org.apache.ivy.util.Message;

/**
 * 
 */
public final class IvyAuthenticator extends Authenticator {

    /**
     * The sole instance.
     */
    public static final IvyAuthenticator INSTANCE = new IvyAuthenticator();

    /**
     * Private c'tor to prevent instantiation. Also installs this as the default Authenticator to
     * use by the JVM.
     */
    private IvyAuthenticator() {
        Authenticator.setDefault(this);
    }



    protected PasswordAuthentication getPasswordAuthentication() {
        PasswordAuthentication result = null;
        
        String proxyHost = System.getProperty("http.proxyHost");
        if (getRequestingHost().equals(proxyHost)) {
            String proxyUser = System.getProperty("http.proxyUser");
            if ((proxyUser != null) && (proxyUser.trim().length() > 0)) {
                String proxyPass = System.getProperty("http.proxyPassword", "");
                Message.debug("authenicating to proxy server with username [" + proxyUser + "]");
                result = new PasswordAuthentication(proxyUser, proxyPass.toCharArray());
            }
        } else {
            Credentials c = CredentialsStore.INSTANCE.getCredentials(getRequestingPrompt(),
                getRequestingHost());
            Message.debug("authentication: k='"
                    + Credentials.buildKey(getRequestingPrompt(), getRequestingHost()) + "' c='" + c
                    + "'");
            if (c != null) {
                result = new PasswordAuthentication(c.getUserName(), c.getPasswd().toCharArray());
            }
        }
        
        return result;
    }

}
