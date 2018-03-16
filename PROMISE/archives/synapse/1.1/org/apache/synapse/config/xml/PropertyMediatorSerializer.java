package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.builtin.PropertyMediator;

/**
 * <pre>
 * &lt;property name="string" [action=set/remove] (value="literal" | expression="xpath")/&gt;
 * </pre>
 */
public class PropertyMediatorSerializer extends AbstractMediatorSerializer {

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof PropertyMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        PropertyMediator mediator = (PropertyMediator) m;
        OMElement property = fac.createOMElement("property", synNS);
        saveTracingState(property, mediator);

        if (mediator.getName() != null) {
            property.addAttribute(fac.createOMAttribute(
                    "name", nullNS, mediator.getName()));
        } else {
            handleException("Invalid property mediator. Name is required");
        }

        if (mediator.getValue() != null) {
            property.addAttribute(fac.createOMAttribute(
                    "value", nullNS, mediator.getValue()));

        } else if (mediator.getExpression() != null) {
            property.addAttribute(fac.createOMAttribute(
                    "expression", nullNS, mediator.getExpression().toString()));
            super.serializeNamespaces(property, mediator.getExpression());

        } else if (mediator.getAction() == PropertyMediator.ACTION_SET) {
            handleException("Invalid property mediator. Value or expression is required if action is SET");
        }
        if (mediator.getScope() != null) {
            property.addAttribute(fac.createOMAttribute("scope", nullNS, mediator.getScope()));
        }
        if (mediator.getAction() == PropertyMediator.ACTION_REMOVE) {
            property.addAttribute(fac.createOMAttribute(
                    "action", nullNS, "remove"));
        }
        if (parent != null) {
            parent.addChild(property);
        }
        return property;
    }

    public String getMediatorClassName() {
        return PropertyMediator.class.getName();
    }
}
