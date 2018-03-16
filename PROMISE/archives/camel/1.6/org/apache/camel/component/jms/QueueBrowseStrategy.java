package org.apache.camel.component.jms;

import java.util.List;

import org.apache.camel.Exchange;
import org.springframework.jms.core.JmsOperations;

/**
 * @version $Revision: 647893 $
 */
public interface QueueBrowseStrategy {
    List<Exchange> browse(JmsOperations template, String queue, JmsQueueEndpoint endpoint);
}
