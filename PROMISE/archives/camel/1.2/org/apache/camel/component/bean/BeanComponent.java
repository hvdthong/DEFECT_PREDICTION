package org.apache.camel.component.bean;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.impl.ProcessorEndpoint;
import org.apache.camel.spi.Registry;
import org.apache.camel.util.IntrospectionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * which will look up the URI in the Spring ApplicationContext and use that to handle message dispatching.
 *
 * @version $Revision: 1.1 $
 */
public class BeanComponent extends DefaultComponent {
    private static final Log LOG = LogFactory.getLog(BeanComponent.class);
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
        Object bean = getBean(remaining);
        BeanProcessor processor = new BeanProcessor(bean, getCamelContext(), getParameterMappingStrategy());
        setProperties(processor, parameters);
        return createEndpoint(uri, processor);
    }

    public Object getBean(String remaining) throws NoBeanAvailableException {
        Registry registry = getCamelContext().getRegistry();
        Object bean = registry.lookup(remaining);
        if (bean == null) {
            throw new NoBeanAvailableException(remaining);
        }
        return bean;
    }

    protected ProcessorEndpoint createEndpoint(String uri, BeanProcessor processor) {
        ProcessorEndpoint answer = new ProcessorEndpoint(uri, this, processor);
        answer.setExchangePattern(ExchangePattern.InOut);
        return answer;
    }
               
    protected ParameterMappingStrategy createParameterMappingStrategy() {
        return BeanProcessor.createParameterMappingStrategy(getCamelContext());
    }
}
