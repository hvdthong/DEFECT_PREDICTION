package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.builtin.ValidateMediator;
import org.jaxen.JaxenException;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Factory for {@link ValidateMediator} instances.
 * <p>
 * Configuration syntax:
 * <pre>
 * &lt;validate [source="xpath"]>
 *   &lt;schema key="string">+
 *   &lt;property name="<validation-feature-name>" value="true|false"/>
 *   &lt;on-fail>
 *     mediator+
 *   &lt;/on-fail>
 * &lt;/validate>
 * </pre>
 */
public class ValidateMediatorFactory extends AbstractListMediatorFactory {

    private static final QName VALIDATE_Q = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "validate");
    private static final QName ON_FAIL_Q  = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "on-fail");
    private static final QName SCHEMA_Q   = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "schema");

    public Mediator createMediator(OMElement elem) {

        ValidateMediator validateMediator = new ValidateMediator();

        List<String> schemaKeys = new ArrayList<String>();
        Iterator schemas = elem.getChildrenWithName(SCHEMA_Q);

        while (schemas.hasNext()) {
            Object o = schemas.next();
            if (o instanceof OMElement) {
                OMElement omElem = (OMElement) o;
                OMAttribute keyAtt = omElem.getAttribute(ATT_KEY);
                if (keyAtt != null) {
                    schemaKeys.add(keyAtt.getAttributeValue());
                } else {
                    handleException("A 'schema' definition must contain a local property 'key'");
                }
            } else {
                handleException("Invalid 'schema' declaration for validate mediator");
            }
        }

        if (schemaKeys.size() == 0) {
            handleException("No schemas specified for the validate mediator");
        } else {
            validateMediator.setSchemaKeys(schemaKeys);
        }

        OMAttribute attSource = elem.getAttribute(ATT_SOURCE);

        if (attSource != null) {
            try {
                validateMediator.setSource(SynapseXPathFactory.getSynapseXPath(elem, ATT_SOURCE));
            } catch (JaxenException e) {
                handleException("Invalid XPath expression specified for attribute 'source'", e);
            }
        }

        OMElement onFail = null;
        Iterator iterator = elem.getChildrenWithName(ON_FAIL_Q);
        if (iterator.hasNext()) {
            onFail = (OMElement)iterator.next();
        }

        if (onFail != null && onFail.getChildElements().hasNext()) {
            addChildren(onFail, validateMediator);
        } else {
            handleException("A non-empty <on-fail> child element is required for " +
                "the <validate> mediator");
        }

        processTraceState(validateMediator,elem);
        Iterator iter = elem.getChildrenWithName(FEATURE_Q);
        while (iter.hasNext()) {
            OMElement featureElem = (OMElement) iter.next();
            OMAttribute attName = featureElem.getAttribute(ATT_NAME);
            OMAttribute attValue = featureElem.getAttribute(ATT_VALUE);
            if (attName != null && attValue != null) {
                String name = attName.getAttributeValue();
                String value = attValue.getAttributeValue();
                if (name != null && value != null) {
                    try {
                        if ("true".equals(value.trim())) {
                            validateMediator.addFeature(name.trim(), true);
                        } else if ("false".equals(value.trim())) {
                            validateMediator.addFeature(name.trim(), false);
                        } else {
                            handleException("The feature must have value true or false");
                        }
                    } catch (SAXException e) {
                        handleException("Error setting validation feature : " + name + " to : " + value, e);
                    }
                } else {
                    handleException("The valid values for both of the name and value are need");
                }
            } else {
                handleException("Both of the name and value attribute are required for a feature");
            }
        }
        return validateMediator;
    }

    public QName getTagQName() {
        return VALIDATE_Q;
    }
}
