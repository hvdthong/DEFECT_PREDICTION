package org.w3c.dom.xpath;

/**
 * A new exception has been created for exceptions specific to these XPath 
 * interfaces.
 */
public class XPathException extends RuntimeException {
    public XPathException(short code, String message) {
       super(message);
       this.code = code;
    }
    public short   code;
    /**
     * If the expression has a syntax error or otherwise is not a legal 
     * expression according to the rules of the specific 
     * <code>XPathEvaluator</code> or contains specialized extension 
     * functions or variables not supported by this implementation.
     */
    public static final short INVALID_EXPRESSION_ERR    = 1;
    /**
     * If the expression cannot be converted to return the specified type.
     */
    public static final short TYPE_ERR                  = 2;

}
