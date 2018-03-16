package org.apache.camel.component.rmi;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.bean.BeanExchange;
import org.apache.camel.impl.DefaultEndpoint;

/**
 * @version $Revision:520964 $
 */
public class RmiEndpoint extends DefaultEndpoint<BeanExchange> {

    private List<Class> remoteInterfaces;
    private ClassLoader classLoader;
    private URI uri;
    private int port;

    protected RmiEndpoint(String endpointUri, RmiComponent component) throws URISyntaxException {
        super(endpointUri, component);
        this.uri = new URI(endpointUri);
    }

    public boolean isSingleton() {
        return false;
    }

    @Override
    public BeanExchange createExchange(ExchangePattern pattern) {
        return new BeanExchange(getContext(), pattern);
    }

    public Consumer<BeanExchange> createConsumer(Processor processor) throws Exception {
        if (remoteInterfaces == null || remoteInterfaces.size() == 0) {
            throw new RuntimeCamelException("To create an RMI consumer, the RMI endpoint's remoteInterfaces property must be be configured.");
        }
        return new RmiConsumer(this, processor);
    }

    public Producer<BeanExchange> createProducer() throws Exception {
        return new RmiProducer(this);
    }

    public String getName() {
        String path = uri.getPath();
        if (path == null) {
            path = uri.getSchemeSpecificPart();
        }
        return path;
    }

    public Registry getRegistry() throws RemoteException {
        if (uri.getHost() != null) {
            if (uri.getPort() == -1) {
                return LocateRegistry.getRegistry(uri.getHost());
            } else {
                return LocateRegistry.getRegistry(uri.getHost(), uri.getPort());
            }
        } else {
            return LocateRegistry.getRegistry();
        }
    }

    public List<Class> getRemoteInterfaces() {
        return remoteInterfaces;
    }

    public void setRemoteInterfaces(List<Class> remoteInterfaces) {
        this.remoteInterfaces = remoteInterfaces;
        if (classLoader == null && !remoteInterfaces.isEmpty()) {
            classLoader = remoteInterfaces.get(0).getClassLoader();
        }
    }

    public void setRemoteInterfaces(Class... remoteInterfaces) {
        setRemoteInterfaces(Arrays.asList(remoteInterfaces));
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
