package org.apache.xerces.validators.datatype;

import java.util.Hashtable;

/**
 * Validator for <duration> datatype (W3C Schema Datatypes)
 * 
 * @author Elena Litani
 * @version $Id: DurationDatatypeValidator.java 317200 2001-05-31 21:51:46Z elena $
 */

public class DurationDatatypeValidator extends DateTimeValidator {

    private final static int[][] DATETIMES= {
        {1696, 9, 1, 0, 0, 0, 0, 'Z'},     
        {1697, 2, 1, 0, 0, 0, 0, 'Z'},
        {1903, 3, 1, 0, 0, 0, 0, 'Z'},
        {1903, 7, 1, 0, 0, 0, 0, 'Z'}};

    private int[][] fDuration = null;


    public  DurationDatatypeValidator() throws InvalidDatatypeFacetException{
        super();
    }
    public  DurationDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
                                        boolean derivedByList ) throws InvalidDatatypeFacetException {

        super(base, facets, derivedByList);
    }


    /**
     * Parses, validates and computes normalized version of duration object
     * 
     * @param str    The lexical representation of duration object PnYn MnDTnH nMnS 
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


        char c=fBuffer.charAt(fStart++);
        if ( c!='P' && c!='-' ) {
            throw new SchemaDateTimeException();
        }
        else {
            date[utc]=(c=='-')?'-':0;
            if ( c=='-' && fBuffer.charAt(fStart++)!='P' ) {
                throw new SchemaDateTimeException();
            }
        }

        int negate = 1;
        if ( date[utc]=='-' ) {
            negate = -1;

        }
        boolean designator = false;

        int endDate = indexOf (fStart, fEnd, 'T'); 
        if ( endDate == -1 ) {
            endDate = fEnd;
        }
        int end = indexOf (fStart, endDate, 'Y');
        if ( end!=-1 ) {
            date[CY]=negate * parseInt(fStart,end);
            fStart = end+1;
            designator = true;
        }

        end = indexOf (fStart, endDate, 'M');
        if ( end!=-1 ) {
            date[M]=negate * parseInt(fStart,end);
            fStart = end+1;
            designator = true;
        }

        end = indexOf (fStart, endDate, 'D');
        if ( end!=-1 ) {
            date[D]=negate * parseInt(fStart,end);
            fStart = end+1;
            designator = true;
        }

        if ( fEnd == endDate && fStart!=fEnd ) {
            throw new SchemaDateTimeException();
        }
        if ( fEnd !=endDate ) {


            end = indexOf (++fStart, fEnd, 'H');
            if ( end!=-1 ) {
                date[h]=negate * parseInt(fStart,end);
                fStart=end+1;
                designator = true;
            }

            end = indexOf (fStart, fEnd, 'M');
            if ( end!=-1 ) {
                date[m]=negate * parseInt(fStart,end);
                fStart=end+1;
                designator = true;
            }

            end = indexOf (fStart, fEnd, 'S');
            if ( end!=-1 ) {
                int mlsec = indexOf (fStart, end, '.');
                if ( mlsec >0 ) {
                    date[s]  = negate * parseInt (fStart, mlsec);
                    date[ms] = negate * parseInt (mlsec+1, end);
                }
                else {
                    date[s]=negate * parseInt(fStart,end);
                }
                fStart=end+1;
                designator = true;
            }
            if ( fStart != fEnd || fBuffer.charAt(--fStart)=='T' ) {
                throw new SchemaDateTimeException();
            }
        }

        if ( !designator ) {
            throw new SchemaDateTimeException();
        }

        return date;
    }


    /**
     * Compares 2 given durations. (refer to W3C Schema Datatypes "3.2.6 duration")
     * 
     * @param date1  Unnormalized duration
     * @param date2  Unnormalized duration
     * @param strict (min/max)Exclusive strict == true ( LESS_THAN ) or ( GREATER_THAN )
     *               (min/max)Inclusive strict == false (LESS_EQUAL) or (GREATER_EQUAL)
     * @return 
     */
    protected  short compareDates(int[] date1, int[] date2, boolean strict) {


        short resultA, resultB= INDETERMINATE;

        resultA = compareOrder (date1, date2);
        if ( resultA == EQUAL ) {
            return EQUAL;
        }
        if ( fDuration == null ) {
            fDuration = new int[2][TOTAL_SIZE];
        }
        int[] tempA = addDuration (date1, 0, fDuration[0]);
        int[] tempB = addDuration (date2, 0, fDuration[1]);
        resultA =  compareOrder(tempA, tempB);
        if ( resultA == INDETERMINATE ) {
            return INDETERMINATE;
        }

        tempA = addDuration(date1, 1, fDuration[0]);
        tempB = addDuration(date2, 1, fDuration[1]);
        resultB = compareOrder(tempA, tempB);
        resultA = compareResults(resultA, resultB, strict);
        if (resultA == INDETERMINATE) {
            return INDETERMINATE;
        }

        tempA = addDuration(date1, 2, fDuration[0]);
        tempB = addDuration(date2, 2, fDuration[1]);
        resultB = compareOrder(tempA, tempB);
        resultA = compareResults(resultA, resultB, strict);
        if (resultA == INDETERMINATE) {
            return INDETERMINATE;
        }

        tempA = addDuration(date1, 3, fDuration[0]);
        tempB = addDuration(date2, 3, fDuration[1]);
        resultB = compareOrder(tempA, tempB);
        resultA = compareResults(resultA, resultB, strict);

        return resultA;
    }

    private short compareResults(short resultA, short resultB, boolean strict){

      if ( resultB == INDETERMINATE ) {
            return INDETERMINATE;
        }
        else if ( resultA!=resultB && strict ) {
            return INDETERMINATE;
        }
        else if ( resultA!=resultB && !strict ) {
            if ( resultA!=EQUAL && resultB!=EQUAL ) {
                return INDETERMINATE;
            }
            else {
                return (resultA!=EQUAL)?resultA:resultB;
            }
        }
        return resultA;
    }

    private int[] addDuration(int[] date, int index, int[] duration) {


        resetDateObj(duration);
        int temp = DATETIMES[index][M] + date[M];
        duration[M] = modulo (temp, 1, 13);
        int carry = fQuotient (temp, 1, 13);

        duration[CY]=DATETIMES[index][CY] + date[CY] + carry;

        temp = DATETIMES[index][s] + date[s];
        carry = fQuotient (temp, 60);
        duration[s] =  mod(temp, 60, carry);
        
        temp = DATETIMES[index][m] +date[m] + carry; 
        carry = fQuotient (temp, 60); 
        duration[m]= mod(temp, 60, carry);         

        temp = DATETIMES[index][h] + date[h] + carry;
        carry = fQuotient(temp, 24);
        duration[h] = mod(temp, 24, carry);
        

        duration[D]=DATETIMES[index][D] + date[D] + carry;

        while ( true ) {

            temp=maxDayInMonthFor(duration[CY], duration[M]);
                duration[D] = duration[D] + maxDayInMonthFor(duration[CY], duration[M]-1);
                carry=-1;
            }
            else if ( duration[D] > temp ) {
                duration[D] = duration[D] - temp;
                carry=1;
            }
            else {
                break;
            }
            temp = duration[M]+carry;
            duration[M] = modulo(temp, 1, 13);
            duration[CY] = duration[CY]+fQuotient(temp, 1, 13);
        }

        duration[utc]='Z';
        return duration;
    }

    protected String dateToString(int[] date) {
        message.setLength(0);
        int negate = 1;
        if ( date[CY]<0 ) {
            message.append('-');
            negate=-1;
        }
        message.append('P');
        message.append(negate * date[CY]);
        message.append('Y');
        message.append(negate * date[M]);
        message.append('M');
        message.append(negate * date[D]);
        message.append('D');
        message.append('T');
        message.append(negate * date[h]);
        message.append('H');
        message.append(negate * date[m]);
        message.append('M');
        message.append(negate * date[s]);
        message.append('.');
        message.append(negate * date[ms]);
        message.append('S');

        return message.toString();
    }
}


