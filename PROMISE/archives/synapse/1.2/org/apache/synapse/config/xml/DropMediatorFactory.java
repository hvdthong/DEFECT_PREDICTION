package org.apache.synapse.config.xml;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.mediators.builtin.DropMediator;

import javax.xml.namespace.QName;

/**
 * Factory for {@link DropMediator} instances.
 * <p>
 * Configuration syntax:
 * <pre>
 * &lt;drop/&gt;
 * </pre>
 */
public class DropMediatorFactory extends AbstractMediatorFactory  {

    private static final QName DROP_Q = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "drop");

    public Mediator createMediator(OMElement el) {

        Mediator dropMediator = new DropMediator();
        processTraceState(dropMediator,el);

        return dropMediator;
    }

    public QName getTagQName() {
        return DROP_Q;
    }
}
