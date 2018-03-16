package org.apache.tools.ant.taskdefs.optional.ide;

import java.io.File;


/**
 * A Remote Access to Tools Servlet to import a Project
 * from files into the Repository. The following
 * table describes the servlet parameters.
 *
 * <table>
 *   <tr>
 *     <td>Parameter</td>
 *     <td>Description</td>
 *   </tr>
 *   <tr>
 *     <td>project</td>
 *     <td>The name of the project where you want the imported
 *         items to go.
 *     </td>
 *   </tr>
 *   <tr>
 *     <td>dir</td>
 *     <td>The directory you want to import from.</td>
 *   </tr>
 * </table>
 *
 * @author Wolf Siberski, based on servlets written by Glenn McAllister
 */
public class VAJImportServlet extends VAJToolsServlet {
    /**
     * Respond to a request to import files to the Repository
     */
    protected void executeRequest() {
        getUtil().importFiles(
                      getFirstParamValueString(PROJECT_NAME_PARAM),
                      new File(getFirstParamValueString(DIR_PARAM)),
                      getParamValues(INCLUDE_PARAM),
                      getParamValues(EXCLUDE_PARAM),
                      getBooleanParam(CLASSES_PARAM, false),
                      getBooleanParam(RESOURCES_PARAM, true),
                      getBooleanParam(SOURCES_PARAM, true),
                      false); 
                      
    }
}
