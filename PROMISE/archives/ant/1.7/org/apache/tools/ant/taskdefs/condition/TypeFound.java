package org.apache.tools.ant.taskdefs.condition;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.AntTypeDefinition;
import org.apache.tools.ant.Project;

/**
 * looks for a task or other Ant type that exists. Existence is defined as
 * the type is defined, and its implementation class is present. This
 * will work for datatypes and preset, script and macro definitions.
 */
public class TypeFound extends ProjectComponent implements Condition {

    private String name;
    private String uri;

    /**
     * the task or other type to look for
     * @param name the name of the type
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The URI for this definition.
     * @param uri the namespace URI. If this is not set, use the
     *            default ant namespace.
     */
    public void setURI(String uri) {
        this.uri = uri;
    }

    /**
     * test for a task or other ant type existing in the current project
     * @param typename the name of the type
     * @return true if the typename exists
     */
    protected boolean doesTypeExist(String typename) {

        ComponentHelper helper =
            ComponentHelper.getComponentHelper(getProject());
        String componentName = ProjectHelper.genComponentName(uri, typename);
        AntTypeDefinition def = helper.getDefinition(componentName);
        if (def == null) {
            return false;
        }
        boolean found = def.getExposedClass(getProject()) != null;
        if (!found) {
            String text = helper.diagnoseCreationFailure(componentName, "type");
            log(text, Project.MSG_VERBOSE);
        }
        return found;
    }


    /**
     * Is this condition true?
     * @return true if the condition is true
     * @exception BuildException if an error occurs
     */
    public boolean eval() throws BuildException {
        if (name == null) {
            throw new BuildException("No type specified");
        }
        return doesTypeExist(name);
    }
}
