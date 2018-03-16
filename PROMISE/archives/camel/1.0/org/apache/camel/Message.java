package org.apache.camel;

import java.util.Map;

/**
 * pattern and represents an inbound or outbound message as part of an {@link Exchange}
 * 
 * @version $Revision: 540152 $
 */
public interface Message {
    
    /**
     * @return the id of the message
     */
    String getMessageId();
    
    /**
     * set the id of the message
     * @param messageId
     */
    void setMessageId(String messageId);

    /**
     * Returns the exchange this message is related to
     * 
     * @return
     */
    Exchange getExchange();
    
    /**
     * Accesses a specific header
     *
     * @param name
     * @return object header associated with the name
     */
    Object getHeader(String name);

    /**
     * Returns a header associated with this message by name and specifying the type required
     *
     * @param name the name of the header
     * @param type the type of the header
     * @return the value of the given header or null if there is no property for the given name or it cannot be
     * converted to the given type
     */
    <T> T getHeader(String name, Class<T> type);

    /**
     * Sets a header on the message
     *
     * @param name  of the header
     * @param value to associate with the name
     */
    void setHeader(String name, Object value);

    /**
     * Returns all of the headers associated with the message
     *
     * @return all the headers in a Map
     */
    Map<String, Object> getHeaders();
    
    /**
     * Set all the headers associated with this message
     * @param headers
     */
    void setHeaders(Map<String,Object> headers);

    /**
     * Returns the body of the message as a POJO
     *
     * @return the body of the message
     */
    public Object getBody();

    /**
     * Returns the body as the specified type
     *
     * @param type the type that the body
     * @return the body of the message as the specified type
     */
    public <T> T getBody(Class<T> type);

    /**
     * Sets the body of the message
     */
    public void setBody(Object body);

    /**
     * Sets the body of the message as a specific type
     */
    public <T> void setBody(Object body, Class<T> type);

    /**
     * Creates a copy of this message so that it can be used and possibly modified further in another exchange
     * 
     * @return a new message instance copied from this message
     */
    Message copy();

}
