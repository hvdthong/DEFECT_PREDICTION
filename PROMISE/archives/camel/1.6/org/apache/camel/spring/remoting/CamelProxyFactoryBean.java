package org.apache.camel.spring.remoting;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Endpoint;
import org.apache.camel.Producer;
import org.apache.camel.component.bean.ProxyHelper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.remoting.support.UrlBasedRemoteAccessor;

/**
 * A {@link FactoryBean} to create a Proxy to a a Camel Pojo Endpoint.
 *
 * @author chirino
 */
public class CamelProxyFactoryBean extends UrlBasedRemoteAccessor implements FactoryBean, CamelContextAware, DisposableBean {
    private CamelContext camelContext;
    private Endpoint endpoint;
    private Object serviceProxy;
    private Producer producer;

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        try {
            if (endpoint == null) {
                if (getServiceUrl() == null || camelContext == null) {
                    throw new IllegalArgumentException("If endpoint is not specified, the serviceUrl and camelContext must be specified.");
                }

                endpoint = camelContext.getEndpoint(getServiceUrl());
                if (endpoint == null) {
                    throw new IllegalArgumentException("Could not resolve endpoint: " + getServiceUrl());
                }
            }

            this.producer = endpoint.createProducer();
            this.producer.start();
            this.serviceProxy = ProxyHelper.createProxy(endpoint, producer, getServiceInterface());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void destroy() throws Exception {
        this.producer.stop();
        this.producer = null;
        this.serviceProxy = null;
    }


    public Class getServiceInterface() {
        return super.getServiceInterface();
    }

    public String getServiceUrl() {
        return super.getServiceUrl();
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
