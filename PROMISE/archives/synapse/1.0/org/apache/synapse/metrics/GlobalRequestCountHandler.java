package org.apache.synapse.metrics;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;

/*
 * This information is published using WS-Management.
 * Access from any location with XXContext.getParameter(Constants.GLOBAL_REQUEST_COUNTER);
 */

public class GlobalRequestCountHandler extends AbstractHandler {

    public InvocationResponse invoke(MessageContext msgContext) throws AxisFault {
        msgContext
                .setProperty(Constants.REQUEST_RECEIVED_TIME, new Long(System.currentTimeMillis()));
        ((Counter) msgContext.getParameter(Constants.GLOBAL_REQUEST_COUNTER).getValue())
                .increment();
        return InvocationResponse.CONTINUE;
    }
}
