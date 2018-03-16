package org.apache.camel.component.cxf.interceptors;

import java.util.logging.Logger;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.phase.Phase;

public class RawMessageInInterceptor extends AbstractInvokerInterceptor {

    private static final Logger LOG = LogUtils.getL7dLogger(RawMessageInInterceptor.class); 

    public RawMessageInInterceptor() {
        super(Phase.RECEIVE);
        
    }
    
    @Override
    protected Logger getLogger() {
        return LOG;
    }
}
