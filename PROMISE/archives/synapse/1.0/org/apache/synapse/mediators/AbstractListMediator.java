package org.apache.synapse.mediators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Mediator;
import org.apache.synapse.MessageContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class implements the base functionality of a List mediator
 *
 * @see ListMediator
 */
public abstract class AbstractListMediator extends AbstractMediator implements ListMediator {

    private static final Log log = LogFactory.getLog(AbstractListMediator.class);

    protected List mediators = new ArrayList();

    public boolean mediate(MessageContext synCtx) {
        try {
            log.debug("Implicit Sequence <" + getType() + "> :: mediate()");
            saveAndSetTraceState(synCtx);
            Iterator it = mediators.iterator();            
            while (it.hasNext()) {
                Mediator m = (Mediator) it.next();
                if (!m.mediate(synCtx)) {
                    return false;
                }
            }
        }
        finally {
            restoreTracingState(synCtx);
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
}
