package org.apache.camel.component.ldap;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link LdapEndpoint}(s).
 *
 * @version
 */
public class LdapComponent<E extends Exchange> extends DefaultComponent<E> {

    public LdapComponent() {
    }

    public LdapComponent(CamelContext context) {
        super(context);
    }

    protected Endpoint<E> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        Endpoint<E> endpoint = new LdapEndpoint<E>(uri, remaining, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
