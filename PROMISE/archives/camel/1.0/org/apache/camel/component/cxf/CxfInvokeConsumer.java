package org.apache.camel.component.cxf;

import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.cxf.endpoint.ServerImpl;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.message.Message;

/**
 * A consumer of exchanges for a service in CXF
 *
 * @version $Revision: 534145 $
 */
public class CxfInvokeConsumer extends DefaultConsumer<CxfExchange> {
    protected CxfInvokeEndpoint cxfEndpoint;
    private ServerImpl server;

    public CxfInvokeConsumer(CxfInvokeEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.cxfEndpoint = endpoint;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        if (server != null) {
            ServerFactoryBean svrBean = new ServerFactoryBean();
            svrBean.setAddress(getEndpoint().getEndpointUri());
            svrBean.setServiceClass(Class.forName(cxfEndpoint.getProperty(CxfConstants.IMPL)));
            svrBean.setBus(cxfEndpoint.getBus());

            server = (ServerImpl) svrBean.create();
            server.start();
        }
    }

    @Override
    protected void doStop() throws Exception {
        if (server != null) {
            server.stop();
            server = null;
        }
        super.doStop();
    }

    protected void incomingCxfMessage(Message message) {
        try {
			CxfExchange exchange = cxfEndpoint.createExchange(message);
			getProcessor().process(exchange);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
