package org.apache.camel.component.cxf.interceptors;

import java.util.logging.Logger;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.phase.Phase;

public class PayloadInInterceptor extends AbstractInvokerInterceptor {

    private static final Logger LOG = LogUtils.getL7dLogger(PayloadInInterceptor.class); 

    public PayloadInInterceptor() {
        super(Phase.READ);       
    }
    
    @Override
    protected Logger getLogger() {
        return LOG;
    }
}
