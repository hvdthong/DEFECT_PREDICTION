package org.apache.camel.spring.factory;

import org.apache.camel.component.bean.BeanEndpoint;
import org.springframework.beans.factory.FactoryBean;

/**
 * Spring {@link org.springframework.beans.factory.FactoryBean} for creating
 * Camel {@link BeanEndpoint} objects.
 *
 * @version $Revision: 673477 $
 */
public class BeanEndpointFactory implements FactoryBean {
    private boolean singleton = true;

    public Object getObject() throws Exception {
        return new BeanEndpoint();
    }

    public Class getObjectType() {
        return BeanEndpoint.class;
    }

    public boolean isSingleton() {
        return singleton;
    }

    protected void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }



}
