package org.apache.camel.component.jpa;

import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.util.ObjectHelper;

/**
 * A JPA Component
 *
 * @version $Revision: 630591 $
 */
public class JpaComponent extends DefaultComponent<Exchange> {
    private EntityManagerFactory entityManagerFactory;

    public Component resolveComponent(CamelContext container, String uri) throws Exception {
        return null;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }


    @Override
    protected Endpoint<Exchange> createEndpoint(String uri, String path, Map options) throws Exception {
        JpaEndpoint endpoint = new JpaEndpoint(uri, this);

        if (path != null) {
            Class<?> type = ObjectHelper.loadClass(path);
            if (type != null) {
                endpoint.setEntityType(type);
            }
        }
        return endpoint;
    }
}
