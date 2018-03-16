package org.apache.synapse.config.xml;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.config.SynapseConfiguration;

/**
 * This interface defines the configuration factories of Synapse
 */
public interface ConfigurationFactory {

    /**
     * Get the tag QName of the element piece that will be
     * build using the factory
     *
     * @return QName describing the element
     */
    QName getTagQName();

    /**
     * Get (basically builds) the configuration of Synapse built up from
     * an OMElement using the defined factory
     *
     * @param element OMElement describing the configuration to be build
     * @return SynapseConfiguration build using the relevant factory
     */
    SynapseConfiguration getConfiguration(OMElement element);

    /**
     * Get the class which serializes the specified element
     *
     * @return Class defining the Serializer
     */
    Class getSerializerClass();
}
