package org.apache.ivy.util;

/**
 * 
 */
public class Credentials {
    private String realm;

    private String host;

    private String userName;

    private String passwd;

    public Credentials(String realm, String host, String userName, String passwd) {
        this.realm = realm;
        this.host = host;
        this.userName = userName;
        this.passwd = passwd;
    }

    public String getHost() {
        return host;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getRealm() {
        return realm;
    }

    public String getUserName() {
        return userName;
    }

    public static String buildKey(String realm, String host) {
        if (realm == null || "".equals(realm.trim())) {
            return host;
        } else {
            return realm + "@" + host;
        }
    }

    /**
     * Return a string that can be used for debug purpose. It contains only stars for each password
     * character.
     */
    public String toString() {
        return getKey() + " " + getUserName() + "/" + getPasswdAsStars();
    }

    private String getPasswdAsStars() {
        if (passwd == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = passwd.length(); i > 0; i--) {
            sb.append('*');
        }
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o instanceof Credentials) {
            Credentials c = (Credentials) o;
            return getKey().equals(c.getKey());
        }

        return false;
    }

    public int hashCode() {
        return getKey().hashCode();
    }

    public String getKey() {
        return buildKey(realm, host);
    }
}
