package org.apache.ivy.core.module.id;

import java.util.Map;

import org.apache.ivy.core.IvyPatternHelper;
import org.apache.ivy.util.extendable.UnmodifiableExtendableItem;

/**
 * Identifies an artifact in a particular module revision
 * 
 * @see <a href="package-summary.html">org.apache.ivy.core.module.id</a>
 */
public class ArtifactRevisionId extends UnmodifiableExtendableItem {
    public static ArtifactRevisionId newInstance(ModuleRevisionId mrid, String name, String type,
            String ext) {
        return newInstance(mrid, name, type, ext, null);
    }

    public static ArtifactRevisionId newInstance(ModuleRevisionId mrid, String name, String type,
            String ext, Map extraAttributes) {
        return new ArtifactRevisionId(new ArtifactId(mrid.getModuleId(), name, type, ext), mrid,
            extraAttributes);
    }

    private ArtifactId artifactId;

    private ModuleRevisionId mrid;

    public ArtifactRevisionId(ArtifactId artifactId, ModuleRevisionId mrid) {
        this(artifactId, mrid, null);
    }

    public ArtifactRevisionId(ArtifactId artfId, ModuleRevisionId mdlRevId, Map extraAttributes) {
        super(null, extraAttributes);
        artifactId = artfId;
        mrid = mdlRevId;

        setStandardAttribute(IvyPatternHelper.ORGANISATION_KEY, getModuleRevisionId()
                .getOrganisation());
        setStandardAttribute(IvyPatternHelper.MODULE_KEY, getModuleRevisionId().getName());
        setStandardAttribute(IvyPatternHelper.REVISION_KEY, getModuleRevisionId().getRevision());
        setStandardAttribute(IvyPatternHelper.ARTIFACT_KEY, getName());
        setStandardAttribute(IvyPatternHelper.TYPE_KEY, getType());
        setStandardAttribute(IvyPatternHelper.EXT_KEY, getExt());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ArtifactRevisionId)) {
            return false;
        }
        ArtifactRevisionId arid = (ArtifactRevisionId) obj;
        return getArtifactId().equals(arid.getArtifactId())
                && getModuleRevisionId().equals(arid.getModuleRevisionId())
                && getQualifiedExtraAttributes().equals(arid.getQualifiedExtraAttributes());
    }

    public int hashCode() {
        int hash = 17;
        hash += getArtifactId().hashCode() * 37;
        hash += getModuleRevisionId().hashCode() * 37;
        hash += getQualifiedExtraAttributes().hashCode() * 37;
        
        return hash;
    }

    public String toString() {
        return getModuleRevisionId()
                + "!" + getName() + "." + getExt() 
                + (getType().equals(getExt()) ? "" : "(" + getType() + ")");
    }

    /**
     * @return Returns the artifactId.
     */
    public ArtifactId getArtifactId() {
        return artifactId;
    }

    public ModuleRevisionId getModuleRevisionId() {
        return mrid;
    }

    public String getName() {
        return artifactId.getName();
    }

    public String getType() {
        return artifactId.getType();
    }

    public String getExt() {
        return artifactId.getExt();
    }

    /**
     * @return Returns the revision.
     */
    public String getRevision() {
        return mrid.getRevision();
    }

}
