package org.w3c.dom.ranges;

/**
 * Range operations may throw a <code>RangeException</code> as specified in 
 * their method descriptions.
 * @since DOM Level 2
 */
public class RangeException extends RuntimeException {
    public RangeException(short code, String message) {
       super(message);
       this.code = code;
    }
    public short   code;
    /**
     * If the boundary-points of a Range do not meet specific requirements.
     */
    public static final short BAD_BOUNDARYPOINTS_ERR    = 1;
    /**
     * If the container of an boundary-point of a Range is being set to either 
     * a node of an invalid type or a node with an ancestor of an invalid 
     * type.
     */
    public static final short INVALID_NODE_TYPE_ERR     = 2;

}
