package org.apache.synapse.core;

import org.apache.synapse.MessageContext;
import org.apache.synapse.endpoints.utils.EndpointDefinition;
import org.apache.synapse.statistics.StatisticsCollector;

/**
 * The SynapseEnvironment allows access into the the host SOAP engine. It allows
 * the sending of messages, classloader access etc.
 */
public interface SynapseEnvironment {

    /**
     * This method injects a new message into the Synapse engine. This is used by
     * the underlying SOAP engine to inject messages into Synapse for mediation.
     * e.g. The SynapseMessageReceiver used by Axis2 invokes this to inject new messages
     */
    public void injectMessage(MessageContext smc);

    /**
     * This method allows a message to be sent through the underlying SOAP engine.
     * <p/>
     * This will send request messages on (forward), and send the response messages back to the client
     */
    public void send(EndpointDefinition endpoint, MessageContext smc);

    /**
     * Creates a new Synapse <code>MessageContext</code> instance.
     * @return a MessageContext
     */
    public MessageContext createMessageContext();

    /**
     * This method returns the StatisticsCollector
     *
     * @return Retruns the StatisticsCollector
     */
    public StatisticsCollector getStatisticsCollector();

    /**
     * To set the StatisticsCollector
     *
     * @param statisticsCollector
     */
    public void setStatisticsCollector(StatisticsCollector statisticsCollector);
}
