package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;

/**
 * Alters the default excludes for the <strong>entire</strong> build..
 *
 *
 * @since Ant 1.6
 *
 * @ant.task category="utility"
 */
public class DefaultExcludes extends Task {
    private String add = "";
    private String remove = "";
    private boolean defaultrequested = false;
    private boolean echo = false;

    private int logLevel = Project.MSG_WARN;

    /**
     * Does the work.
     *
     * @exception BuildException if something goes wrong with the build
     */
    public void execute() throws BuildException {
        if (!defaultrequested && add.equals("") && remove.equals("") && !echo) {
            throw new BuildException("<defaultexcludes> task must set "
                + "at least one attribute (echo=\"false\""
                + " doesn't count since that is the default");
        }
        if (defaultrequested) {
            DirectoryScanner.resetDefaultExcludes();
        }
        if (!add.equals("")) {
            DirectoryScanner.addDefaultExclude(add);
        }
        if (!remove.equals("")) {
            DirectoryScanner.removeDefaultExclude(remove);
        }
        if (echo) {
            StringBuffer message
                = new StringBuffer("Current Default Excludes:\n");
            String[] excludes = DirectoryScanner.getDefaultExcludes();
            for (int i = 0; i < excludes.length; i++) {
                message.append("  " + excludes[i] + "\n");
            }
            log(message.toString(), logLevel);
        }
    }

    /**
     * go back to standard default patterns
     *
     * @param def if true go back to default patterns
     */
    public void setDefault(boolean def) {
        defaultrequested = def;
    }
    /**
     * Pattern to add to the default excludes
     *
     * @param add Sets the value for the pattern to exclude.
     */
    public void setAdd(String add) {
        this.add = add;
    }

     /**
     * Pattern to remove from the default excludes.
     *
     * @param remove Sets the value for the pattern that
     *            should no longer be excluded.
     */
    public void setRemove(String remove) {
        this.remove = remove;
    }

    /**
     * If true, echo the default excludes.
     *
     * @param echo whether or not to echo the contents of
     *             the default excludes.
     */
    public void setEcho(boolean echo) {
        this.echo = echo;
    }


}
