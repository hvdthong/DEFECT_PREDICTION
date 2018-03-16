package org.apache.camel;



/**
 * A more complex version of {@link Processor} which supports asynchronous
 * processing of the {@link Exchange}.  Any processor can be coerced to
 * have an {@link AsyncProcessor} interface by using the
 * {@link org.apache.camel.impl.converter.AsyncProcessorTypeConverter#convert AsyncProcessorTypeConverter.covert}
 * method.
 * 
 * @version $Revision: 642753 $
 */
public interface AsyncProcessor extends Processor {

    /**
     * Processes the message exchange.  Similar to {@link Processor#process}, but
     * the caller supports having the exchange asynchronously processed.
     *
     * @param exchange the {@link Exchange} to process
     * @param  callback the {@link AsyncCallback} will be invoked when the processing
     *         of the exchange is completed. If the exchange is completed synchronously, then the 
     *         callback is also invoked synchronously.  The callback should therefore be careful of
     *         starting recursive loop.
     *         
     * @return true if the processing was completed synchronously.
     */
    boolean process(Exchange exchange, AsyncCallback callback);
    
}
