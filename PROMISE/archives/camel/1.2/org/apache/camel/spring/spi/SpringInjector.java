package org.apache.camel.spring.spi;

import org.apache.camel.spi.Injector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * A Spring implementation of {@link Injector} allowing Spring to be used to dependency inject newly created POJOs
 *
 * @version $Revision: 565855 $
 */
public class SpringInjector implements Injector {
    private final ConfigurableApplicationContext applicationContext;
    private int autowireMode = AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR;
    private boolean dependencyCheck;

    public SpringInjector(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public <T> T newInstance(Class<T> type) {
        Object value = applicationContext.getBeanFactory().createBean(type, autowireMode, dependencyCheck);
        return type.cast(value);
    }

    public int getAutowireMode() {
        return autowireMode;
    }

    public void setAutowireMode(int autowireMode) {
        this.autowireMode = autowireMode;
    }

    public boolean isDependencyCheck() {
        return dependencyCheck;
    }

    public void setDependencyCheck(boolean dependencyCheck) {
        this.dependencyCheck = dependencyCheck;
    }
}
