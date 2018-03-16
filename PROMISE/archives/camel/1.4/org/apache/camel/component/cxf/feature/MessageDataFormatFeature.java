package org.apache.camel.component.cxf.feature;

import java.util.logging.Logger;

import org.apache.camel.component.cxf.interceptors.FaultOutInterceptor;
import org.apache.camel.component.cxf.interceptors.RawMessageContentRedirectInterceptor;
import org.apache.cxf.Bus;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.phase.Phase;

/**
 * This feature just setting up the CXF endpoint interceptor for handling the
 * Message in Message data format
 */
public class MessageDataFormatFeature extends AbstractDataFormatFeature {
    private static final Logger LOG = LogUtils.getL7dLogger(MessageDataFormatFeature.class);
    private static final String[] REMAINING_IN_PHASES = {Phase.RECEIVE , Phase.POST_INVOKE};
    private static final String[] REMAINING_OUT_PHASES = {Phase.PREPARE_SEND, Phase.WRITE, Phase.SEND, Phase.PREPARE_SEND_ENDING};


    @Override
    public void initialize(Client client, Bus bus) {

        removeInterceptorWhichIsOutThePhases(client.getInInterceptors(), REMAINING_IN_PHASES);
        removeInterceptorWhichIsOutThePhases(client.getEndpoint().getInInterceptors(), REMAINING_IN_PHASES);
        client.getEndpoint().getBinding().getInInterceptors().clear();

        removeInterceptorWhichIsOutThePhases(client.getOutInterceptors(), REMAINING_OUT_PHASES);
        removeInterceptorWhichIsOutThePhases(client.getEndpoint().getOutInterceptors(), REMAINING_OUT_PHASES);
        client.getEndpoint().getBinding().getOutInterceptors().clear();
        client.getEndpoint().getOutInterceptors().add(new RawMessageContentRedirectInterceptor());

        client.getEndpoint().getBinding().getOutFaultInterceptors().add(new FaultOutInterceptor());
    }

    @Override
    public void initialize(Server server, Bus bus) {
        removeInterceptorWhichIsOutThePhases(server.getEndpoint().getService().getInInterceptors(), REMAINING_IN_PHASES);
        removeInterceptorWhichIsOutThePhases(server.getEndpoint().getInInterceptors(), REMAINING_IN_PHASES);
        server.getEndpoint().getBinding().getInInterceptors().clear();

        removeInterceptorWhichIsOutThePhases(server.getEndpoint().getService().getOutInterceptors(), REMAINING_OUT_PHASES);
        removeInterceptorWhichIsOutThePhases(server.getEndpoint().getOutInterceptors(), REMAINING_OUT_PHASES);
        server.getEndpoint().getBinding().getOutInterceptors().clear();

        server.getEndpoint().getBinding().getOutFaultInterceptors().add(new FaultOutInterceptor());


        resetServiceInvokerInterceptor(server);
        server.getEndpoint().getOutInterceptors().add(new RawMessageContentRedirectInterceptor());
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

}
