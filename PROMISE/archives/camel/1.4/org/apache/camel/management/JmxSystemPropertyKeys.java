package org.apache.camel.management;

/**
 * This module contains jmx related system property key constants.
 *
 * @version $Revision: 669756 $
 */
public final class JmxSystemPropertyKeys {

    public static final String DISABLED = "org.apache.camel.jmx.disabled";

    public static final String REGISTRY_PORT = "org.apache.camel.jmx.rmiConnector.registryPort";
    
    public static final String CONNECTOR_PORT = "org.apache.camel.jmx.rmiConnector.connectorPort";

    public static final String DOMAIN = "org.apache.camel.jmx.mbeanServerDefaultDomain";
    
    public static final String MBEAN_DOMAIN = "org.apache.camel.jmx.mbeanObjectDomainName";

    public static final String SERVICE_URL_PATH = "org.apache.camel.jmx.serviceUrlPath";
    
    public static final String CREATE_CONNECTOR = "org.apache.camel.jmx.createRmiConnector";
    
    public static final String USE_PLATFORM_MBS = 
        "org.apache.camel.jmx.usePlatformMBeanServer";

    private JmxSystemPropertyKeys() {
    }

}
