package org.apache.tools.ant.taskdefs.optional.net;

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.util.JavaEnvUtils;

/**
 * Sets Java's web proxy properties, so that tasks and code run in
 * the same JVM can have through-the-firewall access to remote web sites,
 * and remote ftp sites.
 * You can nominate an http and ftp proxy, or a socks server, reset the server 
 * settings, or do nothing at all.
 * <p> 
 * Examples
 * <pre>&lt;setproxy/&gt;</pre>
 * do nothing
 * <pre>&lt;setproxy proxyhost="firewall"/&gt;</pre>
 * set the proxy to firewall:80
 * <pre>&lt;setproxy proxyhost="firewall" proxyport="81"/&gt;</pre>
 * set the proxy to firewall:81
 * <pre>&lt;setproxy proxyhost=""/&gt;</pre>
 * stop using the http proxy; don't change the socks settings
 * <pre>&lt;setproxy socksproxyhost="socksy"/&gt;</pre>
 * use socks via socksy:1080
 * <pre>&lt;setproxy socksproxyhost=""/&gt;</pre>
 * stop using the socks server
 
 
 
 *  java 1.4 network property list</a>
 * @author Steve Loughran
  *@since       Ant 1.5
 * @ant.task
 */
public class SetProxy extends Task {

    /**
     * proxy details
     */
    protected String proxyHost = null;

    /**
     * name of proxy port
     */
    protected int proxyPort = 80;

    /**
     * socks host.
     */
    private String socksProxyHost = null;
    
    /**
     * socks proxy port. 1080 is the default
     */
    private int socksProxyPort = 1080;


    /**
     * list of non proxy hosts
     */ 
    private String nonProxyHosts = null;

    /**
     * the HTTP/ftp proxy host. Set this to "" for the http proxy
     * option to be disabled
     *
     * @param hostname the new proxy hostname
     */
    public void setProxyHost(String hostname) {
        proxyHost = hostname;
    }


    /**
     * the HTTP/ftp proxy port number; default is 80
     *
     * @param port port number of the proxy
     */
    public void setProxyPort(int port) {
        proxyPort = port;
    }

    /**
     * The name of a Socks server. Set to "" to turn socks
     * proxying off.
     *
     * @param host The new SocksProxyHost value
     */
    public void setSocksProxyHost(String host) {
        this.socksProxyHost = host;
    }


    /**
     * Set the ProxyPort for socks connections. The default value is 1080
     *
     * @param port The new SocksProxyPort value
     */
    public void setSocksProxyPort(int port) {
        this.socksProxyPort = port;
    }

    /**
     * A list of hosts to bypass the proxy on. These should be separated
     * with the vertical bar character '|'. Only in Java 1.4 does ftp use
     * this list.
     * e.g. fozbot.corp.sun.com|*.eng.sun.com
     * @param nonProxyHosts lists of hosts to talk direct to
     */ 
    public void setNonProxyHosts(String nonProxyHosts) {
        this.nonProxyHosts = nonProxyHosts;
    }

    /**
     * if the proxy port and host settings are not null, then the settings
     * get applied these settings last beyond the life of the object and
     * apply to all network connections
     * Relevant docs: buglist #4183340
     * @return true if the settings were applied
     */

    public void applyWebProxySettings() {
        boolean settingsChanged = false;
        boolean enablingProxy = false;
        Properties sysprops = System.getProperties();
        if (proxyHost != null) {
            settingsChanged = true;
            if (proxyHost.length() != 0) {
                traceSettingInfo();
                enablingProxy = true;
                sysprops.put("http.proxyHost", proxyHost);
                String portString = Integer.toString(proxyPort);
                sysprops.put("http.proxyPort", portString);
                sysprops.put("https.proxyHost", proxyHost);
                sysprops.put("https.proxyPort", portString);
                sysprops.put("ftp.proxyHost", proxyHost);
                sysprops.put("ftp.proxyPort", portString);
                if (nonProxyHosts != null) {
                    sysprops.put("http.nonProxyHosts", nonProxyHosts);
                    sysprops.put("ftp.nonProxyHosts", nonProxyHosts);
                }                    
            } else {
                log("resetting http proxy", Project.MSG_VERBOSE);
                sysprops.remove("http.proxyPort");
                sysprops.remove("https.proxyHost");
                sysprops.remove("https.proxyPort");
                sysprops.remove("ftp.proxyHost");
                sysprops.remove("ftp.proxyPort");
            }
        }

        if (socksProxyHost != null) {
            settingsChanged = true;
            if (socksProxyHost.length() != 0) {
                enablingProxy = true;
                sysprops.put("socksProxyHost", socksProxyHost);
                sysprops.put("socksProxyPort", Integer.toString(socksProxyPort));
            } else {
                log("resetting socks proxy", Project.MSG_VERBOSE);
                sysprops.remove("socksProxyHost");
                sysprops.remove("socksProxyPort");
            }
        }
        

        if (settingsChanged &&
            JavaEnvUtils.isJavaVersion(JavaEnvUtils.JAVA_1_1)) {
            legacyResetProxySettingsCall(enablingProxy);
        }
    }

    /**
     * list out what is going on
     */ 
    private void traceSettingInfo() {
        log("Setting proxy to " 
                + (proxyHost != null ? proxyHost : "''") 
                + ":" + proxyPort,
                Project.MSG_VERBOSE);
    }


    /**
     * make a call to sun.net.www.http.HttpClient.resetProperties();
     * this is only needed for java 1.1; reflection is used to stop the compiler
     * whining, and in case cleanroom JVMs dont have the class.
     * @return true if we did something
     */

    protected boolean legacyResetProxySettingsCall(boolean setProxy) {
        System.getProperties().put("http.proxySet", new Boolean(setProxy).toString());
        try {
            Class c = Class.forName("sun.net.www.http.HttpClient");
            Method reset = c.getMethod("resetProperties", null);
            reset.invoke(null, null);
            return true;
        }
        catch (ClassNotFoundException cnfe) {
            return false;
        }
        catch (NoSuchMethodException e) {
            return false;
        }
        catch (IllegalAccessException e) {
            return false;
        }
        catch (InvocationTargetException e) {
            return false;
        }
    }


    /**
     * Does the work.
     *
     * @exception BuildException thrown in unrecoverable error.
     */
    public void execute() throws BuildException {
        applyWebProxySettings();
    }

}

