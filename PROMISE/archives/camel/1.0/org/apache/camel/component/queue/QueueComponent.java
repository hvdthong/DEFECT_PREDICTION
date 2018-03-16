package org.apache.camel.component.queue;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Exchange;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Represents the component that manages {@link QueueEndpoint}.  It holds the 
 * list of named queues that queue endpoints reference.
 *
 * @org.apache.xbean.XBean
 * @version $Revision: 519973 $
 */
public class QueueComponent<E extends Exchange> extends DefaultComponent<E> {
	
	public BlockingQueue<E> createQueue() {
		return new LinkedBlockingQueue<E>();
	}

    @Override
    protected Endpoint<E> createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        return new QueueEndpoint<E>(uri, this);
    }
}
