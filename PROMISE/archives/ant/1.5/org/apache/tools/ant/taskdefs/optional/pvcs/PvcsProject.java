package org.apache.tools.ant.taskdefs.optional.pvcs;



/**
 * represents a project within the PVCS repository to extract files from.
 */
public class PvcsProject {
    private String name;
    
    public PvcsProject() {
        super();
    }
    
    /**
     * Set the name of the project
     */
    public void setName(String name) {
        PvcsProject.this.name = name;
    }
    
    /**
     * Get the name of the project
     */
    public String getName() {
        return name;
    }
}
