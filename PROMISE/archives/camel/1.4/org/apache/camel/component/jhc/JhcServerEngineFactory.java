package org.apache.camel.component.jhc;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.params.HttpParams;

public final class JhcServerEngineFactory {

    private static Map<Integer, JhcServerEngine> portMap = new HashMap<Integer, JhcServerEngine>();

    private JhcServerEngineFactory() {
    }

    public static synchronized JhcServerEngine getJhcServerEngine(final HttpParams params, final int port, final String protocol) {
        JhcServerEngine engine = portMap.get(port);
        if (engine == null) {
            engine = new JhcServerEngine(params, port, protocol.trim());
            portMap.put(port, engine);
        } else {
            if (!engine.getProtocol().equals(protocol.trim())) {
                throw new IllegalArgumentException("Jhc protocol error, the engine's protocol is "
                                                   + engine.getProtocol() + " you want is " + protocol);
            }
        }
        return engine;
    }

}
