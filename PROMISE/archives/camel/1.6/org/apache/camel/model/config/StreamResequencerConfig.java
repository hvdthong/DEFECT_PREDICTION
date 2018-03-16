package org.apache.camel.model.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.camel.Exchange;
import org.apache.camel.processor.StreamResequencer;
import org.apache.camel.processor.resequencer.DefaultExchangeComparator;
import org.apache.camel.processor.resequencer.ExpressionResultComparator;

/**
 * Defines the configuration parameters for the {@link StreamResequencer}.
 * Usage example:
 * 
 * <pre>
 * from(&quot;direct:start&quot;).resequencer(header(&quot;seqnum&quot;)).stream(
 *         StreamResequencerConfig.getDefault()).to(&quot;mock:result&quot;)
 * </pre>
 * 
 * is equivalent to
 * 
 * <pre>
 * from(&quot;direct:start&quot;).resequencer(header(&quot;seqnum&quot;)).stream().to(&quot;mock:result&quot;)
 * </pre>
 * 
 * Custom values for <code>capacity</code> and <code>timeout</code> can be
 * set like in this example:
 * 
 * <pre>
 * from(&quot;direct:start&quot;).resequencer(header(&quot;seqnum&quot;)).stream(
 *         new StreamResequencerConfig(300, 400L)).to(&quot;mock:result&quot;)
 * </pre>
 * 
 * @author Martin Krasser
 * 
 * @version $Revision: 697732 $
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StreamResequencerConfig {

    @XmlAttribute

    @XmlAttribute
    
    @XmlTransient
    private ExpressionResultComparator<Exchange> comparator;

    /**
     * Creates a new {@link StreamResequencerConfig} instance using default
     * values for <code>capacity</code> (1000) and <code>timeout</code>
     * (1000L). Elements of the sequence are compared using the
     * {@link DefaultExchangeComparator}.
     */
    public StreamResequencerConfig() {
        this(1000, 1000L);
    }

    /**
     * Creates a new {@link BatchResequencerConfig} instance using the given
     * values for <code>capacity</code> and <code>timeout</code>. Elements
     * of the sequence are compared using the {@link DefaultExchangeComparator}.
     * 
     * @param capacity   capacity of the resequencer's inbound queue.
     * @param timeout    minimum time to wait for missing elements (messages).
     */
    public StreamResequencerConfig(int capacity, long timeout) {
        this(capacity, timeout, new DefaultExchangeComparator());
    }

    /**
     * Creates a new {@link BatchResequencerConfig} instance using the given
     * values for <code>capacity</code> and <code>timeout</code>. Elements
     * of the sequence are compared with the given
     * {@link ExpressionResultComparator}.
     * 
     * @param capacity   capacity of the resequencer's inbound queue.
     * @param timeout    minimum time to wait for missing elements (messages).
     */
    public StreamResequencerConfig(int capacity, long timeout, ExpressionResultComparator<Exchange> comparator) {
        this.capacity = capacity;
        this.timeout = timeout;
        this.comparator = comparator;
    }

    /**
     * Returns a new {@link StreamResequencerConfig} instance using default
     * values for <code>capacity</code> (1000) and <code>timeout</code>
     * (1000L). Elements of the sequence are compared using the
     * {@link DefaultExchangeComparator}.
     * 
     * @return a default {@link StreamResequencerConfig}.
     */
    public static StreamResequencerConfig getDefault() {
        return new StreamResequencerConfig();
    }
    
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public ExpressionResultComparator<Exchange> getComparator() {
        return comparator;
    }

    public void setComparator(ExpressionResultComparator<Exchange> comparator) {
        this.comparator = comparator;
    }
    
}
