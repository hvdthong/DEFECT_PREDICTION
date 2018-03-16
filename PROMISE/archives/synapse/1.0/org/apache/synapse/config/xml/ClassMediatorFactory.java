package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.SynapseException;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.ext.ClassMediator;

import javax.xml.namespace.QName;

/**
 * Creates an instance of a Class mediator using XML configuration specified
 *
 * <pre>
 * &lt;class name="class-name"&gt;
 *   &lt;property name="string" (value="literal" | expression="xpath")/&gt;*
 * &lt;/class&gt;
 * </pre>
 */
public class ClassMediatorFactory extends AbstractMediatorFactory {

    private static final Log log = LogFactory.getLog(LogMediatorFactory.class);

    private static final QName CLASS_Q = new QName(Constants.SYNAPSE_NAMESPACE, "class");

    public Mediator createMediator(OMElement elem) {

        ClassMediator classMediator = new ClassMediator();

        OMAttribute name = elem.getAttribute(new QName(Constants.NULL_NAMESPACE, "name"));
        if (name == null) {
            String msg = "The name of the actual mediator class is a required attribute";
            log.error(msg);
            throw new SynapseException(msg);
        }

        try {
            Class clazz = getClass().getClassLoader().loadClass(name.getAttributeValue());
            classMediator.setClazz(clazz);
        } catch (ClassNotFoundException e) {
            String msg = "Cannot find class : " + name.getAttributeValue();
            log.error(msg, e);
            throw new SynapseException(msg, e);
        }
        initMediator(classMediator,elem);
        classMediator.addAllProperties(MediatorPropertyFactory.getMediatorProperties(elem));

        return classMediator;
    }


    public QName getTagQName() {
        return CLASS_Q;
    }
}
