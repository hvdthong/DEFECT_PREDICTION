package org.apache.camel.component.http;

import org.apache.camel.Processor;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultConsumer;

/**
 * @version $Revision: 534063 $
 */
public class HttpConsumer extends DefaultConsumer<HttpExchange> {

	private final HttpEndpoint endpoint;

	public HttpConsumer(HttpEndpoint endpoint, Processor processor) {
		super(endpoint, processor);
		this.endpoint = endpoint;
	}

    @Override
    public HttpEndpoint getEndpoint() {
        return (HttpEndpoint) super.getEndpoint();
    }

    public HttpBinding getBinding() {
        return endpoint.getBinding();
    }

    public String getPath() {
        return endpoint.getPath();
    }

    @Override
	protected void doStart() throws Exception {
		super.doStart();
		endpoint.connect(this);		
	}
	
	@Override
	protected void doStop() throws Exception {
		endpoint.disconnect(this);
		super.doStop();
	}

}
