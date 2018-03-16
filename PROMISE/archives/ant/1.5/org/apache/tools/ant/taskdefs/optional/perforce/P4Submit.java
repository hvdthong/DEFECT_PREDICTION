package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/** Submits a numbered changelist to Perforce.
 *
 * <B>Note:</B> P4Submit cannot (yet) submit the default changelist.
 * This shouldn't be a problem with the ANT task as the usual flow is
 * P4Change to create a new numbered change followed by P4Edit then P4Submit.
 *
 * Example Usage:-<br>
 * &lt;p4submit change="${p4.change}" /&gt;
 *
 * @author <A HREF="mailto:leslie.hughes@rubus.com">Les Hughes</A>
 *
 */
public class P4Submit extends P4Base {

    public String change;

    /**
     * The changelist number to submit; required.
     */
    public void setChange(String change) {
        this.change = change;
    }

    public void execute() throws BuildException {
        if (change != null) {
            execP4Command("submit -c " + change,
                    new P4HandlerAdapter() {
                        public void process(String line) {
                            log(line, Project.MSG_VERBOSE);
                        }
                    });
        } else {
            throw new BuildException("No change specified (no support for default change yet....");
        }
    }

}
