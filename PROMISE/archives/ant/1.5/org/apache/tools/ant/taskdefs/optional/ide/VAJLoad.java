package org.apache.tools.ant.taskdefs.optional.ide;


import java.util.Vector;
import org.apache.tools.ant.BuildException;

/**
 * Load project versions into the Visual Age for Java workspace.
 * Each project is identified by its name and a version qualifier.
 * Allowed qualifiers are:
 * <ul>
 * <li>Any valid Visual Age version name</li>
 * <li>* (loads the latest <b>versioned</b> edition)</li>
 * <li>** (loads the latest edition, including open editions)</li>
 * </ul>
 * Example:
 * <blockquote>
 * &lt;vajload&gt;
 * &nbsp;&lt;project name=&quot;MyVAProject&quot; version=&quot;*&quot;/&gt;
 * &nbsp;&lt;project name=&quot;Apache Xerces&quot; version=&quot;1.2.0&quot;/&gt;
 * &nbsp;&lt;project name=&quot;Brand New Stuff&quot; version=&quot;**&quot;/&gt;
 * &lt;/vajload&gt;
 * </blockquote>
 *
 * <p>Parameters:</p>
 * <table border="1" cellpadding="2" cellspacing="0">
 * <tr>
 *   <td valign="top"><b>Attribute</b></td>
 *   <td valign="top"><b>Description</b></td>
 *   <td align="center" valign="top"><b>Required</b></td>
 * </tr>
 * <tr>
 *   <td valign="top">remote</td>
 *   <td valign="top">remote tool server to run this command against
 *                    (format: &lt;servername&gt; : &lt;port no&gt;)</td>
 *   <td align="center" valign="top">No</td>
 * </tr>
 * <tr>
 *   <td valign="top">haltonerror</td>
 *   <td valign="top">stop the build process if an error occurs,
 *                      defaults to "yes"</td>
 *   <td align="center" valign="top">No</td>
 * </tr>
 * </table>
 * </p>
 *
 * @author Wolf Siberski, TUI Infotec GmbH
 * @author Martin Landers, Beck et al. projects
 */

public class VAJLoad extends VAJTask {
    Vector projectDescriptions = new Vector();

    /**
     * Load specified projects.
     */
    public void execute() {
        try {
            getUtil().loadProjects(projectDescriptions);
        } catch (BuildException ex) {
            if (haltOnError) {
                throw ex;
            } else {
                log(ex.toString());
            }
        }
    }

    /**
     * Add a project description entry on the project list.
     */
    public VAJProjectDescription createVAJProject() {
        VAJProjectDescription d = new VAJProjectDescription();
        projectDescriptions.addElement(d);
        return d;
    }
}
