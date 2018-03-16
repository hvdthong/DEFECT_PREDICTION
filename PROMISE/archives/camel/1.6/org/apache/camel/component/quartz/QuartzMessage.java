package org.apache.camel.component.quartz;

import java.util.Map;

import org.apache.camel.impl.DefaultMessage;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;

/**
 * @version $Revision: 666099 $
 */
public class QuartzMessage extends DefaultMessage {
    private final JobExecutionContext jobExecutionContext;

    public QuartzMessage(QuartzExchange exchange, JobExecutionContext jobExecutionContext) {
        this.jobExecutionContext = jobExecutionContext;
        setExchange(exchange);
        if (jobExecutionContext != null) {
            setBody(jobExecutionContext.getJobDetail());
        }
    }

    public JobExecutionContext getJobExecutionContext() {
        return jobExecutionContext;
    }

    @Override
    protected void populateInitialHeaders(Map<String, Object> map) {
        super.populateInitialHeaders(map);
        if (jobExecutionContext != null) {
            map.put("calendar", jobExecutionContext.getCalendar());
            map.put("fireTime", jobExecutionContext.getFireTime());
            map.put("jobDetail", jobExecutionContext.getJobDetail());
            map.put("jobInstance", jobExecutionContext.getJobInstance());
            map.put("jobRunTime", jobExecutionContext.getJobRunTime());
            map.put("mergedJobDataMap", jobExecutionContext.getMergedJobDataMap());
            map.put("nextFireTime", jobExecutionContext.getNextFireTime());
            map.put("previousFireTime", jobExecutionContext.getPreviousFireTime());
            map.put("refireCount", jobExecutionContext.getRefireCount());
            map.put("result", jobExecutionContext.getResult());
            map.put("scheduledFireTime", jobExecutionContext.getScheduledFireTime());
            map.put("scheduler", jobExecutionContext.getScheduler());
            Trigger trigger = jobExecutionContext.getTrigger();
            map.put("trigger", trigger);
            map.put("triggerName", trigger.getName());
            map.put("triggerGroup", trigger.getGroup());
        }
    }
}
