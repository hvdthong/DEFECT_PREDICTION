package org.apache.ivy.plugins.parser.m2;

public class DefaultPomDependencyMgt implements PomDependencyMgt {
    private String groupId;
    private String artifactId;
    private String version;
    private String scope;
    
    public DefaultPomDependencyMgt(
            String groupId, String artifactId, String version, String scope) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.scope = scope;
    }
    
    public String getScope() {
        return scope;
    }

    public String getGroupId() {
        return groupId;
    }
    public String getArtifactId() {
        return artifactId;
    }
    public String getVersion() {
        return version;
    }
    
    
}
