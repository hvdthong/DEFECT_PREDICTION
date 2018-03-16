package org.apache.xerces.validators.datatype;


/**
 * 
 * @author Jeffrey Rodriguez
 * @version $Id: XMLException.java 315757 2000-06-07 21:41:53Z ericye $
 */
public class XMLException extends Exception {

    public XMLException ( ) {
    }

    public XMLException ( String message ) {
        super(message);
    }
    public XMLException ( Exception exception ) {
    }
    public XMLException ( String message, Exception exception ) {
    }
    public Exception getException ( ) {
        return null;
    }
}
