package org.apache.camel.component.atom;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

import java.util.Map;

/**
 *
 * @version $Revision: 1.1 $
 */
public class AtomComponent extends DefaultComponent {
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        AtomEndpoint answer = new AtomEndpoint(uri, this, remaining);
        setProperties(answer, parameters);
        return answer;
    }
}
