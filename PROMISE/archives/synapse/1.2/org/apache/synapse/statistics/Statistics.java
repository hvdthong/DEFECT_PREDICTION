package org.apache.synapse.statistics;

/**
 * The statistics data structure
 */

public class Statistics {

    /**  Maximum processing time for a one way flow  */
    private long maxProcessingTime = 0;
    /** Minimum processing time for a one way flow   */
    private long minProcessingTime = -1;
    /** Average processing time for a one way flow */
    private double avgProcessingTime = 0;
    /** Total processing time for a one way flow  */
    private double totalProcessingTime;
    /** The number of access count for a one way flow  */
    private int count = 0;
    /** The number of fault count for a one way flow  */
    private int faultCount = 0;

    /**
     * Update the statistics
     *
     * @param inTime  - The processing start time
     * @param outTime - The processing end time
     * @param isFault - A boolean value that indicate whether falut has occured or not
     */
    public void update(long inTime, long outTime, boolean isFault) {

        if (outTime < 0 || inTime < 0) {
            return;
        }

        count++;
        if (isFault) {
            faultCount++;
        }

        long responseTime = outTime - inTime;
        if (maxProcessingTime < responseTime) {
            maxProcessingTime = responseTime;
        }
        if (minProcessingTime > responseTime) {
            minProcessingTime = responseTime;
        }
        if (minProcessingTime == -1) {
            minProcessingTime = responseTime;
        }
        totalProcessingTime = totalProcessingTime + responseTime;
        avgProcessingTime = totalProcessingTime / count;
    }

    /**
     * @return Returns the Maximum processing time
     */
    public long getMaxProcessingTime() {
        return maxProcessingTime;
    }

    /**
     * @return Returns the Avarage processing time
     */
    public double getAvgProcessingTime() {
        return avgProcessingTime;
    }

    /**
     * @return Returns the minimum processing time
     */
    public long getMinProcessingTime() {
        return minProcessingTime;
    }

    /**
     * @return Returns the fault count
     */
    public int getFaultCount() {
        return faultCount;
    }

    /**
     * @return Returns the total count that represents number of access in a one way flow
     */
    public int getCount() {
        return count;
    }
}
