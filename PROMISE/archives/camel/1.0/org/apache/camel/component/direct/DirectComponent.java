package org.apache.camel.component.direct;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.util.IntrospectionSupport;
import org.apache.camel.util.ObjectHelper;
import org.apache.camel.util.URISupport;

/**
 * Represents the component that manages {@link DirectEndpoint}.  It holds the 
 * list of named direct endpoints.
 *
 * @org.apache.xbean.XBean
 * @version $Revision: 519973 $
 */
public class DirectComponent<E extends Exchange> implements Component<E> {

	private CamelContext context;

	public CamelContext getCamelContext() {
		return context;
	}

	public ScheduledExecutorService getExecutorService() {
		return null;
	}

	public Endpoint<E> createEndpoint(String uri) throws Exception {

        ObjectHelper.notNull(getCamelContext(), "camelContext");        
        URI u = new URI(uri);
        Map parameters = URISupport.parseParamters(u);

        Endpoint<E> endpoint = new DirectEndpoint<E>(uri,this);
        if (parameters != null) {
            IntrospectionSupport.setProperties(endpoint, parameters);
        }
        return endpoint;
	}

	public void setCamelContext(CamelContext context) {
		this.context = context;
	}	

}
