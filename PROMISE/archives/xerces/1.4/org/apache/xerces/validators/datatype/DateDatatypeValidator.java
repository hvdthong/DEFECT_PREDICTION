package org.apache.xerces.validators.datatype;

import java.util.Hashtable;

/**
 * Validator for <date> datatype (W3C Schema datatypes)
 * 
 * @author Elena Litani
 * @version $Id: DateDatatypeValidator.java 317299 2001-06-21 20:56:13Z elena $
 */
public class DateDatatypeValidator extends DateTimeValidator {

    public  DateDatatypeValidator() throws InvalidDatatypeFacetException{
        super();
    }


    public  DateDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
                                    boolean derivedByList ) throws InvalidDatatypeFacetException {
        super (base, facets, derivedByList);
    }

    /**
     * Parses, validates and computes normalized version of dateTime object
     * 
     * @param str    The lexical representation of dateTime object CCYY-MM-DD
     *               with possible time zone Z or (-),(+)hh:mm
     * @param date   uninitialized date object
     * @return normalized dateTime representation
     * @exception Exception Invalid lexical representation
     */
    protected int[] parse(String str, int[] date) throws SchemaDateTimeException{
        resetBuffer(str);

        if ( date == null ) {
            date = new int[TOTAL_SIZE];
        }
        resetDateObj(date);

        getDate(fStart, fEnd, date);
        parseTimeZone (fEnd, date);

        validateDateTime(date);

        if ( date[utc]!=0 && date[utc]!='Z' ) {
            normalize(date);
        }
        return date;
    }


}


