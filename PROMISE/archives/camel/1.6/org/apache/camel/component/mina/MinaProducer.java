package org.apache.camel.component.mina;

import java.net.SocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelExchangeException;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeTimedOutException;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.util.ExchangeHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.IoConnector;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.transport.socket.nio.SocketConnector;

/**
 * A {@link Producer} implementation for MINA
 *
 * @version $Revision: 722893 $
 */
public class MinaProducer extends DefaultProducer {
    private static final transient Log LOG = LogFactory.getLog(MinaProducer.class);
    private IoSession session;
    private MinaEndpoint endpoint;
    private CountDownLatch latch;
    private boolean lazySessionCreation;
    private long timeout;
    private IoConnector connector;
    private boolean sync;

    public MinaProducer(MinaEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
        this.lazySessionCreation = endpoint.isLazySessionCreation();
        this.timeout = endpoint.getTimeout();
        this.sync = endpoint.isSync();
    }

    public void process(Exchange exchange) throws Exception {
        if (session == null && !lazySessionCreation) {
            throw new IllegalStateException("Not started yet!");
        }
        if (session == null || !session.isConnected()) {
            openConnection();
        }

        if (endpoint.getCharsetName() != null) {
            exchange.setProperty(Exchange.CHARSET_NAME, endpoint.getCharsetName());
        }

        Object body = MinaPayloadHelper.getIn(endpoint, exchange);
        if (body == null) {
            LOG.warn("No payload to send for exchange: " + exchange);
        }

        if (sync) {
            latch = new CountDownLatch(1);
            ResponseHandler handler = (ResponseHandler) session.getHandler();
            handler.reset();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Writing body: " + body);
        }
        MinaHelper.writeBody(session, body, exchange);

        if (sync) {
            LOG.debug("Waiting for response");
            latch.await(timeout, TimeUnit.MILLISECONDS);
            if (latch.getCount() == 1) {
                throw new ExchangeTimedOutException(exchange, timeout);
            }

            ResponseHandler handler = (ResponseHandler) session.getHandler();
            if (handler.getCause() != null) {
                throw new CamelExchangeException("Response Handler had an exception", exchange, handler.getCause());
            } else if (!handler.isMessageRecieved()) {
                throw new CamelExchangeException("No response received from remote server: " + endpoint.getEndpointUri(), exchange);
            } else {
                if (ExchangeHelper.isOutCapable(exchange)) {
                    MinaPayloadHelper.setOut(exchange, handler.getMessage());
                } else {
                    MinaPayloadHelper.setIn(exchange, handler.getMessage());
                }
            }
        }
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        if (!lazySessionCreation) {
            openConnection();
        }
    }

    @Override
    protected void doStop() throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Stopping connector: " + connector + " at address: " + endpoint.getAddress());
        }

        if (connector instanceof SocketConnector) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Setting SocketConnector WorkerTimeout=0 to force MINA stopping its resources faster");
            }
            ((SocketConnector) connector).setWorkerTimeout(0);
        }

        if (session != null) {
            session.close();
        }

        super.doStop();
    }

    private void openConnection() {
        SocketAddress address = endpoint.getAddress();
        connector = endpoint.getConnector();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating connector to address: " + address + " using connector: " + connector + " timeout: " + timeout + " millis.");
        }
        IoHandler ioHandler = new ResponseHandler(endpoint);
        ConnectFuture future = connector.connect(address, ioHandler, endpoint.getConnectorConfig());
        future.join();
        session = future.getSession();
    }

    /**
     * Handles response from session writes
     *
     * @author <a href="mailto:karajdaar@gmail.com">nsandhu</a>
     */
    private final class ResponseHandler extends IoHandlerAdapter {
        private MinaEndpoint endpoint;
        private Object message;
        private Throwable cause;
        private boolean messageRecieved;

        private ResponseHandler(MinaEndpoint endpoint) {
            this.endpoint = endpoint;
        }

        public void reset() {
            this.message = null;
            this.cause = null;
            this.messageRecieved = false;
        }

        @Override
        public void messageReceived(IoSession ioSession, Object message) throws Exception {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Message received: " + message);
            }
            this.message = message;
            messageRecieved = true;
            cause = null;
            countDown();
        }

        protected void countDown() {
            CountDownLatch downLatch = latch;
            if (downLatch != null) {
                downLatch.countDown();
            }
        }

        @Override
        public void sessionClosed(IoSession session) throws Exception {
            if (sync && message == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Session closed but no message received from address: " + this.endpoint.getAddress());
                }
                countDown();
            }
        }

        @Override
        public void exceptionCaught(IoSession ioSession, Throwable cause) {
            LOG.error("Exception on receiving message from address: " + this.endpoint.getAddress()
                    + " using connector: " + this.endpoint.getConnector(), cause);
            this.message = null;
            this.messageRecieved = false;
            this.cause = cause;
            if (ioSession != null) {
                ioSession.close();
            }
        }

        public Throwable getCause() {
            return this.cause;
        }

        public Object getMessage() {
            return this.message;
        }

        public boolean isMessageRecieved() {
            return messageRecieved;
        }
    }

}
