package org.apache.camel.component.cxf;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

import java.util.Map;


/**

 * @version $Revision: 576522 $
 */
public class CxfComponent extends DefaultComponent<CxfExchange> {
	

    public CxfComponent() {
    }

    public CxfComponent(CamelContext context) {
        super(context);
    }

    @Override
    protected Endpoint<CxfExchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        CxfEndpoint result = new CxfEndpoint(uri, remaining, this);        
        setProperties(result, parameters);
        return result;
    }

    
}
