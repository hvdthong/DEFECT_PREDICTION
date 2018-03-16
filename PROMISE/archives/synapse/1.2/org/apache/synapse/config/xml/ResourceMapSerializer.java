package org.apache.synapse.config.xml;

import java.util.Map;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.core.axis2.ResourceMap;

/**
 * Creates a sequence of <tt>&lt;resource&gt;</tt> elements from a ResourceMap object:
 * <pre>
 * &lt;resource location="..." key="..."/&gt;*
 * </pre>
 */
public class ResourceMapSerializer {
    private static final OMFactory fac = OMAbstractFactory.getOMFactory();
    
    public static void serializeResourceMap(OMElement parent, ResourceMap resourceMap) {
        if (resourceMap != null) {
        	for (Map.Entry<String,String> entry : resourceMap.getResources().entrySet()) {
                OMElement resource = fac.createOMElement("resource",
                    SynapseConstants.SYNAPSE_OMNAMESPACE);
                resource.addAttribute("location", (String)entry.getKey(), null);
                resource.addAttribute("key", (String)entry.getValue(), null);
                parent.addChild(resource);
            }
        }
    }
}
