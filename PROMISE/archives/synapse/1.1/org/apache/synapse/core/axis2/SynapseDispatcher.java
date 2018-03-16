package org.apache.synapse.core.axis2;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.HandlerDescription;
import org.apache.axis2.engine.AbstractDispatcher;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.synapse.SynapseConstants;

import javax.xml.namespace.QName;

/**
 * This is the Axis2 Dispatcher which is registered with the Axis2 engine. It dispatches
 * each and every message received to the SynapseMessageReceiver for processing.
 */
public class SynapseDispatcher extends AbstractDispatcher {

    public void initDispatcher() {
        HandlerDescription hd = new HandlerDescription(qn.getLocalPart());
        super.init(hd);
    }

    public AxisService findService(MessageContext mc) throws AxisFault {
        AxisConfiguration ac = mc.getConfigurationContext().getAxisConfiguration();
        AxisService as = ac.getService(SynapseConstants.SYNAPSE_SERVICE_NAME);
        return as;
    }

    public AxisOperation findOperation(AxisService svc, MessageContext mc) throws AxisFault {
        AxisOperation ao = svc.getOperation(SynapseConstants.SYNAPSE_OPERATION_NAME);
        return ao;
    }
}
