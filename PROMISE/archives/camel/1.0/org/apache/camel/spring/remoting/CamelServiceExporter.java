package org.apache.camel.spring.remoting;

import org.apache.camel.CamelContext;
import org.apache.camel.component.pojo.PojoComponent;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.support.RemoteExporter;

/**
 * Exports a Spring defined service to Camel as a Pojo endpoint.
 *  
 * @author chirino
 */
public class CamelServiceExporter extends RemoteExporter implements InitializingBean, DisposableBean {

	CamelContext camelContext;
	PojoComponent pojoComponent;
	String serviceName;
	
	public void afterPropertiesSet() throws Exception {
		if( serviceName == null ) {
			throw new IllegalArgumentException("The serviceName must be configured.");
		}
		if( pojoComponent == null ) {
			if( camelContext == null ) {
				throw new IllegalArgumentException("A pojoComponent or camelContext must be configured.");
			}
			pojoComponent = (PojoComponent) camelContext.getComponent("pojo");
			if( pojoComponent == null ) {
				throw new IllegalArgumentException("The pojoComponent could not be found.");
			}
		}
		pojoComponent.addService(serviceName, getProxyForService());
	}

	public void destroy() throws Exception {
		if( serviceName!=null ) {
			pojoComponent.removeService(serviceName);
		}
	}

	
	public PojoComponent getPojoComponent() {
		return pojoComponent;
	}
	public void setPojoComponent(PojoComponent pojoComponent) {
		this.pojoComponent = pojoComponent;
	}

	public CamelContext getCamelContext() {
		return camelContext;
	}
	public void setCamelContext(CamelContext camelContext) {
		this.camelContext = camelContext;
	}

	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
