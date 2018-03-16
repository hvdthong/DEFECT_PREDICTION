package org.w3c.dom.events;

/**
 *  Event operations may throw an <code>EventException</code> as specified in 
 * their method descriptions. 
 * @since DOM Level 2
 */
public class EventException extends RuntimeException {
    public EventException(short code, String message) {
       super(message);
       this.code = code;
    }
    public short   code;
    /**
     *  If the <code>Event</code>'s type was not specified by initializing the 
     * event before the method was called. Specification of the Event's type 
     * as <code>null</code> or an empty string will also trigger this 
     * exception. 
     */
    public static final short UNSPECIFIED_EVENT_TYPE_ERR = 0;

}
