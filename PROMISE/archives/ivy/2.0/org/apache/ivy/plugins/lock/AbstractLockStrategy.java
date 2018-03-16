package org.apache.ivy.plugins.lock;

public abstract class AbstractLockStrategy implements LockStrategy {
    private String name;
    
    private boolean debugLocking = false;
    
    protected AbstractLockStrategy() {
    }

    protected AbstractLockStrategy(boolean debugLocking) {
        this.debugLocking = debugLocking;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String toString() {
        return name;
    }
    public boolean isDebugLocking() {
        return debugLocking;
    }
}
