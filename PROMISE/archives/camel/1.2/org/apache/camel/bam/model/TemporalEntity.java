package org.apache.camel.bam.model;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * @version $Revision: $
 */
public abstract class TemporalEntity extends EntitySupport {
    private Date timeStarted;
    private Date timeCompleted;

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
    }
}
