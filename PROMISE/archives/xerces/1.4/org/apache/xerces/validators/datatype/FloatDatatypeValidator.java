package org.apache.xerces.validators.datatype;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.utils.regex.RegularExpression;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

/**
 *
 * @author  Elena Litani
 * @author Ted Leung
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @version  $Id: FloatDatatypeValidator.java 317074 2001-04-25 20:42:11Z elena $
 */

public class FloatDatatypeValidator extends AbstractNumericValidator {

    public FloatDatatypeValidator () throws InvalidDatatypeFacetException{
    }

    public FloatDatatypeValidator ( DatatypeValidator base, Hashtable facets,
                                    boolean derivedByList ) throws InvalidDatatypeFacetException {
        super (base, facets, derivedByList);
    }
    
   public int compare( String value1, String value2) {
        try {
            float f1 = fValueOf(value1).floatValue();
            float f2 = fValueOf(value2).floatValue();
            return compareFloats(f1, f2);
        }
        catch ( NumberFormatException e ) {
            return -1;
        }
    }

    protected void assignAdditionalFacets(String key,  Hashtable facets ) throws InvalidDatatypeFacetException{        
        throw new InvalidDatatypeFacetException( getErrorString(DatatypeMessageProvider.ILLEGAL_FLOAT_FACET,
                                                                DatatypeMessageProvider.MSG_NONE, new Object[] { key}));
    }

    protected int compareValues (Object value1, Object value2) {

        float f1 = ((Float)value1).floatValue();
        float f2 = ((Float)value2).floatValue();
        return compareFloats(f1, f2);
    }
    
    protected void setMaxInclusive (String value) {
        fMaxInclusive = fValueOf(value);
    }
    protected void setMinInclusive (String value) {
        fMinInclusive = fValueOf(value);

    }
    protected void setMaxExclusive (String value) {
        fMaxExclusive = fValueOf(value);

    }
    protected void setMinExclusive (String value) {
        fMinExclusive = fValueOf(value);

    }
    protected void setEnumeration (Vector enumeration) throws InvalidDatatypeValueException{
        if ( enumeration != null ) {
            fEnumeration = new Float[enumeration.size()];
            Object[] baseEnum=null;
            for ( int i = 0; i < enumeration.size(); i++ ){
                    fEnumeration[i] = fValueOf((String) enumeration.elementAt(i));
                    ((FloatDatatypeValidator)fBaseValidator).validate((String)enumeration.elementAt(i), null);
            }
        }
    }


    protected String getMaxInclusive (boolean isBase) {
        return(isBase)?(((FloatDatatypeValidator)fBaseValidator).fMaxInclusive.toString())
        :((Float)fMaxInclusive).toString();
    }
    protected String getMinInclusive (boolean isBase) {
        return(isBase)?(((FloatDatatypeValidator)fBaseValidator).fMinInclusive.toString())
        :((Float)fMinInclusive).toString();
    }
    protected String getMaxExclusive (boolean isBase) {
        return(isBase)?(((FloatDatatypeValidator)fBaseValidator).fMaxExclusive.toString())
        :((Float)fMaxExclusive).toString();
    }
    protected String getMinExclusive (boolean isBase) {
        return(isBase)?(((FloatDatatypeValidator)fBaseValidator).fMinExclusive.toString())
        :((Float)fMinExclusive).toString();
    }

    /**
    * validate if the content is valid against base datatype and facets (if any)
    * this function might be called directly from UnionDatatype or ListDatatype
    *
    * @param content A string containing the content to be validated
    * @param enumeration A vector with enumeration strings
    * @exception throws InvalidDatatypeException if the content is
    *  is not a W3C float type;
    * @exception throws InvalidDatatypeFacetException if enumeration is not float
    */

    protected void checkContent(String content, Object state, Vector enumeration, boolean asBase)
    throws InvalidDatatypeValueException {
        if ( this.fBaseValidator != null ) {
            ((FloatDatatypeValidator)fBaseValidator).checkContent(content, state, enumeration, true);
        }

        if ( (fFacetsDefined & DatatypeValidator.FACET_PATTERN ) != 0 ) {
            if ( fRegex == null || fRegex.matches( content) == false )
                throw new InvalidDatatypeValueException("Value'"+content+
                                                        "does not match regular expression facet" + fPattern );
        }

        if ( asBase )
            return;

        Float f = null;
        try {
            f = fValueOf(content);
        }
        catch ( NumberFormatException nfe ) {
            throw new InvalidDatatypeValueException( getErrorString(DatatypeMessageProvider.NOT_FLOAT,
                                                                    DatatypeMessageProvider.MSG_NONE,
                                                                    new Object [] {content}));
        }

        if ( enumeration != null ) {
            int size =  enumeration.size();
            Float[]     enumFloats = new Float[size];
            int i=0;
            try {
                for ( ; i < size; i++ )
                    enumFloats[i] = fValueOf((String) enumeration.elementAt(i));

            }
            catch ( NumberFormatException nfe ) {
                throw new InvalidDatatypeValueException( getErrorString(DatatypeMessageProvider.INVALID_ENUM_VALUE,
                                                                        DatatypeMessageProvider.MSG_NONE,
                                                                        new Object [] { enumeration.elementAt(i)}));
            }
            enumCheck(f.floatValue(), enumFloats);
        }

        boundsCheck(f);

        if ( ((fFacetsDefined & DatatypeValidator.FACET_ENUMERATION ) != 0 &&
              (fEnumeration != null) ) ) {
               enumCheck(f.floatValue(), (Float[])fEnumeration);
        }
    }

    protected int getInvalidFacetMsg (){
        return DatatypeMessageProvider.ILLEGAL_FLOAT_FACET;
    }  

    private void enumCheck(float v, Float[] enumFloats) throws InvalidDatatypeValueException {
        for ( int i = 0; i < enumFloats.length; i++ ) {
            if ( v == ((Float)enumFloats[i]).floatValue() ) return;
        }
        throw new InvalidDatatypeValueException(
                                               getErrorString(DatatypeMessageProvider.NOT_ENUM_VALUE,
                                                              DatatypeMessageProvider.MSG_NONE,
                                                              new Object [] { new Float(v)}));
    }


    private static Float fValueOf(String s) throws NumberFormatException {
        Float f=null;
        try {
            f = Float.valueOf(s);
        }
        catch ( NumberFormatException nfe ) {
            if ( s.equals("INF") ) {
                f = new Float(Float.POSITIVE_INFINITY);
            }
            else if ( s.equals("-INF") ) {
                f = new Float (Float.NEGATIVE_INFINITY);
            }
            else if ( s.equals("NaN" ) ) {
                f = new Float (Float.NaN);
            }
            else {
                throw nfe;
            }
        }
        return f;
    }

    private int compareFloats( float f1, float f2){
        int f1V = Float.floatToIntBits(f1);
        int f2V = Float.floatToIntBits(f2);
        if ( f1 > f2 ) {
            return 1;
        }
        if ( f1 < f2 ) {
            return -1;
        }
        if ( f1V==f2V ) {
            return 0;
        }
        return(f1V < f2V) ? -1 : 1;
    }
}
