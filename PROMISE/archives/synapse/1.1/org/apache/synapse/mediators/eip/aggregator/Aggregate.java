package org.apache.synapse.mediators.eip.aggregator;

import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.mediators.eip.EIPConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.ArrayList;

/**
 * This holds the Aggregate properties and the list of messages which participate in the aggregation
 */
public class Aggregate {

    /**
     *
     */
    private static final Log log = LogFactory.getLog(Aggregate.class);

    /**
     *
     */
    private static final Log trace = LogFactory.getLog(SynapseConstants.TRACE_LOGGER);

    /**
     *
     */
    private long timeout = 0;

    /**
     *
     */
    private long expireTime = 0;

    /**
     *
     */
    private int minCount = -1;

    /**
     *
     */
    private int maxCount = -1;

    /**
     *
     */
    private String corelation = null;

    /**
     *
     */
    private List messages = new ArrayList();

    /**
     * This is the constructor of the Aggregate which will set the timeout depending on the
     * timeout for the aggregate
     *
     * @param corelation - String representing the corelation name of the messages in the aggregate
     * @param timeout -
     * @param min -
     * @param max -
     */
    public Aggregate(String corelation, long timeout, int min, int max) {
        this.corelation = corelation;
        if (timeout > 0) {
            this.timeout = System.currentTimeMillis() + expireTime;
        }
        if (min > 0) {
            this.minCount = min;
        }
        if (max > 0) {
            this.maxCount = max;
        }
    }

    /**
     * @param synCtx -
     * @return true if the message was added and false if not
     */
    public boolean addMessage(MessageContext synCtx) {
        if (this.maxCount > 0 && this.messages.size() < this.maxCount || this.maxCount <= 0) {
            this.messages.add(synCtx);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return boolean stating the completeness of the corelation
     */
    public boolean isComplete() {

        boolean completed = false;
        if (!messages.isEmpty()) {

            Object o = messages.get(0);
            if (o instanceof MessageContext) {

                Object prop = ((MessageContext) o).getProperty(EIPConstants.MESSAGE_SEQUENCE);
                if (prop instanceof String) {

                    String[] msgSequence
                            = prop.toString().split(EIPConstants.MESSAGE_SEQUENCE_DELEMITER);
                    if (messages.size() >= Integer.parseInt(msgSequence[1])) {
                        completed = true;
                    }
                }
            }
        }

        if (!completed && this.minCount > 0) {
            completed = this.messages.size() >= this.minCount
                    || this.timeout < System.currentTimeMillis();
        }

        return completed;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public String getCorelation() {
        return corelation;
    }

    public void setCorelation(String corelation) {
        this.corelation = corelation;
    }

    public List getMessages() {
        return messages;
    }

    public void setMessages(List messages) {
        this.messages = messages;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

}
