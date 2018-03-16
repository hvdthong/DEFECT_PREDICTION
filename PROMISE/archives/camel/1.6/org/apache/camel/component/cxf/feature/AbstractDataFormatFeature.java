package org.apache.camel.component.cxf.feature;

import java.util.List;
import java.util.logging.Logger;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.phase.PhaseInterceptor;

/**
 * The abstract class for the data format feature
 */
public abstract class AbstractDataFormatFeature extends AbstractFeature {


    protected abstract Logger getLogger();

    protected void resetServiceInvokerInterceptor(Server server) {
        List<Interceptor> serviceInterceptor = server.getEndpoint().getService().getInInterceptors();
        removeInterceptorWhichIsInThePhases(serviceInterceptor, new String[]{Phase.INVOKE});
        serviceInterceptor.add(new MessageInvokerInterceptor());
    }

    protected void removeInterceptorWhichIsInThePhases(List<Interceptor> interceptors, String[] phaseNames) {
        for (Interceptor i : interceptors) {
            if (i instanceof PhaseInterceptor) {
                PhaseInterceptor p = (PhaseInterceptor)i;
                for (String phaseName : phaseNames) {
                    if (p.getPhase().equals(phaseName)) {
                        getLogger().info("removing the interceptor " + p);
                        interceptors.remove(p);
                        break;
                    }
                }
            }
        }
    }

    protected void removeInterceptorWhichIsOutThePhases(List<Interceptor> interceptors, String[] phaseNames) {
        for (Interceptor i : interceptors) {
            boolean outside = false;
            if (i instanceof PhaseInterceptor) {
                PhaseInterceptor p = (PhaseInterceptor)i;
                for (String phaseName : phaseNames) {
                    if (p.getPhase().equals(phaseName)) {
                        outside = true;
                        break;
                    }
                }
                if (!outside) {
                    getLogger().info("removing the interceptor " + p);
                    interceptors.remove(p);
                }
            }
        }
    }

}
