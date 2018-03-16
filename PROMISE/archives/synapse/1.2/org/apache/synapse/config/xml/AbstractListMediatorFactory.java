package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.mediators.ListMediator;
import org.apache.synapse.Mediator;

import javax.xml.namespace.QName;
import java.util.Iterator;

/**
 * This implements the basic logic to build a list mediator from a given XML
 * configuration. It recursively builds the child mediators of the list.
 */
public abstract class AbstractListMediatorFactory extends AbstractMediatorFactory {

    protected static void addChildren(OMElement el, ListMediator m) {
        Iterator it = el.getChildElements();
        while (it.hasNext()) {
            OMElement child = (OMElement) it.next();
            Mediator med = MediatorFactoryFinder.getInstance().getMediator(child);
            if (med != null) {
                m.addChild(med);
            } else {
                String msg = "Unknown mediator : " + child.getLocalName();
                log.error(msg);
                throw new SynapseException(msg);
            }
        }
    }
}
