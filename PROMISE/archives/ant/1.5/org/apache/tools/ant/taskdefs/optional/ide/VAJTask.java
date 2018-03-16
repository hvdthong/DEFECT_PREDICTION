package org.apache.tools.ant.taskdefs.optional.ide;

/**
 * Super class for all VAJ tasks. Contains common
 * attributes (remoteServer) and util methods
 *
 * @author: Wolf Siberski
 * @author: Martin Landers, Beck et al. projects
 */
import org.apache.tools.ant.Task;


public class VAJTask extends Task {
    /**
     * Adaption of VAJLocalUtil to Task context.
     */
    class VAJLocalToolUtil extends VAJLocalUtil {
        public void log(String msg, int level) {
            VAJTask.this.log(msg, level);
        }
    }

    protected String remoteServer = null;

    private VAJUtil util = null;

    protected boolean haltOnError = true;

    /**
     * returns the VAJUtil implementation
     */
    protected VAJUtil getUtil() {
        if (util == null) {
            if (remoteServer == null) {
                util = new VAJLocalToolUtil();
            } else {
                util = new VAJRemoteUtil(this, remoteServer);
            }
        }
        return util;
    }

    /**
     * Name and port of a remote tool server, optiona.
     * Format: &lt;servername&gt;:&lt;port no&gt;.
     * If this attribute is set, the tasks will be executed on the specified tool
     * server.
     */
    public void setRemote(String remoteServer) {
        this.remoteServer = remoteServer;
    }

    /**
    * Flag to control behaviour in case of VAJ errors.
    * If this attribute is set errors will be ignored
    * (no BuildException will be thrown) otherwise
    * VAJ errors will be wrapped into a BuildException and
    * stop the build.
    */
    public void setHaltonerror(boolean newHaltOnError) {
        haltOnError = newHaltOnError;
    }
}
