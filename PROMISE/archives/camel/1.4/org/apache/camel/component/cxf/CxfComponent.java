package org.apache.camel.component.cxf;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;


/**

 * @version $Revision: 645763 $
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
