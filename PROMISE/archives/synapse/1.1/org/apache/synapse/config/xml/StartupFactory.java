package org.apache.synapse.config.xml;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Startup;

/**
 * Defines the factories which builds startups
 */
public interface StartupFactory {

    /**
     * Create (build from OM) from the specified OMElement
     *
     * @param elem
     *          OMELement describing the Startup
     * @return Startup build from the given element
     */
    public Startup createStartup(OMElement elem);

    /**
     * Get the tag QName of the element
     *
     * @return QName of the element
     */
    public QName getTagQName();

    /**
     * Get the Serializer class for this factory
     *
     * @return Class defining the serialization of the startup
     */
    public Class getSerializerClass();
}
