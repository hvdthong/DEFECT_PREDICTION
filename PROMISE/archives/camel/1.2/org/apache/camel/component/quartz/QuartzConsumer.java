package org.apache.camel.component.quartz;

import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;

/**
 * @version $Revision: 1.1 $
 */
public class QuartzConsumer extends DefaultConsumer<QuartzExchange> {
    public QuartzConsumer(QuartzEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }

    @Override
    public QuartzEndpoint getEndpoint() {
        return (QuartzEndpoint) super.getEndpoint();
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        getEndpoint().consumerStarted(this);
    }

    @Override
    protected void doStop() throws Exception {
        getEndpoint().consumerStopped(this);
        super.doStop();
    }
}
