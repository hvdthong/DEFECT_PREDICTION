package org.apache.camel.spring;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlTransient;
import javax.management.MBeanServer;

import org.apache.camel.management.InstrumentationAgentImpl;
import org.apache.camel.CamelContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class SpringInstrumentationAgent extends InstrumentationAgentImpl
        implements InitializingBean, DisposableBean {
    private static final transient Log LOG = LogFactory.getLog(SpringInstrumentationAgent.class);

    public void afterPropertiesSet() throws Exception {
		LOG.debug("Starting JMX agent on server: " + getMBeanServer());
        start();
	}

	public void destroy() throws Exception {
	}
}
