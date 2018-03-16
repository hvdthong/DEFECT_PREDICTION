package org.apache.camel.component.ldap;

import java.net.URISyntaxException;

import javax.naming.directory.SearchControls;

import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.DefaultEndpoint;

/**
 * Represents an endpoint that synchronously invokes an LDAP server when a producer sends a message to it.
 *
 * @version
 */
public class LdapEndpoint<E extends Exchange> extends DefaultEndpoint<E> {
    public static final String SYSTEM_DN  = "ou=system";
    public static final String OBJECT_SCOPE = "object";
    public static final String ONELEVEL_SCOPE = "onelevel";
    public static final String SUBTREE_SCOPE = "subtree";

    private String remaining;
    private String base = SYSTEM_DN;
    private String scope = SUBTREE_SCOPE;

    protected LdapEndpoint(String endpointUri, String remaining, LdapComponent component) throws URISyntaxException {
        super(endpointUri, component);
        this.remaining = remaining;
    }

    public LdapEndpoint(String endpointUri, String remaining) throws URISyntaxException {
        super(endpointUri);
        this.remaining = remaining;
    }

    public Consumer<E> createConsumer(Processor processor) throws Exception {
        throw new RuntimeCamelException("An LDAP Consumer would be the LDAP server itself! No such support here");
    }

    public Producer<E> createProducer() throws Exception {
        return new LdapProducer(this, remaining, base, toSearchControlScope(scope));
    }

    public boolean isSingleton() {
        return true;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    private int toSearchControlScope(String scope) {
        if (scope.equalsIgnoreCase(OBJECT_SCOPE)) {
            return SearchControls.OBJECT_SCOPE;
        } else if (scope.equalsIgnoreCase(ONELEVEL_SCOPE)) {
            return SearchControls.ONELEVEL_SCOPE;
        } else if (scope.equalsIgnoreCase(SUBTREE_SCOPE)) {
            return SearchControls.SUBTREE_SCOPE;
        } else {
            throw new IllegalArgumentException("Invalid search scope \"" + scope
                + "\" for LdapEndpoint: " + getEndpointUri());
        }
    }
}
