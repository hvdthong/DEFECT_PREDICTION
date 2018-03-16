package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;
import java.util.*;
import java.text.*;
import java.lang.RuntimeException;

/**
 * Implements a single threaded task execution.
 * <p>
 * @author Thomas Christen <a href="mailto:chr@active.ch">chr@active.ch</a>
 */
public class Sequential extends Task
                        implements TaskContainer {

    /** Optional Vector holding the nested tasks */
    private Vector nestedTasks = new Vector();

    /**
     * Add a nested task to Sequential.
     * <p>
     * @param nestedTask  Nested task to execute Sequential
     * <p>
     */
    public void addTask(Task nestedTask) {
        nestedTasks.addElement(nestedTask);
    }

    /**
     * Execute all nestedTasks.
     */
    public void execute() throws BuildException {
        for (Enumeration e = nestedTasks.elements(); e.hasMoreElements();) {
            Task nestedTask = (Task)e.nextElement();
            nestedTask.perform();
        }
    }
}
