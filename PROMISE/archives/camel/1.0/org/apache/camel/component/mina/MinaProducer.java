package org.apache.camel.component.mina;

import org.apache.camel.Producer;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.IoConnector;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

import java.net.SocketAddress;

/**
 * A {@link Producer} implementation for MINA
 *
 * @version $Revision: 534145 $
 */
public class MinaProducer extends DefaultProducer {
    private static final transient Log log = LogFactory.getLog(MinaProducer.class);
    private IoSession session;
    private MinaEndpoint endpoint;

    public MinaProducer(MinaEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) {
        if (session == null) {
            throw new IllegalStateException("Not started yet!");
        }
        Object body = exchange.getIn().getBody();
        if (body == null) {
            log.warn("No payload for exchange: " + exchange);
        }
        else {
            session.write(body);
        }
    }

    @Override
    protected void doStart() throws Exception {
        SocketAddress address = endpoint.getAddress();
        IoConnector connector = endpoint.getConnector();
        if (log.isDebugEnabled()) {
            log.debug("Creating connector to address: " + address + " using connector: " + connector);
        }
        IoHandler ioHandler = new IoHandlerAdapter() {
            @Override
            public void messageReceived(IoSession ioSession, Object object) throws Exception {
                super.messageReceived(ioSession, object);    /** TODO */
            }
        };
        ConnectFuture future = connector.connect(address, ioHandler, endpoint.getConfig());
        future.join();
        session = future.getSession();
    }

    @Override
    protected void doStop() throws Exception {
        if (session != null) {
            session.close().join(2000);
        }
    }
}
