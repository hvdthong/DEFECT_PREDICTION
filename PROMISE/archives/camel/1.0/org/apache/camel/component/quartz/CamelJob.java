package org.apache.camel.component.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @version $Revision: 1.1 $
 */
public class CamelJob implements Job {
    public void execute(JobExecutionContext context) throws JobExecutionException {
        QuartzEndpoint component = (QuartzEndpoint) context.getJobDetail().getJobDataMap().get(QuartzEndpoint.ENDPOINT_KEY);
        if (component == null) {
            throw new JobExecutionException("No quartz endpoint available for key: " + QuartzEndpoint.ENDPOINT_KEY + ". Bad job data map");
        }
        component.onJobExecute(context);
    }
}
