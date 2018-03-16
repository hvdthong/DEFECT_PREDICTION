package org.apache.camel.component.quartz;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.util.IntrospectionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import java.net.URI;
import java.util.Map;

/**
 * 
 * @version $Revision:520964 $
 */
public class QuartzComponent extends DefaultComponent<QuartzExchange> {
    private static final transient Log LOG = LogFactory.getLog(QuartzComponent.class);
    private SchedulerFactory factory;
    private Scheduler scheduler;
    private Map<Trigger, JobDetail> triggers;

    public QuartzComponent() {
    }

    public QuartzComponent(CamelContext context) {
        super(context);
    }

    @Override
    protected QuartzEndpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        QuartzEndpoint answer = new QuartzEndpoint(uri, this, getScheduler());

        URI u = new URI(uri);
        String name;
        String group = "Camel";
        String path = u.getPath();
        CronTrigger cronTrigger = null;
        if (path != null && path.length() > 1) {
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            int idx = path.indexOf('/');
            if (idx > 0) {
                cronTrigger = new CronTrigger();
                name = path.substring(0, idx);
                String cronExpression = path.substring(idx + 1);
                cronExpression = cronExpression.replace('/', ' ');
                cronExpression = cronExpression.replace('$', '?');
                LOG.debug("Creating cron trigger: " + cronExpression);
                cronTrigger.setCronExpression(cronExpression);
                answer.setTrigger(cronTrigger);
            } else {
                name = path;
            }
            group = u.getHost();
        } else {
            name = u.getHost();
        }
        /*
         * String[] names = ObjectHelper.splitOnCharacter(remaining, "/", 2); if
         * (names[1] != null) { group = names[0]; name = names[1]; } else { name =
         * names[0]; }
         */
        Trigger trigger = cronTrigger;
        if (trigger == null) {
            trigger = answer.getTrigger();
        }
        trigger.setName(name);
        trigger.setGroup(group);

        Map triggerParameters = IntrospectionSupport.extractProperties(parameters, "trigger.");
        Map jobParameters = IntrospectionSupport.extractProperties(parameters, "job.");

        setProperties(trigger, triggerParameters);
        setProperties(answer.getJobDetail(), jobParameters);

        return answer;
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        getScheduler().start();
    }

    @Override
    protected void doStop() throws Exception {
        if (scheduler != null) {
            scheduler.shutdown();
        }
        super.doStop();
    }

    public SchedulerFactory getFactory() {
        if (factory == null) {
            factory = createSchedulerFactory();
        }
        return factory;
    }

    public void setFactory(SchedulerFactory factory) {
        this.factory = factory;
    }

    public Scheduler getScheduler() throws SchedulerException {
        if (scheduler == null) {
            scheduler = createScheduler();
        }
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Map getTriggers() {
        return triggers;
    }

    public void setTriggers(Map triggers) {
        this.triggers = triggers;
    }

    protected SchedulerFactory createSchedulerFactory() {
        return new StdSchedulerFactory();
    }

    protected Scheduler createScheduler() throws SchedulerException {
        return getFactory().getScheduler();
    }
}
