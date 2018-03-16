package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.base.SynapseMediator;

import javax.xml.namespace.QName;

/**
 * Builds the main mediator (@see SynapseConfiguration) of the Synapse instance
 *
 * <pre>
 * &lt;rules&gt;
 *   mediator+
 * &lt;rules&gt;
 * </pre>
 */
public class SynapseMediatorFactory extends AbstractListMediatorFactory {

    private final static QName RULES_Q = new QName(Constants.SYNAPSE_NAMESPACE, "rules");

    public QName getTagQName() {
        return RULES_Q;
    }

    public Mediator createMediator(OMElement elem) {
        SynapseMediator sm = new SynapseMediator();

        initMediator(sm,elem);

        addChildren(elem, sm);
        return sm;
    }

}
