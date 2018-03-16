package org.apache.xerces.validators.datatype;

import java.util.Hashtable;

/**
 * Validator for <gDay> datatype (W3C Schema datatypes)
 * 
 * @author Elena Litani
 * @version $Id: DayDatatypeValidator.java 317299 2001-06-21 20:56:13Z elena $
 */

public class DayDatatypeValidator extends DateTimeValidator {

    private final static int DAY_SIZE=5;

    public  DayDatatypeValidator() throws InvalidDatatypeFacetException{
        super();
    }

    public  DayDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
                                   boolean derivedByList ) throws InvalidDatatypeFacetException {
        super(base, facets, derivedByList);
    }

    /**
     * Parses, validates and computes normalized version of gDay object
     * 
     * @param str    The lexical representation of gDay object ---DD
     *               with possible time zone Z or (-),(+)hh:mm
     *               Pattern: ---(\\d\\d)(Z|(([-+])(\\d\\d)(:(\\d\\d))?
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
        if (fBuffer.charAt(0)!='-' || fBuffer.charAt(1)!='-' || fBuffer.charAt(2)!='-') {
            throw new SchemaDateTimeException ("Error in day parsing");
        }

        date[CY]=YEAR;
        date[M]=MONTH;
        
        date[D]=parseInt(fStart+3,fStart+5);


        if ( DAY_SIZE<fEnd ) {
            int sign = findUTCSign(DAY_SIZE, fEnd);
            if ( sign<0 ) {
                throw new SchemaDateTimeException ("Error in day parsing");
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
     * Converts gDay object representation to String
     * 
     * @param date   gDay object
     * @return lexical representation of gDay: ---DD with an optional time zone sign
     */
    protected String dateToString(int[] date) {
        message.setLength(0);
        message.append('-');
        message.append('-');
        message.append('-');
        message.append(date[D]);
        message.append((char)date[utc]);
        return message.toString();
    }

}

