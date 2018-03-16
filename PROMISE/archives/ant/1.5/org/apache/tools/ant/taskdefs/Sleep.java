package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

/**
 * Sleep, or pause, for a period of time.
 *
 * A task for sleeping a short period of time, useful when a
 * build or deployment process requires an interval between tasks.
 *<p>
 * A negative value can be supplied to any of attributes provided the total sleep time 
 * is positive, pending fundamental changes in physics and JVM
 * execution tims</p>
 * Note that sleep times are always hints to be interpred by the OS how it feels 
 * small times may either be ignored or rounded up to a minimum timeslice. Note 
 * also that the system clocks often have a fairly low granularity too, which complicates 
 * measuring how long a sleep actually took.</p>
*
 * @author steve_l@iseran.com steve loughran
 * @since Ant 1.4
 * @ant.task category="utility"
 */

public class Sleep extends Task {
    /**
     * failure flag
     */
    private boolean failOnError = true;

    /**
     * sleep seconds
     */
    private int seconds = 0;

    /**
     * sleep hours 
     */
    private int hours = 0;
    /**
     * sleep minutes
     */
    private int minutes = 0;

    /**
     * sleep milliseconds
     */
    private int milliseconds = 0;



    /**
     * Creates new instance
     */
    public Sleep() {
    }


    /**
     * seconds to add to the sleep time
     *
     * @param seconds The new Seconds value
     */
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }


    /**
     * hours to add to the sleep time.
     *
     * @param hours The new Hours value
     */
    public void setHours(int hours) {
        this.hours = hours;
    }


    /**
     * minutes to add to the sleep time
     *
     * @param minutes The new Minutes value
     */
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }


    /**
     * milliseconds to add to the sleep time
     *
     * @param milliseconds The new Milliseconds value
     */
    public void setMilliseconds(int milliseconds) {
        this.milliseconds = milliseconds;
    }


    /**
     * sleep for a period of time
     *
     * @param millis time to sleep
     */
    public void doSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
        }
    }


    /**
     * flag controlling whether to break the build on an error.
     *
     * @param failOnError The new FailOnError value
     */
    public void setFailOnError(boolean failOnError) {
        this.failOnError = failOnError;
    }


    /**
     * return time to sleep
     *
     * @return sleep time. if below 0 then there is an error
     */

    private long getSleepTime() {
        return ((((long) hours * 60) + minutes) * 60 + seconds) * 1000 
            + milliseconds;
    }


    /**
     * verify parameters
     *
     * @throws BuildException if something is invalid
     */
    public void validate() 
        throws BuildException {
        if (getSleepTime() < 0) {
            throw new BuildException("Negative sleep periods are not "
                                     + "supported");
        }
    }


    /**
     * Executes this build task. Throws org.apache.tools.ant.BuildException
     * if there is an error during task execution.
     *
     * @exception BuildException Description of Exception
     */
    public void execute()
        throws BuildException {
        try {
            validate();
            long sleepTime = getSleepTime();
            log("sleeping for " + sleepTime + " milliseconds",
                Project.MSG_VERBOSE);
            doSleep(sleepTime);
        } catch (Exception e) {
            if (failOnError) {
                throw new BuildException(e);
            } else {
                String text = e.toString();
                log(text, Project.MSG_ERR);
            }
        }
    }

}

