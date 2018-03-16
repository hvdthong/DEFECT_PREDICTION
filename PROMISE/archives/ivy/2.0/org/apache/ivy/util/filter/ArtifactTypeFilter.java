package org.apache.ivy.util.filter;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.ivy.core.module.descriptor.Artifact;

public class ArtifactTypeFilter implements Filter {
    private Collection acceptedTypes;

    public ArtifactTypeFilter(Collection acceptedTypes) {
        this.acceptedTypes = new ArrayList(acceptedTypes);
    }

    public boolean accept(Object o) {
        if (!(o instanceof Artifact)) {
            return false;
        }
        Artifact art = (Artifact) o;
        return acceptedTypes.contains(art.getType());
    }
}
