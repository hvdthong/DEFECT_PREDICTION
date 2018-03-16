package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.mediators.MediatorProperty;
import org.jaxen.JaxenException;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A utility class capable of creating instances of MediatorProperty objects by reading
 * through a given XML configuration
 *
 * <pre>
 * &lt;element&gt;
 *    &lt;property name="string" (value="literal" | expression="xpath")/&gt;*
 * &lt;/element&gt;
 * </pre>
 */
public class MediatorPropertyFactory {

    private static final Log log = LogFactory.getLog(MediatorPropertyFactory.class);

    public static List<MediatorProperty> getMediatorProperties(OMElement elem) {

        List<MediatorProperty> propertyList = new ArrayList<MediatorProperty>();

        Iterator iter = elem.getChildrenWithName(
            new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "property"));

        while (iter.hasNext()) {

            OMElement propEle = (OMElement) iter.next();
            OMAttribute attName  = propEle.getAttribute(MediatorProperty.ATT_NAME_Q);
            OMAttribute attValue = propEle.getAttribute(MediatorProperty.ATT_VALUE_Q);
            OMAttribute attExpr  = propEle.getAttribute(MediatorProperty.ATT_EXPR_Q);

            MediatorProperty prop = new MediatorProperty();

            if (attName == null || attName.getAttributeValue() == null ||
                attName.getAttributeValue().trim().length() == 0) {
                String msg = "Entry name is a required attribute for a Log property";
                log.error(msg);
                throw new SynapseException(msg);
            } else {
                prop.setName(attName.getAttributeValue());
            }

            if (attValue != null) {

                if (attValue.getAttributeValue() == null ||
                    attValue.getAttributeValue().trim().length() == 0) {
                    
                    String msg = "Entry attribute value (if specified) " +
                        "is required for a Log property";
                    log.error(msg);
                    throw new SynapseException(msg);

                } else {
                    prop.setValue(attValue.getAttributeValue());
                }

            } else if (attExpr != null) {

                if (attExpr.getAttributeValue() == null ||
                    attExpr.getAttributeValue().trim().length() == 0) {

                    String msg = "Entry attribute expression (if specified) " +
                        "is required for a mediator property";
                    log.error(msg);
                    throw new SynapseException(msg);

                } else {
                    try {
                        prop.setExpression(SynapseXPathFactory.getSynapseXPath(
                            propEle, MediatorProperty.ATT_EXPR_Q));

                    } catch (JaxenException e) {
                        String msg = "Invalid XPapth expression : " + attExpr.getAttributeValue();
                        log.error(msg);
                        throw new SynapseException(msg, e);
                    }
                }

            } else {
                String msg = "Entry attribute value OR expression must " +
                    "be specified for a mediator property";
                log.error(msg);
                throw new SynapseException(msg);
            }

            propertyList.add(prop);
        }

        return propertyList;
    }
}
