package org.apache.xerces.validators.datatype;

import java.util.Hashtable;

/**
 * Validator for <gYear> datatype (W3C Schema Datatypes)
 * 
 * @author Elena Litani
 * @version $Id: YearDatatypeValidator.java 317299 2001-06-21 20:56:13Z elena $
 */

public class YearDatatypeValidator extends DateTimeValidator {

    public  YearDatatypeValidator() throws InvalidDatatypeFacetException{
        super();
    }


    public  YearDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
                                         boolean derivedByList ) throws InvalidDatatypeFacetException {
        super (base, facets, derivedByList);
    }

    /**
     * Parses, validates and computes normalized version of gYear object
     * 
     * @param str    The lexical representation of year object CCYY
     *               with possible time zone Z or (-),(+)hh:mm
     * @param date   uninitialized date object
     * @return normalized date representation
     * @exception Exception Invalid lexical representation
     */
    protected int[] parse(String str, int[] date) throws SchemaDateTimeException{
        resetBuffer(str);

        if ( date == null ) {
            date = new int[TOTAL_SIZE];
        }
        resetDateObj(date);

        int start = 0;
        if (fBuffer.charAt(0)=='-') {
            start = 1;
        }
        int sign = findUTCSign(start, fEnd);
        if (sign == -1) {
            date[CY]=parseIntYear(fEnd);
        }
        else {
            date[CY]=parseIntYear(sign);
            getTimeZone (date, sign);
        }

        date[M]=MONTH;
        date[D]=1;


        validateDateTime(date);                       
        
        if ( date[utc]!=0 && date[utc]!='Z' ) {
            normalize(date);
        }
        return date;
    }

    /**
     * Converts year object representation to String
     * 
     * @param date   year object
     * @return lexical representation of month: CCYY with optional time zone sign
     */
    protected String dateToString(int[] date) {

        message.setLength(0);
        message.append(date[CY]);
        message.append((char)date[utc]);
        return message.toString();
    }

}


