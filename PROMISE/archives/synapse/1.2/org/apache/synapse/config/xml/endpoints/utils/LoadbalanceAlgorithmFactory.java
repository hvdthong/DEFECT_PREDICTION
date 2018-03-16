package org.apache.synapse.config.xml.endpoints.utils;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMAttribute;
import org.apache.synapse.endpoints.algorithms.LoadbalanceAlgorithm;
import org.apache.synapse.endpoints.algorithms.RoundRobin;
import org.apache.synapse.config.xml.XMLConfigConstants;

import javax.xml.namespace.QName;
import java.util.ArrayList;

/**
 * Factory of all load balance algorithms. ESBSendMediatorFactroy will use this to create the
 * appropriate algorithm implementation.
 */
public class LoadbalanceAlgorithmFactory {

    public static LoadbalanceAlgorithm createLoadbalanceAlgorithm(OMElement loadbalanceElement, ArrayList endpoints) {

        LoadbalanceAlgorithm algorithm = null;

        String algorithmName = "roundRobin";
        OMAttribute algoAttribute = loadbalanceElement.getAttribute(new QName(null, XMLConfigConstants.ALGORITHM_NAME));
        if(algoAttribute != null) {
            algorithmName = algoAttribute.getAttributeValue();
        }

        if(algorithmName.equalsIgnoreCase("roundRobin")) {
                algorithm = new RoundRobin(endpoints);
        }

        return algorithm;
    }
}
