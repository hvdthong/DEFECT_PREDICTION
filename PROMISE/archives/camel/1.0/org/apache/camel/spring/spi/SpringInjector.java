package org.apache.camel.spring.spi;

import org.apache.camel.spi.Injector;
import org.apache.camel.impl.ReflectionInjector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 * A Spring implementation of {@link Injector} allowing Spring to be used to dependency inject newly created POJOs
 *
 * @version $Revision: 530384 $
 */
public class SpringInjector extends ReflectionInjector {
    private static final transient Log log = LogFactory.getLog(SpringInjector.class);

    private final AbstractRefreshableApplicationContext applicationContext;
    private int autowireMode = AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR;
    private boolean dependencyCheck = false;

    public SpringInjector(AbstractRefreshableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Object newInstance(Class type) {
        return applicationContext.getBeanFactory().createBean(type, autowireMode, dependencyCheck);
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
