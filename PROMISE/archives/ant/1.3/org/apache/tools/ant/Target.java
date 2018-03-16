package org.apache.tools.ant;

import java.util.*;

/**
 * This class implements a target object with required parameters.
 *
 * @author James Davidson <a href="mailto:duncan@x180.com">duncan@x180.com</a>
 */

public class Target {

    private String name;
    private String ifCondition = "";
    private String unlessCondition = "";
    private Vector dependencies = new Vector(2);
    private Vector tasks = new Vector(5);
    private Project project;
    private String description = null;

    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public void setDepends(String depS) {
        if (depS.length() > 0) {
            StringTokenizer tok =
                new StringTokenizer(depS, ",", false);
            while (tok.hasMoreTokens()) {
                addDependency(tok.nextToken().trim());
            }
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addTask(Task task) {
        tasks.addElement(task);
    }

	/** 
	 * Get the current set of tasks to be executed by this target.
	 * 
     * @return The current set of tasks.
	 */
    public Task[] getTasks() {
        Task[] retval = new Task[tasks.size()];
        tasks.copyInto(retval);
        return retval;
    }

    public void addDependency(String dependency) {
        dependencies.addElement(dependency);
    }

    public Enumeration getDependencies() {
        return dependencies.elements();
    }

    public void setIf(String property) {
        this.ifCondition = (property == null) ? "" : property;
    }
 
    public void setUnless(String property) {
        this.unlessCondition = (property == null) ? "" : property;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return name;
    }

    public void execute() throws BuildException {
        if (testIfCondition() && testUnlessCondition()) {
            Enumeration enum = tasks.elements();
            while (enum.hasMoreElements()) {
                Task task = (Task) enum.nextElement();

                try {
                    project.fireTaskStarted(task);
                    task.maybeConfigure();
                    task.execute();
                    project.fireTaskFinished(task, null);
                }
                catch(RuntimeException exc) {
                    if (exc instanceof BuildException) {
                        BuildException be = (BuildException) exc;
                        if (be.getLocation() == Location.UNKNOWN_LOCATION) {
                            be.setLocation(task.getLocation());
                        }
                    }
                    project.fireTaskFinished(task, exc);
                    throw exc;
                }
            }
        } else if (!testIfCondition()) {
            project.log(this, "Skipped because property '" + this.ifCondition + "' not set.", 
                        Project.MSG_VERBOSE);
        } else {
            project.log(this, "Skipped because property '" + this.unlessCondition + "' set.",
                        Project.MSG_VERBOSE);
        }
    }

    void replaceTask(UnknownElement el, Task t) {
        int index = -1;
        while ((index = tasks.indexOf(el)) >= 0) {
            tasks.setElementAt(t, index);
        }
    }

    private boolean testIfCondition() {
        return "".equals(ifCondition) 
            || project.getProperty(ifCondition) != null;
    }

    private boolean testUnlessCondition() {
        return "".equals(unlessCondition) 
            || project.getProperty(unlessCondition) == null;
    }
}
