package org.apache.camel.component.mina;

import java.net.SocketAddress;

import org.apache.camel.CamelException;
import org.apache.camel.Exchange;
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
 * @version $Revision: 691178 $
 */
public class MinaConsumer extends DefaultConsumer<MinaExchange> {
    private static final transient Log LOG = LogFactory.getLog(MinaConsumer.class);

    private final MinaEndpoint endpoint;
    private final SocketAddress address;
    private final IoAcceptor acceptor;
    private boolean sync;

    public MinaConsumer(final MinaEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.address = endpoint.getAddress();
        this.acceptor = endpoint.getAcceptor();
        this.sync = endpoint.isSync();
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        if (LOG.isInfoEnabled()) {
            LOG.info("Binding to server address: " + address + " using acceptor: " + acceptor);
        }

        IoHandler handler = new ReceiveHandler();
        acceptor.bind(address, handler, endpoint.getAcceptorConfig());
    }

    @Override
    protected void doStop() throws Exception {
        if (LOG.isInfoEnabled()) {
            LOG.info("Unbinding from server address: " + address + " using acceptor: " + acceptor);
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
            if (endpoint.getCharsetName() != null) {
                exchange.setProperty(Exchange.CHARSET_NAME, endpoint.getCharsetName());
            }
            getProcessor().process(exchange);

            if (sync) {
                Object body;
                if (ExchangeHelper.isOutCapable(exchange)) {
                    body = MinaPayloadHelper.getOut(endpoint, exchange);
                } else {
                    body = MinaPayloadHelper.getIn(endpoint, exchange);
                }
                boolean failed = exchange.isFailed();
                if (failed && !endpoint.isTransferExchange()) {
                    if (exchange.getException() != null) {
                        body = exchange.getException();
                    } else {
                        body = exchange.getFault().getBody();
                    }
                }

                if (body == null) {
                    LOG.warn("Can not write body since its null, closing session: " + exchange);
                    session.close();
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Writing body: " + body);
                    }
                    MinaHelper.writeBody(session, body, exchange);
                }
            }
        }

    }

}
