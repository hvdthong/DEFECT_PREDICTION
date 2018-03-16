package org.apache.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Intercept;
import org.apache.camel.Processor;
import org.apache.camel.util.ServiceHelper;

/**
 * An interceptor which provides the processing logic as a pluggable processor
 * which allows the {@link #proceed(Exchange)} method to be called at some point
 *
 * @version $Revision: 662664 $
 */
public class Interceptor extends DelegateProcessor implements Intercept {
    private Processor interceptorLogic;

    public Interceptor() {
    }

    public Interceptor(Processor interceptorLogic) {
        this.interceptorLogic = interceptorLogic;
    }

    public void process(Exchange exchange) throws Exception {
        interceptorLogic.process(exchange);
    }

    public Processor getInterceptorLogic() {
        return interceptorLogic;
    }

    public void setInterceptorLogic(Processor interceptorLogic) {
        this.interceptorLogic = interceptorLogic;
    }

    @Override
    protected void doStart() throws Exception {
        ServiceHelper.startService(interceptorLogic);
        super.doStart();
    }

    @Override
    protected void doStop() throws Exception {
        ServiceHelper.stopService(interceptorLogic);
        super.doStop();
    }
}
