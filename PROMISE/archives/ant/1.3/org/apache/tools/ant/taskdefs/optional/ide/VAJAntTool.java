import com.ibm.ivj.util.base.Project;
import com.ibm.ivj.util.base.ToolData;
import com.ibm.ivj.util.base.IvjException;
import org.apache.tools.ant.BuildException;

/**
 * This class is the equivalent to org.apache.tools.ant.Main for the
 * VAJ tool environment. It's main is called when the user selects
 * Tools->Ant Build from the VAJ project menu.
 * Additionally this class provides methods to save build info for
 * a project in the repository and load it from the repository
 *
 * @author: Wolf Siberski
 */
public class VAJAntTool {
	private static final String TOOL_DATA_KEY = "AntTool";
/**
 * Loads the BuildInfo for the specified VAJ project from the
 * tool data for this project.
 * If there is no build info stored for that project, a new
 * default BuildInfo is returned
 * 
 * @return BuildInfo buildInfo build info for the specified project
 * @param projectName String project name
 */
public static VAJBuildInfo loadBuildData(String projectName) {
	VAJBuildInfo result = null;
	try {
		Project project = VAJUtil.getWorkspace().loadedProjectNamed( projectName );
		if ( project.testToolRepositoryData(TOOL_DATA_KEY) ) {
			ToolData td = project.getToolRepositoryData(TOOL_DATA_KEY);
			String data = (String)td.getData();
			result = VAJBuildInfo.parse( data );
		} else {
			result = new VAJBuildInfo();
		}
		result.setVAJProjectName( projectName );
	} catch (Throwable t) {
		System.out.println("BuildInfo for Project " + projectName + 
			" could not be loaded" + t);
		throw new BuildException(t);
	}
	return result;
}
/**
 * Starts the application.
 * @param args an array of command-line arguments
 */
public static void main(java.lang.String[] args) {
	VAJBuildInfo info;
	if ( args.length >= 2 && args[1] instanceof String ) {
		String projectName = (String)args[1];
		info = loadBuildData( projectName );
	} 
	else {
		info = new VAJBuildInfo();
	}
	
	VAJAntToolGUI mainFrame = new VAJAntToolGUI( info );
	mainFrame.show();
}
/**
 * Saves the BuildInfo for a project in the VAJ repository.
 * @param info BuildInfo build info to save
 */
public static void saveBuildData(VAJBuildInfo info) {
	String data = info.asDataString();
	try {
		ToolData td = new ToolData( TOOL_DATA_KEY, data );
		VAJUtil.getWorkspace().loadedProjectNamed( info.getVAJProjectName() ).setToolRepositoryData( td );
	} catch (Throwable t) {
		throw new BuildException("BuildInfo for Project " + info.getVAJProjectName() + 
			" could not be saved", t);
	}
}
}
