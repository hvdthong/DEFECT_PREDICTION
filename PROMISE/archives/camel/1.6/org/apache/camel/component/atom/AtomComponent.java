package org.apache.camel.component.atom;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * <p/>
 * Camel uses Apache Abdera as the Atom implementation. 
 *
 * @version $Revision: 656933 $
 */
public class AtomComponent extends DefaultComponent {

    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new AtomEndpoint(uri, this, remaining);
    }

}
