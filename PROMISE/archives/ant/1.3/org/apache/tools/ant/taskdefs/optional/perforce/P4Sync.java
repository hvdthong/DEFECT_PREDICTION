package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.tools.ant.*;

/** P4Sync  - synchronise client space to a perforce depot view.
 *  The API allows additional functionality of the "p4 sync" command 
 *
 * <b>Example Usage:</b>
 * <table border="1">
 * <th>Function</th><th>Command</th>
 * <tr><td>Sync to a label</td><td>&lt;P4Sync label="myPerforceLabel" /&gt;</td></tr>
 * </table>
 *
 * ToDo:  Add decent label error handling for non-exsitant labels
 *
 * @author <A HREF="mailto:leslie.hughes@rubus.com">Les Hughes</A>
 */
public class P4Sync extends P4Base {

    String label;
    private String syncCmd = "";

    public void setLabel(String label) throws BuildException { 
        if(label == null && !label.equals(""))
                throw new BuildException("P4Sync: Labels cannot be Null or Empty");

        this.label = label;

    }


    public void setForce(String force) throws BuildException {
        if(force == null && !label.equals(""))
                throw new BuildException("P4Sync: If you want to force, set force to non-null string!");
            P4CmdOpts = "-f";
        }
        
    public void execute() throws BuildException {


        if (P4View != null) {
                syncCmd = P4View;
        }

        
        if(label != null && !label.equals("")) {
                syncCmd = syncCmd + "@" + label;
        } 

        
        log("Execing sync "+P4CmdOpts+" "+syncCmd, Project.MSG_VERBOSE);

        execP4Command("-s sync "+P4CmdOpts+" "+syncCmd, new SimpleP4OutputHandler(this));
    }
}
