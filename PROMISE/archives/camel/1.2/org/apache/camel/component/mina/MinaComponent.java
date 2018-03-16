package org.apache.camel.component.mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.IoConnector;
import org.apache.mina.common.support.BaseIoConnectorConfig;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.DatagramAcceptor;
import org.apache.mina.transport.socket.nio.DatagramConnector;
import org.apache.mina.transport.socket.nio.DatagramConnectorConfig;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;
import org.apache.mina.transport.vmpipe.VmPipeAcceptor;
import org.apache.mina.transport.vmpipe.VmPipeAddress;
import org.apache.mina.transport.vmpipe.VmPipeConnector;

/**
 * @version $Revision: 577559 $
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
            return createSocketEndpoint(uri, u, parameters);
        } else if (protocol.equals("udp") || protocol.equals("mcast") || protocol.equals("multicast")) {
            return createDatagramEndpoint(uri, u, parameters);
        } else if (protocol.equals("vm")) {
            return createVmEndpoint(uri, u);
        } else {
            throw new IOException("Unrecognised MINA protocol: " + protocol + " for uri: " + uri);
        }
    }

    protected MinaEndpoint createVmEndpoint(String uri, URI connectUri) {
        IoAcceptor acceptor = new VmPipeAcceptor();
        SocketAddress address = new VmPipeAddress(connectUri.getPort());
        IoConnector connector = new VmPipeConnector();
        return new MinaEndpoint(uri, this, address, acceptor, connector, null);
    }

    protected MinaEndpoint createSocketEndpoint(String uri, URI connectUri, Map parameters) {
        IoAcceptor acceptor = new SocketAcceptor();
        SocketAddress address = new InetSocketAddress(connectUri.getHost(), connectUri.getPort());
        IoConnector connector = new SocketConnector();

        SocketConnectorConfig config = new SocketConnectorConfig();
        configureCodecFactory(config, parameters);
        return new MinaEndpoint(uri, this, address, acceptor, connector, config);
    }
    
    protected MinaEndpoint createDatagramEndpoint(String uri, URI connectUri, Map parameters) {
        IoAcceptor acceptor = new DatagramAcceptor();
        SocketAddress address = new InetSocketAddress(connectUri.getHost(), connectUri.getPort());
        IoConnector connector = new DatagramConnector();

        DatagramConnectorConfig config = new DatagramConnectorConfig();
        configureCodecFactory(config, parameters);

        return new MinaEndpoint(uri, this, address, acceptor, connector, config);
    }

    protected void configureCodecFactory(BaseIoConnectorConfig config, Map parameters){
        boolean textline = false;
        if (parameters != null) {
            if (parameters.containsKey("codec")) {
                String value = (String) parameters.get("codec");
                if (value.equals("textline")) {
                    textline = true;
                }
            } else {
                textline = false;
            }
        }

        if (textline) {
            config.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory()));
        } else {
            config.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        }
    }

}
