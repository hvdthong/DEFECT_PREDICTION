package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.filters.InMediator;

import javax.xml.namespace.QName;

/**
 * Creates an In mediator instance
 *
 * <pre>
 * &lt;in&gt;
 *    mediator+
 * &lt;/in&gt;
 * </pre>
 */
public class InMediatorFactory extends AbstractListMediatorFactory {

    private static final QName IN_Q = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "in");

    public Mediator createMediator(OMElement elem) {
        InMediator filter = new InMediator();
        processTraceState(filter,elem);
        addChildren(elem, filter);
        return filter;
    }

    public QName getTagQName() {
        return IN_Q;
    }
}
