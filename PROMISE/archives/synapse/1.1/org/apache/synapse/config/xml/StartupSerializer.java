package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Startup;

/**
 * Defines the serialization of Startups
 */
public interface StartupSerializer {

    /**
     * Serializes the Startup to an OMElement and
     * attaches as a child to the provided parent OMElement
     *
     * @param parent  - OMElement to which, serialized startup will be attached
     * @param startup - Startup to be serialized
     * @return Serialized OMElement
     */
    public OMElement serializeStartup(OMElement parent, Startup startup);
}
