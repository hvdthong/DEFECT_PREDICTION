package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;

/**
 * Call another target in the same project.
 *
 *  <pre>
 *    <target name="foo">
 *      <antcall target="bar">
 *        <param name="property1" value="aaaaa" />
 *        <param name="foo" value="baz" />
 *       </antcall>
 *    </target>
 *
 *    <target name="bar" depends="init">
 *      <echo message="prop is ${property1} ${foo}" />
 *    </target>
 * </pre>
 *
 * <p>This only works as expected if neither property1 nor foo are
 * defined in the project itself.
 *
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 */
public class CallTarget extends Task {

    private Ant callee;
    private String subTarget;
    private boolean initialized = false;
    private boolean inheritAll = true;

    /**
     * If true, inherit all properties from parent Project
     * If false, inherit only userProperties and those defined
     * inside the antcall call itself
     **/
    public void setInheritAll(boolean inherit) {
       inheritAll = inherit;

    public void init() {
        callee = (Ant) project.createTask("ant");
        callee.setOwningTarget(target);
        callee.setTaskName(getTaskName());
        callee.setLocation(location);
        callee.init();
        initialized = true;
    }

    public void execute() {
        if (!initialized) {
            init();
        }
        
        if (subTarget == null) {
            throw new BuildException("Attribute target is required.", 
                                     location);
        }
        
        callee.setDir(project.getBaseDir());
        callee.setAntfile(project.getProperty("ant.file"));
        callee.setTarget(subTarget);
        callee.setInheritAll(inheritAll);
        callee.execute();
    }

    public Property createParam() {
        return callee.createProperty();
    }

    public void setTarget(String target) {
        subTarget = target;
    }

    protected void handleOutput(String line) {
        if (callee != null) {
            callee.handleOutput(line);
        }
        else {
            super.handleOutput(line);
        }
    }
    
    protected void handleErrorOutput(String line) {
        if (callee != null) {
            callee.handleErrorOutput(line);
        }
        else {
            super.handleErrorOutput(line);
        }
    }
    
}
