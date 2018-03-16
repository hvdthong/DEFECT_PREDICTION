package org.apache.camel.component.rmi;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.pojo.PojoExchange;
import org.apache.camel.impl.DefaultComponent;

/**
 * @version $Revision:520964 $
 */
public class RmiComponent extends DefaultComponent<PojoExchange> {

	public RmiComponent() {
	}

	public RmiComponent(CamelContext context) {
		super(context);
	}

	@Override
	protected Endpoint<PojoExchange> createEndpoint(String uri,
			String remaining, Map parameters) throws Exception {
		return new RmiEndpoint(uri, this);
	}

}
