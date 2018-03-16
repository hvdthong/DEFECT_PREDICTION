package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.filters.OutMediator;

import javax.xml.namespace.QName;

/**
 * Creates an Out mediator instance
 *
 * <pre>
 * &lt;out&gt;
 *    mediator+
 * &lt;/out&gt;
 * </pre>
 */
public class OutMediatorFactory extends AbstractListMediatorFactory {

    private static final QName OUT_Q = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "out");

    public Mediator createMediator(OMElement elem) {
        OutMediator filter = new OutMediator();

        processTraceState(filter,elem);

        addChildren(elem, filter);
        return filter;
    }

    public QName getTagQName() {
        return OUT_Q;
    }
}
