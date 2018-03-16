package org.apache.camel.component.mina;

import java.net.SocketAddress;

import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

/**
 * A
 * 
 * @{link Consumer} for MINA
 * @version $Revision: 563665 $
 */
public class MinaConsumer extends DefaultConsumer<MinaExchange> {
    private static final transient Log LOG = LogFactory.getLog(MinaConsumer.class);

    private final MinaEndpoint endpoint;
    private final SocketAddress address;
    private final IoAcceptor acceptor;

    public MinaConsumer(final MinaEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        address = endpoint.getAddress();
        acceptor = endpoint.getAcceptor();
    }

    @Override
    protected void doStart() throws Exception {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Binding to server address: " + address + " using acceptor: " + acceptor);
        }

        IoHandler handler = new IoHandlerAdapter() {
            @Override
            public void messageReceived(IoSession session, Object object) throws Exception {
                getProcessor().process(endpoint.createExchange(session, object));
            }
        };

        acceptor.bind(address, handler, endpoint.getConfig());
    }

    @Override
    protected void doStop() throws Exception {
        acceptor.unbind(address);
        super.doStop();
    }
}
