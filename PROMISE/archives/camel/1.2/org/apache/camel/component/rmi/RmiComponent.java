package org.apache.camel.component.rmi;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.bean.BeanExchange;
import org.apache.camel.impl.DefaultComponent;

/**
 * @version $Revision:520964 $
 */
public class RmiComponent extends DefaultComponent<BeanExchange> {

    public RmiComponent() {
    }

    public RmiComponent(CamelContext context) {
        super(context);
    }

    @Override
    protected Endpoint<BeanExchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new RmiEndpoint(uri, this);
    }

}
