package org.apache.ivy.core.module.descriptor;

import java.util.Map;

/**
 *
 */
public abstract class AbstractArtifact implements Artifact {
    public AbstractArtifact() {
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Artifact)) {
            return false;
        }
        Artifact art = (Artifact) obj;
        return getModuleRevisionId().equals(art.getModuleRevisionId())
                && getPublicationDate() == null ? true : getPublicationDate().equals(
            art.getPublicationDate())
                && getName().equals(art.getName())
                && getExt().equals(art.getExt())
                && getType().equals(art.getType())
                && getQualifiedExtraAttributes().equals(art.getQualifiedExtraAttributes());
    }

    public int hashCode() {
        int hash = 33;
        hash = hash * 17 + getModuleRevisionId().hashCode();
        if (getPublicationDate() != null) {
            hash = hash * 17 + getPublicationDate().hashCode();
        }
        hash = hash * 17 + getName().hashCode();
        hash = hash * 17 + getExt().hashCode();
        hash = hash * 17 + getType().hashCode();
        hash = hash * 17 + getQualifiedExtraAttributes().hashCode();
        return hash;
    }

    public String toString() {
        return String.valueOf(getId());
    }

    public String getAttribute(String attName) {
        return getId().getAttribute(attName);
    }

    public Map getAttributes() {
        return getId().getAttributes();
    }

    public String getExtraAttribute(String attName) {
        return getId().getExtraAttribute(attName);
    }

    public Map getExtraAttributes() {
        return getId().getExtraAttributes();
    }
    
    public Map getQualifiedExtraAttributes() {
        return getId().getQualifiedExtraAttributes();
    }


}
