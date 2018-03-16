package org.apache.xerces.validators.datatype;

import java.util.Hashtable;

/**
 * Validator for <gMonthDay> datatype (W3C Schema Datatypes)
 * 
 * @author Elena Litani
 * @version $Id: MonthDayDatatypeValidator.java 317299 2001-06-21 20:56:13Z elena $
 */

public class MonthDayDatatypeValidator extends DateTimeValidator {

    private final static int MONTHDAY_SIZE = 7;

    public  MonthDayDatatypeValidator() throws InvalidDatatypeFacetException{
        super();
    }

    public  MonthDayDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
                                        boolean derivedByList ) throws InvalidDatatypeFacetException {
        super(base, facets, derivedByList);
    }

    /** 
     * Parses, validates and computes normalized version of gMonthDay object
     * 
     * @param str    The lexical representation of gMonthDay object --MM-DD
     *               with possible time zone Z or (-),(+)hh:mm
     * @param date   uninitialized date object
     * @return normalized date representation
     * @exception Exception Invalid lexical representation
     */
    protected int[] parse(String str, int[] date) throws SchemaDateTimeException{

        resetBuffer(str);

        if ( date== null ) {
            date=new int[TOTAL_SIZE];
        }
        resetDateObj(date);

        date[CY]=YEAR;

        if (fBuffer.charAt(0)!='-' || fBuffer.charAt(1)!='-') {
            throw new SchemaDateTimeException("Invalid format for gMonthDay: "+str);
        }
        date[M]=parseInt(fStart+2,fStart+4);
        fStart+=4;
        
        if (fBuffer.charAt(fStart++)!='-') {
            throw new SchemaDateTimeException("Invalid format for gMonthDay: " + str);
        }
        
        date[D]=parseInt(fStart, fStart+2);

        if ( MONTHDAY_SIZE<fEnd ) {
            int sign = findUTCSign(MONTHDAY_SIZE, fEnd);
            if ( sign<0 ) {
                throw new SchemaDateTimeException ("Error in month parsing:" +str);
            }
            else {
                getTimeZone(date, sign);
            }
        }

        validateDateTime(date);
        
        if ( date[utc]!=0 && date[utc]!='Z' ) {
            normalize(date);
        }
        return date;
    }


    /**
     * Converts gMonthDay object representation to String
     * 
     * @param date   gmonthDay object
     * @return lexical representation of month: --MM-DD with an optional time zone sign
     */
    protected String dateToString(int[] date) {
        message.setLength(0);
        message.append('-');
        message.append('-');
        message.append(date[M]);
        message.append('-');
        message.append(date[D]);
        message.append((char)date[utc]);
        return message.toString();
    }

}

