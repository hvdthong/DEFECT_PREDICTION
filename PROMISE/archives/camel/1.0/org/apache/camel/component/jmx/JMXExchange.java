package org.apache.camel.component.jmx;

import javax.management.Notification;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;

/**
 * A {@link Exchange} for a jmx notification
 * 
 * @version $Revision: 520985 $
 */
public class JMXExchange extends DefaultExchange {

	/**
	 * Constructor
	 * @param camelContext
	 * @param file
	 */
	public JMXExchange(CamelContext camelContext,Notification notification){
		super(camelContext);
		setIn(new JMXMessage(notification));
	}
}
