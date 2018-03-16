package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.Mediator;

import java.util.Iterator;
import java.util.List;

public abstract class AbstractListMediatorSerializer extends AbstractMediatorSerializer {

    private static final Log log = LogFactory.getLog(AbstractListMediatorSerializer.class);

    protected static void serializeChildren(OMElement parent, List list)
    {
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            Mediator child = (Mediator) iter.next();
            MediatorSerializer medSer = MediatorSerializerFinder.getInstance().getSerializer(child);
            if (medSer != null) {
                medSer.serializeMediator(parent, child);
            } else {
                handleException("Unable to find a serializer for mediator : " + child.getType());
            }
        }
    }

    private static void handleException(String msg) {
        log.error(msg);
        throw new SynapseException(msg);
    }

}
