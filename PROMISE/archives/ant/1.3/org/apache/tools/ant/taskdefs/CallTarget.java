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
 * @author <a href="mailto:stefan.bodewig@megabit.net">Stefan Bodewig</a> 
 */
public class CallTarget extends Task {

    private Ant callee;
    private String subTarget;

    public void init() {
        callee = (Ant) project.createTask("ant");
        callee.setOwningTarget(target);
        callee.setTaskName(getTaskName());
        callee.setLocation(location);
        callee.init();
    }

    public void execute() {
        if (subTarget == null) {
            throw new BuildException("Attribute target is required.", 
                                     location);
        }
        
        callee.setDir(project.getBaseDir());
        callee.setAntfile(project.getProperty("ant.file"));
        callee.setTarget(subTarget);
        callee.execute();
    }

    public Property createParam() {
        return callee.createProperty();
    }

    public void setTarget(String target) {
        subTarget = target;
    }
}
