package org.apache.camel.component.cxf.phase;


import java.util.SortedSet;
import org.apache.cxf.common.util.SortedArraySet;
import org.apache.cxf.phase.Phase;

public class RawMessagePhaseManagerImpl extends AbstractPhaseManagerImpl {

    protected SortedSet<Phase> createInPhases() {
        SortedSet<Phase> inPhases = new SortedArraySet<Phase>();

        int i = 0;
        
        inPhases.add(new Phase(Phase.RECEIVE, ++i * 1000));
        
        return inPhases;
    }

    protected SortedSet<Phase> createOutPhases() {
        SortedSet<Phase> outPhases = new SortedArraySet<Phase>();

        int i = 0;
        outPhases.add(new Phase(Phase.PREPARE_SEND, ++i * 1000));
        outPhases.add(new Phase(Phase.WRITE, ++i * 1000));
        outPhases.add(new Phase(Phase.SEND, ++i * 1000));
        outPhases.add(new Phase(Phase.PREPARE_SEND_ENDING, ++i * 1000));
        
        return outPhases;
    }
}

