package org.apache.camel;


/**
 * The callback interface for an {@link AsyncProcessor} so that it can
 * notify you when an {@link Exchange} has completed. 
 */
public interface AsyncCallback {
    
    /**
     * This method is invoked once the Exchange is completed.  If an error 
     * occurred while processing the exchange, the exception field of the 
     * {@link Exchange} being processed will hold the error. 
     *  
     * @param doneSynchronously set to true if the processing of the exchange was completed synchronously thread.
     */
    void done(boolean doneSynchronously);    
    
}
