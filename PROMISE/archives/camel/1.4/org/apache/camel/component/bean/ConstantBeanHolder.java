package org.apache.camel.component.bean;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.util.CamelContextHelper;

/**
 * A constant (singleton) bean implementation of {@link BeanHolder}
 *
 * @version $Revision: 640438 $
 */
public class ConstantBeanHolder implements BeanHolder {
    private final Object bean;
    private Processor processor;
    private BeanInfo beanInfo;

    public ConstantBeanHolder(Object bean, BeanInfo beanInfo) {
        this.bean = bean;
        this.beanInfo = beanInfo;
        this.processor = CamelContextHelper.convertTo(beanInfo.getCamelContext(), Processor.class, bean);
    }

    public ConstantBeanHolder(Object bean, CamelContext context) {
        this(bean, new BeanInfo(context, bean.getClass()));
    }
    public ConstantBeanHolder(Object bean, CamelContext context, ParameterMappingStrategy parameterMappingStrategy) {
        this(bean, new BeanInfo(context, bean.getClass(), parameterMappingStrategy));
    }

    @Override
    public String toString() {
        return bean.toString();
    }

    public Object getBean()  {
        return bean;
    }

    public Processor getProcessor() {
        return processor;
    }

    public BeanInfo getBeanInfo() {
        return beanInfo;
    }
}
