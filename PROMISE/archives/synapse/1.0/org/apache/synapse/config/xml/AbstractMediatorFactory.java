package org.apache.synapse.config.xml;

import org.apache.synapse.Mediator;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMAttribute;

import javax.xml.namespace.QName;

public abstract class AbstractMediatorFactory implements MediatorFactory {

    /**
     * This is to Initialize the mediator with the default attributes
     *
     * @param mediator
     * @param mediatorOmElement
     */
    protected void initMediator(Mediator mediator, OMElement mediatorOmElement) {

        OMAttribute trace = mediatorOmElement.getAttribute(
            new QName(Constants.NULL_NAMESPACE, Constants.TRACE_ATTRIB_NAME));

        if (trace != null) {
            String traceValue = trace.getAttributeValue();
            if (traceValue != null) {
                if (traceValue.equals(Constants.TRACE_ENABLE)) {
                    mediator.setTraceState(org.apache.synapse.Constants.TRACING_ON);
                } else if (traceValue.equals(Constants.TRACE_DISABLE)) {
                    mediator.setTraceState(org.apache.synapse.Constants.TRACING_OFF);
                }
            }
        }
    }
}
