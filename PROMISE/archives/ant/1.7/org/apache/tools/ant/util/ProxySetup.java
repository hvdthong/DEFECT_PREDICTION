package org.apache.tools.ant.util;

import org.apache.tools.ant.Project;

/**
 * Code to do proxy setup. This is just factored out of the main system just to
 * keep everything else less convoluted.
 * @since Ant1.7
 */

public class ProxySetup {

    /**
     * owner project; used for logging and extracting properties
     */
    private Project owner;

    /**
     * Java1.5 property that enables use of system proxies.
     * @value
     */
    public static final String USE_SYSTEM_PROXIES = "java.net.useSystemProxies";
    /** the http proxyhost property */
    public static final String HTTP_PROXY_HOST = "http.proxyHost";
    /** the http proxyport property */
    public static final String HTTP_PROXY_PORT = "http.proxyPort";
    /** the https proxyhost property */
    public static final String HTTPS_PROXY_HOST = "https.proxyHost";
    /** the https proxyport property */
    public static final String HTTPS_PROXY_PORT = "https.proxyPort";
    /** the ftp proxyhost property */
    public static final String FTP_PROXY_HOST = "ftp.proxyHost";
    /** the ftp proxyport property */
    public static final String FTP_PROXY_PORT = "ftp.proxyPort";
    /** the ftp proxyport property */
    public static final String HTTP_NON_PROXY_HOSTS = "http.nonProxyHosts";
    /** the http hosts not to be proxied property */
    public static final String HTTPS_NON_PROXY_HOSTS = "https.nonProxyHosts";
    /** the ftp hosts not to be proxied property */
    public static final String FTP_NON_PROXY_HOSTS = "ftp.nonProxyHosts";
    /** the http proxy username property */
    public static final String HTTP_PROXY_USERNAME = "http.proxyUser";
    /** the http proxy password property */
    public static final String HTTP_PROXY_PASSWORD = "http.proxyPassword";
    /** the socks proxy host property */
    public static final String SOCKS_PROXY_HOST = "socksProxyHost";
    /** the socks proxy port property */
    public static final String SOCKS_PROXY_PORT = "socksProxyPort";
    /** the socks proxy username property */
    public static final String SOCKS_PROXY_USERNAME = "java.net.socks.username";
    /** the socks proxy password property */
    public static final String SOCKS_PROXY_PASSWORD = "java.net.socks.password";


    /**
     * create a proxy setup class bound to this project
     * @param owner the project that owns this setup.
     */
    public ProxySetup(Project owner) {
        this.owner = owner;
    }

    /**
     * Get the current system property settings
     * @return current value; null for none or no access
     */
    public static String getSystemProxySetting() {
        try {
            return System.getProperty(USE_SYSTEM_PROXIES);
        } catch (SecurityException e) {
            return null;
        }
    }

    /**
     * turn proxies on;
     * if the proxy key is already set to some value: leave alone.
     * if an ant property of the value {@link #USE_SYSTEM_PROXIES}
     * is set, use that instead. Else set to "true".
     */
    public void enableProxies() {
        if (!(getSystemProxySetting() != null)) {
            String proxies = owner.getProperty(USE_SYSTEM_PROXIES);
            if (proxies == null || Project.toBoolean(proxies)) {
                proxies = "true";
            }
            String message = "setting " + USE_SYSTEM_PROXIES + " to " + proxies;
            try {
                owner.log(message, Project.MSG_DEBUG);
                System.setProperty(USE_SYSTEM_PROXIES, proxies);
            } catch (SecurityException e) {
                owner.log("Security Exception when " + message);
            }
        }
    }

}
