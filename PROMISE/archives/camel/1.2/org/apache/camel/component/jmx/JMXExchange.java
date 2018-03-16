package org.apache.camel.component.jmx;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;

import javax.management.Notification;

/**
 * A {@link Exchange} for a jmx notification
 * 
 * @version $Revision: 520985 $
 */
public class JMXExchange extends DefaultExchange {

    /**
     * Constructor
     * 
     * @param camelContext
     * @param pattern
     */
    public JMXExchange(CamelContext camelContext, ExchangePattern pattern, Notification notification) {
        super(camelContext, pattern);
        setIn(new JMXMessage(notification));
    }
}
