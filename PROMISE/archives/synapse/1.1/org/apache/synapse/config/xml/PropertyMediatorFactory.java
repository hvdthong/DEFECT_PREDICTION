package org.apache.synapse.config.xml;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseException;
import org.apache.synapse.mediators.builtin.PropertyMediator;
import org.jaxen.JaxenException;

/**
 * Creates a property mediator through the supplied XML configuration
 * <p/>
 * <pre>
 * &lt;property name="string" [action=set/remove] (value="literal" | expression="xpath") [scope=(axis2 | axis2-client | transport)]/&gt;
 * </pre>
 */
public class PropertyMediatorFactory extends AbstractMediatorFactory {
    private static final QName ATT_SCOPE = new QName("scope");
    private static final QName ATT_ACTION = new QName("action");

    public Mediator createMediator(OMElement elem) {

        PropertyMediator propMediator = new PropertyMediator();
        OMAttribute name = elem.getAttribute(ATT_NAME);
        OMAttribute value = elem.getAttribute(ATT_VALUE);
        OMAttribute expression = elem.getAttribute(ATT_EXPRN);
        OMAttribute scope = elem.getAttribute(ATT_SCOPE);
        OMAttribute action = elem.getAttribute(ATT_ACTION);

        if (name == null) {
            String msg = "The 'name' attribute is required for the configuration of a property mediator";
            log.error(msg);
            throw new SynapseException(msg);
        } else if ((value == null && expression == null) && !(action != null && "remove".equals(action.getAttributeValue()))) {
            String msg = "Either an 'value' or 'expression' attribute is required for a property mediator when action is SET";
            log.error(msg);
            throw new SynapseException(msg);
        }
        propMediator.setName(name.getAttributeValue());
        if (value != null) {
            propMediator.setValue(value.getAttributeValue());
        } else if (expression != null) {
            try {
                AXIOMXPath xp = new AXIOMXPath(expression.getAttributeValue());
                OMElementUtils.addNameSpaces(xp, elem, log);
                propMediator.setExpression(xp);

            } catch (JaxenException e) {
                String msg = "Invalid XPath expression for attribute 'expression' : " + expression.getAttributeValue();
                log.error(msg);
                throw new SynapseException(msg);
            }
        }
        if (scope != null) {
            String valueStr = scope.getAttributeValue();
            if (!XMLConfigConstants.SCOPE_AXIS2.equals(valueStr) && !XMLConfigConstants.SCOPE_TRANSPORT.equals(valueStr)
                    && !XMLConfigConstants.SCOPE_DEFAULT.equals(valueStr) && !XMLConfigConstants.SCOPE_CLIENT.equals(valueStr)) {
                String msg = "Only '" + XMLConfigConstants.SCOPE_AXIS2 + "' or '" + XMLConfigConstants.SCOPE_TRANSPORT + "' or '" + XMLConfigConstants.SCOPE_CLIENT
                        + "' values are allowed for attribute scope for a property mediator"
                        + ", Unsupported scope " + valueStr;
                log.error(msg);
                throw new SynapseException(msg);
            }
            propMediator.setScope(valueStr);
        }
        processTraceState(propMediator, elem);
        if (action != null && "remove".equals(action.getAttributeValue())) {
            propMediator.setAction(PropertyMediator.ACTION_REMOVE);
        }
        return propMediator;
    }

    public QName getTagQName() {
        return PROP_Q;
    }
}
