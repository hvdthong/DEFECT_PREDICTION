package org.apache.camel.component.cxf.feature;

import java.util.logging.Logger;

import org.apache.camel.component.cxf.interceptors.DOMInInterceptor;
import org.apache.camel.component.cxf.interceptors.DOMOutInterceptor;
import org.apache.camel.component.cxf.interceptors.FaultOutInterceptor;
import org.apache.camel.component.cxf.interceptors.PayloadContentRedirectInterceptor;
import org.apache.cxf.Bus;
import org.apache.cxf.binding.Binding;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.phase.Phase;

/**
 * This feature just setting up the CXF endpoint interceptor for handling the
 * Message in PAYLOAD data format
 */
public class PayLoadDataFormatFeature extends AbstractDataFormatFeature {
    private static final Logger LOG = LogUtils.getL7dLogger(PayLoadDataFormatFeature.class);
    private static final String[] REMOVING_IN_PHASES = {Phase.UNMARSHAL, Phase.PRE_LOGICAL, Phase.PRE_LOGICAL_ENDING, Phase.POST_LOGICAL, Phase.POST_LOGICAL_ENDING };

    private static final String[] REMOVING_OUT_PHASES = {Phase.MARSHAL, Phase.MARSHAL_ENDING, Phase.PRE_LOGICAL, Phase.PRE_LOGICAL_ENDING, Phase.POST_LOGICAL, Phase.POST_LOGICAL_ENDING };

    @Override
    public void initialize(Client client, Bus bus) {
        removeInterceptorWhichIsInThePhases(client.getInInterceptors(), REMOVING_IN_PHASES);
        removeInterceptorWhichIsInThePhases(client.getEndpoint().getService().getInInterceptors(), REMOVING_IN_PHASES);
        removeInterceptorWhichIsInThePhases(client.getEndpoint().getInInterceptors(), REMOVING_IN_PHASES);
        removeInterceptorWhichIsInThePhases(client.getEndpoint().getBinding().getInInterceptors(), REMOVING_IN_PHASES);

        removeInterceptorWhichIsInThePhases(client.getOutInterceptors(), REMOVING_OUT_PHASES);
        removeInterceptorWhichIsInThePhases(client.getEndpoint().getService().getOutInterceptors(), REMOVING_OUT_PHASES);
        removeInterceptorWhichIsInThePhases(client.getEndpoint().getOutInterceptors(), REMOVING_OUT_PHASES);
        removeInterceptorWhichIsInThePhases(client.getEndpoint().getBinding().getOutInterceptors(), REMOVING_OUT_PHASES);

        addDataHandlingInterceptors(client.getEndpoint().getBinding());
        client.getEndpoint().getBinding().getOutFaultInterceptors().add(new FaultOutInterceptor());
    }

    @Override
    public void initialize(Server server, Bus bus) {

        removeInterceptorWhichIsInThePhases(server.getEndpoint().getService().getInInterceptors(), REMOVING_IN_PHASES);
        removeInterceptorWhichIsInThePhases(server.getEndpoint().getInInterceptors(), REMOVING_IN_PHASES);
        removeInterceptorWhichIsInThePhases(server.getEndpoint().getBinding().getInInterceptors(), REMOVING_IN_PHASES);

        removeInterceptorWhichIsInThePhases(server.getEndpoint().getService().getOutInterceptors(), REMOVING_OUT_PHASES);
        removeInterceptorWhichIsInThePhases(server.getEndpoint().getOutInterceptors(), REMOVING_OUT_PHASES);
        removeInterceptorWhichIsInThePhases(server.getEndpoint().getBinding().getOutInterceptors(), REMOVING_OUT_PHASES);

        resetServiceInvokerInterceptor(server);

        addDataHandlingInterceptors(server.getEndpoint().getBinding());
        server.getEndpoint().getBinding().getOutFaultInterceptors().add(new FaultOutInterceptor());
    }

    private void addDataHandlingInterceptors(Binding binding) {
        binding.getInInterceptors().add(new DOMInInterceptor());
        binding.getOutInterceptors().add(new DOMOutInterceptor());
        binding.getOutInterceptors().add(new PayloadContentRedirectInterceptor());
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

}
