package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;

import javax.xml.namespace.QName;

/**
 * A mediator factory capable of creating an instance of a mediator through a given
 * XML should implement this interface
 */
public interface MediatorFactory {
    /**
     * Creates an instance of the mediator using the OMElement
     * @param elem
     * @return the created mediator
     */
    public Mediator createMediator(OMElement elem);

    /**
     * The QName of this mediator element in the XML config
     * @return QName of the mediator element
     */
    public QName getTagQName();
}
