package org.apache.camel.component.vm;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.component.seda.SedaComponent;
import org.apache.camel.component.seda.SedaEndpoint;

/**
 * for asynchronous SEDA exchanges on a {@link BlockingQueue} within the classloader tree containing
 * the camel-core.jar. i.e. to handle communicating across CamelContext instances and possibly across
 * web application contexts, providing that camel-core.jar is on the system classpath.
 *
 * @version $Revision: 1.1 $
 */
public class VmComponent extends SedaComponent {
    
    private static final AtomicInteger START_COUNTER = new AtomicInteger();
    protected static Map<String, BlockingQueue> queues = new HashMap<String, BlockingQueue>();

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map parameters) throws Exception {
        BlockingQueue<Exchange> blockingQueue = getBlockingQueue(uri);
        return new SedaEndpoint(uri, this, blockingQueue);
    }

    protected BlockingQueue<Exchange> getBlockingQueue(String uri) {
        synchronized (queues) {
            BlockingQueue<Exchange> answer = queues.get(uri);
            if (answer == null) {
                answer = createQueue();
                queues.put(uri, answer);
            }
            return answer;
        }
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();
        START_COUNTER.incrementAndGet();
    }
    
    @Override
    protected void doStop() throws Exception {
        super.doStop();
        if (START_COUNTER.decrementAndGet() == 0) {
            synchronized (queues) {
                for (BlockingQueue q : queues.values()) {
                    q.clear();
                }
                queues.clear();
            }
        }
    }

}
