package org.apache.camel.component.jmx;

import javax.management.Notification;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;

/**
 * A {@link Exchange} for JMX notification
 *
 * @version $Revision: 659782 $
 */
public class JMXExchange extends DefaultExchange {

    public JMXExchange(CamelContext camelContext, ExchangePattern pattern, Notification notification) {
        super(camelContext, pattern);
        setIn(new JMXMessage(notification));
    }
}
