package org.apache.xerces.validators.datatype;

import java.util.Hashtable;


/**
 * Validator for <dateTime> datatype (W3C Schema Datatypes)
 * 
 * @author Elena Litani
 * @version $Id: DateTimeDatatypeValidator.java 317921 2001-11-15 18:56:03Z  $
 */
public class DateTimeDatatypeValidator extends DateTimeValidator {

    public  DateTimeDatatypeValidator() throws InvalidDatatypeFacetException{
        super();
    }


    public  DateTimeDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
                                        boolean derivedByList ) throws InvalidDatatypeFacetException {
        super (base, facets, derivedByList);
    }

    /**
     * Parses, validates and computes normalized version of dateTime object
     * 
     * @param str    The lexical representation of dateTime object CCYY-MM-DDThh:mm:ss.sss
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
        int end = indexOf (fStart, fEnd, 'T');        

        getDate(fStart, end, date);
        getTime(end+1, fEnd, date);

        
        validateDateTime(date);


        if ( (date[utc]!=0  && date[utc]!='Z') || date[h] == 24 ) {
            normalize(date);
        }
        return date;
    }


}


