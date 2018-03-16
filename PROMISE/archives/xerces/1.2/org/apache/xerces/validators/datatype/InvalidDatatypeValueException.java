 
package org.apache.xerces.validators.datatype;

/**
 * InvalidDatatypeValueException is thrown when data value doesn't match it's datatype
 * 
 * @author Jeffrey Rodriguez
 * @version $Id: InvalidDatatypeValueException.java 315999 2000-08-11 02:12:02Z jeffreyr $
 */
public class InvalidDatatypeValueException extends XMLException {
    private int majorCode = -1;
    private int minorCode  = -1;

    public  int getMinorCode(){
        return  minorCode;
    }
    public  int getMajorCode(){
        return  majorCode;
    }

    public  void setMinorCode(int code ){
        majorCode = code;
    }
    public  void setMajorCode(int code ){
        minorCode = code;
    }

    public  InvalidDatatypeValueException() { super(); }
    public  InvalidDatatypeValueException(String msg) { super(msg); }
    public  InvalidDatatypeValueException ( Exception exception ) {
        super( exception );
    }
    public InvalidDatatypeValueException ( String message, Exception exception ) {
        super( message, exception );
    }
}
