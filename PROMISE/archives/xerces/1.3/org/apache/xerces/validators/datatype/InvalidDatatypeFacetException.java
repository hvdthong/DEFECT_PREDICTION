package org.apache.xerces.validators.datatype;



/**
 * 
 * @author Jeffrey Rodriguez
 * @version $Id: InvalidDatatypeFacetException.java 315999 2000-08-11 02:12:02Z jeffreyr $
 */
public class InvalidDatatypeFacetException extends XMLException {
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

    public InvalidDatatypeFacetException ( ){ 
        super();
    }
    public InvalidDatatypeFacetException ( String message ) {
        super( message );
    }
    public InvalidDatatypeFacetException ( Exception exception ) {
        super( exception );
    }
    public InvalidDatatypeFacetException ( String message, Exception exception ) {
        super( message, exception );
    }
}
