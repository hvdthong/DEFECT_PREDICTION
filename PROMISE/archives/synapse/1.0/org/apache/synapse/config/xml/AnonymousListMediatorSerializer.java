package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is Serializer  for serialization of an anonymous list mediator(an unnamed list of mediators )
 */

public abstract class AnonymousListMediatorSerializer extends AbstractListMediatorSerializer {

    private static final Log log = LogFactory.getLog(AnonymousListMediatorSerializer.class);

    /**
     * To serialize an  anonymous list mediator
     *
     * @param parent
     * @param m
     * @return OMElement
     */
    public static OMElement serializeAnonymousListMediator(OMElement parent, Mediator m) {
        if (!(m instanceof AnonymousListMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }
        AnonymousListMediator mediator = (AnonymousListMediator) m;
        serializeChildren(parent, mediator.getList());
        return parent;
    }

    private static void handleException(String msg) {
        log.error(msg);
        throw new SynapseException(msg);
    }
}
