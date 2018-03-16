package org.apache.camel.spi;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.camel.Service;
import org.apache.camel.management.CamelNamingStrategy;

public interface InstrumentationAgent extends Service {

    /**
     * Registers object with management infrastructure with a specific name. Object must be annotated or 
     * implement standard MBean interface.
     * @param obj
     * @param name
     * @throws JMException
     */
    void register(Object obj, ObjectName name) throws JMException;
    
    /**
     * Registers object with management infrastructure with a specific name. Object must be annotated or 
     * implement standard MBean interface.
     * @param obj
     * @param name
     * @param forceRegistration if set to true, then component will be registered despite existing component.
     * @throws JMException
     */
    void register(Object obj, ObjectName name, boolean forceRegistration) throws JMException;
    
    /**
     * Unregisters component based upon registered name
     * @param name
     * @throws JMException
     */
    void unregister(ObjectName name) throws JMException;

    /**
     * Get the MBeanServer which hosts managed components
     * NOTE: if the configuration is not set the JMXEnabled to be true, this method
     * will return null
     * @return the MBeanServer 
     */
    MBeanServer getMBeanServer();

    CamelNamingStrategy getNamingStrategy();
}
