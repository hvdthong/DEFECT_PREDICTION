package org.apache.camel.component.mina;

import java.net.SocketAddress;

import org.apache.camel.CamelException;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.camel.util.ExchangeHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

/**
 * A {@link org.apache.camel.Consumer Consumer} implementation for Apache MINA.
 *
 * @version $Revision: 641152 $
 */
public class MinaConsumer extends DefaultConsumer<MinaExchange> {
    private static final transient Log LOG = LogFactory.getLog(MinaConsumer.class);

    private final MinaEndpoint endpoint;
    private final SocketAddress address;
    private final IoAcceptor acceptor;

    public MinaConsumer(final MinaEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.address = endpoint.getAddress();
        this.acceptor = endpoint.getAcceptor();
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Binding to server address: " + address + " using acceptor: " + acceptor);
        }

        IoHandler handler = new ReceiveHandler();
        acceptor.bind(address, handler, endpoint.getAcceptorConfig());
    }

    @Override
    protected void doStop() throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Unbinding from server address: " + address + " using acceptor: " + acceptor);
        }
        acceptor.unbind(address);
        super.doStop();
    }

    /**
     * Handles comsuming messages and replying if the exchange is out capable.
     */
    private final class ReceiveHandler extends IoHandlerAdapter {

        @Override
        public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            if (session != null) {
                LOG.debug("Closing session as an exception was thrown from MINA");
                session.close();
            }

            throw new CamelException(cause);
        }

        @Override
        public void messageReceived(IoSession session, Object object) throws Exception {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Received body: " + object);
            }

            MinaExchange exchange = endpoint.createExchange(session, object);
            getProcessor().process(exchange);

            if (ExchangeHelper.isOutCapable(exchange)) {
                Object body = MinaPayloadHelper.getOut(endpoint, exchange);
                boolean failed = exchange.isFailed();

                if (failed) {
                    LOG.warn("Can not write body since the exchange is failed, closing session: " + exchange);
                    session.close();
                } else if (body == null) {
                    LOG.warn("Can not write body since its null, closing session: " + exchange);
                    session.close();
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Writing body: " + body);
                    }
                    MinaHelper.writeBody(session, body, exchange);
                }
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Can not write body since this exchange is not out capable: " + exchange);
                }
            }
        }

    }

}
