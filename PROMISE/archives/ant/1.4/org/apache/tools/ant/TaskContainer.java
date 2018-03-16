package org.apache.tools.ant;

/**
 * Interface for objects which can contain tasks 
 *
 * @author <a href="mailto:conor@apache.org">Conor MacNeill</a>
 */
public interface TaskContainer {
    /**
     * Add a task to this task container
     *
     * @param task the task to be added to this container
     */
    void addTask(Task task);
}

