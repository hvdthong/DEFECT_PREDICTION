package org.apache.tools.ant;

import java.util.Vector;

/**
 * Wrapper class that holds all information necessary to create a task
 * that did not exist when Ant started.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class UnknownElement extends Task {

    private String elementName;
    private Task realTask;
    private Vector children = new Vector();
    
    public UnknownElement (String elementName) {
        this.elementName = elementName;
    }
    
    /**
     * return the corresponding XML tag.
     */
    public String getTag() {
        return elementName;
    }

    public void maybeConfigure() throws BuildException {
        realTask = makeTask(this, wrapper);

        wrapper.setProxy(realTask);
        realTask.setRuntimeConfigurableWrapper(wrapper);

        handleChildren(realTask, wrapper);

        realTask.maybeConfigure();
        target.replaceTask(this, realTask);
    }

    /**
     * Called when the real task has been configured for the first time.
     */
    public void execute() {
        if (realTask == null) {
            throw new BuildException("Could not create task of type: "
                                     + elementName, location);
        }
        realTask.execute();
    }

    public void addChild(UnknownElement child) {
        children.addElement(child);
    }

    protected void handleChildren(Object parent, 
                                  RuntimeConfigurable parentWrapper) 
        throws BuildException {

        if (parent instanceof TaskAdapter) {
            parent = ((TaskAdapter) parent).getProxy();
        }

        Class parentClass = parent.getClass();
        IntrospectionHelper ih = IntrospectionHelper.getHelper(parentClass);
        
        for (int i=0; i<children.size(); i++) {
            RuntimeConfigurable childWrapper = parentWrapper.getChild(i);
            UnknownElement child = (UnknownElement) children.elementAt(i);
            Object realChild = null;
            if (parent instanceof TaskContainer) {
                realChild = makeTask(child, childWrapper);
                ((TaskContainer) parent).addTask((Task) realChild);
            } else {
                realChild = ih.createElement(project, parent, child.getTag());
            }

            childWrapper.setProxy(realChild);
            if (realChild instanceof Task) {
                ((Task) realChild).setRuntimeConfigurableWrapper(childWrapper);
            }
            child.handleChildren(realChild, childWrapper);
            if (realChild instanceof Task) {
                ((Task) realChild).maybeConfigure();
            }
        }
    }

    /**
     * Create a named task and configure it up to the init() stage.
     */
    protected Task makeTask(UnknownElement ue, RuntimeConfigurable w) {
        Task task = project.createTask(ue.getTag());
        if (task == null) {
            log("Could not create task of type: " + elementName + " Common solutions" +
                " are adding the task to defaults.properties and executing bin/bootstrap",
                Project.MSG_DEBUG);
            throw new BuildException("Could not create task of type: " + elementName +
                                     ". Common solutions are to use taskdef to declare" +
                                     " your task, or, if this is an optional task," +
                                     " to put the optional.jar in the lib directory of" +
                                     " your ant installation (ANT_HOME).", location);
        }

        task.setLocation(getLocation());
        String id = w.getAttributes().getValue("id");
        if (id != null) {
            project.addReference(id, task);
        }
        task.setOwningTarget(target);

        task.init();
        return task;
    }

    /**
     * Get the name to use in logging messages.
     *
     * @return the name to use in logging messages.
     */
    public String getTaskName() {
        return realTask == null ? super.getTaskName() : realTask.getTaskName();
    }

