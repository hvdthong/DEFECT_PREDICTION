package org.apache.camel;

/**
 * Represents a <a
 * Consumer</a> where the caller polls for messages when it is ready.
 * 
 * @version $Revision: 1.1 $
 */
public interface PollingConsumer<E extends Exchange> extends Consumer<E> {

    /**
     * Waits until a message is available and then returns it. Warning that this
     * method could block indefinitely if no messages are available.
     * 
     * @return the message exchange received.
     */
    E receive();

    /**
     * Attempts to receive a message exchange immediately without waiting and
     * returning null if a message exchange is not available yet.
     * 
     * @return the message exchange if one is immediately available otherwise
     *         null
     */
    E receiveNoWait();

    /**
     * Attempts to receive a message exchange, waiting up to the given timeout
     * to expire if a message is not yet available
     * 
     * @param timeout the amount of time in milliseconds to wait for a message
     *                before timing out and returning null
     * 
     * @return the message exchange if one iwas available within the timeout
     *         period, or null if the timeout expired
     */
    E receive(long timeout);

}
