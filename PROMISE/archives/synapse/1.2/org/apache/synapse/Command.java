package org.apache.synapse;

/**
 * This is the Command interface that will be implemented to write command objects, but it is not
 * required to implement this interface Synapse will use reflection to find an execute method as
 * a convention in the command pattern and will start the execution from the execute method
 */
public interface Command {

    /**
     * This method will be implemented with a set of getters and setters for the command properties
     * in an implementation of a Command.
     */
    public void execute();
}
