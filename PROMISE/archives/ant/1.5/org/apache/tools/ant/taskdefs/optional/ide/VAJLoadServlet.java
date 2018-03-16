package org.apache.tools.ant.taskdefs.optional.ide;

import java.util.Vector;

/**
 * A Remote Access to Tools Servlet to load a Project
 * from the Repository into the Workbench. The following 
 * table describes the servlet parameters.
 *
 * <table>
 *   <tr>
 *     <td>Parameter</td>
 *     <td>Description</td>
 *   </tr>
 *   <tr>
 *     <td>project</td>
 *     <td>The name of the Project you want to load into
 *         the Workbench.</td>
 *   </tr>
 *   <tr>
 *     <td>version</td>
 *     <td>The version of the package you want to load into
 *         the Workbench.</td>
 *   </tr>
 * </table>
 * 
 * @author Wolf Siberski, based on servlets written by Glenn McAllister
 */
public class VAJLoadServlet extends VAJToolsServlet {

    public static final String VERSION_PARAM = "version";

    /**
     * Respond to a request to load a project from the Repository
     * into the Workbench.
     */
    protected void executeRequest() {
        String[] projectNames = getParamValues(PROJECT_NAME_PARAM);
        String[] versionNames = getParamValues(VERSION_PARAM);

        Vector projectDescriptions = new Vector(projectNames.length);
        for (int i = 0; i < projectNames.length && i < versionNames.length; i++) {
            VAJProjectDescription desc = new VAJProjectDescription();
            desc.setName(projectNames[i]);
            desc.setVersion(versionNames[i]);
            projectDescriptions.addElement(desc);
        }

        util.loadProjects(projectDescriptions);
    }
}
