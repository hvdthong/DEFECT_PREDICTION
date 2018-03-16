package org.apache.xerces.validators.datatype;

import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import java.lang.Math;
import org.apache.xerces.utils.regex.RegularExpression;
import org.apache.xerces.validators.schema.SchemaSymbols;


/**
 * This is the base class of all date/time datatype validators.
 * It implements common code for parsing, validating and comparing datatypes.
 * Classes that extend this class, must implement parse() method.
 * 
 * @author Elena Litani
 * @author Len Berman  
 *
 * @version $Id: DateTimeValidator.java 317921 2001-11-15 18:56:03Z  $
 */

public abstract class DateTimeValidator extends AbstractNumericFacetValidator {

    private static final boolean DEBUG=false;
    

    protected final static int CY = 0,  M = 1, D = 2, h = 3, 
    m = 4, s = 5, ms = 6, utc=7, hh=0, mm=1;

    protected static final short LESS_THAN=-1;
    protected static final short EQUAL=0;
    protected static final short GREATER_THAN=1;

    protected final static int TOTAL_SIZE = 8;

    protected final static int MONTH_SIZE = 6; 

    private final static int YEARMONTH_SIZE = 7;

    protected final static int YEAR=2000;
    protected final static int MONTH=01;
    protected final static int DAY = 15;

    protected int[] timeZone;

    protected int  fEnumSize;

    protected int fEnd; 
    protected int fStart;

    protected StringBuffer fBuffer;     

    protected int[] fDateValue;
    private int[] fTempDate;

    protected StringBuffer message;



    public  DateTimeValidator () throws InvalidDatatypeFacetException {

    }

    public DateTimeValidator (DatatypeValidator base, Hashtable facets, boolean derivedByList ) 
    throws InvalidDatatypeFacetException {
        super (base, facets, derivedByList);
    }

    protected void initializeValues(){
        fDateValue = new int[TOTAL_SIZE];
        fTempDate = new int[TOTAL_SIZE];
        fEnd = 30; 
        fStart = 0;
        message = new StringBuffer(TOTAL_SIZE);
        fBuffer = new StringBuffer(fEnd);
        timeZone = new int[2];
    }

    protected void assignAdditionalFacets(String key,  Hashtable facets ) throws InvalidDatatypeFacetException{        
        throw new InvalidDatatypeFacetException( getErrorString(DatatypeMessageProvider.ILLEGAL_DATETIME_FACET,
                                                                DatatypeMessageProvider.MSG_NONE, new Object[] { key }));
    }
    
    protected int compareValues (Object value1, Object value2) {
            return compareDates((int[])value1, (int[])value2, true);
    }

    protected void setMaxInclusive (String value) {
        fMaxInclusive = parse(value, null);
    }
    protected void setMinInclusive (String value) {
        fMinInclusive = parse(value, null);
    }
    
    protected void setMaxExclusive (String value) {
        fMaxExclusive = parse(value, null);

    }
    protected void setMinExclusive (String value) {
        fMinExclusive = parse(value, null);

    }
    protected void setEnumeration (Vector enumeration) throws InvalidDatatypeValueException{
   
    if ( enumeration != null ) {
         
        fEnumSize = enumeration.size();
        fEnumeration = new int[fEnumSize][];
        for ( int i=0; i<fEnumSize; i++ ) {
            try {
                fEnumeration[i] = parse((String)enumeration.elementAt(i), null);
            }
            catch ( RuntimeException e ) {
                throw new InvalidDatatypeValueException(e.getMessage());
            }
        }
    }
}


    protected String getMaxInclusive (boolean isBase) {
        return (isBase)?(dateToString((int[]) ((DateTimeValidator)fBaseValidator).fMaxInclusive))
        :dateToString((int[])fMaxInclusive);
    }
    protected String getMinInclusive (boolean isBase) {
        return (isBase)?(dateToString((int[]) ((DateTimeValidator)fBaseValidator).fMinInclusive))
        :dateToString((int[])fMinInclusive);
    }
    protected String getMaxExclusive (boolean isBase) {
        return (isBase)?(dateToString((int[]) ((DateTimeValidator)fBaseValidator).fMaxExclusive))
        :dateToString((int[])fMaxExclusive);
    }
    protected String getMinExclusive (boolean isBase) {
        return (isBase)?(dateToString((int[]) ((DateTimeValidator)fBaseValidator).fMinExclusive))
        :dateToString((int[])fMinExclusive);
    }

    protected void checkContent( String content, Object State, Vector enum, boolean asBase)
                                    throws InvalidDatatypeValueException{
    }

    /**
     * Implemented by each subtype, calling appropriate function to parse
     * given date/time
     * 
     * @param content String value of the date/time
     * @param date    Storage to represent date/time object.
     *                If null - new object will be created, otherwise
     *                date will be reset and reused
     * @return updated date/time object
     * @exception Exception
     */
    abstract protected int[] parse (String content, int[] date) throws SchemaDateTimeException;

    /**
     * Validate that a string is a W3C date/time type
     * 
     * @param content string value of date/time
     * @param state
     * @return  
     * @exception InvalidDatatypeValueException
     */
    public Object validate(String content, Object state) throws InvalidDatatypeValueException{

        try {
            resetDateObj(fDateValue);
            parse(content, fDateValue);
        }
        catch ( RuntimeException e ) {
            throw new InvalidDatatypeValueException("Value '"+content+
                                                    "' is not legal value for current datatype. " +e.getMessage() );
        }
        validateDate (fDateValue, content);
        return null;
    }

    /**
     * Validates date object against facet and base datatype
     * 
     * @param date    represents date/time obj
     * @param content lexical representation of date/time obj
     * @exception InvalidDatatypeValueException
     */
    protected void validateDate (int[] date, String content) throws InvalidDatatypeValueException{

            if ( (fFacetsDefined & DatatypeValidator.FACET_PATTERN ) != 0 ) {
                if ( fRegex == null || fRegex.matches( content) == false )
                    throw new InvalidDatatypeValueException("Value'"+content+
                                                            "' does not match regular expression facet " + fRegex.getPattern() );
            }
            ((DateTimeValidator)this.fBaseValidator).validateDate( date, content);
            if ( (fFacetsDefined & DatatypeValidator.FACET_ENUMERATION ) != 0 ) {
                int count=0;
                boolean valid = false;
                while ( count < fEnumSize ) {
                    if ( compareDates(date, (int[])fEnumeration[count], false) == EQUAL ) {
                        valid = true;
                        break;
                    }
                    count++;
                }
                if ( !valid ) {
                    throw new InvalidDatatypeValueException("Value'"+content+
                                                            "' does not match enumeration values" );
                }
            }

            short c;
            if ( fMinInclusive != null ) {
                
                c = compareDates(date, (int[])fMinInclusive, false);
                if ( c == LESS_THAN || c == INDETERMINATE ) {
                    throw new InvalidDatatypeValueException("Value '"+content+
                                                            "' is less than minInclusive: " +dateToString((int[])fMinInclusive) );
                }
            }
            if ( fMinExclusive != null ) {

                if ( compareDates(date, (int[])fMinExclusive, true) != GREATER_THAN ) {
                    throw new InvalidDatatypeValueException("Value '"+content+
                                                            "' is less than or equal to minExclusive: " +dateToString((int[])fMinExclusive));
                }
            }

            if ( fMaxInclusive != null ) {

                c = compareDates(date, (int[])fMaxInclusive, false );
                if ( c  == GREATER_THAN  || c == INDETERMINATE ) {
                    throw new InvalidDatatypeValueException("Value '"+content+
                                                            "' is greater than maxInclusive: " +dateToString((int[])fMaxInclusive) );
                }
            }

            if ( fMaxExclusive != null ) {

                if ( compareDates(date, (int[])fMaxExclusive, true ) != LESS_THAN ) {
                    throw new InvalidDatatypeValueException("Value '"+content+
                                                            "' is greater than or equal to maxExlusive: " +dateToString((int[])fMaxExclusive) );
                }
            }
        }
        else {
            return;
        }

    }
    public int compare( String content1, String content2)  {
        try{        
            parse(content1, fDateValue);
            parse(content2,fTempDate);
            int result = compareDates(fDateValue, fTempDate, true);
            return (result==INDETERMINATE)?-1:result;
        }
        catch ( RuntimeException e ) {
            return -1;
        
        }
    }


    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("clone() is not supported in "+this.getClass().getName());
    }



    /**
     * Compare algorithm described in dateDime (3.2.7).
     * Duration datatype overwrites this method
     * 
     * @param date1  normalized date representation of the first value
     * @param date2  normalized date representation of the second value
     * @param strict
     * @return less, greater, less_equal, greater_equal, equal
     */
    protected  short compareDates(int[] date1, int[] date2, boolean strict) {
        if ( date1[utc]==date2[utc] ) {
            return compareOrder(date1, date2);    
        }
        short c1, c2;

        if ( date1[utc]=='Z' ) {

            timeZone[hh]=14;
            timeZone[mm]=0;
            fTempDate[utc]='+';
            normalize(fTempDate);
            c1 = compareOrder(date1, fTempDate);

            timeZone[hh]=14;
            timeZone[mm]=0;
            fTempDate[utc]='-';
            normalize(fTempDate);
            c2 = compareOrder(date1, fTempDate);

            if ( (c1==LESS_THAN && c2==GREATER_THAN) ||
                 (c1==GREATER_THAN && c2==LESS_THAN) ) {
                return INDETERMINATE; 
            }
            return(c1!=INDETERMINATE)?c1:c2;
        }
        else if ( date2[utc]=='Z' ) {

            timeZone[hh]=14;
            timeZone[mm]=0;

            fTempDate[utc]='-';
            if (DEBUG) {
               System.out.println("fTempDate=" + dateToString(fTempDate));
            }
            normalize(fTempDate);
            c1 = compareOrder(fTempDate, date2);
            if (DEBUG) {
                System.out.println("date=" + dateToString(date2));
                System.out.println("fTempDate=" + dateToString(fTempDate));
            }
            timeZone[hh]=14;
            timeZone[mm]=0;
            fTempDate[utc]='+';
            normalize(fTempDate);
            c2 = compareOrder(fTempDate, date2);
            if (DEBUG) {
               System.out.println("fTempDate=" + dateToString(fTempDate));
            }
            if ( (c1==LESS_THAN && c2==GREATER_THAN) ||
                 (c1==GREATER_THAN && c2==LESS_THAN) ) {
                return INDETERMINATE; 
            }
            return(c1!=INDETERMINATE)?c1:c2;
        }
        return INDETERMINATE;

    }


    /**
     * Given normalized values, determines order-relation
     * between give date/time objects.
     * 
     * @param date1  date/time object
     * @param date2  date/time object
     * @return 
     */
    protected short compareOrder (int[] date1, int[] date2) {
        
        for ( int i=0;i<TOTAL_SIZE;i++ ) {
            if ( date1[i]<date2[i] ) {
                return LESS_THAN;
            }
            else if ( date1[i]>date2[i] ) {
                return GREATER_THAN;
            }
        }
        return EQUAL;
    }


    /**
     * Parses time hh:mm:ss.sss and time zone if any
     * 
     * @param start
     * @param end
     * @param data
     * @return 
     * @exception Exception
     */
    protected  void getTime (int start, int end, int[] data) throws RuntimeException{
        
        int stop = start+2;
        
        data[h]=parseInt(start,stop);


        if (fBuffer.charAt(stop++)!=':') {
                throw new RuntimeException("Error in parsing time zone" );
        }
        start = stop;
        stop = stop+2;
        data[m]=parseInt(start,stop);

        if (fBuffer.charAt(stop++)!=':') {
                throw new RuntimeException("Error in parsing time zone" );
        }
        start = stop;
        stop = stop+2;               
        data[s]=parseInt(start,stop);

        int milisec = indexOf(start, end, '.');

        int sign = findUTCSign((milisec!=-1)?milisec:start, end);

        if ( milisec != -1 ) {

            if ( sign<0 ) {

                data[ms]=parseInt(milisec+1,fEnd);
            }
            else {

                data[ms]=parseInt(milisec+1,sign);
            }

        }

        if ( sign>0 ) {
            getTimeZone(data,sign);
        }
    }


    /**
     * Parses date CCYY-MM-DD
     * 
     * @param start
     * @param end
     * @param data
     * @return 
     * @exception Exception
     */
    protected void getDate (int start, int end, int[] date) throws RuntimeException{

        getYearMonth(start, end, date);

        if (fBuffer.charAt(fStart++) !='-') {
            throw new RuntimeException("CCYY-MM must be followed by '-' sign");
        }
        int stop = fStart + 2;
        date[D]=parseInt(fStart, stop);
    }

    /**
     * Parses date CCYY-MM
     * 
     * @param start
     * @param end
     * @param data
     * @return 
     * @exception Exception
     */
    protected void getYearMonth (int start, int end, int[] date) throws RuntimeException{

        if ( fBuffer.charAt(0)=='-' ) {
            start++;
        }
        int i = indexOf(start, end, '-');
        if ( i==-1 ) throw new RuntimeException("Year separator is missing or misplaced");
        date[CY]= parseIntYear(i);
        if (fBuffer.charAt(i)!='-') {
            throw new RuntimeException("CCYY must be followed by '-' sign");
        }
        start = ++i;
        i = start +2;
        date[M]=parseInt(start, i);
    }



    /**
     * Shared code from Date and YearMonth datatypes.
     * Finds if time zone sign is present
     * 
     * @param end
     * @param date
     * @return 
     * @exception Exception
     */
    protected void parseTimeZone (int end, int[] date) throws RuntimeException{

 
        if ( fStart<fEnd ) {
            int sign = findUTCSign(fStart, fEnd);
            if ( sign<0 ) {
                throw new RuntimeException ("Error in month parsing");
            }
            else {
                getTimeZone(date, sign);
            }
        }
    }

    /**
     * Parses time zone: 'Z' or {+,-} followed by  hh:mm
     * 
     * @param data
     * @param sign
     * @return 
     */
    protected void getTimeZone (int[] data, int sign) throws RuntimeException{
        data[utc]=fBuffer.charAt(sign);

        if ( fBuffer.charAt(sign) == 'Z' ) {
            if (fEnd>(++sign)) {
                throw new RuntimeException("Error in parsing time zone");
            }
            return;
        }
        if ( sign<=(fEnd-6) ) {
             
            int stop = ++sign+2;
            timeZone[hh]=parseInt(sign, stop);
            if (fBuffer.charAt(stop++)!=':') {
                throw new RuntimeException("Error in parsing time zone" );
            }            
            
            timeZone[mm]=parseInt(stop, stop+2);
            
            if ( stop+2!=fEnd ) {
                throw new RuntimeException("Error in parsing time zone");
            }
            
        }
        else {
            throw new RuntimeException("Error in parsing time zone");
        }
        if ( DEBUG ) {
            System.out.println("time[hh]="+timeZone[hh] + " time[mm]=" +timeZone[mm]);
        }
    }



    /**
     * Computes index of given char within StringBuffer
     * 
     * @param start
     * @param end
     * @param ch     character to look for in StringBuffer
     * @return index of ch within StringBuffer
     */
    protected  int indexOf (int start, int end, char ch) {
        for ( int i=start;i<end;i++ ) {
            if ( fBuffer.charAt(i) == ch ) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Validates given date/time object accoring to W3C PR Schema 
     * [D.1 ISO 8601 Conventions]
     * 
     * @param data
     * @return 
     */
    protected void validateDateTime (int[]  data) {

        if ( data[CY]==0 ) {
            throw new RuntimeException("The year \"0000\" is an illegal year value");

        }

        if ( data[M]<1 || data[M]>12 ) {
            throw new RuntimeException("The month must have values 1 to 12");

        }

        if ( data[D]>maxDayInMonthFor(data[CY], data[M]) || data[D]==0 ) {
            throw new RuntimeException("The day must have values 1 to 31");
        }

        if ( data[h]>24 || data[h]<0 ||
             (data[h] == 24 && (data[m]!=0 || data[s]!=0 || data[ms]!=0)) ) {
            throw new RuntimeException("Hour must have values 0-24.  If hour is 24, minutes and seconds must both have the value 0.");
        }

        if ( data[m]>59 || data[m]<0 ) {
            throw new RuntimeException("Minute must have values 0-59");
        }

        if ( data[s]>60 || data[s]<0 ) {
            throw new RuntimeException("Second must have values 0-60");

        }

        if ( Math.abs(timeZone[hh])>14 ||
             (Math.abs(timeZone[hh]) == 14 && timeZone[mm] != 0) ) {
            throw new RuntimeException("Time zone should have range -14..+14");
        }

        if ( Math.abs(timeZone[mm]) > 59 ) {
            throw new RuntimeException("Minute must have values 0-59");
        }
    }


    /**
     * Return index of UTC char: 'Z', '+', '-'
     * 
     * @param start
     * @param end
     * @return 
     */
    protected int findUTCSign (int start, int end) {
        int c;
        for ( int i=start;i<end;i++ ) {
            c=fBuffer.charAt(i);
            if ( c == 'Z' || c=='+' || c=='-' ) {
                return i;
            }

        }
        return -1;
    }


    /**
     * Given start and end position, parses string value
     * 
     * @param value  string to parse
     * @param start  Start position
     * @param end    end position
     * @return  return integer representation of characters
     */
    protected  int parseInt (int start, int end) 
    throws NumberFormatException{ 
        int radix=10;
        int result = 0;
        int digit=0;
        int limit = -Integer.MAX_VALUE;
        int multmin = limit / radix;
        int i = start;
        do {
            digit = Character.digit(fBuffer.charAt(i),radix);
            if ( digit < 0 ) throw new NumberFormatException("'"+fBuffer.toString()+"' has wrong format");
            if ( result < multmin ) throw new NumberFormatException("'"+fBuffer.toString()+"' has wrong format");
            result *= radix;
            if ( result < limit + digit ) throw new NumberFormatException("'"+fBuffer.toString()+"' has wrong format");
            result -= digit;

        }while ( ++i < end );
        return -result;
    }

    protected int parseIntYear (int end){
        int radix=10;
        int result = 0;
        boolean negative = false;
        int i=0;
        int limit;
        int multmin;
        int digit=0;

        if (fBuffer.charAt(0) == '-'){
            negative = true;
            limit = Integer.MIN_VALUE;
            i++;
        } 
        else{
            limit = -Integer.MAX_VALUE;
        }

        int length = end-i;
        if (length<4 ||
            (length > 4 && fBuffer.charAt(i)=='0')) {
            throw new RuntimeException("Leading zeros are required if the year value would otherwise have fewer than four digits; otherwise they are forbidden.");
        }

        multmin = limit / radix;
        while (i < end)
        {
            digit = Character.digit(fBuffer.charAt(i++),radix);
            if (digit < 0) throw new NumberFormatException("'"+fBuffer.toString()+"' has wrong format");
            if (result < multmin) throw new NumberFormatException("'"+fBuffer.toString()+"' has wrong format");
            result *= radix;
            if (result < limit + digit) throw new NumberFormatException("'"+fBuffer.toString()+"' has wrong format");
            result -= digit;
        }

        if (negative)
        {
            if (i > 1) return result;
            else throw new NumberFormatException("'"+fBuffer.toString()+"' has wrong format");
        }
        return -result;



    }

    /**
     * normalize dateTime  [E Adding durations to dateTimes]
     * If timezone present - normalize to UTC
     * If hour is 24 - normalize to start of following day
     * 
     * @param date   CCYY-MM-DDThh:mm:ss+03
     * @return CCYY-MM-DDThh:mm:ssZ
     */
    protected  void normalize (int[] date) {


        int negate = 1;
        if (date[utc]=='+') {
            negate = -1;
        }
        if ( DEBUG ) {
            System.out.println("==>date[m]"+date[m]);
            System.out.println("==>timeZone[mm]" +timeZone[mm]);
        }
        int temp = date[m] + negate*timeZone[mm];
        int carry = fQuotient (temp, 60);
        date[m]= mod(temp, 60, carry);
        
        if ( DEBUG ) {
            System.out.println("==>carry: " + carry);
        }
        temp = date[h] + negate*timeZone[hh] + carry;
        carry = fQuotient(temp, 24);
        date[h]=mod(temp, 24, carry);
        if ( DEBUG ) {
            System.out.println("==>date[h]"+date[h]);
            System.out.println("==>carry: " + carry);
        }

        date[D]=date[D]+carry;

        while ( true ) {
            temp=maxDayInMonthFor(date[CY], date[M]);
            if (date[D]<1) {
                date[D] = date[D] + maxDayInMonthFor(date[CY], date[M]-1);
                carry=-1;
            }
            else if ( date[D]>temp ) {
                date[D]=date[D]-temp;
                carry=1;
            }
            else {
                break;
            }
            temp=date[M]+carry;
            date[M]=modulo(temp, 1, 13);
            date[CY]=date[CY]+fQuotient(temp, 1, 13);
        }

        if (date[utc] != 0) {
            date[utc]='Z';
        }
    }


    /**
     * Resets fBuffer to store string representation of 
     * date/time
     * 
     * @param str    Lexical representation of date/time
     */
    protected void resetBuffer (String str) {
        fBuffer.setLength(0);
        fStart=fEnd=0;
        timeZone[hh]=timeZone[mm]=0;
        fBuffer.append(str);
        fEnd = fBuffer.length();
        
    }


    /**
     * Resets object representation of date/time
     * 
     * @param data   date/time object
     */
    protected void resetDateObj (int[] data) {
        for ( int i=0;i<TOTAL_SIZE;i++ ) {
            data[i]=0;
        }
    }


    /**
     * Given {year,month} computes maximum 
     * number of days for given month
     * 
     * @param year
     * @param month
     * @return 
     */
    protected int maxDayInMonthFor(int year, int month) {
        if ( month==4 || month==6 || month==9 || month==11 ) {
            return 30;
        }
        else if ( month==2 ) {
            if ( isLeapYear(year) ) {
                return 29;
            }
            else {
                return 28;
            }
        }
        else {
            return 31;
        }
    }


    private boolean isLeapYear(int year) {
        return((year%4 == 0) && ((year%100 != 0) || (year%400 == 0))); 
    }

    protected int mod (int a, int b, int quotient) {
        return (a - quotient*b) ;
    }
    
    protected int fQuotient (int a, int b) {
        
        return (int)Math.floor((float)a/b);
    }

    protected int modulo (int temp, int low, int high) {
        int a = temp - low;
        int b = high - low;
        return (mod (a, b, fQuotient(a, b)) + low) ;
    }

    protected int fQuotient (int temp, int low, int high) {
  
        return fQuotient(temp - low, high - low);
    }


    protected String dateToString(int[] date) {
        message.setLength(0);
        message.append(date[CY]);
        message.append('-');
        message.append(date[M]);
        message.append('-');
        message.append(date[D]);
        message.append('T');
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


    /**
     * Use this function to report errors in constructor
     * 
     * @param msg
     * @param value
     */
    protected void reportError(String msg, String value) {
        System.err.println("[Error]: " +msg+": Value  '"+value+"' is not legal for current datatype");
    }



    private void cloneDate (int[] finalValue) {
        resetDateObj(fTempDate);
        for ( int i=0;i<TOTAL_SIZE;i++ ) {
            fTempDate[i]=finalValue[i];
        }
    }

}
