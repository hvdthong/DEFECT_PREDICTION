package org.apache.camel.processor;

import java.io.Serializable;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * The policy used to decide how many times to redeliver and the time between
 * the redeliveries before being sent to a <a
 * Channel</a>
 * <p>
 * The default values is:
 * <ul>
 *   <li>maximumRedeliveries = 6</li>
 *   <li>initialRedeliveryDelay = 1000L</li>
 *   <li>maximumRedeliveryDelay = 60 * 1000L</li>
 *   <li>backOffMultiplier = 2</li>
 *   <li>useExponentialBackOff = false</li>
 *   <li>collisionAvoidanceFactor = 0.15d</li>
 *   <li>useCollisionAvoidance = false</li>
 * </ul>
 *
 * @version $Revision: 674383 $
 */
public class RedeliveryPolicy implements Cloneable, Serializable {
    protected static transient Random randomNumberGenerator;
    private static final transient Log LOG = LogFactory.getLog(RedeliveryPolicy.class);

    protected int maximumRedeliveries = 6;
    protected long initialRedeliveryDelay = 1000L;
    protected long maximumRedeliveryDelay = 60 * 1000L;
    protected double backOffMultiplier = 2;
    protected boolean useExponentialBackOff;
    protected double collisionAvoidanceFactor = 0.15d;
    protected boolean useCollisionAvoidance;

    public RedeliveryPolicy() {
    }

    @Override
    public String toString() {
        return "RedeliveryPolicy[maximumRedeliveries=" + maximumRedeliveries + "]";
    }

    public RedeliveryPolicy copy() {
        try {
            return (RedeliveryPolicy)clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Could not clone: " + e, e);
        }
    }

    /**
     * Returns true if the policy decides that the message exchange should be
     * redelivered
     */
    public boolean shouldRedeliver(int redeliveryCounter) {
        if (getMaximumRedeliveries() < 0) {
            return true;
        }
        return redeliveryCounter < getMaximumRedeliveries();
    }


    /**
     * Calculates the new redelivery delay based on the last one then sleeps for the necessary amount of time
     */
    public long sleep(long redeliveryDelay) {
        redeliveryDelay = getRedeliveryDelay(redeliveryDelay);

        if (redeliveryDelay > 0) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Sleeping for: " + redeliveryDelay + " millis until attempting redelivery");
            }
            try {
                Thread.sleep(redeliveryDelay);
            } catch (InterruptedException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Thread interrupted: " + e, e);
                }
            }
        }
        return redeliveryDelay;
    }


    public long getRedeliveryDelay(long previousDelay) {
        long redeliveryDelay;

        if (previousDelay == 0) {
            redeliveryDelay = initialRedeliveryDelay;
        } else if (useExponentialBackOff && backOffMultiplier > 1) {
            redeliveryDelay = Math.round(backOffMultiplier * previousDelay);
        } else {
            redeliveryDelay = previousDelay;
        }

        if (useCollisionAvoidance) {

            /*
             * First random determines +/-, second random determines how far to
             * go in that direction. -cgs
             */
            Random random = getRandomNumberGenerator();
            double variance = (random.nextBoolean() ? collisionAvoidanceFactor : -collisionAvoidanceFactor)
                              * random.nextDouble();
            redeliveryDelay += redeliveryDelay * variance;
        }

        if (maximumRedeliveryDelay > 0 && redeliveryDelay > maximumRedeliveryDelay) {
            redeliveryDelay = maximumRedeliveryDelay;
        }

        return redeliveryDelay;
    }



    /**
     * Sets the maximum number of times a message exchange will be redelivered
     */
    public RedeliveryPolicy maximumRedeliveries(int maximumRedeliveries) {
        setMaximumRedeliveries(maximumRedeliveries);
        return this;
    }

    /**
     * Sets the initial redelivery delay in milliseconds on the first redelivery
     */
    public RedeliveryPolicy initialRedeliveryDelay(long initialRedeliveryDelay) {
        setInitialRedeliveryDelay(initialRedeliveryDelay);
        return this;
    }

    /**
     * Enables collision avoidence which adds some randomization to the backoff
     * timings to reduce contention probability
     */
    public RedeliveryPolicy useCollisionAvoidance() {
        setUseCollisionAvoidance(true);
        return this;
    }

    /**
     * Enables exponential backof using the {@link #getBackOffMultiplier()} to
     * increase the time between retries
     */
    public RedeliveryPolicy useExponentialBackOff() {
        setUseExponentialBackOff(true);
        return this;
    }

    /**
     * Enables exponential backoff and sets the multiplier used to increase the
     * delay between redeliveries
     */
    public RedeliveryPolicy backOffMultiplier(double multiplier) {
        useExponentialBackOff();
        setBackOffMultiplier(multiplier);
        return this;
    }

    /**
     * Enables collision avoidence and sets the percentage used
     */
    public RedeliveryPolicy collisionAvoidancePercent(double collisionAvoidancePercent) {
        useCollisionAvoidance();
        setCollisionAvoidancePercent(collisionAvoidancePercent);
        return this;
    }

    /**
     * Sets the maximum redelivery delay if using exponential back off.
     * Use -1 if you wish to have no maximum
     */
    public RedeliveryPolicy maximumRedeliveryDelay(long maximumRedeliveryDelay) {
        setMaximumRedeliveryDelay(maximumRedeliveryDelay);
        return this;
    }

    public double getBackOffMultiplier() {
        return backOffMultiplier;
    }

    /**
     * Sets the multiplier used to increase the delay between redeliveries if
     * {@link #setUseExponentialBackOff(boolean)} is enabled
     */
    public void setBackOffMultiplier(double backOffMultiplier) {
        this.backOffMultiplier = backOffMultiplier;
    }

    public short getCollisionAvoidancePercent() {
        return (short)Math.round(collisionAvoidanceFactor * 100);
    }

    /**
     * Sets the percentage used for collision avoidence if enabled via
     * {@link #setUseCollisionAvoidance(boolean)}
     */
    public void setCollisionAvoidancePercent(double collisionAvoidancePercent) {
        this.collisionAvoidanceFactor = collisionAvoidancePercent * 0.01d;
    }

    public double getCollisionAvoidanceFactor() {
        return collisionAvoidanceFactor;
    }

    /**
     * Sets the factor used for collision avoidence if enabled via
     * {@link #setUseCollisionAvoidance(boolean)}
     */
    public void setCollisionAvoidanceFactor(double collisionAvoidanceFactor) {
        this.collisionAvoidanceFactor = collisionAvoidanceFactor;
    }

    public long getInitialRedeliveryDelay() {
        return initialRedeliveryDelay;
    }

    /**
     * Sets the initial redelivery delay in milliseconds on the first redelivery
     */
    public void setInitialRedeliveryDelay(long initialRedeliveryDelay) {
        this.initialRedeliveryDelay = initialRedeliveryDelay;
    }

    public int getMaximumRedeliveries() {
        return maximumRedeliveries;
    }

    /**
     * Sets the maximum number of times a message exchange will be redelivered.
     * Setting a negative value will retry forever.
     */
    public void setMaximumRedeliveries(int maximumRedeliveries) {
        this.maximumRedeliveries = maximumRedeliveries;
    }

    public long getMaximumRedeliveryDelay() {
        return maximumRedeliveryDelay;
    }

    /**
     * Sets the maximum redelivery delay if using exponential back off.
     * Use -1 if you wish to have no maximum
     */
    public void setMaximumRedeliveryDelay(long maximumRedeliveryDelay) {
        this.maximumRedeliveryDelay = maximumRedeliveryDelay;
    }

    public boolean isUseCollisionAvoidance() {
        return useCollisionAvoidance;
    }

    /**
     * Enables/disables collision avoidence which adds some randomization to the
     * backoff timings to reduce contention probability
     */
    public void setUseCollisionAvoidance(boolean useCollisionAvoidance) {
        this.useCollisionAvoidance = useCollisionAvoidance;
    }

    public boolean isUseExponentialBackOff() {
        return useExponentialBackOff;
    }

    /**
     * Enables/disables exponential backof using the
     * {@link #getBackOffMultiplier()} to increase the time between retries
     */
    public void setUseExponentialBackOff(boolean useExponentialBackOff) {
        this.useExponentialBackOff = useExponentialBackOff;
    }

    protected static synchronized Random getRandomNumberGenerator() {
        if (randomNumberGenerator == null) {
            randomNumberGenerator = new Random();
        }
        return randomNumberGenerator;
    }
}
