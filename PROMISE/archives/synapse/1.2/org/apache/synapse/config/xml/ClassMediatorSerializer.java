package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.ext.ClassMediator;

import javax.xml.namespace.QName;
import java.util.Iterator;

/**
 * <pre>
 * &lt;class name="class-name"&gt;
 *   &lt;property name="string" (value="literal" | expression="xpath")/&gt;*
 * &lt;/class&gt;
 * </pre>
 */
public class ClassMediatorSerializer extends AbstractMediatorSerializer  {

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof ClassMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }
        ClassMediator mediator = (ClassMediator) m;
        OMElement clazz = fac.createOMElement("class", synNS);
        saveTracingState(clazz, mediator);

        if (mediator.getMediator() != null && mediator.getMediator().getClass().getName() != null) {
            clazz.addAttribute(fac.createOMAttribute(
                "name", nullNS, mediator.getMediator().getClass().getName()));
        } else {
            handleException("Invalid class mediator. The class name is required");
        }

        Iterator itr = mediator.getProperties().keySet().iterator();
        while(itr.hasNext()) {
            String propName = (String) itr.next();
            Object o = mediator.getProperties().get(propName);
            OMElement prop = fac.createOMElement(PROP_Q);
            prop.addAttribute(fac.createOMAttribute("name", nullNS, propName));

            if (o instanceof String) {
                prop.addAttribute(fac.createOMAttribute("value", nullNS, (String) o));
            } else {
                prop.addChild((OMNode) o);
            }
            clazz.addChild(prop);
        }

        if (parent != null) {
            parent.addChild(clazz);
        }
        return clazz;
    }

    public String getMediatorClassName() {
        return ClassMediator.class.getName();
    }
}
