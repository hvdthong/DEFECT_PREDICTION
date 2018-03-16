package org.apache.camel.component.ldap;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @version $
 */
public class LdapProducer<E extends Exchange> extends DefaultProducer<DefaultExchange> {
    private static final transient Log LOG = LogFactory.getLog(LdapProducer.class);
    private DirContext ldapContext;
    private SearchControls controls;
    private String searchBase;

    public LdapProducer(LdapEndpoint endpoint, String remaining, String base, int scope) throws Exception {
        super(endpoint);

        ldapContext = (DirContext)getEndpoint().getCamelContext().getRegistry().lookup(remaining);
        searchBase = base;
        controls = new SearchControls();
        controls.setSearchScope(scope);
    }

    public void process(Exchange exchange) throws Exception {
        String filter = exchange.getIn().getBody(String.class);

        List<SearchResult> data = new ArrayList<SearchResult>();
        NamingEnumeration<SearchResult> namingEnumeration =
            ldapContext.search(searchBase, filter, getControls());

        while (namingEnumeration.hasMore()) {
            data.add(namingEnumeration.next());
        }
        exchange.getOut().setBody(data);
    }

    public DirContext getDirContext() {
        return ldapContext;
    }

    protected SearchControls getControls() {
        return controls;
    }
}
