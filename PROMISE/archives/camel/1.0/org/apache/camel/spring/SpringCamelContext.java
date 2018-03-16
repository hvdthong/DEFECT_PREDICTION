package org.apache.camel.spring;

import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.Injector;
import org.apache.camel.spi.ComponentResolver;
import org.apache.camel.CamelContext;
import org.apache.camel.spring.spi.SpringComponentResolver;
import org.apache.camel.spring.spi.SpringInjector;
import org.apache.camel.spring.component.BeanComponent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;

/**
 * A Spring aware implementation of {@link CamelContext} which will automatically register itself with Springs lifecycle
 * methods  plus allows spring to be used to customize a any
 * and beans via the Spring {@link ApplicationContext}
 *
 * @version $Revision: 538919 $
 */
public class SpringCamelContext extends DefaultCamelContext implements InitializingBean, DisposableBean, ApplicationContextAware {
    private ApplicationContext applicationContext;

    public SpringCamelContext() {
    }

    public SpringCamelContext(ApplicationContext applicationContext) {
        setApplicationContext(applicationContext);
    }

    public static SpringCamelContext springCamelContext(ApplicationContext applicationContext) throws Exception {
        String[] names = applicationContext.getBeanNamesForType(SpringCamelContext.class);
        if (names.length == 1) {
            return (SpringCamelContext) applicationContext.getBean(names[0], SpringCamelContext.class);
        }
        SpringCamelContext answer = new SpringCamelContext();
        answer.setApplicationContext(applicationContext);
        answer.afterPropertiesSet();
        return answer;
    }

    public static SpringCamelContext springCamelContext(String configLocations) throws Exception {
        return springCamelContext(new ClassPathXmlApplicationContext(configLocations));
    }

    public void afterPropertiesSet() throws Exception {
        getInjector();

        start();
    }

    public void destroy() throws Exception {
        stop();
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        addComponent("bean", new BeanComponent(applicationContext));
    }

    @Override
    protected Injector createInjector() {
        return new SpringInjector((AbstractRefreshableApplicationContext) getApplicationContext());
    }

    @Override
    protected ComponentResolver createComponentResolver() {
        ComponentResolver defaultResolver = super.createComponentResolver();
        return new SpringComponentResolver(getApplicationContext(), defaultResolver);
    }
}
