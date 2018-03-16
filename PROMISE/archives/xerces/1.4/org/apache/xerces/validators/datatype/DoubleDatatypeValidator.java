package org.apache.xerces.validators.datatype;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.utils.regex.RegularExpression;

/**
 * @author Elena Litani
 * @author Ted Leung
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @version $Id: DoubleDatatypeValidator.java 317074 2001-04-25 20:42:11Z elena $
 */

public class DoubleDatatypeValidator extends AbstractNumericValidator {

    public DoubleDatatypeValidator () throws InvalidDatatypeFacetException {
    }

    public DoubleDatatypeValidator ( DatatypeValidator base, Hashtable facets,
                                     boolean derivedByList ) throws InvalidDatatypeFacetException  {
        super(base, facets, derivedByList);
    }

    public int compare( String value1, String value2) {
        try {
            double d1 = dValueOf(value1).doubleValue();
            double d2 = dValueOf(value2).doubleValue();
            return compareDoubles(d1, d2);
        }
        catch ( NumberFormatException e ) {
            return -1;
        }

    }

    protected void assignAdditionalFacets(String key,  Hashtable facets ) throws InvalidDatatypeFacetException{        
        throw new InvalidDatatypeFacetException( getErrorString(DatatypeMessageProvider.ILLEGAL_DOUBLE_FACET,
                                                                DatatypeMessageProvider.MSG_NONE, new Object[] { key}));
    }
    /**
     * Compares 2 double values.
     * 
     * @param value1 - Double
     * @param value2 - Double
     * @return value1<value2 return -1
     *         value1>value2 return 1
     *         value1==value2 return 0
     */
    protected int compareValues (Object value1, Object value2) {
        double d1 = ((Double)value1).doubleValue();
        double d2 = ((Double)value2).doubleValue();
        return compareDoubles(d1, d2);
    }

    protected void setMaxInclusive (String value) {
        fMaxInclusive = dValueOf(value);
    }
    protected void setMinInclusive (String value) {
        fMinInclusive = dValueOf(value);

    }
    protected void setMaxExclusive (String value) {
        fMaxExclusive = dValueOf(value);

    }
    protected void setMinExclusive (String value) {
        fMinExclusive = dValueOf(value);

    }
    protected void setEnumeration (Vector enumeration) throws InvalidDatatypeValueException{
        if ( enumeration != null ) {
            fEnumeration = new Double[enumeration.size()];
            Object baseEnum=null;
            try {

                for ( int i = 0; i < enumeration.size(); i++ ) {
                    fEnumeration[i] = dValueOf((String)enumeration.elementAt(i));
                    ((DoubleDatatypeValidator)fBaseValidator).validate((String)enumeration.elementAt(i), null);

                }
            }
            catch ( Exception e ) {
                throw new InvalidDatatypeValueException(e.getMessage());
            }
        }
    }


    protected String getMaxInclusive (boolean isBase) {
        return(isBase)?(((DoubleDatatypeValidator)fBaseValidator).fMaxInclusive.toString())
        :((Double)fMaxInclusive).toString();
    }
    protected String getMinInclusive (boolean isBase) {
        return(isBase)?(((DoubleDatatypeValidator)fBaseValidator).fMinInclusive.toString())
        :((Double)fMinInclusive).toString();
    }
    protected String getMaxExclusive (boolean isBase) {
        return(isBase)?(((DoubleDatatypeValidator)fBaseValidator).fMaxExclusive.toString())
        :((Double)fMaxExclusive).toString();
    }
    protected String getMinExclusive (boolean isBase) {
        return(isBase)?(((DoubleDatatypeValidator)fBaseValidator).fMinExclusive.toString())
        :((Double)fMinExclusive).toString();
    }

    /**
    * validate if the content is valid against base datatype and facets (if any)
    * this function might be called directly from UnionDatatype or ListDatatype
    *
    * @param content A string containing the content to be validated
    * @param enumeration A vector with enumeration strings
    * @exception throws InvalidDatatypeException if the content is
    *  is not a W3C double type;
    * @exception throws InvalidDatatypeFacetException if enumeration is not double
    */

    protected void checkContentEnum(String content, Object state, Vector enumeration)
    throws InvalidDatatypeValueException {
        checkContent (content, state, enumeration, false);
    }

    protected void checkContent(String content, Object state, Vector enumeration, boolean asBase)
    throws InvalidDatatypeValueException {
        if ( this.fBaseValidator != null ) {
            ((DoubleDatatypeValidator)fBaseValidator).checkContent(content, state, enumeration, true);
        }

        if ( (fFacetsDefined & DatatypeValidator.FACET_PATTERN ) != 0 ) {
            if ( fRegex == null || fRegex.matches( content) == false )
                throw new InvalidDatatypeValueException("Value'"+content+
                                                        "does not match regular expression facet" + fPattern );
        }

        if ( asBase )
            return;

        Double d = null;
        try {
            d = dValueOf(content);
        }
        catch ( NumberFormatException nfe ) {
            throw new InvalidDatatypeValueException( getErrorString(DatatypeMessageProvider.NOT_DOUBLE,
                                                                    DatatypeMessageProvider.MSG_NONE,
                                                                    new Object [] { content}));
        }

            int size = enumeration.size();
            Double[] enumDoubles = new Double[size];
            int i=0;
            try {
                for ( ; i < size; i++ )
                    enumDoubles[i] = dValueOf((String) enumeration.elementAt(i));
            }
            catch ( NumberFormatException nfe ) {
                throw new InvalidDatatypeValueException( getErrorString(DatatypeMessageProvider.INVALID_ENUM_VALUE,
                                                                        DatatypeMessageProvider.MSG_NONE,
                                                                        new Object [] { enumeration.elementAt(i)}));
            }

            enumCheck(d.doubleValue(), enumDoubles);
        }

        boundsCheck(d);

        if ( ((fFacetsDefined & DatatypeValidator.FACET_ENUMERATION ) != 0 &&
              (fEnumeration != null) ) )
            enumCheck(d.doubleValue(), (Double[])fEnumeration);
    }

    protected int getInvalidFacetMsg (){
        return DatatypeMessageProvider.ILLEGAL_DOUBLE_FACET;
    }

    private static Double dValueOf(String s) throws NumberFormatException {
        Double d;
        try {
            d = Double.valueOf(s);
        }
        catch ( NumberFormatException nfe ) {
            if ( s.equals("INF") ) {
                d = new Double(Double.POSITIVE_INFINITY);
            }
            else if ( s.equals("-INF") ) {
                d = new Double (Double.NEGATIVE_INFINITY);
            }
            else if ( s.equals("NaN" ) ) {
                d = new Double (Double.NaN);
            }
            else {
                throw nfe;
            }
        }
        return d;
    }

    private void enumCheck(double v, Double[] enumDoubles) throws InvalidDatatypeValueException {
        for ( int i = 0; i < enumDoubles.length; i++ ) {
            if ( v == enumDoubles[i].doubleValue() ) return;
        }
        throw new InvalidDatatypeValueException(
                                               getErrorString(DatatypeMessageProvider.NOT_ENUM_VALUE,
                                                              DatatypeMessageProvider.MSG_NONE,
                                                              new Object [] { new Double(v)}));
    }

    private int compareDoubles(double d1, double d2) {
        long d1V = Double.doubleToLongBits(d1);
        long d2V = Double.doubleToLongBits(d2);

        if ( d1 > d2 ) {
            return 1;
        }
        if ( d1 < d2 ) {
            return -1;
        }
        if ( d1V == d2V ) {
            return 0;
        }
        return(d1V < d2V) ? -1 : 1;
    }
}
