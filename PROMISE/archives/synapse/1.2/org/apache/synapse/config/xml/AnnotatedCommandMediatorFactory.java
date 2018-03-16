package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseException;
import org.apache.synapse.mediators.ext.AnnotatedCommandMediator;
import org.apache.synapse.util.xpath.SynapseXPath;
import org.jaxen.JaxenException;

import javax.xml.namespace.QName;
import java.util.Iterator;

/**
 * Creates an instance of a AnnotatedCommand mediator using XML configuration specified
 * <p/>
 * <pre>
 * &lt;annotatedCommand name=&quot;class-name&quot;&gt;
 *   &lt;property name=&quot;string&quot; value=&quot;literal&quot;&gt;
 *      either literal or XML child
 *   &lt;/property&gt;
 *   &lt;property name=&quot;string&quot; expression=&quot;XPATH expression&quot;/&gt;
 * &lt;/annoatedCommand&gt;
 * </pre>
 */
public class AnnotatedCommandMediatorFactory extends AbstractMediatorFactory {

    private static final QName ANNOTATED_COMMAND_Q =
        new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "annotatedCommand");

    public Mediator createMediator(OMElement elem) {

        AnnotatedCommandMediator pojoMediator = new AnnotatedCommandMediator();

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
                        SynapseXPath xpath = null;
                        try {
                            xpath = SynapseXPathFactory.getSynapseXPath(child, ATT_EXPRN);
                            pojoMediator.addMessageSetterProperty(propName, xpath);
                        } catch (JaxenException e) {
                            handleException("Error instantiating XPath expression : " +
                                child.getAttribute(ATT_EXPRN), e);
                        }
                    } else {
                        if (child.getAttribute(ATT_VALUE) != null) {
                            pojoMediator.addStaticSetterProperty(propName,
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
        return ANNOTATED_COMMAND_Q;
    }

}

