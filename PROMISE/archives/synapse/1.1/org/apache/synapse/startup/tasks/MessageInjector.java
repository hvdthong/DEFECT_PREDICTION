package org.apache.synapse.startup.tasks;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.startup.Task;
import org.apache.synapse.util.PayloadHelper;

/**
 * Injects a Message in to the Synapse environment
 */
public class MessageInjector implements Task, ManagedLifecycle {

    /**
     * Holds the logger for logging purposes
     */
    private Log log = LogFactory.getLog(MessageInjector.class);

    /**
     * Holds the Message to be injected
     */
    private OMElement message = null;

    /**
     * Holds the to address for the message to be injected
     */
    private String to = null;

    private String soapAction = null;

    /**
     * Holds the SynapseEnv to which the message will be injected
     */
    private SynapseEnvironment synapseEnvironment;

    /**
     * Initializes the Injector
     *
     * @param se
     *          SynapseEnvironment of synapse
     */
    public void init(SynapseEnvironment se) {
		synapseEnvironment = se;
	}

    /**
     * Set the message to be injected
     *
     * @param elem
     *          OMElement describing the message
     */
    public void setMessage(OMElement elem) {
		log.debug("set message " + elem.toString());
		message = elem;
	}

    /**
     * Set the to address of the message to be injected
     *
     * @param url
     *          String containing the to address
     */
    public void setTo(String url) {
		to = url;
	}

    public void setSoapAction(String soapAction) {
        this.soapAction = soapAction;
    }

    /**
     * This will be invoked by the schedular to inject the message
     * in to the SynapseEnvironment
     */
    public void execute() {
		log.debug("execute");
		if (synapseEnvironment == null) {
			log.error("Synapse Environment not set");
			return;
		}
		if (message == null) {
			log.error("message not set");
			return;

		}
		if (to == null) {
			log.error("to address not set");
			return;

		}
        MessageContext mc = synapseEnvironment.createMessageContext();
        mc.setTo(new EndpointReference(to));
        PayloadHelper.setXMLPayload(mc, message.cloneOMElement());
        if (soapAction != null) {
            mc.setSoapAction(soapAction);
        }
        synapseEnvironment.injectMessage(mc);

	}

    /**
     * Destroys the Injector
     */
    public void destroy() {
	}

}
