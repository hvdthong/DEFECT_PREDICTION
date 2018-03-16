package org.apache.camel.component.bean;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.converter.ObjectConverter;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.ProcessorEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * which will look up the URI in the Spring ApplicationContext and use that to handle message dispatching.
 *
 * @version $Revision: 660275 $
 */
public class BeanComponent extends DefaultComponent {
    private static final transient Log LOG = LogFactory.getLog(BeanComponent.class);
    private ParameterMappingStrategy parameterMappingStrategy;

    public BeanComponent() {
    }

    /**
     * A helper method to create a new endpoint from a bean with a generated URI
     */
    public ProcessorEndpoint createEndpoint(Object bean) {
        String uri = "bean:generated:" + bean;
        return createEndpoint(bean, uri);
    }

    /**
     * A helper method to create a new endpoint from a bean with a given URI
     */
    public ProcessorEndpoint createEndpoint(Object bean, String uri) {
        BeanProcessor processor = new BeanProcessor(bean, getCamelContext(), getParameterMappingStrategy());
        return createEndpoint(uri, processor);
    }

    public ParameterMappingStrategy getParameterMappingStrategy() {
        if (parameterMappingStrategy == null) {
            parameterMappingStrategy = createParameterMappingStrategy();
        }
        return parameterMappingStrategy;
    }

    public void setParameterMappingStrategy(ParameterMappingStrategy parameterMappingStrategy) {
        this.parameterMappingStrategy = parameterMappingStrategy;
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        BeanEndpoint endpoint = new BeanEndpoint(uri, this);
        endpoint.setBeanName(remaining);
        endpoint.setCache(ObjectConverter.toBool(parameters.remove("cache")));
        Processor processor = endpoint.getProcessor();
        setProperties(processor, parameters);
        return endpoint;
    }

    protected BeanEndpoint createEndpoint(String uri, BeanProcessor processor) {
        return new BeanEndpoint(uri, this, processor);
    }

    protected ParameterMappingStrategy createParameterMappingStrategy() {
        return BeanInfo.createParameterMappingStrategy(getCamelContext());
    }
}
