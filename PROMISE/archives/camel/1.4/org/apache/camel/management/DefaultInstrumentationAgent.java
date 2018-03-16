package org.apache.camel.management;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.RequiredModelMBean;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.spi.InstrumentationAgent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource;
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;

/**
 * Default implementation of the Camel JMX service agent
 */
public class DefaultInstrumentationAgent extends ServiceSupport implements InstrumentationAgent {

    public static final String DEFAULT_DOMAIN = "org.apache.camel";
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_REGISTRY_PORT = 1099;
    public static final int DEFAULT_CONNECTION_PORT = -1;
    public static final String DEFAULT_SERVICE_URL_PATH = "/jmxrmi/camel";
    private static final transient Log LOG = LogFactory.getLog(DefaultInstrumentationAgent.class);

    private MBeanServer server;
    private Set<ObjectName> mbeans = new HashSet<ObjectName>();
    private MetadataMBeanInfoAssembler assembler;
    private JMXConnectorServer cs;

    private Integer registryPort;
    private Integer connectorPort;
    private String mBeanServerDefaultDomain;
    private String mBeanObjectDomainName;
    private String serviceUrlPath;
    private Boolean usePlatformMBeanServer;
    private Boolean createConnector;

    protected void finalizeSettings() {
        if (registryPort == null) {
            registryPort = Integer.getInteger(JmxSystemPropertyKeys.REGISTRY_PORT,
                    DEFAULT_REGISTRY_PORT);
        }

        if (connectorPort == null) {
            connectorPort = Integer.getInteger(JmxSystemPropertyKeys.CONNECTOR_PORT,
                    DEFAULT_CONNECTION_PORT);
        }

        if (mBeanServerDefaultDomain == null) {
            mBeanServerDefaultDomain =
                System.getProperty(JmxSystemPropertyKeys.DOMAIN, DEFAULT_DOMAIN);
        }

        if (mBeanObjectDomainName == null) {
            mBeanObjectDomainName =
                System.getProperty(JmxSystemPropertyKeys.MBEAN_DOMAIN, DEFAULT_DOMAIN);
        }

        if (serviceUrlPath == null) {
            serviceUrlPath =
                System.getProperty(JmxSystemPropertyKeys.SERVICE_URL_PATH,
                        DEFAULT_SERVICE_URL_PATH);
        }

        if (createConnector == null) {
            createConnector = Boolean.getBoolean(JmxSystemPropertyKeys.CREATE_CONNECTOR);
        }

        if (usePlatformMBeanServer == null) {
            usePlatformMBeanServer =
                Boolean.getBoolean(JmxSystemPropertyKeys.USE_PLATFORM_MBS);
        }
    }


    public void setRegistryPort(Integer value) {
        registryPort = value;
    }

    public void setConnectorPort(Integer value) {
        connectorPort = value;
    }

    public void setMBeanServerDefaultDomain(String value) {
        mBeanServerDefaultDomain = value;
    }

    public void setMBeanObjectDomainName(String value) {
        mBeanObjectDomainName = value;
    }

    public void setServiceUrlPath(String value) {
        serviceUrlPath = value;
    }

    public void setCreateConnector(Boolean flag) {
        createConnector = flag;
    }

    public void setUsePlatformMBeanServer(Boolean flag) {
        usePlatformMBeanServer = flag;
    }

    public MBeanServer getMBeanServer() {
        return server;
    }

    public void register(Object obj, ObjectName name) throws JMException {
        register(obj, name, false);
    }

    public void register(Object obj, ObjectName name, boolean forceRegistration) throws JMException {
        try {
            registerMBeanWithServer(obj, name, forceRegistration);
        } catch (NotCompliantMBeanException e) {
            ModelMBeanInfo mbi = null;
            mbi = assembler.getMBeanInfo(obj, name.toString());
            RequiredModelMBean mbean = (RequiredModelMBean)server.instantiate(RequiredModelMBean.class
                .getName());
            mbean.setModelMBeanInfo(mbi);
            try {
                mbean.setManagedResource(obj, "ObjectReference");
            } catch (InvalidTargetObjectTypeException itotex) {
                throw new JMException(itotex.getMessage());
            }
            registerMBeanWithServer(mbean, name, forceRegistration);
        }
    }

    public void unregister(ObjectName name) throws JMException {
        server.unregisterMBean(name);
    }

    protected void doStart() throws Exception {
        assembler = new MetadataMBeanInfoAssembler();
        assembler.setAttributeSource(new AnnotationJmxAttributeSource());

        if (server == null) {
            finalizeSettings();
            createMBeanServer();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Starting JMX agent on server: " + getMBeanServer());
        }
    }

    protected void doStop() throws Exception {
        if (cs != null) {
            try {
                cs.stop();
            } catch (IOException e) {
            }
            cs = null;
        }

        Object[] mBeans = mbeans.toArray();
        int caught = 0;
        for (Object name : mBeans) {
            mbeans.remove((ObjectName)name);
            try {
                unregister((ObjectName)name);
            } catch (JMException jmex) {
                LOG.info("Exception unregistering MBean", jmex);
                caught++;
            }
        }
        if (caught > 0) {
            LOG.warn("A number of " + caught
                     + " exceptions caught while unregistering MBeans during stop operation."
                     + " See INFO log for details.");
        }
    }

    private void registerMBeanWithServer(Object obj, ObjectName name, boolean forceRegistration)
        throws JMException {

        boolean exists = server.isRegistered(name);
        if (exists) {
            if (forceRegistration) {
                LOG.info("ForceRegistration enabled, unregistering existing MBean");
                server.unregisterMBean(name);
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("MBean already registered with objectname: " + name);
                }
            }
        }

        ObjectInstance instance = null;
        if (forceRegistration || !exists) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Registering MBean with objectname: " + name);
            }
            instance = server.registerMBean(obj, name);
        }

        if (instance != null) {
            ObjectName registeredName = instance.getObjectName();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Registered MBean with objectname: " + registeredName);
            }
            
            mbeans.add(registeredName);
        }
    }

    protected void createMBeanServer() {
        String hostName = DEFAULT_HOST;
        boolean canAccessSystemProps = true;
        try {
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPropertiesAccess();
            }
        } catch (SecurityException se) {
            canAccessSystemProps = false;
        }

        if (canAccessSystemProps) {
            try {
                hostName = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException uhe) {
                LOG.info("Cannot determine localhost name. Using default: "
                         + DEFAULT_REGISTRY_PORT, uhe);
                hostName = DEFAULT_HOST;
            }
        } else {
            hostName = DEFAULT_HOST;
        }

        server = findOrCreateMBeanServer();

        try {
            if (createConnector) {
                createJmxConnector(hostName);
            }
        } catch (IOException ioe) {
            LOG.warn("Could not create and start JMX connector.", ioe);
        }
    }

    @SuppressWarnings("unchecked")
    protected MBeanServer findOrCreateMBeanServer() {

        if (Boolean.getBoolean(JmxSystemPropertyKeys.USE_PLATFORM_MBS) || usePlatformMBeanServer) {
            return ManagementFactory.getPlatformMBeanServer();
        }

        List<MBeanServer> servers =
            (List<MBeanServer>)MBeanServerFactory.findMBeanServer(null);

        for (MBeanServer server : servers) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Found MBeanServer with default domain " + server.getDefaultDomain());
            }
            
            if (mBeanServerDefaultDomain.equals(server.getDefaultDomain())) {
                return server;
            }
        }

        return MBeanServerFactory.createMBeanServer(mBeanServerDefaultDomain);
    }

    protected void createJmxConnector(String host) throws IOException {
        try {
            LocateRegistry.createRegistry(registryPort);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Created JMXConnector RMI regisry on port " + registryPort);
            }
        } catch (RemoteException ex) {
        }

        JMXServiceURL url;

        if (connectorPort > 0) {
                                    + ":" + registryPort + serviceUrlPath);
        } else {
                                    + serviceUrlPath);
        }
        cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, server);

        Thread connectorThread = new Thread() {
            public void run() {
                try {
                    cs.start();
                } catch (IOException ioe) {
                    LOG.warn("Could not start JMXConnector thread.", ioe);
                }
            }
        };
        connectorThread.setName("Camel JMX Connector Thread [" + url + "]");
        connectorThread.start();
        LOG.info("JMX Connector thread started and listening at: " + url);
    }

    public String getMBeanObjectDomainName() {
        return mBeanObjectDomainName;
    }

    public void setServer(MBeanServer value) {
        server = value;
    }

}
