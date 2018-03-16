package org.apache.camel.jmxconnect;


import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * @version $Revision: 689344 $
 */
public class CamelJmxConnectorSupport {
    public static final String DEFAULT_DESTINATION_PREFIX = "org.apache.camel.jmxconnect.";
    public static final String MBEAN_SERVER_NAME = "*";
    public static final String MBEAN_GROUP_NAME = "*";

    public static String getEndpointUri(JMXServiceURL serviceURL, String expectedProtocol) throws IOException {
        String protocol = serviceURL.getProtocol();
        if (!expectedProtocol.equals(protocol)) {
            throw new MalformedURLException("Wrong protocol " + protocol + " expecting " + expectedProtocol);
        }
        String path = serviceURL.getURLPath();
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path;
    }

}
