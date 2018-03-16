package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import java.util.*;
import java.text.*;
import java.lang.RuntimeException;

/**
 * Implements a multi threaded task execution.
 * <p>
 * @author Thomas Christen <a href="mailto:chr@active.ch">chr@active.ch</a>
 * @author <a href="mailto:conor@apache.org">Conor MacNeill </a>
 */
public class Parallel extends Task
                      implements TaskContainer {

    /** Collection holding the nested tasks */
    private Vector nestedTasks = new Vector();


    /**
     * Add a nested task to execute parallel (asynchron).
     * <p>
     * @param nestedTask  Nested task to be executed in parallel
     */
    public void addTask(Task nestedTask) throws BuildException {
        nestedTasks.addElement(nestedTask);
    }

    /**
     * Block execution until the specified time or for a
     * specified amount of milliseconds and if defined,
     * execute the wait status.
     */
    public void execute() throws BuildException {
        TaskThread[] threads = new TaskThread[nestedTasks.size()];
        int threadNumber = 0;
        for (Enumeration e = nestedTasks.elements(); e.hasMoreElements(); threadNumber++) {
            Task nestedTask = (Task)e.nextElement();
            threads[threadNumber] = new TaskThread(threadNumber, nestedTask);
        }

        for (int i = 0; i < threads.length; ++i) {
            threads[i].start();
        }

        for (int i = 0; i < threads.length; ++i) {
            try {
                threads[i].join();
            }
            catch (InterruptedException ie) {
            }
        }
        
        StringBuffer exceptionMessage = new StringBuffer();
        String lSep = System.getProperty("line.separator");
        int numExceptions = 0;
        Throwable firstException = null;
        Location firstLocation = Location.UNKNOWN_LOCATION;;
        for (int i = 0; i < threads.length; ++i) {
            Throwable t = threads[i].getException();
            if (t != null) {
                numExceptions++;
                if (firstException == null) {
                    firstException = t;
                }
                if (t instanceof BuildException && 
                        firstLocation == Location.UNKNOWN_LOCATION) {
                    firstLocation = ((BuildException)t).getLocation();
                }
                exceptionMessage.append(lSep);
                exceptionMessage.append(t.getMessage());
            }
        }
        
        if (numExceptions == 1) {
            if (firstException instanceof BuildException) {
                throw (BuildException)firstException;
            }
            else {
                throw new BuildException(firstException);
            }
        }
        else if (numExceptions > 1) {
            throw new BuildException(exceptionMessage.toString(), firstLocation);
        }
    }

    class TaskThread extends Thread {
        private Throwable exception;
        private Task task;
        private int taskNumber;

        /**
         * Construct a new TaskThread<p>
         *
         * @param task the Task to be executed in a seperate thread
         */
        TaskThread(int taskNumber, Task task) {
            this.task = task;
            this.taskNumber = taskNumber;
        }

        /**
         * Executes the task within a thread and takes care about
         * Exceptions raised within the task.
         */
        public void run() {
            try {
                task.perform();
            }
            catch (Throwable t) {
                exception = t;
            }
        }
        
        public Throwable getException() { 
            return exception;
        }
    }
}
