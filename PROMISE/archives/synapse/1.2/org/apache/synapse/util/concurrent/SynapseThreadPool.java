package org.apache.synapse.util.concurrent;

import org.apache.synapse.config.SynapseConfiguration;

import java.util.concurrent.*;

/**
 * This is the executor service that will be returned by the env
 */
public class SynapseThreadPool extends ThreadPoolExecutor {

    public static final int SYNAPSE_CORE_THREADS  = 20;
    public static final int SYNAPSE_MAX_THREADS   = 100;
    public static final int SYNAPSE_KEEP_ALIVE    = 5;
    public static final int SYNAPSE_THREAD_QLEN   = 10;
    public static final String SYNAPSE_THREAD_GROUP     = "synapse-thread-group";
    public static final String SYNAPSE_THREAD_ID_PREFIX = "SynapseWorker";

    public static final String SYN_THREAD_CORE     = "synapse.threads.core";
    public static final String SYN_THREAD_MAX      = "synapse.threads.max";
    public static final String SYN_THREAD_ALIVE    = "synapse.threads.keepalive";
    public static final String SYN_THREAD_QLEN     = "synapse.threads.qlen";
    public static final String SYN_THREAD_GROUP    = "synapse.threads.group";
    public static final String SYN_THREAD_IDPREFIX = "synapse.threads.idprefix";

    /**
     * Constructor for the Synapse thread poll
     * 
     * @param corePoolSize    - number of threads to keep in the pool, even if they are idle
     * @param maximumPoolSize - the maximum number of threads to allow in the pool
     * @param keepAliveTime   - this is the maximum time that excess idle threads will wait
     *  for new tasks before terminating.
     * @param unit            - the time unit for the keepAliveTime argument.
     * @param workQueue       - the queue to use for holding tasks before they are executed.
     */
    public SynapseThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
        TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
            new SynapseThreadFactory(
                new ThreadGroup(SYNAPSE_THREAD_GROUP), SYNAPSE_THREAD_ID_PREFIX));
    }

    /**
     * Default Constructor for the thread pool and will use all the values as default
     */
    public SynapseThreadPool() {
        this(SYNAPSE_CORE_THREADS, SYNAPSE_MAX_THREADS, SYNAPSE_KEEP_ALIVE,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    /**
     * Constructor for the SynapseThreadPool
     * 
     * @param corePoolSize  - number of threads to keep in the pool, even if they are idle
     * @param maxPoolSize   - the maximum number of threads to allow in the pool
     * @param keepAliveTime - this is the maximum time that excess idle threads will wait
     *  for new tasks before terminating.
     * @param qlen          - Thread Blocking Queue length
     * @param threadGroup    - ThreadGroup name
     * @param threadIdPrefix - Thread id prefix
     */
    public SynapseThreadPool(int corePoolSize, int maxPoolSize, long keepAliveTime, int qlen,
        String threadGroup, String threadIdPrefix) {
        super(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(qlen),
            new SynapseThreadFactory(new ThreadGroup(threadGroup), threadIdPrefix));
    }
}
