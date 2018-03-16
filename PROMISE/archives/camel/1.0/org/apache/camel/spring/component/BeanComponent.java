package org.apache.camel.spring.component;

import org.apache.camel.Endpoint;
import org.apache.camel.component.pojo.PojoEndpoint;
import org.apache.camel.impl.DefaultComponent;
import static org.apache.camel.util.ObjectHelper.notNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * which will look up the URI in the Spring ApplicationContext and use that to handle message dispatching.
 * 
 * @version $Revision: 1.1 $
 */
public class BeanComponent extends DefaultComponent implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public BeanComponent() {
    }

    public BeanComponent(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        notNull(applicationContext, "applicationContext");
        Object object = applicationContext.getBean(remaining);
        if (object != null) {
            return new PojoEndpoint(uri, this, object);
        }
        return null;
    }

}
