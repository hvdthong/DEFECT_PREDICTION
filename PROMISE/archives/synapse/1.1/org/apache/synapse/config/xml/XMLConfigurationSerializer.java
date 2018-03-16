package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.Mediator;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.config.Entry;
import org.apache.synapse.config.xml.endpoints.EndpointAbstractSerializer;
import org.apache.synapse.core.axis2.ProxyService;

import javax.xml.stream.XMLStreamException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

/**
 * Serialize a SynapseConfiguration into an OutputStream
 */
public class XMLConfigurationSerializer {

    private static final Log log = LogFactory.getLog(XMLConfigurationSerializer.class);

    /**
     * order of entries is irrelavant, however its nice to have some order
     * @param synCfg
     * @param outputStream
     * @throws XMLStreamException
     */
    public static void serializeConfiguration(SynapseConfiguration synCfg,
        OutputStream outputStream) throws XMLStreamException {

        log.info("Serializing the XML Configuration to the output stream");
        
        OMElement definitions
                = ConfigurationFactoryAndSerializerFinder.serializeConfiguration(synCfg);
        definitions.serialize(outputStream);
    }
    
}
