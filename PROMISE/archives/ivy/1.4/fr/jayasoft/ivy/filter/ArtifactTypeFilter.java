package fr.jayasoft.ivy.filter;

import java.util.ArrayList;
import java.util.Collection;

import fr.jayasoft.ivy.Artifact;

public class ArtifactTypeFilter implements Filter {
    private Collection _acceptedTypes;
    
    public ArtifactTypeFilter(Collection acceptedTypes) {
        _acceptedTypes = new ArrayList(acceptedTypes);
    }
    
    public boolean accept(Object o) {
        if (! (o instanceof Artifact)) {
            return false;
        }
        Artifact art = (Artifact)o;
        return _acceptedTypes.contains(art.getType());
    }
}
