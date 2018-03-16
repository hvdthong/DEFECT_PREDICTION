package org.apache.camel.processor;

import java.io.Serializable;
import java.util.Random;


/**
 * The policy used to decide how many times to redeliver and the time between the redeliveries before being sent to a
 *
 * @version $Revision: 530858 $
 */
public class RedeliveryPolicy implements Cloneable, Serializable {
    protected static transient Random randomNumberGenerator;
    protected int maximumRedeliveries = 6;
    protected long initialRedeliveryDelay = 1000L;
    protected double backOffMultiplier = 2;
    protected boolean useExponentialBackOff = false;
    protected double collisionAvoidanceFactor = 0.15d;
    protected boolean useCollisionAvoidance = false;

    public RedeliveryPolicy() {
    }

    @Override
    public String toString() {
        return "RedeliveryPolicy[maximumRedeliveries=" + maximumRedeliveries + "]";
    }

    public RedeliveryPolicy copy() {
        try {
            return (RedeliveryPolicy) clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException("Could not clone: " + e, e);
        }
    }

    /**
     * Returns true if the policy decides that the message exchange should be redelivered
     */
    public boolean shouldRedeliver(int redeliveryCounter) {
        return redeliveryCounter < getMaximumRedeliveries();
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
     * Enables collision avoidence which adds some randomization to the backoff timings to reduce contention probability
     */
    public RedeliveryPolicy useCollisionAvoidance() {
        setUseCollisionAvoidance(true);
        return this;
    }

    /**
     * Enables exponential backof using the {@link #getBackOffMultiplier()} to increase the time between retries
     */
    public RedeliveryPolicy useExponentialBackOff() {
        setUseExponentialBackOff(true);
        return this;
    }

    /**
     * Enables exponential backoff and sets the multiplier used to increase the delay between redeliveries
     */
    public RedeliveryPolicy backOffMultiplier(double backOffMultiplier) {
        useExponentialBackOff();
        setBackOffMultiplier(backOffMultiplier);
        return this;
    }

    /**
     * Enables collision avoidence and sets the percentage used
     */
    public RedeliveryPolicy collisionAvoidancePercent(short collisionAvoidancePercent) {
        useCollisionAvoidance();
        setCollisionAvoidancePercent(collisionAvoidancePercent);
        return this;
    }

    public double getBackOffMultiplier() {
        return backOffMultiplier;
    }

    /**
     * Sets the multiplier used to increase the delay between redeliveries if {@link #setUseExponentialBackOff(boolean)} is enabled
     */
    public void setBackOffMultiplier(double backOffMultiplier) {
        this.backOffMultiplier = backOffMultiplier;
    }

    public short getCollisionAvoidancePercent() {
        return (short) Math.round(collisionAvoidanceFactor * 100);
    }

    /**
     * Sets the percentage used for collision avoidence if enabled via {@link #setUseCollisionAvoidance(boolean)}
     */
    public void setCollisionAvoidancePercent(short collisionAvoidancePercent) {
        this.collisionAvoidanceFactor = collisionAvoidancePercent * 0.01d;
    }

    public double getCollisionAvoidanceFactor() {
        return collisionAvoidanceFactor;
    }

    /**
     * Sets the factor used for collision avoidence if enabled via {@link #setUseCollisionAvoidance(boolean)}
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
     * Sets the maximum number of times a message exchange will be redelivered
     */
    public void setMaximumRedeliveries(int maximumRedeliveries) {
        this.maximumRedeliveries = maximumRedeliveries;
    }

    public long getRedeliveryDelay(long previousDelay) {
        long redeliveryDelay;

        if (previousDelay == 0) {
            redeliveryDelay = initialRedeliveryDelay;
        }
        else if (useExponentialBackOff && backOffMultiplier > 1) {
            redeliveryDelay = Math.round(backOffMultiplier * previousDelay);
        }
        else {
            redeliveryDelay = previousDelay;
        }

        if (useCollisionAvoidance) {

            /*
             * First random determines +/-, second random determines how far to
             * go in that direction. -cgs
             */
            Random random = getRandomNumberGenerator();
            double variance = (random.nextBoolean() ? collisionAvoidanceFactor : -collisionAvoidanceFactor) * random.nextDouble();
            redeliveryDelay += redeliveryDelay * variance;
        }

        return redeliveryDelay;
    }

    public boolean isUseCollisionAvoidance() {
        return useCollisionAvoidance;
    }

    /**
     * Enables/disables collision avoidence which adds some randomization to the backoff timings to reduce contention probability
     */
    public void setUseCollisionAvoidance(boolean useCollisionAvoidance) {
        this.useCollisionAvoidance = useCollisionAvoidance;
    }

    public boolean isUseExponentialBackOff() {
        return useExponentialBackOff;
    }

    /**
     * Enables/disables exponential backof using the {@link #getBackOffMultiplier()} to increase the time between retries
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
