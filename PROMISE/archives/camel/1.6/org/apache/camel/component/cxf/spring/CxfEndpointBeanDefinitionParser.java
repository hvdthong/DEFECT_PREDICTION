package org.apache.camel.component.cxf.spring;

import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.bus.spring.BusWiringBeanFactoryPostProcessor;
import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.configuration.spring.AbstractBeanDefinitionParser;
import org.apache.cxf.service.factory.ReflectionServiceFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;



public class CxfEndpointBeanDefinitionParser extends AbstractBeanDefinitionParser {

    @Override
    protected Class getBeanClass(Element arg0) {
        return CxfSpringEndpointBean.class;
    }

    @Override
    protected void mapAttribute(BeanDefinitionBuilder bean, Element e, String name, String val) {
        if ("endpointName".equals(name) || "serviceName".equals(name)) {
            QName q = parseQName(e, val);
            bean.addPropertyValue(name, q);
        } else {
            mapToProperty(bean, name, val);
        }
    }

    @Override
    protected void mapElement(ParserContext ctx, BeanDefinitionBuilder bean, Element el, String name) {
        if ("properties".equals(name)) {
            Map map = ctx.getDelegate().parseMapElement(el, bean.getBeanDefinition());
            bean.addPropertyValue("properties", map);
        } else if ("binding".equals(name)) {
            setFirstChildAsProperty(el, ctx, bean, "bindingConfig");
        } else if ("inInterceptors".equals(name) || "inFaultInterceptors".equals(name)
            || "outInterceptors".equals(name) || "outFaultInterceptors".equals(name)
            || "features".equals(name) || "schemaLocations".equals(name)
            || "handlers".equals(name)) {
            java.util.List list = (java.util.List)ctx.getDelegate().parseListElement(el, bean.getBeanDefinition());
            bean.addPropertyValue(name, list);
        } else {
            setFirstChildAsProperty(el, ctx, bean, name);
        }
    }


    @Override
    protected void doParse(Element element, ParserContext ctx, BeanDefinitionBuilder bean) {
        super.doParse(element, ctx, bean);
        bean.setLazyInit(false);
    }

    @Override
    protected String resolveId(Element elem,
                               AbstractBeanDefinition definition,
                               ParserContext ctx)
        throws BeanDefinitionStoreException {
        String id = super.resolveId(elem, definition, ctx);
        if (StringUtils.isEmpty(id)) {
            throw new BeanDefinitionStoreException("The bean id is needed.");
        }

        return id;
    }

    @Override
    protected boolean hasBusProperty() {
        return true;
    }
    
    public static class CxfSpringEndpointBean extends CxfEndpointBean implements ApplicationContextAware {
        public CxfSpringEndpointBean() {
            super();
        }
        
        public CxfSpringEndpointBean(ReflectionServiceFactoryBean factory) {
            super(factory);
        }
        
        public void setApplicationContext(ApplicationContext ctx) throws BeansException {
            if (getBus() == null) {
                Bus bus = BusFactory.getDefaultBus();                
                setBus(bus);
            }
            BusWiringBeanFactoryPostProcessor.updateBusReferencesInContext(getBus(), ctx);
        }
        
    }


}
