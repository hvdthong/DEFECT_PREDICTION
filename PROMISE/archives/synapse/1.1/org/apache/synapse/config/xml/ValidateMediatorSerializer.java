package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.builtin.ValidateMediator;
import org.apache.synapse.mediators.MediatorProperty;

import java.util.Iterator;
import java.util.List;

/**
 * <validate [source="xpath"]>
 *   <schema key="string">+
 *   <property name="<validation-feature-name>" value="true|false"/>
 *   <on-fail>
 *     mediator+
 *   </on-fail>
 * </validate>
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
            validate.addAttribute(fac.createOMAttribute(
                    "source", nullNS, mediator.getSource().toString()));
            serializeNamespaces(validate, mediator.getSource());
        }

        Iterator iterator = mediator.getSchemaKeys().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            OMElement schema = fac.createOMElement("schema", synNS, validate);
            schema.addAttribute(fac.createOMAttribute("key", nullNS, key));
        }

        List features = mediator.getFeatures();
        if (!features.isEmpty()) {
            for (Iterator iter = features.iterator(); iter.hasNext();) {
                MediatorProperty mp = (MediatorProperty) iter.next();
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
