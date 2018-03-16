package org.apache.camel.bam.model;

import org.apache.camel.bam.processor.ProcessContext;
import org.apache.camel.bam.rules.ActivityRules;
import org.apache.camel.util.ObjectHelper;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * The default state for a specific activity within a process
 *
 * @version $Revision: $
 */
@Entity
public class ActivityState extends TemporalEntity {
    private ProcessInstance processInstance;
    private Integer receivedMessageCount = 0;
    private ActivityDefinition activityDefinition;
    private Date timeExpected;
    private Date timeOverdue;
    private Integer escalationLevel = 0;

    @Override
    @Id
    @GeneratedValue
    public Long getId() {
        return super.getId();
    }

    @Override
    public String toString() {
        return "ActivityState[" + getId() + " " + getActivityDefinition() + "]";
    }

    public synchronized void processExchange(ActivityRules activityRules, ProcessContext context) throws Exception {
        int messageCount = 0;
        Integer count = getReceivedMessageCount();
        if (count != null) {
            messageCount = count.intValue();
        }
        setReceivedMessageCount(++messageCount);

        if (messageCount == 1) {
            onFirstMessage(context);
        }
        int expectedMessages = activityRules.getExpectedMessages();
        if (messageCount == expectedMessages) {
            onExpectedMessage(context);
        }
        else if (messageCount > expectedMessages) {
            onExcessMessage(context);
        }
    }

    /**
     * Returns true if this state is for the given activity
     */
    public boolean isActivity(ActivityRules activityRules) {
        return ObjectHelper.equals(getActivityDefinition(), activityRules.getActivityDefinition());
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
        processInstance.getActivityStates().add(this);
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    public ActivityDefinition getActivityDefinition() {
        return activityDefinition;
    }

    public void setActivityDefinition(ActivityDefinition activityDefinition) {
        this.activityDefinition = activityDefinition;
    }

    public Integer getEscalationLevel() {
        return escalationLevel;
    }

    public void setEscalationLevel(Integer escalationLevel) {
        this.escalationLevel = escalationLevel;
    }

    public Integer getReceivedMessageCount() {
        return receivedMessageCount;
    }

    public void setReceivedMessageCount(Integer receivedMessageCount) {
        this.receivedMessageCount = receivedMessageCount;
    }

    public Date getTimeExpected() {
        return timeExpected;
    }

    public void setTimeExpected(Date timeExpected) {
        this.timeExpected = timeExpected;
    }

    public Date getTimeOverdue() {
        return timeOverdue;
    }

    public void setTimeOverdue(Date timeOverdue) {
        this.timeOverdue = timeOverdue;
    }

    public void setTimeCompleted(Date timeCompleted) {
        super.setTimeCompleted(timeCompleted);
        if (timeCompleted != null) {
            setEscalationLevel(-1);
        }
    }


    /**
     * Called when the first message is reached
     */
    protected void onFirstMessage(ProcessContext context) {
        if (!isStarted()) {
            setTimeStarted(currentTime());
            context.onStarted(this);
        }
    }

    /**
     * Called when the expected number of messages are is reached
     */
    protected void onExpectedMessage(ProcessContext context) {
        if (!isCompleted()) {
            setTimeCompleted(currentTime());
            context.onCompleted(this);
        }
    }

    /**
     * Called when an excess message (after the expected number of messages)
     * are received
     */
    protected void onExcessMessage(ProcessContext context) {
    }

    protected Date currentTime() {
        return new Date();
    }
}
