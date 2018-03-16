package org.apache.ivy.ant;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.ivy.core.report.ArtifactDownloadReport;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet.NameEntry;

/**
 * Creates an ant fileset consisting in all artifacts found during a resolve. Note that this task is
 * not compatible with the useOrigin mode.
 */
public class IvyCacheFileset extends IvyCacheTask {
    private String setid;

    public String getSetid() {
        return setid;
    }

    public void setSetid(String id) {
        setid = id;
    }

    public void setUseOrigin(boolean useOrigin) {
        if (useOrigin) {
            throw new UnsupportedOperationException(
                    "the cachefileset task does not support the useOrigin mode, since filesets "
                    + "require to have only one root directory. Please use the the cachepath "
                    + "task instead");
        }
    }

    public void doExecute() throws BuildException {
        prepareAndCheck();
        if (setid == null) {
            throw new BuildException("setid is required in ivy cachefileset");
        }
        try {
            FileSet fileset = new FileSet();
            fileset.setProject(getProject());
            getProject().addReference(setid, fileset);

            List paths = getArtifactReports();
            File base = null;
            for (Iterator iter = paths.iterator(); iter.hasNext();) {
                ArtifactDownloadReport a = (ArtifactDownloadReport) iter.next();
                if (a.getLocalFile() != null) {
                    base = getBaseDir(base, a.getLocalFile());
                }
            }
            if (base == null) {
                fileset.setDir(new File("."));
                NameEntry ne = fileset.createExclude();
                ne.setName("**/*");
            } else {
                fileset.setDir(base);
                for (Iterator iter = paths.iterator(); iter.hasNext();) {
                    ArtifactDownloadReport a = (ArtifactDownloadReport) iter.next();
                    if (a.getLocalFile() != null) {
                        NameEntry ne = fileset.createInclude();
                        ne.setName(getPath(base, a.getLocalFile()));
                    }
                }
            }
        } catch (Exception ex) {
            throw new BuildException("impossible to build ivy cache fileset: " + ex, ex);
        }
    }

    /**
     * Returns the path of the file relative to the given base directory.
     * 
     * @param base the parent directory to which the file must be evaluated.
     * @param file the file for which the path should be returned
     * @return the path of the file relative to the given base directory.
     */
    private String getPath(File base, File file) {
        String absoluteBasePath = base.getAbsolutePath();
        
        int beginIndex = absoluteBasePath.length();
        
        if (!absoluteBasePath.endsWith(File.separator)) {
        }
        
        return file.getAbsolutePath().substring(beginIndex);
    }

    /**
     * Returns the common base directory between a current base directory and a given file.
     * <p>
     * The returned base directory must be a parent of both the current base and the given file.
     * </p>
     * 
     * @param base
     *            the current base directory, may be null.
     * @param file
     *            the file for which the new base directory should be returned.
     * @return the common base directory between a current base directory and a given file.
     */
    File getBaseDir(File base, File file) {
        if (base == null) {
            return file.getParentFile().getAbsoluteFile();
        } else {
            Iterator bases = getParents(base).iterator();
            Iterator fileParents = getParents(file.getAbsoluteFile()).iterator();
            File result = null;
            while (bases.hasNext() && fileParents.hasNext()) {
                File next = (File) bases.next();
                if (next.equals(fileParents.next())) {
                    result = next; 
                } else {
                    break;
                }
            }
            return result;
        }
    }

    /**
     * @return a list of files, starting with the root and ending with the file itself
     */
    private LinkedList/*<File>*/ getParents(File file) {
        LinkedList r = new LinkedList();
        while (file != null) {
            r.addFirst(file);
            file = file.getParentFile();
        }
        return r;
    }

}
