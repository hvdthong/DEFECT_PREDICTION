package org.apache.tools.ant.taskdefs;

/**
 * Has been merged into ExecuteOn, empty class for backwards compatibility.
 * We leave that in case that external programs access this class direclty,
 * for example via
 *   <tt> Transform tr = (Transform) getProject().createTask("apply") </tt>
 *
 * @ant.task ignore="true"
 */
public class Transform extends ExecuteOn {
}
