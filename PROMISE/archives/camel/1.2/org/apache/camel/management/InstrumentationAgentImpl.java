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

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.spi.InstrumentationAgent;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource;
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;

public class InstrumentationAgentImpl extends ServiceSupport implements InstrumentationAgent, CamelContextAware {

    private static final transient Log LOG = LogFactory.getLog(InstrumentationAgentImpl.class);
    
	public static final String SYSTEM_PROPERTY_JMX = "org.apache.camel.jmx";
	public static final String DEFAULT_DOMAIN = "org.apache.camel";
	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 1099;
	
	private MBeanServer server;
	private CamelContext context;
    private Set<ObjectName> mbeans = new HashSet<ObjectName>();
    private MetadataMBeanInfoAssembler assembler;
    private JMXConnectorServer cs;
	private boolean jmxEnabled = false;
	private String jmxDomainName = null;
	private int jmxConnectorPort = 0;
    private CamelNamingStrategy namingStrategy;

    public InstrumentationAgentImpl() {
    	assembler = new MetadataMBeanInfoAssembler();
    	assembler.setAttributeSource(new AnnotationJmxAttributeSource());
        namingStrategy = new CamelNamingStrategy();
    }

    public CamelContext getCamelContext() {
		return context;
	}

	public void setCamelContext(CamelContext camelContext) {
		context = camelContext;
	}

	public void setMBeanServer(MBeanServer server) {
		this.server = server;
        jmxEnabled = true;
	}
	
	public MBeanServer getMBeanServer() {
        if (server == null) {
            server = ManagementFactory.getPlatformMBeanServer();
        }
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
            RequiredModelMBean mbean = (RequiredModelMBean)server.instantiate(RequiredModelMBean.class.getName());
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

    public CamelNamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    public void setNamingStrategy(CamelNamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }

    protected void doStart() throws Exception {
        ObjectHelper.notNull(context, "camelContext");

        if (getMBeanServer() == null) {
			createMBeanServer();
		}

        if (jmxDomainName == null) {
            jmxDomainName = System.getProperty(SYSTEM_PROPERTY_JMX + ".domain");
            if (jmxDomainName == null || jmxDomainName.length() == 0) {
                jmxDomainName = DEFAULT_DOMAIN;
            }
        }
        configureDomainName();
        
        LOG.debug("Starting JMX agent on server: " + getMBeanServer());

		if (context instanceof DefaultCamelContext) {
			DefaultCamelContext dc = (DefaultCamelContext)context;
			InstrumentationLifecycleStrategy ls = new InstrumentationLifecycleStrategy(this);
			dc.setLifecycleStrategy(ls);
			ls.onContextCreate(context);
		}
    }

    protected void doStop() throws Exception {
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
        	LOG.warn("A number of " + caught + 
        		" exceptions caught while unregistering MBeans during stop operation.  " + 
        		"See INFO log for details."); 
        }
    }
    
    private void registerMBeanWithServer(Object obj, ObjectName name, boolean forceRegistration) 
    		throws JMException {
    	
	    ObjectInstance instance = null;
	    try {
	        instance = server.registerMBean(obj, name);           
	    } catch (InstanceAlreadyExistsException e) {            
	        if (forceRegistration) {
	        	server.unregisterMBean(name);               
	            instance = server.registerMBean(obj, name);
	        } else {
	            throw e;
	        }
	    }
	    
	    if (instance != null) {
	    	mbeans.add(name);
	    }
    }

	public void enableJmx(String domainName, int port) {
		jmxEnabled = true;
        jmxDomainName = domainName;
        configureDomainName();
        jmxConnectorPort = port;
	}

    protected void configureDomainName() {
        if (jmxDomainName != null) {
            namingStrategy.setDomainName(jmxDomainName);
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
        }
        catch (SecurityException se) {
            canAccessSystemProps = false;
        }

    	if (canAccessSystemProps) {
    		if (!jmxEnabled) {
    			jmxEnabled = null != System.getProperty(SYSTEM_PROPERTY_JMX);
    			if (!jmxEnabled) {
    				return;
    			}
    		}
    		
        	if (jmxConnectorPort <= 0) {
        		String portKey = SYSTEM_PROPERTY_JMX + ".port";
	        	String portValue = System.getProperty(portKey);
	        	if (portValue != null && portValue.length() > 0) {
	        		try {
	        			jmxConnectorPort = Integer.parseInt(portValue);
	        		}
	        		catch (NumberFormatException nfe) {
	        			LOG.info("Invalid port number specified via System property [" + 
	        				portKey + "=" + portValue + "].  Using default: " + DEFAULT_PORT);
	        			jmxConnectorPort = DEFAULT_PORT;
	        		}
	        	}
        	}
        	
        	try {
                hostName = InetAddress.getLocalHost().getHostName();
        	}
        	catch (UnknownHostException uhe) {
    			LOG.info("Cannot determine host name.  Using default: " + DEFAULT_PORT, uhe);
                hostName = DEFAULT_HOST;
        	}
    	}
    	else {
			jmxDomainName = jmxDomainName != null ? jmxDomainName : DEFAULT_DOMAIN;
			jmxConnectorPort = jmxConnectorPort > 0 ? jmxConnectorPort : DEFAULT_PORT;
            hostName = DEFAULT_HOST;
    	}
    	
    	if (!jmxEnabled) {
    		return;
    	}

    	List servers = MBeanServerFactory.findMBeanServer(jmxDomainName);
    	if (servers.size() == 0) {
    		server = MBeanServerFactory.createMBeanServer(jmxDomainName);
    	}
    	else {
    		server = (MBeanServer)servers.get(0);
    	}

    	try {
            createJmxConnector(hostName);
    	}
    	catch (IOException ioe) {
			LOG.warn("Could not create and start jmx connector.", ioe);
    	}
    }
    
    protected void createJmxConnector(String host) throws IOException {
    	if (jmxConnectorPort > 0) {
	        try {
	            LocateRegistry.createRegistry(jmxConnectorPort);
	        } catch (RemoteException ex) {
	            LocateRegistry.getRegistry(jmxConnectorPort);
	        }
	
	        JMXServiceURL url = new JMXServiceURL(
			cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, server);
			
	        Thread connectorThread = new Thread() {
	            public void run() {
	                try {
	                    cs.start();
	                } catch (IOException ioe) {
	        			LOG.warn("Could not start jmx connector thread.", ioe);
	                } 
	            }
	        };
	        connectorThread.setName("JMX Connector Thread [" + url + "]");
	        connectorThread.start();
			LOG.info("Jmx connector thread started on " + url);
    	}
    }    
}
