package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.builtin.ValidateMediator;
import org.apache.synapse.config.xml.MediatorSerializer;
import org.apache.synapse.config.xml.AbstractListMediatorSerializer;

import java.util.Iterator;

/**
 * <validate [source="xpath"]>
 *   <schema key="string">+
 *   <property name="<validation-feature-id>" value="true|false"/> *
 *   <on-fail>
 *     mediator+
 *   </on-fail>
 * </validate>
 */
public class ValidateMediatorSerializer extends AbstractListMediatorSerializer
    implements MediatorSerializer {

    private static final Log log = LogFactory.getLog(ValidateMediatorSerializer.class);

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof ValidateMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }

        ValidateMediator mediator = (ValidateMediator) m;
        OMElement validate = fac.createOMElement("validate", synNS);
        finalizeSerialization(validate,mediator);

        if (mediator.getSource() != null) {
            validate.addAttribute(fac.createOMAttribute(
                "source", nullNS, mediator.getSource().toString()));
            serializeNamespaces(validate, mediator.getSource());
        }

        Iterator iter = mediator.getSchemaKeys().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            OMElement schema = fac.createOMElement("schema", synNS, validate);
            schema.addAttribute(fac.createOMAttribute("key", nullNS, key));
        }

        serializeProperties(validate, mediator.getProperties());

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

    private void handleException(String msg) {
        log.error(msg);
        throw new SynapseException(msg);
    }
}
