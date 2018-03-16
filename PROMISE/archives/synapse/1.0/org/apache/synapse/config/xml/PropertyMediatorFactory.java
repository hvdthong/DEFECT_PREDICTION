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
 * &lt;property name="string" [action=set/remove] (value="literal" | expression="xpath")/&gt;
 * </pre>
 */
public class PropertyMediatorFactory extends AbstractMediatorFactory {

    private static final Log log = LogFactory.getLog(LogMediatorFactory.class);

    private static final QName PROP_Q = new QName(Constants.SYNAPSE_NAMESPACE, "property");

    public Mediator createMediator(OMElement elem) {

        PropertyMediator propMediator = new PropertyMediator();
        OMAttribute name = elem.getAttribute(new QName(Constants.NULL_NAMESPACE, "name"));
        OMAttribute value = elem.getAttribute(new QName(Constants.NULL_NAMESPACE, "value"));
        OMAttribute expression = elem.getAttribute(new QName(Constants.NULL_NAMESPACE, "expression"));
        OMAttribute scope = elem.getAttribute(new QName(Constants.NULL_NAMESPACE, "scope"));
        OMAttribute action = elem.getAttribute(new QName(Constants.NULL_NAMESPACE, "action"));
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
            if (!Constants.SCOPE_AXIS2.equals(valueStr) && !Constants.SCOPE_TRANSPORT.equals(valueStr)
                    && !Constants.SCOPE_DEFAULT.equals(valueStr)) {
                String msg = "Only '" + Constants.SCOPE_AXIS2 + "' or '" + Constants.SCOPE_TRANSPORT
                        + "' values are allowed for attribute scope for a property mediator"
                        + ", Unsupported scope " + valueStr;
                log.error(msg);
                throw new SynapseException(msg);
            }
            propMediator.setScope(valueStr);
        }
        initMediator(propMediator, elem);
        if (action != null && "remove".equals(action.getAttributeValue())) {
            propMediator.setAction(PropertyMediator.ACTION_REMOVE);
        }
        return propMediator;
    }

    public QName getTagQName() {
        return PROP_Q;
    }
}
