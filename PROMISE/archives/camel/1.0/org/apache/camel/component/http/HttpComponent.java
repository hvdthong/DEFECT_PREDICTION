package org.apache.camel.component.http;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 *
 * @version $Revision: 550734 $
 */
public class HttpComponent extends DefaultComponent<HttpExchange> {
	
	private CamelServlet camelServlet;
	
	/** 
	 * Connects the URL specified on the endpoint to the specified processor.
	 *  
	 * @throws Exception
	 */
	public void connect(HttpConsumer consumer) throws Exception {
		camelServlet.connect(consumer);
	}

	/**
	 * Disconnects the URL specified on the endpoint from the specified processor.
	 * 
	 * @throws Exception
	 */
	public void disconnect(HttpConsumer consumer) throws Exception {
		camelServlet.disconnect(consumer);
	}

	public CamelServlet getCamelServlet() {
		return camelServlet;
	}

	public void setCamelServlet(CamelServlet camelServlet) {
		this.camelServlet = camelServlet;
	}

	@Override
	protected Endpoint<HttpExchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
		return new HttpEndpoint(uri, this);
	}

}
