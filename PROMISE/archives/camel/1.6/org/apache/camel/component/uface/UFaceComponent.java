package org.apache.camel.component.uface;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * @version $Revision: 642879 $
 */
public class UFaceComponent extends DefaultComponent {
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new UFaceEndpoint(uri, this);
    }
}
