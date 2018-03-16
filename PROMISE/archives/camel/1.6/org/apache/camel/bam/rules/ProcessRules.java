package org.apache.camel.bam.rules;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.bam.model.ActivityState;
import org.apache.camel.bam.model.ProcessDefinition;
import org.apache.camel.bam.model.ProcessInstance;
import org.apache.camel.impl.ServiceSupport;
import org.apache.camel.util.ServiceHelper;

/**
 * @version $Revision: 630574 $
 */
public class ProcessRules extends ServiceSupport {
    private ProcessDefinition processDefinition;
    private List<ActivityRules> activities = new ArrayList<ActivityRules>();

    public void processExpired(ActivityState activityState) throws Exception {
        for (ActivityRules activityRules : activities) {
            activityRules.processExpired(activityState);
        }
    }

    public void processExchange(Exchange exchange, ProcessInstance process) {
        for (ActivityRules activityRules : activities) {
            activityRules.processExchange(exchange, process);
        }
    }

    public List<ActivityRules> getActivities() {
        return activities;
    }

    public ProcessDefinition getProcessDefinition() {
        return processDefinition;
    }

    public void setProcessDefinition(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
    }

    protected void doStart() throws Exception {
        ServiceHelper.startServices(activities);
    }

    protected void doStop() throws Exception {
        ServiceHelper.stopServices(activities);
    }
}



