package org.apache.ivy.plugins.parser.m2;

public interface PomDependencyMgt {

    public abstract String getGroupId();

    public abstract String getArtifactId();

    public abstract String getVersion();
    
    public abstract String getScope();

}
