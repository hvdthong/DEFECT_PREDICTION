package org.apache.camel.component.bean;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.spi.Registry;
import org.apache.camel.util.CamelContextHelper;
import org.apache.camel.util.ObjectHelper;

/**
 * An implementation of a {@link BeanHolder} which will look up a bean from the registry and act as a cache of its metadata
 *
 * @version $Revision: 656397 $
 */
public class RegistryBean implements BeanHolder {
    private final CamelContext context;
    private final String name;
    private final Registry registry;
    private Processor processor;
    private BeanInfo beanInfo;
    private Object bean;
    private ParameterMappingStrategy parameterMappingStrategy;

    public RegistryBean(CamelContext context, String name) {
        this.context = context;
        this.name = name;
        this.registry = context.getRegistry();
    }

    public RegistryBean(CamelContext context, String name, ParameterMappingStrategy parameterMappingStrategy) {
        this(context, name);
        this.parameterMappingStrategy = parameterMappingStrategy;
    }

    @Override
    public String toString() {
        return "bean: " + name;
    }


    public ConstantBeanHolder createCacheHolder() throws Exception {
        return new ConstantBeanHolder(getBean(), getBeanInfo());
    }

    public Object getBean() throws Exception {
        Object value = lookupBean();
        if (value == null) {
            throw new NoBeanAvailableException(name);
        }
        if (value != bean) {
            bean = value;
            processor = null;
            if (!ObjectHelper.equal(ObjectHelper.type(bean), ObjectHelper.type(value))) {
                beanInfo = null;
            }
        }
        return value;
    }

    public Processor getProcessor() {
        if (processor == null && bean != null) {
            processor = CamelContextHelper.convertTo(context, Processor.class, bean);
        }
        return processor;
    }

    public BeanInfo getBeanInfo() {
        if (beanInfo == null && bean != null) {
            this.beanInfo = createBeanInfo();
        }
        return beanInfo;
    }

    public String getName() {
        return name;
    }

    public Registry getRegistry() {
        return registry;
    }

    public CamelContext getContext() {
        return context;
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

    protected BeanInfo createBeanInfo() {
        return new BeanInfo(context, bean.getClass(), getParameterMappingStrategy());
    }

    protected ParameterMappingStrategy createParameterMappingStrategy() {
        return BeanInfo.createParameterMappingStrategy(context);
    }

    protected Object lookupBean() throws Exception {
        return registry.lookup(name);
    }
}
