package org.apache.xerces.validators.datatype;

import java.util.Hashtable;

/**
 * Validator for <gYearMonth> datatype (W3C Schema Datatypes)
 * 
 * @author Elena Litani
 * @version $Id: YearMonthDatatypeValidator.java 317921 2001-11-15 18:56:03Z  $
 */

public class YearMonthDatatypeValidator extends DateTimeValidator {

    public  YearMonthDatatypeValidator() throws InvalidDatatypeFacetException{
        super();
    }


    public  YearMonthDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
                                         boolean derivedByList ) throws InvalidDatatypeFacetException {
        super (base, facets, derivedByList);
    }

    /**
     * Parses, validates and computes normalized version of yearMonth object
     * 
     * @param str    The lexical representation of yearMonth object CCYY-MM
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

        getYearMonth(fStart, fEnd, date);
        parseTimeZone (fEnd, date);

        date[D]=DAY;

        validateDateTime(date);
        
        if ( date[utc]!=0 && date[utc]!='Z' ) {
            normalize(date);
        }
        return date;
    }


}


