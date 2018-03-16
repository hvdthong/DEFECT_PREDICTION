package org.apache.ivy.plugins.namespace;

public class NamespaceRule {
    private String name;

    private String description;

    private MRIDTransformationRule fromSystem;

    private MRIDTransformationRule toSystem;

    public MRIDTransformationRule getFromSystem() {
        return fromSystem;
    }

    public void addFromsystem(MRIDTransformationRule fromSystem) {
        if (this.fromSystem != null) {
            throw new IllegalArgumentException("only one fromsystem is allowed per rule");
        }
        this.fromSystem = fromSystem;
    }

    public MRIDTransformationRule getToSystem() {
        return toSystem;
    }

    public void addTosystem(MRIDTransformationRule toSystem) {
        if (this.toSystem != null) {
            throw new IllegalArgumentException("only one tosystem is allowed per rule");
        }
        this.toSystem = toSystem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
