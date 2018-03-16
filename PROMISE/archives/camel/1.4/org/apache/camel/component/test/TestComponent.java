package org.apache.camel.component.test;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.util.CamelContextHelper;
import org.apache.camel.util.ObjectHelper;
import org.apache.camel.util.UnsafeUriCharactersEncoder;

/**
 * Test Component.
 *
 * @see org.apache.camel.component.test.TestEndpoint
 *
 * @version $Revision: 673477 $
 */
public class TestComponent extends DefaultComponent<Exchange> {

    public Endpoint<Exchange> createEndpoint(String uri) throws Exception {

        ObjectHelper.notNull(getCamelContext(), "camelContext");
        URI u = new URI(UnsafeUriCharactersEncoder.encode(uri));
        String path = u.getSchemeSpecificPart();

        return createEndpoint(uri, path, new HashMap());
    }

    @Override
    protected Endpoint<Exchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        Endpoint endpoint = CamelContextHelper.getMandatoryEndpoint(getCamelContext(), remaining);
        return new TestEndpoint(uri, this, endpoint);
    }

}
