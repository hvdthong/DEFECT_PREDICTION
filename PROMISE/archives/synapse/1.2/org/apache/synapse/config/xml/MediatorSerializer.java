package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;

/**
 * Interface which should be implemented by mediator serializers. Does the
 * reverse of the MediatorFactory
 */
public interface MediatorSerializer {

    /**
     * Return the XML representation of this mediator
     * @param m mediator to be serialized
     * @param parent the OMElement to which the serialization should be attached
     * @return the serialized mediator XML
     */
    public OMElement serializeMediator(OMElement parent, Mediator m);

    /**
     * Return the class name of the mediator which can be serialized
     * @return the class name 
     */
    public String getMediatorClassName();
}
