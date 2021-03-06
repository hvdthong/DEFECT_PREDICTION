package org.apache.synapse.core.axis2;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.description.InOutAxisOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Mediator;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.SynapseException;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.endpoints.utils.EndpointDefinition;
import org.apache.synapse.mediators.MediatorWorker;
import org.apache.synapse.mediators.base.SequenceMediator;
import org.apache.synapse.statistics.StatisticsCollector;
import org.apache.synapse.statistics.StatisticsUtils;
import org.apache.synapse.util.UUIDGenerator;
import org.apache.synapse.util.concurrent.SynapseThreadPool;

import java.util.concurrent.ExecutorService;

/**
 * This is the Axis2 implementation of the SynapseEnvironment
 */
public class Axis2SynapseEnvironment implements SynapseEnvironment {

    private static final Log log = LogFactory.getLog(Axis2SynapseEnvironment.class);

    private SynapseConfiguration synapseConfig;
    private ConfigurationContext configContext;
    private ExecutorService executorService;
    private boolean initialized = false;

    /** The StatisticsCollector object */
    private StatisticsCollector statisticsCollector;

    public Axis2SynapseEnvironment(SynapseConfiguration synCfg) {
        
        int coreThreads = SynapseThreadPool.SYNAPSE_CORE_THREADS;
        int maxThreads  = SynapseThreadPool.SYNAPSE_MAX_THREADS;
        long keepAlive  = SynapseThreadPool.SYNAPSE_KEEP_ALIVE;
        int qlength     = SynapseThreadPool.SYNAPSE_THREAD_QLEN;
        
        try {
            qlength = Integer.parseInt(synCfg.getProperty(SynapseThreadPool.SYN_THREAD_QLEN));
        } catch (Exception ignore) {}

        try {
            coreThreads = Integer.parseInt(synCfg.getProperty(SynapseThreadPool.SYN_THREAD_CORE));
        } catch (Exception ignore) {}

        try {
            maxThreads = Integer.parseInt(synCfg.getProperty(SynapseThreadPool.SYN_THREAD_MAX));
        } catch (Exception ignore) {}

        try {
            keepAlive = Long.parseLong(synCfg.getProperty(SynapseThreadPool.SYN_THREAD_ALIVE));
        } catch (Exception ignore) {}
        
        this.executorService = new SynapseThreadPool(coreThreads, maxThreads, keepAlive, qlength,
            synCfg.getProperty(SynapseThreadPool.SYN_THREAD_GROUP,
                SynapseThreadPool.SYNAPSE_THREAD_GROUP),
            synCfg.getProperty(SynapseThreadPool.SYN_THREAD_IDPREFIX,
                SynapseThreadPool.SYNAPSE_THREAD_ID_PREFIX));
    }

    public Axis2SynapseEnvironment(ConfigurationContext cfgCtx,
        SynapseConfiguration synapseConfig) {
        this(synapseConfig);
        this.configContext = cfgCtx;
        this.synapseConfig = synapseConfig;
    }

    public boolean injectMessage(final MessageContext synCtx) {
        if (log.isDebugEnabled()) {
            log.debug("Injecting MessageContext");
        }
        synCtx.setEnvironment(this);
        if (synCtx.isResponse()) {
            StatisticsUtils.processEndPointStatistics(synCtx);
            StatisticsUtils.processProxyServiceStatistics(synCtx);
            StatisticsUtils.processSequenceStatistics(synCtx);
        }

        if (synCtx.getProperty(SynapseConstants.PROXY_SERVICE) != null) {

            if (synCtx.getConfiguration().getProxyService((String) synCtx.getProperty(
                    SynapseConstants.PROXY_SERVICE)).getTargetOutSequence() != null) {

                String sequenceName = synCtx.getConfiguration().getProxyService((String) synCtx.
                        getProperty(SynapseConstants.PROXY_SERVICE)).getTargetOutSequence();
                Mediator outSequence = synCtx.getSequence(sequenceName);

                if (outSequence != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Using the sequence named " + sequenceName
                                + " for the outgoing message mediation of the proxy service "
                                + synCtx.getProperty(SynapseConstants.PROXY_SERVICE));
                    }
                    outSequence.mediate(synCtx);
                } else {
                    log.error("Unable to find the out-sequence " +
                            "specified by the name " + sequenceName);
                    throw new SynapseException("Unable to find the " +
                            "out-sequence specified by the name " + sequenceName);
                }

            } else if (synCtx.getConfiguration().getProxyService((String) synCtx.getProperty(
                    SynapseConstants.PROXY_SERVICE)).getTargetInLineOutSequence() != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Using the anonymous out-sequence specified in the proxy service "
                            + synCtx.getProperty(SynapseConstants.PROXY_SERVICE)
                            + " for outgoing message mediation");
                }
                synCtx.getConfiguration().getProxyService((String) synCtx.getProperty(
                        SynapseConstants.PROXY_SERVICE)).getTargetInLineOutSequence().mediate(synCtx);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Proxy service " + synCtx.getProperty(SynapseConstants.PROXY_SERVICE)
                            + " does not specifies an out-sequence - sending the response back");
                }
                Axis2Sender.sendBack(synCtx);
            }

        } else {
            if (log.isDebugEnabled()) {
                log.debug("Using Main Sequence for injected message");
            }
            return synCtx.getMainSequence().mediate(synCtx);
        }
        return true;
    }

    public void injectAsync(final MessageContext synCtx, SequenceMediator seq) {
        if (log.isDebugEnabled()) {
            log.debug("Injecting MessageContext for asynchronous mediation using the : "
                + (seq.getName() == null? "Anonymous" : seq.getName()) + " Sequence");
        }
        synCtx.setEnvironment(this);
        if (synCtx.isResponse()) {
            StatisticsUtils.processEndPointStatistics(synCtx);
            StatisticsUtils.processProxyServiceStatistics(synCtx);
            StatisticsUtils.processSequenceStatistics(synCtx);
        }

        executorService.execute(new MediatorWorker(seq, synCtx));

    }

    /**
     * This will be used for sending the message provided, to the endpoint specified by the
     * EndpointDefinition using the axis2 environment.
     *
     * @param endpoint - EndpointDefinition to be used to find the endpoint information
     *                      and the properties of the sending process
     * @param synCtx   - Synapse MessageContext to be sent
     */
    public void send(EndpointDefinition endpoint, MessageContext synCtx) {
        if (synCtx.isResponse()) {

            if (endpoint != null) {
                StatisticsUtils.processEndPointStatistics(synCtx);
                StatisticsUtils.processProxyServiceStatistics(synCtx);
                StatisticsUtils.processAllSequenceStatistics(synCtx);

                Axis2Sender.sendOn(endpoint, synCtx);

            } else {
                Axis2Sender.sendBack(synCtx);
            }
        } else {
            Axis2Sender.sendOn(endpoint, synCtx);
        }
    }

    /**
     * This method will be used to create a new MessageContext in the Axis2 environment for
     * synapse. This will set all the relevant parts to the messagecontext, but for this message
     * context to be usefull creator has to fill in the data like envelope and operation context
     * and so on. This will set a default envelope of type soap12 and a new messageID for the
     * created message along with the ConfigurationContext is being set in to the message
     * correctly.
     *
     * @return Synapse MessageContext with the underlying axis2 message context set
     */
    public MessageContext createMessageContext() {

        if (log.isDebugEnabled()) {
            log.debug("Creating Message Context");
        }

        org.apache.axis2.context.MessageContext axis2MC
                = new org.apache.axis2.context.MessageContext();
        axis2MC.setConfigurationContext(this.configContext);

        ServiceContext svcCtx = new ServiceContext();
        OperationContext opCtx = new OperationContext(new InOutAxisOperation(), svcCtx);
        axis2MC.setServiceContext(svcCtx);
        axis2MC.setOperationContext(opCtx);
        MessageContext mc = new Axis2MessageContext(axis2MC, synapseConfig, this);
        mc.setMessageID(UUIDGenerator.getUUID());
        try {
			mc.setEnvelope(OMAbstractFactory.getSOAP12Factory().createSOAPEnvelope());
			mc.getEnvelope().addChild(OMAbstractFactory.getSOAP12Factory().createSOAPBody());
		} catch (Exception e) {
			e.printStackTrace();
		}

        return mc;
    }

    /**
     * This method returns the StatisticsCollector
     *
     * @return Retruns the StatisticsCollector
     */
    public StatisticsCollector getStatisticsCollector() {
        return statisticsCollector;
    }

    /**
     * To set the StatisticsCollector
     *
     * @param collector - Statistics collector to be set
     */
    public void setStatisticsCollector(StatisticsCollector collector) {
        this.statisticsCollector = collector;
    }

    /**
     * This will give the access to the synapse thread pool for the
     * advanced mediation tasks.
     *
     * @return an ExecutorService to execute the tasks in a new thread from the pool
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * Has this environment properly initialized?
     * @return true if ready for processing
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Mark this environment as ready for processing
     * @param state true means ready for processing
     */
    public void setInitialized(boolean state) {
        this.initialized = state;
    }

}
