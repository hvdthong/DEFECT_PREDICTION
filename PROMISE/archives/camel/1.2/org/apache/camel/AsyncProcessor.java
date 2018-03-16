package org.apache.camel;



/**
 * A more complex version of {@see Processor} which supports asynchronous
 * processing of the {@see Exchange}.  Any processor can be coerced to
 * have an {@see AsyncProcessor} interface by using the {@see AsyncProcessorTypeConverter.convert()}
 * method.
 * 
 * @version $Revision: 575602 $
 */
public interface AsyncProcessor extends Processor {

    /**
     * Processes the message exchange.  Similar to {@see Processor.process}, but
     * the caller supports having the exchange asynchronously processed.
     *
     * @param exchange the exchange to process
     * @param  callback The @{see AsyncCallback} will be invoked when the processing
     *         of the exchange is completed. If the exchange is completed synchronously, then the 
     *         callback is also invoked synchronously.  The callback should therefore be careful of
     *         starting recursive loop.
     *         
     * @return true if the processing was completed synchronously.
     */
    boolean process(Exchange exchange, AsyncCallback callback);
    
}
