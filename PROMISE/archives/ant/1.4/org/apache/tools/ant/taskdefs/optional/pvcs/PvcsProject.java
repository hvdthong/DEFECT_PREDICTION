package org.apache.tools.ant.taskdefs.optional.pvcs;

import org.apache.tools.ant.Project;

/**
 * class to handle &lt;pvcsprojec&gt; elements
 */
public class PvcsProject {
    private String name;
    
    public PvcsProject() {
        super();
    }
    
    public void setName(String name) {
        PvcsProject.this.name = name;
    }
    public String getName() {
        return name;
    }
}
