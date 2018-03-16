package org.apache.tools.ant.types;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.helper.ProjectHelper2;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.UnknownElement;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.helper.ProjectHelperImpl;

import java.util.Vector;


/**
 * Description is used to provide a project-wide description element
 * (that is, a description that applies to a buildfile as a whole).
 * If present, the &lt;description&gt; element is printed out before the
 * target descriptions.
 *
 * Description has no attributes, only text.  There can only be one
 * project description per project.  A second description element will
 * overwrite the first.
 *
 *
 * @ant.datatype ignore="true"
 */
public class Description extends DataType {

    /**
     * Adds descriptive text to the project.
     *
     * @param text the descriptive text
     */
    public void addText(String text) {

        ProjectHelper ph = ProjectHelper.getProjectHelper();
        if (!(ph instanceof ProjectHelperImpl)) {
            return;
        }
        String currentDescription = getProject().getDescription();
        if (currentDescription == null) {
            getProject().setDescription(text);
        } else {
            getProject().setDescription(currentDescription + text);
        }
    }

    /**
     * Return the descriptions from all the targets of
     * a project.
     *
     * @param project the project to get the descriptions for.
     * @return a string containing the concatenated descriptions of
     *         the targets.
     */
    public static String getDescription(Project project) {
        Vector targets = (Vector) project.getReference(ProjectHelper2.REFID_TARGETS);
        if (targets == null) {
            return null;
        }
        StringBuffer description = new StringBuffer();
        for (int i = 0; i < targets.size(); i++) {
            Target t = (Target) targets.elementAt(i);
            concatDescriptions(project, t, description);
        }
        return description.toString();
    }

    private static void concatDescriptions(Project project, Target t,
                                           StringBuffer description) {
        if (t == null) {
            return;
        }
        Vector tasks = findElementInTarget(project, t, "description");
        if (tasks == null) {
            return;
        }
        for (int i = 0; i < tasks.size(); i++) {
            Task task = (Task) tasks.elementAt(i);
            if (!(task instanceof UnknownElement)) {
                continue;
            }
            UnknownElement ue = ((UnknownElement) task);
            String descComp = ue.getWrapper().getText().toString();
            if (descComp != null) {
                description.append(project.replaceProperties(descComp));
            }
        }
    }

    private static Vector findElementInTarget(Project project,
                                              Target t, String name) {
        Task[] tasks = t.getTasks();
        Vector elems = new Vector();
        for (int i = 0; i < tasks.length; i++) {
            if (name.equals(tasks[i].getTaskName())) {
                elems.addElement(tasks[i]);
            }
        }
        return elems;
    }

}
