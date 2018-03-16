package org.apache.tools.ant.types;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;

/**
 * Class to hold a reference to another object in the project.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class Reference {

    private String refid;

    public Reference() {
        super();
    }

    public Reference(String id) {
        this();
        setRefId(id);
    }

    public void setRefId(String id) {
        refid = id;
    }

    public String getRefId() {
        return refid;
    }

    public Object getReferencedObject(Project project) throws BuildException {
        if (refid == null) {
            throw new BuildException("No reference specified");
        }
        
        Object o = project.getReference(refid);
        if (o == null) {
            throw new BuildException("Reference " + refid + " not found.");
        }
        return o;
    }
}
