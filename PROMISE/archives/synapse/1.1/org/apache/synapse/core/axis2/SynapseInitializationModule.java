package org.apache.synapse.core.axis2;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.*;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.modules.Module;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.SynapseException;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.config.SynapseConfigurationBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * This is the Synapse Module implementation class, which would initialize Synapse when it is
 * deployed onto an Axis2 configuration.
 */
public class SynapseInitializationModule implements Module {

    private static final Log log = LogFactory.getLog(SynapseInitializationModule.class);
    private SynapseConfiguration synCfg;

    public void init(ConfigurationContext configurationContext,
        AxisModule axisModule) throws AxisFault {

        log.info("Initializing Synapse at : " + new Date());
        try {
            InetAddress addr = InetAddress.getLocalHost();
            if (addr != null) {
                String ipAddr = addr.getHostAddress();
                if (ipAddr != null) {
                    MDC.put("ip", ipAddr);
                }

                String hostname = addr.getHostName();
                if (hostname == null) {
                    hostname = ipAddr;
                }
                MDC.put("host", hostname);
            }
        } catch (UnknownHostException e) {
            log.warn("Unable to determine hostname or IP address of the server for logging", e);
        }

        log.info("Loading mediator extensions...");
        configurationContext.getAxisConfiguration().getConfigurator().loadServices();

        log.info("Initializing the Synapse configuration ...");
        synCfg = getConfiguration(configurationContext);

        log.info("Deploying the Synapse service..");
        AxisConfiguration axisCfg = configurationContext.getAxisConfiguration();
        AxisService synapseService = new AxisService(SynapseConstants.SYNAPSE_SERVICE_NAME);
        AxisOperation mediateOperation = new InOutAxisOperation(SynapseConstants.SYNAPSE_OPERATION_NAME);
        mediateOperation.setMessageReceiver(new SynapseMessageReceiver());
        synapseService.addOperation(mediateOperation);
        List transports = new ArrayList();
        transports.add(Constants.TRANSPORT_HTTP);
        transports.add(Constants.TRANSPORT_HTTPS);
        synapseService.setExposedTransports(transports);
        axisCfg.addService(synapseService);

        log.info("Initializing Sandesha 2...");
        AxisModule sandeshaAxisModule = configurationContext.getAxisConfiguration().
            getModule(SynapseConstants.SANDESHA2_MODULE_NAME);
        if (sandeshaAxisModule != null) {
            Module sandesha2 = sandeshaAxisModule.getModule();
            sandesha2.init(configurationContext, sandeshaAxisModule);
        }

        log.info("Deploying Proxy services...");
        Iterator iter = synCfg.getProxyServices().iterator();
        while (iter.hasNext()) {
            ProxyService proxy = (ProxyService) iter.next();
            proxy.buildAxisService(synCfg, axisCfg);
            log.info("Deployed Proxy service : " + proxy.getName());
            if (!proxy.isStartOnLoad()) {
                proxy.stop(synCfg);
            }
        }
        
        log.info("Synapse initialized successfully...!");
    }

    private static SynapseConfiguration getConfiguration(ConfigurationContext cfgCtx) {

        cfgCtx.setProperty("addressing.validateAction", Boolean.FALSE);
        AxisConfiguration axisConfiguration = cfgCtx.getAxisConfiguration();
        SynapseConfiguration synapseConfiguration;

        String config = System.getProperty(SynapseConstants.SYNAPSE_XML);

        if (config != null) {
            if (log.isDebugEnabled()) {
                log.debug("System property '" + SynapseConstants.SYNAPSE_XML +
                        "' specifies Synapse configuration as " + config);
            }
            synapseConfiguration = SynapseConfigurationBuilder.getConfiguration(config);
        } else {
            log.warn("System property '" + SynapseConstants.SYNAPSE_XML +
                "' is not specified. Using default configuration..");
            synapseConfiguration = SynapseConfigurationBuilder.getDefaultConfiguration();
        }

        synapseConfiguration.setAxisConfiguration(cfgCtx.getAxisConfiguration());

        Parameter synapseCtxParam = new Parameter(SynapseConstants.SYNAPSE_CONFIG, null);
        synapseCtxParam.setValue(synapseConfiguration);
        MessageContextCreatorForAxis2.setSynConfig(synapseConfiguration);

        Parameter synapseEnvParam = new Parameter(SynapseConstants.SYNAPSE_ENV, null);
        Axis2SynapseEnvironment synEnv = new Axis2SynapseEnvironment(cfgCtx, synapseConfiguration);
        synapseEnvParam.setValue(synEnv);
        MessageContextCreatorForAxis2.setSynEnv(synEnv);

        try {
            axisConfiguration.addParameter(synapseCtxParam);
            axisConfiguration.addParameter(synapseEnvParam);

        } catch (AxisFault e) {
            String msg =
                "Could not set parameters '" + SynapseConstants.SYNAPSE_CONFIG +
                    "' and/or '" + SynapseConstants.SYNAPSE_ENV +
                    "'to the Axis2 configuration : " + e.getMessage();
            log.fatal(msg, e);
            throw new SynapseException(msg, e);
        }
        synapseConfiguration.init(synEnv);
        
        return synapseConfiguration;
    }

    public void engageNotify(AxisDescription axisDescription) throws AxisFault {
    }

    public boolean canSupportAssertion(Assertion assertion) {
        return false;
    }

    public void applyPolicy(Policy policy, AxisDescription axisDescription) throws AxisFault {
    }

    public void shutdown(ConfigurationContext configurationContext)
        throws AxisFault {
    	synCfg.destroy();
    }
}
