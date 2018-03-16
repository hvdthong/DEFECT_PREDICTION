package org.apache.camel.component.event;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultComponent;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * for working with Spring ApplicationEvents
 * 
 * @version $Revision: 630591 $
 */
public class EventComponent extends DefaultComponent<Exchange> implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public EventComponent() {
    }

    public EventComponent(ApplicationContext applicationContext) {
        setApplicationContext(applicationContext);
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ConfigurableApplicationContext getConfigurableApplicationContext() {
        ApplicationContext applicationContext = getApplicationContext();
        if (applicationContext instanceof ConfigurableApplicationContext) {
            return (ConfigurableApplicationContext)applicationContext;
        } else {
            throw new IllegalArgumentException("Not created with a ConfigurableApplicationContext! Was: " + applicationContext);
        }
    }

    protected EventEndpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        EventEndpoint answer = new EventEndpoint(uri, this);
        return answer;
    }
}
