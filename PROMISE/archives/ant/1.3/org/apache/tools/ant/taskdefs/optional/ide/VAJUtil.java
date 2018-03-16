package org.apache.tools.ant.taskdefs.optional.ide;


import com.ibm.ivj.util.base.IvjException;
import com.ibm.ivj.util.base.ToolEnv;
import com.ibm.ivj.util.base.Workspace;
import org.apache.tools.ant.BuildException;

/**
 * Helper class for VAJ tasks. Holds Workspace singleton and
 * wraps IvjExceptions into BuildExceptions
 *
 * @author Wolf Siberski, TUI Infotec GmbH
 */
class VAJUtil {
    static private Workspace workspace;
        
    /**
     * Wraps IvjException into a BuildException
     *
     * @return org.apache.tools.ant.BuildException
     * @param errMsg Additional error message
     * @param e IvjException which is wrapped
     */
    public static BuildException createBuildException(
                                                      String errMsg, 
                                                      IvjException e) {
        errMsg = errMsg + "\n" + e.getMessage();
        String[] errors = e.getErrors();
        if (errors != null) {
            for (int i = 0; i < errors.length; i++) {
                errMsg = errMsg + "\n" + errors[i];
            }
        }
        return new BuildException(errMsg);
    }
        
    /**
     * Insert the method's description here.
     * Creation date: (19.09.2000 13:41:21)
     * @return com.ibm.ivj.util.base.Workspace
     */
    public static Workspace getWorkspace() {
        if (workspace == null) {
            workspace = ToolEnv.connectToWorkspace();
            if (workspace == null) {
                throw new BuildException(
                                         "Unable to connect to Workspace! "
                                         + "Make sure you are running in VisualAge for Java."); 
            }
        }

        return workspace;
    }
}
