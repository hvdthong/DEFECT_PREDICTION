package org.apache.camel.component.cxf.phase;

import java.util.List;
import java.util.SortedSet;

import org.apache.cxf.phase.Phase;
import org.apache.cxf.phase.PhaseManager;

public abstract class AbstractPhaseManagerImpl implements PhaseManager {
    private SortedSet<Phase> inPhases;
    private SortedSet<Phase> outPhases;
    
    public AbstractPhaseManagerImpl() {
        inPhases = createInPhases();
        outPhases = createOutPhases();
    }
    
    public SortedSet<Phase> getInPhases() {
        return inPhases;
    }

    public SortedSet<Phase> getOutPhases() {
        return outPhases;
    }

    protected abstract SortedSet<Phase> createInPhases();
    
    protected abstract SortedSet<Phase> createOutPhases();
}
