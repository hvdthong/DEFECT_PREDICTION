package org.apache.camel.component.cxf;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.HeaderFilterStrategyAware;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.spi.HeaderFilterStrategy;


/**

 * @version $Revision: 682597 $
 */
public class CxfComponent extends DefaultComponent<CxfExchange> implements HeaderFilterStrategyAware {


    private HeaderFilterStrategy headerFilterStrategy = new CxfHeaderFilterStrategy();

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

    public HeaderFilterStrategy getHeaderFilterStrategy() {
        return headerFilterStrategy;
    }

    public void setHeaderFilterStrategy(HeaderFilterStrategy strategy) {
        headerFilterStrategy = strategy;
    }


}
