package org.apache.camel.component.jmx;

import javax.management.Notification;
import javax.management.NotificationListener;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;

/**
 * Generate an Exchange after getting a JMX Notification
 * @version $Revision: 523016 $
 */
public class JMXConsumer extends DefaultConsumer implements
        NotificationListener {

	JMXEndpoint jmxEndpoint;

	public JMXConsumer(JMXEndpoint endpoint,Processor processor){
		super(endpoint,processor);
		this.jmxEndpoint=endpoint;
	}

	public void handleNotification(Notification notification,Object handback){
		try{
			getProcessor().process(jmxEndpoint.createExchange(notification));
		}catch(Throwable e){
			handleException(e);
		}
	}
}
