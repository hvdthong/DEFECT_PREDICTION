package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.builtin.ValidateMediator;
import org.apache.synapse.mediators.MediatorProperty;

import java.util.List;

/**
 * Serializer for {@link ValidateMediator} instances.
 * 
 * @see ValidateMediatorSerializer
 */
public class ValidateMediatorSerializer extends AbstractListMediatorSerializer
    implements MediatorSerializer {

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof ValidateMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        ValidateMediator mediator = (ValidateMediator) m;
        OMElement validate = fac.createOMElement("validate", synNS);
        saveTracingState(validate, mediator);

        if (mediator.getSource() != null) {
            SynapseXPathSerializer.serializeXPath(mediator.getSource(), validate, "source");
        }

        for (String key : mediator.getSchemaKeys()) {
            OMElement schema = fac.createOMElement("schema", synNS, validate);
            schema.addAttribute(fac.createOMAttribute("key", nullNS, key));
        }

        List<MediatorProperty> features = mediator.getFeatures();
        if (!features.isEmpty()) {
            for (MediatorProperty mp : features) {
                OMElement feature = fac.createOMElement("feature", synNS, validate);
                if (mp.getName() != null) {
                    feature.addAttribute(fac.createOMAttribute("name", nullNS, mp.getName()));
                } else {
                    handleException("The Feature name is missing");
                }
                if (mp.getValue() != null) {
                    feature.addAttribute(fac.createOMAttribute("value", nullNS, mp.getValue()));
                } else {
                    handleException("The Feature value is missing");
                }
            }
        }
        OMElement onFail = fac.createOMElement("on-fail", synNS, validate);
        serializeChildren(onFail, mediator.getList());

        if (parent != null) {
            parent.addChild(validate);
        }
        return validate;
    }

    public String getMediatorClassName() {
        return ValidateMediator.class.getName();
    }

}
