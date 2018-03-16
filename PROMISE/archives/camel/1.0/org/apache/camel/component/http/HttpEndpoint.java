package org.apache.camel.component.http;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.DefaultEndpoint;

/**
 *
 * @version $Revision: 550760 $
 */
public class HttpEndpoint extends DefaultEndpoint<HttpExchange> {

    private HttpBinding binding;
	private HttpComponent component;
	private URI httpUri;
	
    protected HttpEndpoint(String uri, HttpComponent component) throws URISyntaxException {
        super(uri, component);
		this.component = component;
		this.httpUri = new URI(uri);
    }

    public HttpProducer createProducer() throws Exception {
    	return new HttpProducer(this);
    }

    public Consumer<HttpExchange> createConsumer(Processor processor) throws Exception {
        return new HttpConsumer(this, processor);
    }

    public HttpExchange createExchange() {
        return new HttpExchange(this);
    }

    public HttpExchange createExchange(HttpServletRequest request, HttpServletResponse response) {
        return new HttpExchange(this, request, response);
    }

    public HttpBinding getBinding() {
        if (binding == null) {
            binding = new HttpBinding();
        }
        return binding;
    }

    public void setBinding(HttpBinding binding) {
        this.binding = binding;
    }
    
	public boolean isSingleton() {
		return true;
	}

	public void connect(HttpConsumer consumer) throws Exception {
		component.connect(consumer);
	}

	public void disconnect(HttpConsumer consumer) throws Exception {
		component.disconnect(consumer);
	}

	public String getPath() {
		return httpUri.getPath();
	}

	public int getPort() {
		if( httpUri.getPort() == -1 ) {
			if( "https".equals(getProtocol() ) ) {
				return 443;
			} else {
				return 80;
			}
		}
		return httpUri.getPort();
	}

	public String getProtocol() {
		return httpUri.getScheme(); 
	}
}
