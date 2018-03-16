package org.apache.ivy.ant;

import java.util.Iterator;

import org.apache.ivy.core.report.ArtifactDownloadReport;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;

/**
 * Creates an ant path consisting in all artifacts found during a resolve.
 */
public class IvyCachePath extends IvyCacheTask {
    private String pathid;

    private String id;

    public String getPathid() {
        return pathid;
    }

    public void setPathid(String id) {
        pathid = id;
    }

    /**
     * @deprecated use setPathid instead
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    public void doExecute() throws BuildException {
        prepareAndCheck();
        if (pathid == null) {
            if (id != null) {
                pathid = id;
                log("ID IS DEPRECATED, PLEASE USE PATHID INSTEAD", Project.MSG_WARN);
            } else {
                throw new BuildException("pathid is required in ivy classpath");
            }
        }
        try {
            Path path = new Path(getProject());
            getProject().addReference(pathid, path);
            for (Iterator iter = getArtifactReports().iterator(); iter.hasNext();) {
                ArtifactDownloadReport a = (ArtifactDownloadReport) iter.next();
                path.createPathElement().setLocation(a.getLocalFile());
            }
        } catch (Exception ex) {
            throw new BuildException("impossible to build ivy path: " + ex, ex);
        }

    }

}
