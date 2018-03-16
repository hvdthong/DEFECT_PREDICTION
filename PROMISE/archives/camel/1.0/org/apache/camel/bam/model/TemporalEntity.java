package org.apache.camel.bam.model;

import javax.persistence.Transient;
import java.util.Date;

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

    public Date getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(Date timeStarted) {
        this.timeStarted = timeStarted;
    }

    public Date getTimeCompleted() {
        return timeCompleted;
    }

    public void setTimeCompleted(Date timeCompleted) {
        this.timeCompleted = timeCompleted;
    }
}
