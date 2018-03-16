package org.apache.tools.ant.taskdefs;

/**
 * Define a new task.
 *
 * @author <a href="stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class Taskdef extends Definer {
    protected void addDefinition(String name, Class c) {
        project.addTaskDefinition(name, c);
    }
}
