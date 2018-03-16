package org.apache.synapse.config.xml;

import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseException;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.util.Iterator;

/**
 * This is factory for creating an anonymous list mediator(an unnamed list of mediators )
 *
 */

public abstract class AnonymousListMediatorFactory extends AbstractListMediatorFactory {

    private static final Log log = LogFactory.getLog(AnonymousListMediator.class);

    /**
     * To create an anonymous list mediator form OMElement
     * @param el
     * @return List mediator
     */
    public static AnonymousListMediator createAnonymousListMediator(OMElement el) {
        AnonymousListMediator mediator = new AnonymousListMediator();
        {
            addChildren(el, mediator);
        }
        return mediator;
    }

}
