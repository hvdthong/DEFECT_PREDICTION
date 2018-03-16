package org.apache.ivy.util.url;

import java.util.HashMap;
import java.util.Map;

import org.apache.ivy.util.Credentials;
import org.apache.ivy.util.Message;

/**
 * 
 */
public final class CredentialsStore {
    /**
     * A Map of Credentials objects keyed by the 'key' of the Credentials.
     */
    private static final Map KEYRING = new HashMap();

    public static final CredentialsStore INSTANCE = new CredentialsStore();

    private CredentialsStore() {
    }

    public void addCredentials(String realm, String host, String userName, String passwd) {
        if (userName == null) {
            return;
        }
        Credentials c = new Credentials(realm, host, userName, passwd);
        Message.debug("credentials added: " + c);
        KEYRING.put(c.getKey(), c);
        KEYRING.put(c.getHost(), c);
    }

    public Credentials getCredentials(String realm, String host) {
        return (Credentials) KEYRING.get(Credentials.buildKey(realm, host));
    }

}
