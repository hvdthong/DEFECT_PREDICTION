package org.apache.camel.component.list;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 *
 * @version $Revision: 659782 $
 */
public class ListComponent extends DefaultComponent {

    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new ListEndpoint(uri, this);
    }
}
