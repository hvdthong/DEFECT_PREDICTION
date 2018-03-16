package org.apache.ivy.core.install;

import org.apache.ivy.plugins.matcher.PatternMatcher;
import org.apache.ivy.util.filter.Filter;
import org.apache.ivy.util.filter.FilterHelper;

public class InstallOptions {
    private boolean transitive = true;
    private boolean validate = true;
    private boolean overwrite = false;
    private Filter artifactFilter = FilterHelper.NO_FILTER;
    private String matcherName = PatternMatcher.EXACT;
    
    public boolean isTransitive() {
        return transitive;
    }
    public InstallOptions setTransitive(boolean transitive) {
        this.transitive = transitive;
        return this;
    }
    public boolean isValidate() {
        return validate;
    }
    public InstallOptions setValidate(boolean validate) {
        this.validate = validate;
        return this;
    }
    public boolean isOverwrite() {
        return overwrite;
    }
    public InstallOptions setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
        return this;
    }
    public Filter getArtifactFilter() {
        return artifactFilter;
    }
    public InstallOptions setArtifactFilter(Filter artifactFilter) {
        this.artifactFilter = artifactFilter == null ? FilterHelper.NO_FILTER : artifactFilter;
        return this;
    }
    public String getMatcherName() {
        return matcherName;
    }
    public InstallOptions setMatcherName(String matcherName) {
        this.matcherName = matcherName;
        return this;
    }
}
