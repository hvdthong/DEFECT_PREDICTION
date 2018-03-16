package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.synapse.config.xml.OMElementUtils;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.transform.XSLTMediator;
import org.apache.synapse.config.xml.XMLConfigConstants;
import org.apache.synapse.config.xml.AbstractMediatorFactory;
import org.apache.synapse.config.xml.MediatorPropertyFactory;
import org.jaxen.JaxenException;

import javax.xml.namespace.QName;
import java.util.Iterator;

/**
 * Creates a XSLT mediator from the given XML
 *
 * <pre>
 * &lt;xslt key="property-key" [source="xpath"]&gt;
 *   &lt;property name="string" (value="literal" | expression="xpath")/&gt;*
 * &lt;/transform&gt;
 * </pre>
 */
public class XSLTMediatorFactory extends AbstractMediatorFactory {

    private static final QName TAG_NAME    = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "xslt");

    public QName getTagQName() {
        return TAG_NAME;
    }

    public Mediator createMediator(OMElement elem) {

        XSLTMediator transformMediator = new XSLTMediator();

        OMAttribute attXslt   = elem.getAttribute(ATT_KEY);
        OMAttribute attSource = elem.getAttribute(ATT_SOURCE);

        if (attXslt != null) {
            transformMediator.setXsltKey(attXslt.getAttributeValue());
        } else {
            handleException("The 'key' attribute is required for the XSLT mediator");
        }

        if (attSource != null) {
            try {
                transformMediator.setSourceXPathString(attSource.getAttributeValue());
                AXIOMXPath xp = new AXIOMXPath(attSource.getAttributeValue());
                OMElementUtils.addNameSpaces(xp, elem, log);
                transformMediator.setSource(xp);

            } catch (JaxenException e) {
                handleException("Invalid XPath specified for the source attribute : " +
                    attSource.getAttributeValue());
            }
        }
        processTraceState(transformMediator, elem);
        Iterator iter = elem.getChildrenWithName(FEATURE_Q);
        while (iter.hasNext()) {
            OMElement featureElem = (OMElement) iter.next();
            OMAttribute attName = featureElem.getAttribute(ATT_NAME);
            OMAttribute attValue = featureElem.getAttribute(ATT_VALUE);
            if (attName != null && attValue != null) {
                String name = attName.getAttributeValue();
                String value = attValue.getAttributeValue();
                if (name != null && value != null) {
                    if ("true".equals(value.trim())) {
                        transformMediator.addFeature(name.trim(),
                                true);
                    } else if ("false".equals(value.trim())) {
                        transformMediator.addFeature(name.trim(),
                                false);
                    } else {
                        handleException("The feature must have value true or false");
                    }
                } else {
                    handleException("The valid values for both of the name and value are need");
                }
            } else {
                handleException("Both of the name and value attribute are required for a feature");
            }
        }
        transformMediator.addAllProperties(
            MediatorPropertyFactory.getMediatorProperties(elem));

        return transformMediator;
    }
}
