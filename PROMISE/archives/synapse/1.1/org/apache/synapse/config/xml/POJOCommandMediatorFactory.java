package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseException;
import org.apache.synapse.mediators.ext.POJOCommandMediator;
import org.jaxen.JaxenException;

import javax.xml.namespace.QName;
import java.util.Iterator;

/**
 * Creates an instance of a Class mediator using XML configuration specified
 * <p/>
 * <pre>
 * &lt;pojoCommand name=&quot;class-name&quot;&gt;
 *   &lt;property name=&quot;string&quot; value=&quot;literal&quot;&gt;
 *      either literal or XML child
 *   &lt;/property&gt;
 *   &lt;property name=&quot;string&quot; expression=&quot;XPATH expression&quot;/&gt;
 * &lt;/pojoCommand&gt;
 * </pre>
 */
public class POJOCommandMediatorFactory extends AbstractMediatorFactory {

    private static final QName POJO_COMMAND_Q =
        new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "pojoCommand");

    public Mediator createMediator(OMElement elem) {

        POJOCommandMediator pojoMediator = new POJOCommandMediator();

        OMAttribute name = elem.getAttribute(ATT_NAME);
        if (name == null) {
            String msg = "The name of the actual POJO command implementation class" +
                    " is a required attribute";
            log.error(msg);
            throw new SynapseException(msg);
        }

        try {
            pojoMediator.setCommand(
                    getClass().getClassLoader().loadClass(name.getAttributeValue()));
        } catch (ClassNotFoundException e) {
            handleException("Unable to load the class specified as the command "
                    + name.getAttributeValue(), e);
        }

        for (Iterator it = elem.getChildElements(); it.hasNext();) {
            OMElement child = (OMElement) it.next();
            if("property".equals(child.getLocalName())) {

                String propName = child.getAttribute(ATT_NAME).getAttributeValue();
                if (propName == null) {
                    handleException(
                        "A POJO command mediator property must specify the name attribute");
                } else {
                    if (child.getAttribute(ATT_EXPRN) != null) {
                        AXIOMXPath xpath = null;
                        try {
                            xpath = new AXIOMXPath(
                                child.getAttribute(ATT_EXPRN).getAttributeValue());
                            OMElementUtils.addNameSpaces(xpath, child, log);
                            pojoMediator.addDynamicProperty(propName, xpath);
                        } catch (JaxenException e) {
                            handleException("Error instantiating XPath expression : " +
                                child.getAttribute(ATT_EXPRN), e);
                        }
                    } else {
                        if (child.getAttribute(ATT_VALUE) != null) {
                            pojoMediator.addStaticProperty(propName,
                                child.getAttribute(ATT_VALUE).getAttributeValue());
                        } else {
                            handleException("A POJO mediator property must specify either " +
                                "name and expression attributes, or name and value attributes");
                        }
                    }
                }
            }
        }

        return pojoMediator;
    }

    public QName getTagQName() {
        return POJO_COMMAND_Q;
    }

}

