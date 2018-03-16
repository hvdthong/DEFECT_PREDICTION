package org.apache.tools.ant;

/**
 * Interface for objects which can contain tasks.
 * <p>
 * It is recommended that implementations call perform rather than
 * execute for the tasks they contain, as this method ensures that the
 * appropriate BuildEvents will be generated.
 *
 * @see Task#perform
 * @see Task#execute
 * @see BuildEvent
 *
 */
public interface TaskContainer {
    /**
     * Adds a task to this task container
     *
     * @param task The task to be added to this container.
     *             Must not be <code>null</code>.
     */
    void addTask(Task task);
}

