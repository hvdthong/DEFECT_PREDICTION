package org.apache.camel.component.quartz;

import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;
import org.quartz.JobExecutionContext;

/**
 * @version $Revision: 630591 $
 */
public class QuartzExchange extends DefaultExchange {
    public QuartzExchange(CamelContext context, ExchangePattern pattern, JobExecutionContext jobExecutionContext) {
        super(context, pattern);
        setIn(new QuartzMessage(this, jobExecutionContext));
    }

    @Override
    public QuartzMessage getIn() {
        return (QuartzMessage) super.getIn();
    }

    public JobExecutionContext getJobExecutionContext() {
        return getIn().getJobExecutionContext();
    }
}
