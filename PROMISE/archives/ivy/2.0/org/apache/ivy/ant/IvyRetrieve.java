package org.apache.ivy.ant;

import java.util.Arrays;
import java.util.Collection;

import org.apache.ivy.core.LogOptions;
import org.apache.ivy.core.retrieve.RetrieveOptions;
import org.apache.ivy.util.filter.Filter;
import org.apache.tools.ant.BuildException;

/**
 * This task allow to retrieve dependencies from the cache to a local directory like a lib dir.
 */
public class IvyRetrieve extends IvyPostResolveTask {
    private String pattern;

    private String ivypattern = null;

    private boolean sync = false;

    private boolean symlink = false;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void doExecute() throws BuildException {
        prepareAndCheck();

        if (!getAllowedLogOptions().contains(getLog())) {
            throw new BuildException("invalid option for 'log': " + getLog() 
                + ". Available options are " + getAllowedLogOptions());
        }

        pattern = getProperty(pattern, getSettings(), "ivy.retrieve.pattern");
        try {
            Filter artifactFilter = getArtifactFilter();
            int targetsCopied = getIvyInstance().retrieve(
                getResolvedMrid(),
                pattern,
                ((RetrieveOptions) new RetrieveOptions()
                    .setLog(getLog()))
                    .setConfs(splitConfs(getConf()))
                    .setDestIvyPattern(ivypattern)
                    .setArtifactFilter(artifactFilter)
                    .setSync(sync)
                    .setUseOrigin(isUseOrigin())
                    .setMakeSymlinks(symlink)
                    .setResolveId(getResolveId()));
            boolean haveTargetsBeenCopied = targetsCopied > 0;
            getProject().setProperty("ivy.nb.targets.copied", String.valueOf(targetsCopied));
            getProject().setProperty("ivy.targets.copied", String.valueOf(haveTargetsBeenCopied));
        } catch (Exception ex) {
            throw new BuildException("impossible to ivy retrieve: " + ex, ex);
        }
    }

    protected Collection/*<String>*/ getAllowedLogOptions() {
        return Arrays.asList(new String [] {
                LogOptions.LOG_DEFAULT, LogOptions.LOG_DOWNLOAD_ONLY, LogOptions.LOG_QUIET});
    }

    public String getIvypattern() {
        return ivypattern;
    }

    public void setIvypattern(String ivypattern) {
        this.ivypattern = ivypattern;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    /**
     * Option to create symlinks instead of copying.
     */
    public void setSymlink(boolean symlink) {
        this.symlink = symlink;
    }
}
