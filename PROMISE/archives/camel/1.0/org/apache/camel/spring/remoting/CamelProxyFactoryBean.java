package org.apache.camel.spring.remoting;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.pojo.PojoComponent;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.remoting.support.UrlBasedRemoteAccessor;

/**
 * Creates a Proxy to Camel Pojo Endpoint.
 *  
 * @author chirino
 */
public class CamelProxyFactoryBean extends UrlBasedRemoteAccessor implements FactoryBean {

	private CamelContext camelContext;
	private Endpoint endpoint;
	private Object serviceProxy;
	
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		try {
			if( endpoint == null ) {
				if( getServiceUrl() == null || camelContext==null ) {
					throw new IllegalArgumentException("If endpoint is not specified, the serviceUrl and camelContext must be specified.");
				}
				
				endpoint = camelContext.getEndpoint(getServiceUrl());
				if( endpoint == null ) {
					throw new IllegalArgumentException("Could not resolve endpoint: "+getServiceUrl());
				}
			}
			
			this.serviceProxy = PojoComponent.createProxy(endpoint, getServiceInterface());
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public Object getObject() throws Exception {
		return serviceProxy;
	}

	public Class getObjectType() {
		return getServiceInterface();
	}

	public boolean isSingleton() {
		return true;
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	public CamelContext getCamelContext() {
		return camelContext;
	}

	public void setCamelContext(CamelContext camelContext) {
		this.camelContext = camelContext;
	}
	
}
