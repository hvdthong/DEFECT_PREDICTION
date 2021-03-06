package org.apache.tools.ant.types;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/**
 * Class to hold a reference to another object in the project.
 *
 */
public class Reference {

    private String refid;
    private Project project;

    /**
     * Create a reference.
     * @deprecated since 1.7.
     *             Please use {@link Reference#Reference(Project,String)}
     *             instead.
     */
    public Reference() {
    }

    /**
     * Create a reference to a named ID.
     * @param id the name of this reference
     * @deprecated since 1.7.
     *             Please use {@link Reference#Reference(Project,String)}
     *             instead.
     */
    public Reference(String id) {
        setRefId(id);
    }

    /**
     * Create a reference to a named ID in a particular project.
     * @param p the project this reference is associated with
     * @param id the name of this reference
     * @since Ant 1.6.3
     */
    public Reference(Project p, String id) {
        setRefId(id);
        setProject(p);
    }

    /**
     * Set the reference id. Should not normally be necessary;
     * use {@link Reference#Reference(Project, String)}.
     * @param id the reference id to use
     */
    public void setRefId(String id) {
        refid = id;
    }

    /**
     * Get the reference id of this reference.
     * @return the reference id
     */
    public String getRefId() {
        return refid;
    }

    /**
     * Set the associated project. Should not normally be necessary;
     * use {@link Reference#Reference(Project,String)}.
     * @param p the project to use
     * @since Ant 1.6.3
     */
    public void setProject(Project p) {
        this.project = p;
    }

    /**
     * Get the associated project, if any; may be null.
     * @return the associated project
     * @since Ant 1.6.3
     */
    public Project getProject() {
        return project;
    }

    /**
     * Resolve the reference, using the associated project if
     * it set, otherwise use the passed in project.
     * @param fallback the fallback project to use if the project attribute of
     *                 reference is not set.
     * @return the dereferenced object.
     * @throws BuildException if the reference cannot be dereferenced.
     */
    public Object getReferencedObject(Project fallback) throws BuildException {
        if (refid == null) {
            throw new BuildException("No reference specified");
        }

        Object o = project == null ? fallback.getReference(refid) : project.getReference(refid);
        if (o == null) {
            throw new BuildException("Reference " + refid + " not found.");
        }
        return o;
    }

    /**
     * Resolve the reference, looking in the associated project.
     * @see Project#getReference
     * @return the dereferenced object.
     * @throws BuildException if the project is null or the reference cannot be dereferenced
     * @since Ant 1.6.3
     */
    public Object getReferencedObject() throws BuildException {
        if (project == null) {
            throw new BuildException("No project set on reference to " + refid);
        }
        return getReferencedObject(project);
    }

}
