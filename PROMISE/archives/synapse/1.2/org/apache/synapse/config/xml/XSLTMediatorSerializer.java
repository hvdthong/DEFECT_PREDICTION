package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.transform.XSLTMediator;
import org.apache.synapse.mediators.MediatorProperty;
import org.apache.synapse.config.xml.AbstractMediatorSerializer;

import java.util.List;

/**
 * Serializer for {@link XSLTMediator} instances.
 * 
 * @see XSLTMediatorFactory
 */
public class XSLTMediatorSerializer extends AbstractMediatorSerializer {

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof XSLTMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        XSLTMediator mediator = (XSLTMediator) m;
        OMElement xslt = fac.createOMElement("xslt", synNS);

        if (mediator.getXsltKey() != null) {
            xslt.addAttribute(fac.createOMAttribute(
                "key", nullNS, mediator.getXsltKey()));
        } else {
            handleException("Invalid XSLT mediator. XSLT registry key is required");
        }
        saveTracingState(xslt,mediator);

        if (mediator.getSource() != null &&
            !XSLTMediator.DEFAULT_XPATH.equals(mediator.getSource().toString())) {

            SynapseXPathSerializer.serializeXPath(mediator.getSource(), xslt, "source");
        }
        if (mediator.getTargetPropertyName() != null) {
            xslt.addAttribute(fac.createOMAttribute(
                "target", nullNS, mediator.getTargetPropertyName()));
        }
        serializeProperties(xslt, mediator.getProperties());
        List<MediatorProperty> features = mediator.getFeatures();
        if (!features.isEmpty()) {
            for (MediatorProperty mp : features) {
                OMElement prop = fac.createOMElement("feature", synNS, xslt);
                if (mp.getName() != null) {
                    prop.addAttribute(fac.createOMAttribute("name", nullNS, mp.getName()));
                } else {
                    handleException("The Feature name is missing");
                }
                if (mp.getValue() != null) {
                    prop.addAttribute(fac.createOMAttribute("value", nullNS, mp.getValue()));
                }  else {
                    handleException("The Feature value is missing");
                }
            }
        }
        if (parent != null) {
            parent.addChild(xslt);
        }
        return xslt;
    }

    public String getMediatorClassName() {
        return XSLTMediator.class.getName();
    }
}
