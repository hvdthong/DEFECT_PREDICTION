package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

/**
 * Call another target in the same project.
 *
 *  <pre>
 *    &lt;target name="foo"&gt;
 *      &lt;antcall target="bar"&gt;
 *        &lt;param name="property1" value="aaaaa" /&gt;
 *        &lt;param name="foo" value="baz" /&gt;
 *       &lt;/antcall&gt;
 *    &lt;/target&gt;
 *
 *    &lt;target name="bar" depends="init"&gt;
 *      &lt;echo message="prop is ${property1} ${foo}" /&gt;
 *    &lt;/target&gt;
 * </pre>
 *
 * <p>This only works as expected if neither property1 nor foo are
 * defined in the project itself.
 *
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 *
 * @since Ant 1.2
 *
 * @ant.task name="antcall" category="control"
 */
public class CallTarget extends Task {

    private Ant callee;
    private String subTarget;
    private boolean inheritAll = true;
    private boolean inheritRefs = false;

    /**
     * If true, pass all properties to the new Ant project.
     * Defaults to true.
     */
    public void setInheritAll(boolean inherit) {
       inheritAll = inherit;
    }

    /**
     * If true, pass all references to the new Ant project.
     * Defaults to false
     * @param inheritRefs new value
     */
    public void setInheritRefs(boolean inheritRefs) {
        this.inheritRefs = inheritRefs;
    }

    /**
     * init this task by creating new instance of the ant task and
     * configuring it's by calling its own init method.
     */
    public void init() {
        callee = (Ant) getProject().createTask("ant");
        callee.setOwningTarget(getOwningTarget());
        callee.setTaskName(getTaskName());
        callee.setLocation(getLocation());
        callee.init();
    }

    /**
     * hand off the work to the ant task of ours, after setting it up
     * @throws BuildException on validation failure or if the target didn't
     * execute
     */
    public void execute() throws BuildException {
        if (callee == null) {
            init();
        }
        
        if (subTarget == null) {
            throw new BuildException("Attribute target is required.", 
                                     location);
        }
        
        callee.setAntfile(getProject().getProperty("ant.file"));
        callee.setTarget(subTarget);
        callee.setInheritAll(inheritAll);
        callee.setInheritRefs(inheritRefs);
        callee.execute();
    }

    /**
     * Property to pass to the invoked target.
     */
    public Property createParam() {
        if (callee == null) {
            init();
        }
        return callee.createProperty();
    }

    /**
     * Reference element identifying a data type to carry
     * over to the invoked target.
     * @since Ant 1.5
     */
    public void addReference(Ant.Reference r) {
        if (callee == null) {
            init();
        }
        callee.addReference(r);
    }

    /**
     * Target to execute, required.
     */
    public void setTarget(String target) {
        subTarget = target;
    }

    /**
     * Pass output sent to System.out to the new project.
     *
     * @since Ant 1.5
     */
    public void handleOutput(String line) {
        if (callee != null) {
            callee.handleOutput(line);
        } else {
            super.handleOutput(line);
        }
    }
    
    /**
     * Pass output sent to System.out to the new project.
     *
     * @since Ant 1.5.2
     */
    public void handleFlush(String line) {
        if (callee != null) {
            callee.handleFlush(line);
        } else {
            super.handleFlush(line);
        }
    }
    
    /**
     * Pass output sent to System.err to the new project.
     *
     * @since Ant 1.5
     */
    public void handleErrorOutput(String line) {
        if (callee != null) {
            callee.handleErrorOutput(line);
        } else {
            super.handleErrorOutput(line);
        }
    }
    
    /**
     * Pass output sent to System.err to the new project.
     *
     * @since Ant 1.5.2
     */
    public void handleErrorFlush(String line) {
        if (callee != null) {
            callee.handleErrorFlush(line);
        } else {
            super.handleErrorFlush(line);
        }
    }
}
