package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseException;
import org.apache.synapse.mediators.ext.POJOCommandMediator;
import org.apache.synapse.util.xpath.SynapseXPath;
import org.jaxen.JaxenException;

import javax.xml.namespace.QName;
import java.util.Iterator;

/**
 * Factory for {@link POJOCommandMediator} instances.
 * <p>
 * Configuration syntax:
 * <pre>
 * &lt;pojoCommand name=&quot;class-name&quot;&gt;
 *   &lt;property name=&quot;string&quot; value=&quot;literal&quot; expression=&quot;xpath&quot;?
 *          context-name=&quot;string&quot;? &gt;
 *      either literal or XML child
 *   &lt;/property&gt;
 *   &lt;property name=&quot;string&quot; expression=&quot;XPATH expression&quot;
 *                action=(&quot;ReadMessage&quot; | &quot;UpdateMessage&quot; |
 *                  &quot;ReadAndUpdateMessage&quot;) context-name=&quot;string&quot;? /&gt;
 *   &lt;property name=&quot;string&quot; context-name=&quot;string&quot;
 *                action=(&quot;ReadContext&quot; | &quot;UpdateContext&quot; |
 *                  &quot;ReadAndUpdateContext&quot;) expression=&quot;XPATH expression&quot;? /&gt;
 * &lt;/pojoCommand&gt;
 * </pre>
 */
public class POJOCommandMediatorFactory extends AbstractMediatorFactory {

    private static final QName POJO_COMMAND_Q
        = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "pojoCommand");
    protected static final QName ATT_ACTION = new QName("action");
    protected static final QName ATT_CTXNAME = new QName("context-name");

    protected static final String RM_ACTION = "ReadMessage";
    protected static final String UM_ACTION = "UpdateMessage";
    protected static final String RC_ACTION = "ReadContext";
    protected static final String UC_ACTION = "UpdateContext";
    protected static final String RAUM_ACTION = "ReadAndUpdateMessage";
    protected static final String RAUC_ACTION = "ReadAndUpdateContext";

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

                OMAttribute nameAttr = child.getAttribute(ATT_NAME);
                if (nameAttr != null && nameAttr.getAttributeValue() != null
                    && !"".equals(nameAttr.getAttributeValue())) {

                    handlePropertyAction(nameAttr.getAttributeValue(), child, pojoMediator);
                } else {
                    handleException("A POJO command mediator " +
                        "property must specify the name attribute");
                }
            }
        }

        return pojoMediator;
    }

    private void handlePropertyAction(String name, OMElement propElem, POJOCommandMediator m) {

        OMAttribute valueAttr   = propElem.getAttribute(ATT_VALUE);
        OMAttribute exprAttr    = propElem.getAttribute(ATT_EXPRN);
        OMAttribute ctxNameAttr = propElem.getAttribute(ATT_CTXNAME);
        OMAttribute actionAttr  = propElem.getAttribute(ATT_ACTION);

        SynapseXPath xpath = null;
        try {
            if (exprAttr != null) {
                xpath = SynapseXPathFactory.getSynapseXPath(propElem, ATT_EXPRN);
            }
        } catch (JaxenException e) {
            handleException("Error in building the expression as an SynapseXPath" + e);
        }

        if (valueAttr != null) {
            String value = valueAttr.getAttributeValue();
            if (exprAttr != null && ctxNameAttr != null) {
                handleException("Command properties can not contain all three 'value', " +
                    "'expression' and 'context-name' attributes. Only one or " +
                    "combination of two can be there.");
            } else {
                m.addStaticSetterProperty(name, value);
                if (exprAttr != null) {
                    m.addMessageGetterProperty(name, xpath);
                } else if (ctxNameAttr != null) {
                    m.addContextGetterProperty(name, ctxNameAttr.getAttributeValue());
            }
        } else if (propElem.getFirstElement() != null) {
            if (exprAttr != null && ctxNameAttr != null) {
                handleException("Command properties can not contain all the " +
                    "'expression' and 'context-name' attributes with a child. Only one " +
                    "attribute of those can co-exists with a child");
            } else {
                m.addStaticSetterProperty(name, propElem.getFirstElement());
                if (exprAttr != null) {
                    m.addMessageGetterProperty(name, xpath);
                } else if (ctxNameAttr != null) {
                    m.addContextGetterProperty(name, ctxNameAttr.getAttributeValue());
            }
        } else {
            if (exprAttr != null && ctxNameAttr != null) {
                if (actionAttr != null && actionAttr.getAttributeValue() != null) {
                    String action = actionAttr.getAttributeValue();
                    if (RM_ACTION.equals(action) || UC_ACTION.equals(action)) {
                        m.addMessageSetterProperty(name, xpath);
                        m.addContextGetterProperty(name, ctxNameAttr.getAttributeValue());
                    } else if (RC_ACTION.equals(action) || UM_ACTION.equals(action)) {
                        m.addContextSetterProperty(name, ctxNameAttr.getAttributeValue());
                        m.addMessageGetterProperty(name, xpath);
                    } else {
                        handleException("Invalid action for " +
                            "the command property with the name " + name);
                    }
                } else {
                    handleException("Action attribute " +
                        "is required for the command property with name " + name);
                }
            } else {
                if (actionAttr != null && actionAttr.getAttributeValue() != null) {
                    String action = actionAttr.getAttributeValue();
                    if (exprAttr != null) {
                        if (RM_ACTION.equals(action)) {
                            m.addMessageSetterProperty(name, xpath);
                        } else if (UM_ACTION.equals(action)) {
                            m.addMessageGetterProperty(name, xpath);
                        } else if (RAUM_ACTION.equals(action)) {
                            m.addMessageSetterProperty(name, xpath);
                            m.addMessageGetterProperty(name, xpath);
                        } else {
                            handleException("Invalid action for " +
                                "the command property with the name " + name);
                        }
                    } else if (ctxNameAttr != null) {
                        String ctxName = ctxNameAttr.getAttributeValue();
                        if (RC_ACTION.equals(action)) {
                            m.addContextSetterProperty(name, ctxName);
                        } else if (UC_ACTION.equals(action)) {
                            m.addContextGetterProperty(name, ctxName);
                        } else if (RAUC_ACTION.equals(action)) {
                            m.addContextSetterProperty(name, ctxName);
                            m.addContextGetterProperty(name, ctxName);
                        } else {
                            handleException("Invalid action for " +
                                "the command property with the name " + name);
                        }
                    } else {
                        handleException("Unrecognized command property with the name " + name);
                    }
                } else {
                    if (exprAttr != null) {
                        m.addMessageSetterProperty(name, xpath);
                        m.addMessageGetterProperty(name, xpath);
                    } else if (ctxNameAttr != null) {
                        String ctxName = ctxNameAttr.getAttributeValue();
                        m.addContextSetterProperty(name, ctxName);
                        m.addContextGetterProperty(name, ctxName);
                    } else {
                        handleException("Unrecognized command property with the name " + name);
                    }
                }
            }
        }
    }

    public QName getTagQName() {
        return POJO_COMMAND_Q;
    }

}

