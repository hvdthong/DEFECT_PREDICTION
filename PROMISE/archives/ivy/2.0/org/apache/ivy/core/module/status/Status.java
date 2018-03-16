package org.apache.ivy.core.module.status;

public class Status {
    private String name;

    private boolean integration;

    public Status() {
    }

    public Status(String name, boolean integration) {
        this.name = name;
        this.integration = integration;
    }

    public boolean isIntegration() {
        return integration;
    }

    public void setIntegration(boolean integration) {
        this.integration = integration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
