package org.apache.camel.bam;

import org.apache.camel.bam.model.ActivityState;
import org.apache.camel.bam.model.ProcessInstance;
import org.apache.camel.bam.rules.ActivityRules;
import org.apache.camel.bam.rules.TemporalRule;
import org.apache.camel.util.ObjectHelper;

import java.util.Date;

/**
 * @version $Revision: $
 */
public abstract class TimeExpression {
    private ActivityRules activityRules;
    private ActivityBuilder builder;
    private ActivityLifecycle lifecycle;

    public TimeExpression(ActivityBuilder builder, ActivityLifecycle lifecycle) {
        this.lifecycle = lifecycle;
        this.builder = builder;
        this.activityRules = builder.getActivityRules();
    }

    public boolean isActivityLifecycle(ActivityRules activityRules, ActivityLifecycle lifecycle) {
        return ObjectHelper.equals(activityRules, this.activityRules) && ObjectHelper.equals(lifecycle, this.lifecycle);
    }

    /**
     * Creates a new temporal rule on this expression and the other expression
     */
    public TemporalRule after(TimeExpression expression) {
        TemporalRule rule = new TemporalRule(expression, this);
        rule.getSecond().getActivityRules().addRule(rule);
        return rule;
    }

    public Date evaluate(ProcessInstance processInstance) {
        ActivityState state = processInstance.getActivityState(activityRules);
        if (state != null) {
            return evaluate(processInstance, state);
        }
        return null;
    }

    public abstract Date evaluate(ProcessInstance instance, ActivityState state);


    public ActivityBuilder getBuilder() {
        return builder;
    }

    public ActivityRules getActivityRules() {
        return activityRules;
    }

    public ActivityLifecycle getLifecycle() {
        return lifecycle;
    }

    public ActivityState getActivityState(ProcessInstance instance) {
        return instance.getActivityState(activityRules);
    }

    public ActivityState getOrCreateActivityState(ProcessInstance instance) {
        return instance.getOrCreateActivityState(activityRules);
    }
}
