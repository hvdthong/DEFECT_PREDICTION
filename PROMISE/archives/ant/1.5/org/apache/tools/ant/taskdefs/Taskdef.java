package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildException;

/**
 * Adds a task definition to the current project, such that this new task can be
 * used in the current project. Two attributes are needed, the name that identifies
 * this task uniquely, and the full name of the class (including the packages) that
 * implements this task.</p>
 * <p>You can also define a group of tasks at once using the file or
 * resource attributes.  These attributes point to files in the format of
 * Java property files.  Each line defines a single task in the
 * format:</p>
 * <pre>
 * taskname=fully.qualified.java.classname
 * </pre>
 * @author <a href="stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @since Ant 1.1
 * @ant.task category="internal"
 */
public class Taskdef extends Definer {
    
    /**
     * subclassed handler for definitions; called by parent during
     * execution.
     */
    protected void addDefinition(String name, Class c) throws BuildException {
        project.addTaskDefinition(name, c);
    }
}
