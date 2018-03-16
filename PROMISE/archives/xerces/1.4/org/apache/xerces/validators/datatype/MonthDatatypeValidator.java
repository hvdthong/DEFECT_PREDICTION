package org.apache.xerces.validators.datatype;

import java.util.Hashtable;

/**
 * Validator for <gMonth> datatype (W3C Schema Datatypes)
 * 
 * @author Elena Litani
 * @version $Id: MonthDatatypeValidator.java 317299 2001-06-21 20:56:13Z elena $
 */

public class MonthDatatypeValidator extends DateTimeValidator {

    public  MonthDatatypeValidator() throws InvalidDatatypeFacetException{
        super();
    }

    public  MonthDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
                                     boolean derivedByList ) throws InvalidDatatypeFacetException {
        super(base, facets, derivedByList);
    }

    /**
     * Parses, validates and computes normalized version of gMonth object
     * 
     * @param str    The lexical representation of gMonth object --MM--
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
        date[D]=DAY;
        if (fBuffer.charAt(0)!='-' || fBuffer.charAt(1)!='-') {
            throw new SchemaDateTimeException("Invalid format for gMonth: "+str);
        }
        int stop = fStart +4;
        date[M]=parseInt(fStart+2,stop);

        if (fBuffer.charAt(stop++)!='-' || fBuffer.charAt(stop)!='-') {
            throw new SchemaDateTimeException("Invalid format for gMonth: "+str);
        }
        if ( MONTH_SIZE<fEnd ) {
            int sign = findUTCSign(MONTH_SIZE, fEnd);
            if ( sign<0 ) {
                throw new SchemaDateTimeException ("Error in month parsing: "+str);
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
     * Overwrite compare algorithm to optimize month comparison
     * 
     * @param date1
     * @param date2
     * @return 
     */
    protected  short compareDates(int[] date1, int[] date2) {

        if ( date1[utc]==date2[utc] ) {
            return (date1[M]>=date2[M])?(date1[M]>date2[M])?LESS_THAN:EQUAL:GREATER_THAN;
        }

        if ( date1[utc]=='Z' || date2[utc]=='Z' ) {
            
            if ( date1[M]==date2[M] ) {
                return INDETERMINATE;
            }
            if ( (date1[M]+1 == date2[M] || date1[M]-1 == date2[M]) ) {
                return INDETERMINATE;
            }
        }

        if ( date1[M]<date2[M] ) {
            return LESS_THAN;
        }
        else {
            return GREATER_THAN;
        }

    }

    /**
     * Converts month object representation to String
     * 
     * @param date   month object
     * @return lexical representation of month: --MM-- with an optional time zone sign
     */
    protected String dateToString(int[] date) {

        message.setLength(0);
        message.append('-');
        message.append('-');
        message.append(date[M]);
        message.append('-');
        message.append('-');
        message.append((char)date[utc]);
        return message.toString();
    }

}
