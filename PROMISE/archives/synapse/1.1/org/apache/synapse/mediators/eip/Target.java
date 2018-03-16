package org.apache.synapse.mediators.eip;

import org.apache.synapse.Mediator;
import org.apache.synapse.MessageContext;
import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.mediators.base.SequenceMediator;
import org.apache.axis2.addressing.EndpointReference;

/**
 * This class will be a bean which carries the target information for most of the EIP mediators
 */
public class Target {

    /**
     * Holds the to address of the target endpoint
     */
    private String to = null;

    /**
     * Holds the soapAction of the target service
     */
    private String soapAction = null;

    /**
     * Holds the target mediation sequence as an annonymous sequence
     */
    private SequenceMediator sequence = null;

    /**
     * Holds the target mediation sequence as a sequence reference
     */
    private String sequenceRef = null;

    /**
     * Holds the target endpoint to which the message will be sent
     */
    private Endpoint endpoint = null;

    /**
     * Holds the reference to the target endpoint to which the message will be sent
     */
    private String endpointRef = null;

    /**
     * This method will be called by the EIP mediators to mediated the target (may be to mediate
     * using the target sequence, send message to the target endpoint or both)
     *
     * @param synCtx - MessageContext to be mediated
     */
    public void mediate(MessageContext synCtx) {

        if (soapAction != null) {
            synCtx.setSoapAction(soapAction);
        }

        if (to != null) {
            if (synCtx.getTo() != null) {
                synCtx.getTo().setAddress(to);
            } else {
                synCtx.setTo(new EndpointReference(to));
            }
        }

        if (sequence != null) {
            synCtx.getEnvironment().injectAsync(synCtx, sequence);
        } else if (sequenceRef != null) {
            SequenceMediator refSequence = (SequenceMediator) synCtx.getConfiguration().getSequence(sequenceRef);
            if (refSequence != null) {
                synCtx.getEnvironment().injectAsync(synCtx, refSequence);
            }
        }

        if (endpoint != null) {
            endpoint.send(synCtx);
        } else if (endpointRef != null) {
            Endpoint epr = synCtx.getConfiguration().getEndpoint(endpointRef);
            if (epr != null) {
                epr.send(synCtx);
            }
        }

    }


    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSoapAction() {
        return soapAction;
    }

    public void setSoapAction(String soapAction) {
        this.soapAction = soapAction;
    }

    public SequenceMediator getSequence() {
        return sequence;
    }

    public void setSequence(SequenceMediator sequence) {
        this.sequence = sequence;
    }

    public String getSequenceRef() {
        return sequenceRef;
    }

    public void setSequenceRef(String sequenceRef) {
        this.sequenceRef = sequenceRef;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpointRef() {
        return endpointRef;
    }

    public void setEndpointRef(String endpointRef) {
        this.endpointRef = endpointRef;
    }
}
