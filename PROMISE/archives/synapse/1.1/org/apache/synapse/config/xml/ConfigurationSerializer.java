package org.apache.synapse.config.xml;

import java.io.OutputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.synapse.config.SynapseConfiguration;
import org.apache.axiom.om.OMElement;

/**
 * This interface defines the configuration serializers of Synapse
 */
public interface ConfigurationSerializer {

    /**
     * Serializes the given configuraiton to an OMElement
     *
     * @param synCfg Configuration to be serialized
     * @return OMElement describing the configuraiton
     */
    OMElement serializeConfiguration(SynapseConfiguration synCfg);

    /**
     * Get the tag QName of the element
     *
     * @return QName describing the element name
     */
    QName getTagQName();

}
