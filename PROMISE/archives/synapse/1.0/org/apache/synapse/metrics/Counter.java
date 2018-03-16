package org.apache.synapse.metrics;
/*
 * This is the basic unit to get Global, Proxy Service or
 * Proxy Opertaion Level hit count. 
 */

public class Counter {

    private long count = 0;

    public synchronized void increment(){
        count++;
    }

    public long getCount() {
        return count;
    }
}
