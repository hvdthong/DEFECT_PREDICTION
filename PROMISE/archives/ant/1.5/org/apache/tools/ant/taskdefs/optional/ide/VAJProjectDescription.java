package org.apache.tools.ant.taskdefs.optional.ide;

import org.apache.tools.ant.BuildException;

/**
 * Type class. Holds information about a project edition.
 * @author Wolf Siberski
 */
public class VAJProjectDescription {
    private String name;
    private String version;
    private boolean projectFound;

    public VAJProjectDescription() {
    }

    public VAJProjectDescription(String n, String v) {
        name = n;
        version = v;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public boolean projectFound() {
        return projectFound;
    }

    /**
     * name of the VAJ project to load into
     * the workspace; required
     */
    public void setName(String newName) {
        if (newName == null || newName.equals("")) {
            throw new BuildException("name attribute must be set");
        }
        name = newName;
    }

    /**
     * name of the requested version; required.
     */
    public void setVersion(String newVersion) {
        if (newVersion == null || newVersion.equals("")) {
            throw new BuildException("version attribute must be set");
        }
        version = newVersion;
    }

    /**
     * this may be a helper method, and is being ignored for now
     
     * @ant.attribute ignore="true"
     */
    public void setProjectFound() {
        projectFound = true;
    }
}
