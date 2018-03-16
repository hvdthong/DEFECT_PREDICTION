package org.apache.synapse.config.xml;

import java.io.InputStream;

import org.apache.axiom.om.*;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.config.SynapseConfiguration;

import javax.xml.stream.XMLStreamException;

/**
 * Builds a Synapse Configuration from an XML input stream
 */
public class XMLConfigurationBuilder {

    private static Log log = LogFactory.getLog(XMLConfigurationBuilder.class);

    public static SynapseConfiguration getConfiguration(InputStream is) throws XMLStreamException {

        log.info("Generating the Synapse configuration model by parsing the XML configuration");
        
        OMElement definitions = new StAXOMBuilder(is).getDocumentElement();
        definitions.build();

        return ConfigurationFactoryAndSerializerFinder.getInstance().getConfiguration(definitions);
        
    }
}
