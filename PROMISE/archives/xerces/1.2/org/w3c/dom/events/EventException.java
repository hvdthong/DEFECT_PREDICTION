package org.w3c.dom.events;

/**
 *  Event operations may throw an <code>EventException</code> as specified in 
 * their method descriptions. 
 */
public class EventException extends RuntimeException {
    public EventException(short code, String message) {
       super(message);
       this.code = code;
    }
    public short   code;
    public static final short UNSPECIFIED_EVENT_TYPE_ERR = 0;

}

