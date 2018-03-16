package org.apache.camel.spi;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.camel.Service;

/**
 * Camel JMX service agent
 */
public interface InstrumentationAgent extends Service {

    /**
     * Registers object with management infrastructure with a specific name. Object must be annotated or 
     * implement standard MBean interface.
     *
     * @param obj  the object to register
     * @param name the name
     * @throws JMException is thrown if the registration failed
     */
    void register(Object obj, ObjectName name) throws JMException;
    
    /**
     * Registers object with management infrastructure with a specific name. Object must be annotated or 
     * implement standard MBean interface.
     *
     * @param obj  the object to register
     * @param name the name
     * @param forceRegistration if set to <tt>true</tt>, then object will be registered despite
     * existing object is already registered with the name.
     * @throws JMException is thrown if the registration failed
     */
    void register(Object obj, ObjectName name, boolean forceRegistration) throws JMException;
    
    /**
     * Unregisters object based upon registered name
     *
     * @param name the name
     * @throws JMException is thrown if the unregistration failed
     */
    void unregister(ObjectName name) throws JMException;

    /**
     * Get the MBeanServer which hosts managed objects.
     * <p/>
     * <b>Notice:</b> If the JMXEnabled configuration is not set to <tt>true</tt>,
     * this method will return <tt>null</tt>.
     * 
     * @return the MBeanServer
     */
    MBeanServer getMBeanServer();

    /**
     * Get domain name for Camel MBeans.
     * <p/>
     * <b>Notice:</b> That this can be different that the default domain name of the MBean Server.
     * 
     * @return domain name
     */
    String getMBeanObjectDomainName();

}
