package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.config.SynapseConfiguration;

import javax.xml.stream.XMLStreamException;
import java.io.OutputStream;

/**
 * Serialize a SynapseConfiguration into an OutputStream
 */
public class XMLConfigurationSerializer {

    private static final Log log = LogFactory.getLog(XMLConfigurationSerializer.class);

    /**
     * Order of entries is irrelevant, however its nice to have some order.
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
