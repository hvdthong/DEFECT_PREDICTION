package org.apache.camel.component.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.apache.camel.Exchange;
import org.apache.camel.component.bean.BeanExchange;
import org.apache.camel.component.bean.BeanHolder;
import org.apache.camel.component.bean.BeanProcessor;
import org.apache.camel.impl.DefaultProducer;

/**
 * @version $Revision: 655516 $
 */
public class RmiProducer extends DefaultProducer<BeanExchange> {

    private BeanProcessor beanProcessor;

    public RmiProducer(RmiEndpoint endpoint) throws RemoteException, NotBoundException {
        super(endpoint);
        BeanHolder holder = new RmiRegistryBean(endpoint.getCamelContext(), endpoint.getName(), endpoint.getRegistry());
        beanProcessor = new BeanProcessor(holder);
        String method = endpoint.getMethod();
        if (method != null) {
            beanProcessor.setMethod(method);
        }
    }

    public void process(Exchange exchange) throws Exception {
        beanProcessor.process(exchange);
    }

}
