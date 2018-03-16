package org.apache.camel.component.rmi;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import org.apache.camel.Exchange;
import org.apache.camel.component.bean.BeanProcessor;
import org.apache.camel.impl.DefaultProducer;

/**
 * @version $Revision: 533076 $
 */
public class RmiProducer extends DefaultProducer {

    private final RmiEndpoint endpoint;
    private Remote remote;
    private BeanProcessor beanProcessor;

    public RmiProducer(RmiEndpoint endpoint) throws RemoteException, NotBoundException {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        if (beanProcessor == null) {
            beanProcessor = new BeanProcessor(getRemote(), getEndpoint().getContext());
        }
        beanProcessor.process(exchange);
    }

    public Remote getRemote() throws RemoteException, NotBoundException {
        if (remote == null) {
            Registry registry = endpoint.getRegistry();
            remote = registry.lookup(endpoint.getName());
        }
        return remote;
    }

}
