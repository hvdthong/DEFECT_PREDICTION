package org.apache.synapse.config;

import org.apache.axiom.om.OMNode;

/**
 * Defines the interface which should be implemented by a mapper that could
 * convert a XML resource into a known Object such as WSDL, XSD, etc..
 */
public interface XMLToObjectMapper {

    /**
     * Create an application object from the given OMNode
     * @param om the XML
     * @return a suitable application object
     */
    public Object getObjectFromOMNode(OMNode om);
}
