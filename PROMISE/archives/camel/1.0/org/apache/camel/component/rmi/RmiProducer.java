package org.apache.camel.component.rmi;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import org.apache.camel.component.pojo.PojoEndpoint;
import org.apache.camel.component.pojo.PojoExchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.Exchange;

/**
 * @version $Revision: 533076 $
 */
public class RmiProducer extends DefaultProducer {

	private final RmiEndpoint endpoint;
	private Remote remote;

	public RmiProducer(RmiEndpoint endpoint) throws AccessException, RemoteException, NotBoundException {
		super(endpoint);
		this.endpoint = endpoint;
	}

	public void process(Exchange exchange) throws AccessException, RemoteException, NotBoundException {
        PojoExchange pojoExchange = endpoint.toExchangeType(exchange);
        PojoEndpoint.invoke(getRemote(), pojoExchange);
        exchange.copyFrom(pojoExchange);
    }

	public Remote getRemote() throws AccessException, RemoteException, NotBoundException {
		if( remote == null ) {
			Registry registry = endpoint.getRegistry();				
			remote = registry.lookup(endpoint.getName());			
		}
		return remote;
	}
	
}
