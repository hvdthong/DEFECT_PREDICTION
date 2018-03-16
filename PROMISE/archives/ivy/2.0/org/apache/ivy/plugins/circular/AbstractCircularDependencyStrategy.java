package org.apache.ivy.plugins.circular;

public abstract class AbstractCircularDependencyStrategy implements CircularDependencyStrategy {
    private String name;

    protected AbstractCircularDependencyStrategy(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return getName();
    }
}
