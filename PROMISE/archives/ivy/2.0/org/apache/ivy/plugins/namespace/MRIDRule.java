package org.apache.ivy.plugins.namespace;

public class MRIDRule {
    private String org;

    private String module;

    private String branch;

    private String rev;

    public MRIDRule(String org, String mod, String rev) {
        this.org = org;
        this.module = mod;
        this.rev = rev;
    }

    public MRIDRule() {
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String toString() {
        return "[ " + org + " " + module + (branch != null ? " " + branch : "") + " " + rev
                + " ]";
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
