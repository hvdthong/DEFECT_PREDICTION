package org.apache.xerces.utils.regex;



/**
 *
 * @author TAMURA Kent &lt;kent@trl.ibm.co.jp&gt;
 */
public class ParseException extends RuntimeException {
    int location;

    /*
    public ParseException(String mes) {
        this(mes, -1);
    }
    */
    /**
     *
     */
    public ParseException(String mes, int location) {
        super(mes);
        this.location = location;
    }

    /**
     *
     * @return -1 if location information is not available.
     */
    public int getLocation() {
        return this.location;
    }
}
