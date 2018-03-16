package org.apache.xerces.validators.datatype;

import java.util.Hashtable;

/**
 * Validator for <time> datatype (W3C Schema Datatypes)
 * 
 * @author Elena Litani
 * @version $Id: TimeDatatypeValidator.java 317921 2001-11-15 18:56:03Z  $
 */
public class TimeDatatypeValidator extends DateTimeValidator {

    public  TimeDatatypeValidator() throws InvalidDatatypeFacetException{
        super();
    }


    public  TimeDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
                                        boolean derivedByList ) throws InvalidDatatypeFacetException {
        super (base, facets, derivedByList);
    }

    /**
     * Parses, validates and computes normalized version of time object
     * 
     * @param str    The lexical representation of time object hh:mm:ss.sss
     *               with possible time zone Z or (-),(+)hh:mm
     *               Pattern: "(\\d\\d):(\\d\\d):(\\d\\d)(\\.(\\d)*)?(Z|(([-+])(\\d\\d)(:(\\d\\d))?))?")
     * @param date   uninitialized date object
     * @return normalized time representation
     * @exception Exception Invalid lexical representation
     */
    protected int[] parse(String str, int[] date) throws SchemaDateTimeException{
        
        resetBuffer(str);

        if ( date == null ) {
            date = new int[TOTAL_SIZE];
        }
        resetDateObj(date);

        date[CY]=YEAR;
        date[M]=MONTH;
        date[D]=DAY;
        getTime(fStart, fEnd, date);

        
        validateDateTime(date);
        
        if ( date[utc]!=0 || date[h] == 24 ) {
            normalize(date);
        }
        return date;
    }


    /**
     * Converts time object representation to String
     * 
     * @param date   time object
     * @return lexical representation of time: hh:mm:ss.sss with an optional time zone sign
     */
     
    protected String dateToString(int[] date) {
        message.setLength(0);
        message.append(date[h]);
        message.append(':');
        message.append(date[m]);
        message.append(':');
        message.append(date[s]);
        message.append('.');
        message.append(date[ms]);
        message.append((char)date[utc]);
        return message.toString();
    }
     

}


