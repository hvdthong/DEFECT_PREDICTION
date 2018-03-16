package org.apache.camel.component.mina;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.DatagramAcceptor;
import org.apache.mina.transport.socket.nio.DatagramConnector;
import org.apache.mina.transport.socket.nio.DatagramConnectorConfig;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;
import org.apache.mina.transport.vmpipe.VmPipeAcceptor;
import org.apache.mina.transport.vmpipe.VmPipeAddress;
import org.apache.mina.transport.vmpipe.VmPipeConnector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @version $Revision: 525634 $
 */
public class MinaComponent extends DefaultComponent<MinaExchange> {
    public MinaComponent() {
    }

    public MinaComponent(CamelContext context) {
        super(context);
    }

    @Override
    protected Endpoint<MinaExchange> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        URI u = new URI(remaining);

        String protocol = u.getScheme();
        if (protocol.equals("tcp")) {
            return createSocketEndpoint(uri, u);
        }
        else if (protocol.equals("udp") || protocol.equals("mcast") || protocol.equals("multicast")) {
            return createDatagramEndpoint(uri, u);
        }
        else if (protocol.equals("vm")) {
            return createVmEndpoint(uri, u);
        }
        else {
            throw new IOException("Unrecognised MINA protocol: " + protocol + " for uri: " + uri);
        }
    }

    protected MinaEndpoint createVmEndpoint(String uri, URI connectUri) {
        IoAcceptor acceptor = new VmPipeAcceptor();
        SocketAddress address = new VmPipeAddress(connectUri.getPort());
        IoConnector connector = new VmPipeConnector();
        return new MinaEndpoint(uri, this, address, acceptor, connector, null);
    }

    protected MinaEndpoint createSocketEndpoint(String uri, URI connectUri) {
        IoAcceptor acceptor = new SocketAcceptor();
        SocketAddress address = new InetSocketAddress(connectUri.getHost(), connectUri.getPort());
        IoConnector connector = new SocketConnector();

        SocketConnectorConfig config = new SocketConnectorConfig();
        config.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

        return new MinaEndpoint(uri, this, address, acceptor, connector, config);
    }

    protected MinaEndpoint createDatagramEndpoint(String uri, URI connectUri) {
        IoAcceptor acceptor = new DatagramAcceptor();
        SocketAddress address = new InetSocketAddress(connectUri.getHost(), connectUri.getPort());
        IoConnector connector = new DatagramConnector();

        DatagramConnectorConfig config = new DatagramConnectorConfig();
        config.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

        return new MinaEndpoint(uri, this, address, acceptor, connector, config);
    }
}
