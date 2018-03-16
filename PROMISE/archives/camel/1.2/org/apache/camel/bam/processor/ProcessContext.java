package org.apache.camel.bam.processor;

import org.apache.camel.Exchange;
import org.apache.camel.bam.model.ActivityState;
import org.apache.camel.bam.model.ProcessInstance;
import org.apache.camel.bam.rules.ActivityRules;
import org.apache.camel.bam.rules.ProcessRules;

/**
 * @version $Revision: 1.1 $
 */
public class ProcessContext {
    private Exchange exchange;
    private ProcessRules processRules;
    private ActivityRules activityRules;
    private ProcessInstance processInstance;
    private ActivityState activityState;

    public ProcessContext(Exchange exchange, ActivityRules activityRules, ActivityState activityState) {
        this.exchange = exchange;
        this.activityRules = activityRules;
        this.activityState = activityState;
        this.processRules = activityRules.getProcessRules();
        this.processInstance = activityState.getProcessInstance();
    }

    public ActivityRules getActivity() {
        return activityRules;
    }

    public void setActivity(ActivityRules activityRules) {
        this.activityRules = activityRules;
    }

    public ActivityState getActivityState() {
        return activityState;
    }

    public void setActivityState(ActivityState activityState) {
        this.activityState = activityState;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public ProcessRules getProcessDefinition() {
        return processRules;
    }

    public void setProcessDefinition(ProcessRules processRules) {
        this.processRules = processRules;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public ActivityState getActivityState(ActivityRules activityRules) {
        return getProcessInstance().getActivityState(activityRules);
    }

    /**
     * Called when the activity is started which may end up creating some timers
     * for dependent actions
     */
    public void onStarted(ActivityState activityState) {
        /** TODO */
    }

    /**
     * Called when the activity is completed which may end up creating some timers
     * for dependent actions
     */
    public void onCompleted(ActivityState activityState) {
        /** TODO */
    }
}
