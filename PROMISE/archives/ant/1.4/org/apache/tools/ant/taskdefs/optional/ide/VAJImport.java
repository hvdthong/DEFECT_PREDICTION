package org.apache.tools.ant.taskdefs.optional.ide;

import com.ibm.ivj.util.base.ImportCodeSpec;
import com.ibm.ivj.util.base.IvjException;
import com.ibm.ivj.util.base.Project;
import com.ibm.ivj.util.base.ProjectEdition;
import com.ibm.ivj.util.base.Type;
import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

/**
 * Import source, class files, and resources to the Visual Age for Java 
 * workspace using FileSets.
 * <p>
 * Example:  
 * <pre> 
 * &lt;vajimport project="MyVAProject"&gt;
 *   &lt;fileset dir="src"&gt;
 *     &lt;include name="org/foo/subsystem1/**" /&gt;
 *     &lt;exclude name="/org/foo/subsystem1/test/**" /&gt;
 *  &lt;/fileset&gt;
 * &lt;/vajexport&gt;
 * </pre>
 * import all source and resource files from the "src" directory
 * which start with 'org.foo.subsystem1', except of these starting with 
 * 'org.foo.subsystem1.test' into the project MyVAProject.
 * </p>
 * <p>If MyVAProject isn't loaded into the Workspace, a new edition is
 * created in the repository and automatically loaded into the Workspace.
 * There has to be at least one nested FileSet element.
 * </p>
 * <p>There are attributes to choose which items to export:
 * <table border="1" cellpadding="2" cellspacing="0">
 * <tr>
 *   <td valign="top"><b>Attribute</b></td>
 *   <td valign="top"><b>Description</b></td>
 *   <td align="center" valign="top"><b>Required</b></td>
 * </tr>
 * <tr>
 *   <td valign="top">vajproject</td>
 *   <td valign="top">the name of the Project to import to</td>
 *   <td align="center" valign="top">Yes</td>
 * </tr>
 * <tr>
 *   <td valign="top">importSources</td>
 *   <td valign="top">import Java sources, defaults to "yes"</td>
 *   <td align="center" valign="top">No</td>
 * </tr>
 * <tr>
 *   <td valign="top">importResources</td>
 *   <td valign="top">import resource files (anything that doesn't
 *                    end with .java or .class), defaults to "yes"</td>
 *   <td align="center" valign="top">No</td>
 * </tr>
 * <tr>
 *   <td valign="top">importClasses</td>
 *   <td valign="top">import class files, defaults to "no"</td>
 *   <td align="center" valign="top">No</td>
 * </tr>
 * </table>
 *
 * @author: Glenn McAllister, inspired by a similar task written by Peter Kelley
 */
public class VAJImport extends Task {
    protected Vector filesets = new Vector();
    protected boolean importSources = true;
    protected boolean importResources = true;
    protected boolean importClasses = false;
    protected String importProject = null;
    protected Project vajproject = null;

    /**
     * The VisualAge for Java Project name to import into.
     */
    public void setVajproject(String projectName) {
        this.importProject = projectName;
    }

    /**
     * Adds a set of files (nested fileset attribute).
     */
    public void addFileset(FileSet set) {
        filesets.addElement(set);
    }

    /**
     * Import .class files.
     */
    public void setImportClasses(boolean importClasses) {
        this.importClasses = importClasses;
    }

    /**
     * Import resource files (anything that doesn't end in
     * .class or .java)
     */
    public void setImportResources(boolean importResources) {
        this.importResources = importResources;
    }

    /**
     * Import .java files
     */
    public void setImportSources(boolean importSources) {
        this.importSources = importSources;
    }

    /**
     * Do the import.
     */
    public void execute() throws BuildException {
        if (filesets.size() == 0) {
            throw new BuildException("At least one fileset is required!");
        }

        if (importProject == null || "".equals(importProject)) {
            throw new BuildException("The VisualAge for Java Project name is required!");
        }

        vajproject = getVAJProject();
        if (vajproject == null) {
            try {
                vajproject = VAJUtil.getWorkspace().createProject(this.importProject, true);
            } catch (IvjException e) {
                throw VAJUtil.createBuildException( "Error while creating Project " + 
                                                    importProject + ": ", 
                                                    e ); 
            }
        }

        for (Enumeration e = filesets.elements(); e.hasMoreElements();) {
            importFileset((FileSet) e.nextElement());
        }
    }

    /**
     * Try to get the project we want from the Workspace.
     */
    protected Project getVAJProject() {
        Project found = null;
        Project[] currentProjects = VAJUtil.getWorkspace().getProjects();

        for (int i = 0; i < currentProjects.length; i++) {
            Project p = currentProjects[i];
            if (p.getName().equals(this.importProject)) {
                found = p;
                break;
            }
        }

        return found;
    }

    /**
     * Import all files from the fileset into the Project in the
     * Workspace.
     */
    protected void importFileset(FileSet fileset) {
        DirectoryScanner ds = fileset.getDirectoryScanner(this.project);
        if (ds.getIncludedFiles().length == 0) {
            return;
        }

        Vector classes = new Vector();
        Vector sources = new Vector();
        Vector resources = new Vector();

        String[] classesArr = null;
        String[] sourcesArr = null;
        String[] resourcesArr = null;

        StringBuffer msg = new StringBuffer();
        msg.append("Importing ");
        String connector = "";

        ImportCodeSpec importSpec = new ImportCodeSpec();
        importSpec.setDefaultProject(vajproject);

        scan(
             fileset.getDir(this.project), 
             ds.getIncludedFiles(), 
             classes, 
             sources, 
             resources); 

        if (importClasses) {
            classesArr = new String[classes.size()];
            classes.copyInto(classesArr);
            importSpec.setClassFiles(classesArr);
            if (classesArr.length > 0) {
                logFiles(classes, "class");
                msg.append( classesArr.length );
                msg.append( " class " );
                msg.append( classesArr.length > 1 ? "files" : "file" );
                connector = ", ";
            }
        }

        if (importSources) {
            sourcesArr = new String[sources.size()];
            sources.copyInto(sourcesArr);
            importSpec.setJavaFiles(sourcesArr);
            if (sourcesArr.length > 0) {
                logFiles(sources, "source");
                msg.append( connector );
                msg.append( sourcesArr.length );
                msg.append( " source " );
                msg.append( sourcesArr.length > 1 ? "files" : "file" );
                connector = ", ";
            }
        }

        if (importResources) {
            String resourcePath = fileset.getDir(this.project).getAbsolutePath();
            resourcesArr = new String[resources.size()];
            resources.copyInto(resourcesArr);
            importSpec.setResourcePath(resourcePath);
            importSpec.setResourceFiles(resourcesArr);
            if (resourcesArr.length > 0) {
                logFiles(resources, "resource");
                log( "  (relative to resource path '" + resourcePath + "')", 
                     org.apache.tools.ant.Project.MSG_VERBOSE );

                msg.append( connector );
                msg.append( resourcesArr.length );
                msg.append( " resource " );
                msg.append( resourcesArr.length > 1 ? "files" : "file" );
            }
        }

        msg.append( " into the " );
        msg.append( importProject );
        msg.append( " project." );

        log(msg.toString());
                
        try {
            Type[] importedTypes = VAJUtil.getWorkspace().importData(importSpec);
            if (importedTypes == null) {
                throw new BuildException("Unable to import into Workspace!");
            }
        } catch (IvjException ivje) {
            throw VAJUtil.createBuildException("Error while importing into Workspace: ", ivje);
        }
    }

    /**
     * Sort the files into classes, sources, and resources.
     */
    protected void scan(
                        File dir, 
                        String[] files, 
                        Vector classes, 
                        Vector sources, 
                        Vector resources) {
        for (int i = 0; i < files.length; i++) {
            String file = (new File(dir, files[i])).getAbsolutePath();
            if (file.endsWith(".java") || file.endsWith(".JAVA")) {
                sources.addElement(file);
            } else
                if (file.endsWith(".class") || file.endsWith(".CLASS")) {
                    classes.addElement(file);
                } else {
                    resources.addElement(files[i]);
                }
        }
    }
                
    /**
     * Logs a list of file names to the message log
     * @param fileNames java.util.Vector file names to be logged
     * @param type java.lang.String file type
     */
    protected void logFiles(Vector fileNames, String fileType) {
        log(  fileType + " files found for import:", org.apache.tools.ant.Project.MSG_VERBOSE);
        for ( Enumeration e = fileNames.elements(); e.hasMoreElements(); ) {
            log( "    " + e.nextElement(), org.apache.tools.ant.Project.MSG_VERBOSE );
        }
    } 
}
