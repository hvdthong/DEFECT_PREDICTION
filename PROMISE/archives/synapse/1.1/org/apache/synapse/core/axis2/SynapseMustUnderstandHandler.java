package org.apache.synapse.core.axis2;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This is a handler for axis2 which will clear the mustUnderstand ness of the headers
 * if there are any after the Dispatch, which will allow Synapse to get the message
 * even with unprocessed mustUnderstand headers
 */
public class SynapseMustUnderstandHandler extends AbstractHandler {
    
    public InvocationResponse invoke(MessageContext messageContext) throws AxisFault {

        SOAPEnvelope envelope = messageContext.getEnvelope();

        if (envelope.getHeader() != null) {
            Iterator headerBlocks = envelope.getHeader().getHeadersToProcess(null);
            ArrayList markedHeaderBlocks = new ArrayList();

            while (headerBlocks.hasNext()) {
                SOAPHeaderBlock headerBlock = (SOAPHeaderBlock) headerBlocks.next();

                if (!headerBlock.isProcessed() && headerBlock.getMustUnderstand()) {
                    markedHeaderBlocks.add(headerBlock);
                    headerBlock.setProcessed();
                }
            }

            messageContext.setProperty("headersMarkedAsProcessedBySynapse", markedHeaderBlocks);
        }

        return InvocationResponse.CONTINUE;
    }
}
