package org.apache.camel.component.mina;

import java.net.SocketAddress;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoConnector;
import org.apache.mina.common.IoServiceConfig;
import org.apache.mina.common.IoSession;

/**
 * @version $Revision: 572899 $
 */
public class MinaEndpoint extends DefaultEndpoint<MinaExchange> {
    private final IoAcceptor acceptor;
    private final SocketAddress address;
    private final IoConnector connector;
    private final IoServiceConfig config;

    public MinaEndpoint(String endpointUri, MinaComponent component, SocketAddress address, IoAcceptor acceptor, IoConnector connector, IoServiceConfig config) {
        super(endpointUri, component);
        this.config = config;
        this.address = address;
        this.acceptor = acceptor;
        this.connector = connector;
    }

    public Producer<MinaExchange> createProducer() throws Exception {
        return new MinaProducer(this);
    }

    public Consumer<MinaExchange> createConsumer(Processor processor) throws Exception {
        return new MinaConsumer(this, processor);
    }

    @Override
    public MinaExchange createExchange(ExchangePattern pattern) {
        return new MinaExchange(getContext(), pattern);
    }

    public MinaExchange createExchange(IoSession session, Object object) {
        MinaExchange exchange = new MinaExchange(getContext(), getExchangePattern());
        exchange.getIn().setBody(object);
        return exchange;
    }

    public IoAcceptor getAcceptor() {
        return acceptor;
    }

    public SocketAddress getAddress() {
        return address;
    }

    public IoConnector getConnector() {
        return connector;
    }

    public IoServiceConfig getConfig() {
        return config;
    }

    public boolean isSingleton() {
        return true;
    }

}
