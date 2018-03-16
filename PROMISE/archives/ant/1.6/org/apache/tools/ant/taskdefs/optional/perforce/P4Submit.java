package org.apache.tools.ant.taskdefs.optional.perforce;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import java.util.Vector;

/** Submits a numbered changelist to Perforce.
 *
 * <B>Note:</B> P4Submit cannot (yet) submit the default changelist.
 * This shouldn't be a problem with the ANT task as the usual flow is
 * P4Change to create a new numbered change followed by P4Edit then P4Submit.
 *
 * Example Usage:-<br>
 * &lt;p4submit change="${p4.change}" /&gt;
 *
 *
 * @ant.task category="scm"
 */
public class P4Submit extends P4Base {

    /**
     * change list number
     */
    public String change;
    /**
     * change property
     */
    private String changeProperty;
    /**
     * needsresolveproperty
     */
    private String needsResolveProperty;
    /**
     * set the change list number to submit
     * @param change The changelist number to submit; required.
     */
    public void setChange(String change) {
        this.change = change;
    }
    /**
     * property defining the change number if the change number gets renumbered
     * @param changeProperty name of a new property to which the change number
     * will be assigned if it changes
     * @since ant 1.6.1
     */
    public void setChangeProperty(String changeProperty) {
        this.changeProperty = changeProperty;
    }
    /**
     * property defining the need to resolve the change list
     * @param needsResolveProperty a property which will be set if the change needs resolve
     * @since ant 1.6.1
     */
    public void setNeedsResolveProperty(String needsResolveProperty) {
        this.needsResolveProperty = needsResolveProperty;
    }

    /**
     * do the work
     * @throws BuildException if no change list specified
     */
    public void execute() throws BuildException {
        if (change != null) {
            execP4Command("submit -c " + change, (P4HandlerAdapter) new P4SubmitAdapter(this));
        } else {
            throw new BuildException("No change specified (no support for default change yet....");
        }
    }

    /**
     * internal class used to process the output of p4 submit
     */
    public class P4SubmitAdapter extends SimpleP4OutputHandler {
        public P4SubmitAdapter(P4Base parent) {
            super(parent);
        }
        /**
         * process a line of stdout/stderr coming from Perforce
         * @param line line of stdout or stderr coming from Perforce
         */
        public void process(String line) {
            super.process(line);
            getProject().setProperty("p4.needsresolve", "0");
            if (util.match("/renamed/", line)) {
                try {
                    Vector myarray = new Vector();
                    util.split(myarray, line);
                    boolean found = false;
                    for (int counter = 0; counter < myarray.size(); counter++) {
                        if (found == true) {
                            String chnum = (String) myarray.elementAt(counter + 1);
                            int changenumber = Integer.parseInt(chnum);
                            log("Perforce change renamed " + changenumber, Project.MSG_INFO);
                            getProject().setProperty("p4.change", "" + changenumber);
                            if (changeProperty != null) {
                                getProject().setNewProperty(changeProperty, chnum);
                            }
                            found = false;
                        }
                        if (((myarray.elementAt(counter))).equals("renamed")) {
                            found = true;
                        }
                    }
                } catch (Exception e) {
                    String msg = "Failed to parse " + line  + "\n"
                            + " due to " + e.getMessage();
                    throw new BuildException(msg, e, getLocation());
                }
            }
            if (util.match("/p4 submit -c/", line)) {
                getProject().setProperty("p4.needsresolve", "1");
                if (needsResolveProperty != null) {
                    getProject().setNewProperty(needsResolveProperty, "true");
                }
            }

        }
    }

}
