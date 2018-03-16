package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.ext.ClassMediator;
import org.apache.synapse.mediators.filters.FilterMediator;

/**
 * <pre>
 * &lt;class name="class-name"&gt;
 *   &lt;property name="string" (value="literal" | expression="xpath")/&gt;*
 * &lt;/class&gt;
 * </pre>
 */
public class ClassMediatorSerializer extends AbstractMediatorSerializer  {

    private static final Log log = LogFactory.getLog(ClassMediatorSerializer.class);

    public OMElement serializeMediator(OMElement parent, Mediator m) {

        if (!(m instanceof ClassMediator)) {
            handleException("Unsupported mediator passed in for serialization : " + m.getType());
        }
        ClassMediator mediator = (ClassMediator) m;
        OMElement clazz = fac.createOMElement("class", synNS);
        finalizeSerialization(clazz, mediator);

        if (mediator.getClazz() != null) {
            clazz.addAttribute(fac.createOMAttribute(
                "name", nullNS, mediator.getClazz().getName()));
        } else {
            handleException("Invalid class mediator. " +
                "The class name is required");
        }

        serializeProperties(clazz, mediator.getProperties());

        if (parent != null) {
            parent.addChild(clazz);
        }
        return clazz;
    }

    public String getMediatorClassName() {
        return ClassMediator.class.getName();
    }

    private void handleException(String msg) {
        log.error(msg);
        throw new SynapseException(msg);
    }
}
