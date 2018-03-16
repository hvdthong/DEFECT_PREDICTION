package org.apache.ivy.core.cache;

import org.apache.ivy.plugins.namespace.Namespace;

public class CacheMetadataOptions extends CacheDownloadOptions {
    private boolean validate = false;
    private Namespace namespace = Namespace.SYSTEM_NAMESPACE;
    private Boolean isCheckmodified = null;
    private String changingMatcherName = null;
    private String changingPattern = null;
    
    public Namespace getNamespace() {
        return namespace;
    }
    public CacheMetadataOptions setNamespace(Namespace namespace) {
        this.namespace = namespace;
        return this;
    }
    public boolean isValidate() {
        return validate;
    }
    public CacheMetadataOptions setValidate(boolean validate) {
        this.validate = validate;
        return this;
    }
    public Boolean isCheckmodified() {
        return isCheckmodified;
    }
    public CacheMetadataOptions setCheckmodified(Boolean isCheckmodified) {
        this.isCheckmodified = isCheckmodified;
        return this;
    }
    public String getChangingMatcherName() {
        return changingMatcherName;
    }
    public CacheMetadataOptions setChangingMatcherName(String changingMatcherName) {
        this.changingMatcherName = changingMatcherName;
        return this;
    }
    public String getChangingPattern() {
        return changingPattern;
    }
    public CacheMetadataOptions setChangingPattern(String changingPattern) {
        this.changingPattern  = changingPattern;
        return this;
    }
}
