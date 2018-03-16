package org.apache.camel.bam.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import javax.persistence.*;

import org.apache.camel.bam.rules.ActivityRules;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Represents a single business process
 *
 * @version $Revision: $
 */
@Entity
public class ProcessInstance  {
    private static final transient Log LOG = LogFactory.getLog(ProcessInstance.class);
    private ProcessDefinition processDefinition;
    private Collection<ActivityState> activityStates = new HashSet<ActivityState>();
    private String correlationKey;
    private Date timeStarted;
    private Date timeCompleted;

    public ProcessInstance() {
        setTimeStarted(new Date());
    }

    public String toString() {
        return "ProcessInstance[" + getCorrelationKey() + "]";
    }

    @Id
    public String getCorrelationKey() {
        return correlationKey;
    }

    public void setCorrelationKey(String correlationKey) {
        this.correlationKey = correlationKey;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST })
    public ProcessDefinition getProcessDefinition() {
        return processDefinition;
    }

    public void setProcessDefinition(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
    }

    @OneToMany(mappedBy = "processInstance", fetch = FetchType.LAZY, cascade = {CascadeType.ALL })
    public Collection<ActivityState> getActivityStates() {
        return activityStates;
    }

    public void setActivityStates(Collection<ActivityState> activityStates) {
        this.activityStates = activityStates;
    }


    @Transient
    public boolean isStarted() {
        return timeStarted != null;
    }

    @Transient
    public boolean isCompleted() {
        return timeCompleted != null;
    }

    @Temporal(TemporalType.TIME)
    public Date getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(Date timeStarted) {
        this.timeStarted = timeStarted;
    }

    @Temporal(TemporalType.TIME)
    public Date getTimeCompleted() {
        return timeCompleted;
    }

    public void setTimeCompleted(Date timeCompleted) {
        this.timeCompleted = timeCompleted;

    /**
     * Returns the activity state for the given activity
     *
     * @param activityRules the activity to find the state for
     * @return the activity state or null if no state could be found for the
     *         given activity
     */
    public ActivityState getActivityState(ActivityRules activityRules) {
        for (ActivityState activityState : getActivityStates()) {
            if (activityState.isActivity(activityRules)) {
                return activityState;
            }
        }
        return null;
    }

    public ActivityState getOrCreateActivityState(ActivityRules activityRules) {
        ActivityState state = getActivityState(activityRules);

        if (state == null) {
            state = createActivityState();
            state.setProcessInstance(this);
            state.setActivityDefinition(activityRules.getActivityDefinition());
        }

        return state;
    }

    protected ActivityState createActivityState() {
        return new ActivityState();
    }
}
