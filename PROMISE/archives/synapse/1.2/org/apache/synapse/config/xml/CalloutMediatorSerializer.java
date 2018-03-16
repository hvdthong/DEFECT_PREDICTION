package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.builtin.CalloutMediator;

/**
 * <pre>
 * &lt;callout serviceURL="string" [action="string"]&gt;
 *      &lt;configuration [axis2xml="string"] [repository="string"]/&gt;?
 *      &lt;source xpath="expression" | key="string"&gt;
 *      &lt;target xpath="expression" | key="string"/&gt;
 * &lt;/callout&gt;
 * </pre>
 */
public class CalloutMediatorSerializer extends AbstractMediatorSerializer {

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof CalloutMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        CalloutMediator mediator = (CalloutMediator) m;
        OMElement callout = fac.createOMElement("callout", synNS);
        saveTracingState(callout, mediator);

        callout.addAttribute(fac.createOMAttribute("serviceURL", nullNS, mediator.getServiceURL()));
        if (mediator.getAction() != null) {
            callout.addAttribute(fac.createOMAttribute("action", nullNS, mediator.getAction()));
        }

        if (mediator.getClientRepository() != null || mediator.getAxis2xml() != null) {
            OMElement config = fac.createOMElement("configuration", synNS);
            if (mediator.getClientRepository() != null) {
                config.addAttribute(fac.createOMAttribute(
                        "repository", nullNS, mediator.getClientRepository()));
            }
            if (mediator.getAxis2xml() != null) {
                config.addAttribute(fac.createOMAttribute(
                        "axis2xml", nullNS, mediator.getAxis2xml()));
            }
            callout.addChild(config);
        }

        OMElement source = fac.createOMElement("source", synNS, callout);
        if (mediator.getRequestXPath() != null) {
            SynapseXPathSerializer.serializeXPath(mediator.getRequestXPath(), source, "xpath");
        } else if (mediator.getRequestKey() != null) {
            source.addAttribute(fac.createOMAttribute(
                "key", nullNS, mediator.getRequestKey()));
        }

        OMElement target = fac.createOMElement("target", synNS, callout);
        if (mediator.getTargetXPath() != null) {
            SynapseXPathSerializer.serializeXPath(mediator.getTargetXPath(), target, "xpath");
        } else if (mediator.getTargetKey() != null) {
            target.addAttribute(fac.createOMAttribute(
                "key", nullNS, mediator.getTargetKey()));
        }

        if (parent != null) {
            parent.addChild(callout);
        }
        return callout;
    }

    public String getMediatorClassName() {
        return CalloutMediator.class.getName();
    }
}
