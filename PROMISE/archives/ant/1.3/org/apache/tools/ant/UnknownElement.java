package org.apache.tools.ant;

import java.util.Vector;

/**
 * Wrapper class that holds all information necessary to create a task
 * that did not exist when Ant started.
 *
 * @author <a href="stefan.bodewig@megabit.net">Stefan Bodewig</a> 
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
        realTask = project.createTask(elementName);
        if (realTask == null) {
            log("Could not create task of type: " + elementName + " Common solutions" +
                " are adding the task to defaults.properties and executing bin/bootstrap",
                Project.MSG_DEBUG);
            throw new BuildException("Could not create task of type: " + elementName +
                                     ". Common solutions are to use taskdef to declare" +
                                     " your task, or, if this is an optional task," +
                                     " to put the optional.jar in the lib directory of" +
                                     " your ant installation (ANT_HOME).", location);
        }

        realTask.setLocation(location);
        String id = wrapper.getAttributes().getValue("id");
        if (id != null) {
            project.addReference(id, realTask);
        }
        realTask.init();

        realTask.setOwningTarget(target);

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
            UnknownElement child = (UnknownElement) children.elementAt(i);
            Object realChild = ih.createElement(parent, child.getTag());
            RuntimeConfigurable childWrapper = parentWrapper.getChild(i);
            childWrapper.setProxy(realChild);
            child.handleChildren(realChild, childWrapper);
        }
    }

