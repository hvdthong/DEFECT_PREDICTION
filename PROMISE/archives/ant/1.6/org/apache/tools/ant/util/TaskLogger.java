package org.apache.tools.ant.util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * A facade that makes logging nicers to use.
 *
 */
public final class TaskLogger {
    /**
     * Task to use to do logging.
     */
    private Task m_task;

    public TaskLogger(final Task task) {
        this.m_task = task;
    }

    public void info(final String message) {
        m_task.log(message, Project.MSG_INFO);
    }

    public void error(final String message) {
        m_task.log(message, Project.MSG_ERR);
    }

    public void warning(final String message) {
        m_task.log(message, Project.MSG_WARN);
    }

    public void verbose(final String message) {
        m_task.log(message, Project.MSG_VERBOSE);
    }

    public void debug(final String message) {
        m_task.log(message, Project.MSG_DEBUG);
    }
}
