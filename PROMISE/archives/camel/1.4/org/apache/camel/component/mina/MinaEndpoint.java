package org.apache.camel.component.mina;

import java.net.SocketAddress;

import org.apache.camel.Consumer;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoAcceptorConfig;
import org.apache.mina.common.IoConnector;
import org.apache.mina.common.IoConnectorConfig;
import org.apache.mina.common.IoSession;

/**
 * Endpoint for Camel MINA.
 *
 * @version $Revision: 655516 $
 */
public class MinaEndpoint extends DefaultEndpoint<MinaExchange> {

    private static final long DEFAULT_TIMEOUT = 30000;
    private long timeout = DEFAULT_TIMEOUT;

    private final IoAcceptor acceptor;
    private final SocketAddress address;
    private final IoConnector connector;
    private final IoAcceptorConfig acceptorConfig;
    private final IoConnectorConfig connectorConfig;
    private final boolean lazySessionCreation;
    private final boolean transferExchange;

    public MinaEndpoint(String endpointUri, MinaComponent component, SocketAddress address,
                        IoAcceptor acceptor, IoAcceptorConfig acceptorConfig, IoConnector connector,
                        IoConnectorConfig connectorConfig, boolean lazySessionCreation, long timeout,
                        boolean transferExchange) {
        super(endpointUri, component);
        this.address = address;
        this.acceptor = acceptor;
        this.acceptorConfig = acceptorConfig;
        this.connectorConfig = connectorConfig;
        this.connector = connector;
        this.lazySessionCreation = lazySessionCreation;
        if (timeout > 0) {
            this.timeout = timeout;
        }
        this.transferExchange = transferExchange;
    }

    @SuppressWarnings({"unchecked"})
    public Producer<MinaExchange> createProducer() throws Exception {
        return new MinaProducer(this);
    }

    public Consumer<MinaExchange> createConsumer(Processor processor) throws Exception {
        return new MinaConsumer(this, processor);
    }

    @Override
    public MinaExchange createExchange(ExchangePattern pattern) {
        return new MinaExchange(getCamelContext(), pattern, null);
    }

    public MinaExchange createExchange(IoSession session, Object payload) {
        MinaExchange exchange = new MinaExchange(getCamelContext(), getExchangePattern(), session);
        MinaPayloadHelper.setIn(exchange, payload);
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

    public boolean isLazySessionCreation() {
        return lazySessionCreation;
    }

    public IoAcceptorConfig getAcceptorConfig() {
        return acceptorConfig;
    }

    public IoConnectorConfig getConnectorConfig() {
        return connectorConfig;
    }

    public boolean isSingleton() {
        return true;
    }

    public long getTimeout() {
        return timeout;
    }

    public boolean isTransferExchange() {
        return transferExchange;
    }

}
