package org.apache.synapse.statistics;

/**
 *  This interface need to be implemented by each of the entry that
 *  need to collect statistics
 *
 */

public interface StatisticsStack {

    /**
     * This method is used to put current statistics
     * @param key
     * @param initTime
     * @param isInFlow
     * @param isStatisticsEnable
     * @param isFault
     */
    public void put(String key,long initTime, boolean isInFlow, boolean isStatisticsEnable,
                    boolean isFault);

    /**
     * This method used to report the latest  statistics to the StatisticsCollector
     * @param statisticsCollector
     * @param isFault
     */
    public void reportToStatisticsCollector(StatisticsCollector statisticsCollector,
                                            boolean isFault);

    /**
     * Report the particular statistics to the StatisticsCollector
     *
     * @param statisticsCollector
     * @param isFault
     * @param name
     */
    public void reportToStatisticsCollector(StatisticsCollector statisticsCollector,
                                            boolean isFault, String name);

    /**
     * This method  used to unreported all statistics to the StatisticsCollector
     * @param statisticsCollector
     * @param isFault
     */
    public void reportAllToStatisticsCollector(StatisticsCollector statisticsCollector,
                                               boolean isFault);

}
