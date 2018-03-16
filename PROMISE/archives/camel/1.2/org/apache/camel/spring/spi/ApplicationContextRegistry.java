package org.apache.camel.spring.spi;

import org.apache.camel.spi.Registry;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

/**
 * A {@link Registry} implementation which looks up the objects in the Spring
 * {@link ApplicationContext}
 * 
 * @version $Revision: 1.1 $
 */
public class ApplicationContextRegistry implements Registry {
    private ApplicationContext applicationContext;

    public ApplicationContextRegistry(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public <T> T lookup(String name, Class<T> type) {
        try {
            Object value = applicationContext.getBean(name, type);
            return type.cast(value);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    public Object lookup(String name) {
        try {
            return applicationContext.getBean(name);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }
}
