package org.apache.synapse.core.axis2;

import org.apache.axis2.AxisFault;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.Constants;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.SynapseEnvironment;

/**
 * The MessageContext needs to be set up and then is used by the SynapseMessageReceiver to inject messages.
 * This class is used by the SynapseMessageReceiver to find the environment. The env is stored in a Parameter to the Axis2 config
 */
public class MessageContextCreatorForAxis2 implements Constants {

    private static Log log = LogFactory.getLog(MessageContextCreatorForAxis2.class);

    private static SynapseConfiguration synCfg = null;
    private static SynapseEnvironment   synEnv = null;

    public static MessageContext getSynapseMessageContext(
            org.apache.axis2.context.MessageContext axisMsgCtx) throws AxisFault {

        if (synCfg == null || synEnv == null) {
            String msg = "Synapse environment has not initialized properly..";
            log.fatal(msg);
            throw new SynapseException(msg);
        }

        return new Axis2MessageContext(axisMsgCtx, synCfg, synEnv);
    }

    public static void setSynConfig(SynapseConfiguration synCfg) {
        MessageContextCreatorForAxis2.synCfg = synCfg;
    }

    public static void setSynEnv(SynapseEnvironment synEnv) {
        MessageContextCreatorForAxis2.synEnv = synEnv;
    }
}
