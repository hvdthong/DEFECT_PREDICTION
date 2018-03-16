package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.PatternSet;

import java.util.*;

/**
 * This task will compile and load a new taskdef all in one step.
 * At times, this is useful for eliminating ordering dependencies
 * which otherwise would require multiple executions of Ant.
 *
 * @author Sam Ruby <a href="mailto:rubys@us.ibm.com">rubys@us.ibm.com</a>
 *
 * @deprecated use &lt;taskdef&gt; elements nested into &lt;target&gt;s instead
 */

public class CompileTask extends Javac {

    protected Vector taskList = new Vector();

    /**
     * add a new task entry on the task list
     */
    public Taskdef createTaskdef() {
        Taskdef task = new Taskdef();
        taskList.addElement(task);
        return task;
    }
    
    /**
     * do all the real work in init
     */
    public void init() {
        log("!! CompileTask is deprecated. !!");
        log("Use <taskdef> elements nested into <target>s instead");

        for (Enumeration e=taskList.elements(); e.hasMoreElements(); ) {
            Taskdef task = (Taskdef)e.nextElement();
            String source = task.getClassname().replace('.','/') + ".java";
            PatternSet.NameEntry include = super.createInclude();
            include.setName("**/" + source);
        }

        super.init();        
        super.execute();        

        for (Enumeration e=taskList.elements(); e.hasMoreElements(); ) {
            Taskdef task = (Taskdef)e.nextElement();
            task.init();
        }

    }

    /**
     * have execute do nothing
     */
    public void execute() {
    }
}
