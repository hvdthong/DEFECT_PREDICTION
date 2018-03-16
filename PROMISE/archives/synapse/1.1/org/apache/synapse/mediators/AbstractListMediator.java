package org.apache.synapse.mediators;

import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.Mediator;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.SynapseEnvironment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is the base class for all List mediators
 *
 * @see ListMediator
 */
public abstract class AbstractListMediator extends AbstractMediator
    implements ListMediator, ManagedLifecycle {

    /** the list of child mediators held. These are executed sequentially */
    protected List mediators = new ArrayList();

    public boolean mediate(MessageContext synCtx) {

        int parentsEffectiveTraceState = synCtx.getTracingState();
        setEffectiveTraceState(synCtx);
        int myEffectiveTraceState = synCtx.getTracingState();

        try {
            if (isTraceOrDebugOn(isTraceOn(synCtx))) {
                traceOrDebug(isTraceOn(synCtx), "Sequence <" + getType() + "> :: mediate()");
            }

            for (Iterator it = mediators.iterator(); it.hasNext();) {
                Mediator m = (Mediator) it.next();

                synCtx.setTracingState(myEffectiveTraceState);
                if (!m.mediate(synCtx)) {
                    return false;
                }
            }
        } finally {
            synCtx.setTracingState(parentsEffectiveTraceState);
        }
        return true;
    }

    public List getList() {
        return mediators;
    }

    public boolean addChild(Mediator m) {
        return mediators.add(m);
    }

    public boolean addAll(List c) {
        return mediators.addAll(c);
    }

    public Mediator getChild(int pos) {
        return (Mediator) mediators.get(pos);
    }

    public boolean removeChild(Mediator m) {
        return mediators.remove(m);
    }

    public Mediator removeChild(int pos) {
        return (Mediator) mediators.remove(pos);
    }

    /**
     * Initialize child mediators recursively
     * @param se synapse environment
     */
    public void init(SynapseEnvironment se) {
        if (log.isDebugEnabled()) {
            log.debug("Initializing child mediators");
        }

        for (Iterator it = mediators.iterator(); it.hasNext();) {
            Mediator m = (Mediator) it.next();

            if (m instanceof ManagedLifecycle) {
            	((ManagedLifecycle) m).init(se);
            }
        } 
    }

    /**
     * Destroy child mediators recursively
     */
    public void destroy() {
        if (log.isDebugEnabled()) {
            log.debug("Destroying child mediators");
        }

        for (Iterator it = mediators.iterator(); it.hasNext();) {
            Mediator m = (Mediator) it.next();

            if (m instanceof ManagedLifecycle) {
            	((ManagedLifecycle) m).destroy();
            }
        } 
    }
}
