package org.apache.camel.component.stringtemplate;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * @version $Revision: 663817 $
 */
public class StringTemplateComponent extends DefaultComponent {

    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new StringTemplateEndpoint(uri, this, remaining, parameters);
    }

}
