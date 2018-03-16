package org.apache.camel.component.bean;

import org.apache.camel.Component;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.impl.ProcessorEndpoint;

/**
 * Endpoint for the bean component.
 *
 * @version $Revision: 662301 $
 */
public class BeanEndpoint extends ProcessorEndpoint {
    private boolean cache;
    private String beanName;
    private String method;
    private BeanHolder beanHolder;

    public BeanEndpoint() {
        init();
    }

    public BeanEndpoint(String endpointUri) {
        super(endpointUri);
        init();
    }

    public BeanEndpoint(String endpointUri, BeanProcessor processor) {
        super(endpointUri, processor);
        init();
    }

    public BeanEndpoint(String endpointUri, Component component, BeanProcessor processor) {
        super(endpointUri, component, processor);
        init();
    }

    public BeanEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
        init();
    }


    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public BeanHolder getBeanHolder() {
        return beanHolder;
    }

    public void setBeanHolder(BeanHolder beanHolder) {
        this.beanHolder = beanHolder;
    }


    @Override
    protected String createEndpointUri() {
        return "bean:" + getBeanName() + (method != null ? "?method=" + method : "");
    }

    private void init() {
        setExchangePattern(ExchangePattern.InOut);
    }

    @Override
    protected Processor createProcessor() throws Exception {
        BeanHolder holder = getBeanHolder();
        if (holder == null) {
            RegistryBean registryBean = new RegistryBean(getCamelContext(), beanName);
            if (cache) {
                holder = registryBean.createCacheHolder();
            } else {
                holder = registryBean;
            }
        }
        BeanProcessor processor = new BeanProcessor(holder);
        if (method != null) {
            processor.setMethod(method);
        }
        return processor;
    }
}
