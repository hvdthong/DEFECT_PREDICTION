package org.apache.camel.component.rmi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.apache.camel.Processor;
import org.apache.camel.component.bean.BeanExchange;
import org.apache.camel.component.bean.BeanInvocation;
import org.apache.camel.impl.DefaultConsumer;

/**
 * A {@link org.apache.camel.Consumer Consumer} which uses RMI's {@link UnicastRemoteObject} to consume
 * method invocations.
 * 
 * @version $Revision: 642772 $
 */
public class RmiConsumer extends DefaultConsumer<BeanExchange> implements InvocationHandler {

    private final RmiEndpoint endpoint;
    private Remote stub;
    private Remote proxy;

    public RmiConsumer(RmiEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;

    }

    @Override
    protected void doStart() throws Exception {
        Class[] interfaces = new Class[endpoint.getRemoteInterfaces().size()];
        endpoint.getRemoteInterfaces().toArray(interfaces);
        proxy = (Remote)Proxy.newProxyInstance(endpoint.getClassLoader(), interfaces, this);
        stub = UnicastRemoteObject.exportObject(proxy, endpoint.getPort());

        try {
            Registry registry = endpoint.getRegistry();
            String name = endpoint.getName();
            registry.bind(name, stub);

            try {
                UnicastRemoteObject.unexportObject(stub, true);
            } catch (Throwable ignore) {
            }
            stub = null;
            throw e;
        }
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        try {
            Registry registry = endpoint.getRegistry();
            registry.unbind(endpoint.getName());
        }
        UnicastRemoteObject.unexportObject(proxy, true);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!isStarted()) {
            throw new IllegalStateException("The endpoint is not active: " + getEndpoint().getEndpointUri());
        }
        BeanInvocation invocation = new BeanInvocation(method, args);
        BeanExchange exchange = getEndpoint().createExchange();
        exchange.setInvocation(invocation);
        getProcessor().process(exchange);
        Throwable fault = exchange.getException();
        if (fault != null) {
            throw new InvocationTargetException(fault);
        }
        return exchange.getOut().getBody();
    }

    public Remote getProxy() {
        return proxy;
    }

    public Remote getStub() {
        return stub;
    }
}
