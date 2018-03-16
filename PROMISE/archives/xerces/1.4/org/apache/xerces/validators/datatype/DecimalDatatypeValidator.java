package org.apache.xerces.validators.datatype;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.io.IOException;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.utils.regex.RegularExpression;

/**
 *
 * DecimalDatatypeValidator validates that content satisfies the W3C XML Datatype for decimal
 *
 * @author  Elena Litani
 * @author Ted Leung
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @version $Id: DecimalDatatypeValidator.java 317223 2001-06-08 20:55:23Z sandygao $
 */

public class DecimalDatatypeValidator extends AbstractNumericValidator {

    protected int                 fTotalDigits;
    protected int                 fFractionDigits;

    public DecimalDatatypeValidator () throws InvalidDatatypeFacetException {
    }

    public DecimalDatatypeValidator ( DatatypeValidator base, Hashtable facets,
                                      boolean derivedByList ) throws InvalidDatatypeFacetException {
        super (base, facets, derivedByList);
    }

    public int compare( String value1, String value2) {
        try {
            BigDecimal d1 = new BigDecimal(stripPlusIfPresent(value1));
            BigDecimal d2 = new BigDecimal(stripPlusIfPresent(value2));
            return d1.compareTo(d2);
        }
        catch ( NumberFormatException e ) {
            return -1;
        }
        catch ( Exception e){
            return -1;
        }
    }

    protected void inheritAdditionalFacets() {

        if ( (( ((DecimalDatatypeValidator)fBaseValidator).fFacetsDefined & DatatypeValidator.FACET_TOTALDIGITS) != 0) &&
             !((fFacetsDefined & DatatypeValidator.FACET_TOTALDIGITS) != 0) ) {
            fFacetsDefined |= FACET_TOTALDIGITS;
            fTotalDigits = ((DecimalDatatypeValidator)fBaseValidator).fTotalDigits;
        }
        if ( (( ((DecimalDatatypeValidator)fBaseValidator).fFacetsDefined & DatatypeValidator.FACET_FRACTIONDIGITS) != 0)
             && !((fFacetsDefined & DatatypeValidator.FACET_FRACTIONDIGITS) != 0) ) {
            fFacetsDefined |= FACET_FRACTIONDIGITS;
            fFractionDigits = ((DecimalDatatypeValidator)fBaseValidator).fFractionDigits;
        }
    }

    protected void checkFacetConstraints() throws InvalidDatatypeFacetException{
        if ( ((fFacetsDefined & DatatypeValidator.FACET_FRACTIONDIGITS) != 0) &&
             ((fFacetsDefined & DatatypeValidator.FACET_TOTALDIGITS) != 0) ) {
            if ( fFractionDigits > fTotalDigits )
                throw new InvalidDatatypeFacetException( "fractionDigits value ='" + this.fFractionDigits + "'must be <= totalDigits value ='" +
                                                         this.fTotalDigits + "'. " );
        }
    }

    protected void checkBaseFacetConstraints() throws InvalidDatatypeFacetException{

        if ( ((fFacetsDefined & DatatypeValidator.FACET_TOTALDIGITS) != 0) ) {
            if ( (( ((DecimalDatatypeValidator)fBaseValidator).fFacetsDefined & DatatypeValidator.FACET_TOTALDIGITS) != 0)){
                if ((((DecimalDatatypeValidator)fBaseValidator).fFlags & DatatypeValidator.FACET_TOTALDIGITS) != 0 &&
                     fTotalDigits != ((DecimalDatatypeValidator)fBaseValidator).fTotalDigits){
                    throw new InvalidDatatypeFacetException("totalDigits value = '" + fTotalDigits +
                                                            "' must be equal to base.totalDigits value = '" +
                                                            ((DecimalDatatypeValidator)fBaseValidator).fTotalDigits +
                                                            "' with attribute {fixed} = true" );
                }
                if (fTotalDigits > ((DecimalDatatypeValidator)fBaseValidator).fTotalDigits ){
                    throw new InvalidDatatypeFacetException( "totalDigits value ='" + fTotalDigits + "' must be <= base.totalDigits value ='" +
                                                         ((DecimalDatatypeValidator)fBaseValidator).fTotalDigits + "'." );
                }
            }
        }
        if ( ((fFacetsDefined & DatatypeValidator.FACET_FRACTIONDIGITS) != 0) ) {
            if ( (( ((DecimalDatatypeValidator)fBaseValidator).fFacetsDefined & DatatypeValidator.FACET_FRACTIONDIGITS) != 0)){
                if ((((DecimalDatatypeValidator)fBaseValidator).fFlags & DatatypeValidator.FACET_FRACTIONDIGITS) != 0 &&
                     fFractionDigits != ((DecimalDatatypeValidator)fBaseValidator).fFractionDigits){
                    throw new InvalidDatatypeFacetException("fractionDigits value = '" + fFractionDigits +
                                                            "' must be equal to base.fractionDigits value = '" +
                                                            ((DecimalDatatypeValidator)fBaseValidator).fFractionDigits +
                                                            "' with attribute {fixed} = true" );
                }
            }
        }
    }

    protected void assignAdditionalFacets(String key,  Hashtable facets ) throws InvalidDatatypeFacetException{

        String value = null;
        try {
            if ( key.equals(SchemaSymbols.ELT_TOTALDIGITS) ) {
                value = ((String) facets.get(key ));
                fFacetsDefined |= DatatypeValidator.FACET_TOTALDIGITS;
                fTotalDigits      = Integer.parseInt(value );
                if ( fTotalDigits <= 0 )
                    throw new InvalidDatatypeFacetException("totalDigits value '"+fTotalDigits+"' must be a positiveInteger.");
            }
            else if ( key.equals(SchemaSymbols.ELT_FRACTIONDIGITS) ) {
                value = ((String) facets.get(key ));
                fFacetsDefined |= DatatypeValidator.FACET_FRACTIONDIGITS;
                fFractionDigits          = Integer.parseInt( value );
                if ( fFractionDigits < 0 )
                    throw new InvalidDatatypeFacetException("fractionDigits value '"+fFractionDigits+"' must be a positiveInteger.");
            }
            else {
                throw new InvalidDatatypeFacetException( getErrorString( DatatypeMessageProvider.ILLEGAL_DECIMAL_FACET,
                                                                         DatatypeMessageProvider.MSG_NONE, new Object [] { value, key}));
            }
        }
        catch ( Exception ex ) {
            throw new InvalidDatatypeFacetException( getErrorString( DatatypeMessageProvider.ILLEGAL_FACET_VALUE,
                                                                     DatatypeMessageProvider.MSG_NONE, new Object [] { value, key}));
        }
    }

    protected int compareValues (Object value1, Object value2) {
        return((BigDecimal)value1).compareTo((BigDecimal)value2);
    }

    protected void setMaxInclusive (String value) {
        fMaxInclusive = new BigDecimal(stripPlusIfPresent(value));
    }
    protected void setMinInclusive (String value) {
        fMinInclusive = new BigDecimal(stripPlusIfPresent(value));

    }
    protected void setMaxExclusive (String value) {
        fMaxExclusive = new BigDecimal(stripPlusIfPresent(value));

    }
    protected void setMinExclusive (String value) {
        fMinExclusive = new BigDecimal(stripPlusIfPresent(value));

    }
    protected void setEnumeration (Vector enumeration) throws InvalidDatatypeValueException{
        if ( enumeration != null ) {
            fEnumeration = new BigDecimal[enumeration.size()];
            Object baseEnum=null;
            try {

                for ( int i = 0; i < enumeration.size(); i++ ) {
                    fEnumeration[i] = new BigDecimal( stripPlusIfPresent(((String) enumeration.elementAt(i))));;
                    ((DecimalDatatypeValidator)fBaseValidator).validate((String)enumeration.elementAt(i), null);
                }
            }
            catch ( Exception e ) {
                throw new InvalidDatatypeValueException(e.getMessage());
            }
        }
    }


    protected String getMaxInclusive (boolean isBase) {
        return(isBase)?(((DecimalDatatypeValidator)fBaseValidator).fMaxInclusive.toString())
        :((BigDecimal)fMaxInclusive).toString();
    }
    protected String getMinInclusive (boolean isBase) {
        return(isBase)?(((DecimalDatatypeValidator)fBaseValidator).fMinInclusive.toString())
        :((BigDecimal)fMinInclusive).toString();
    }
    protected String getMaxExclusive (boolean isBase) {
        return(isBase)?(((DecimalDatatypeValidator)fBaseValidator).fMaxExclusive.toString())
        :((BigDecimal)fMaxExclusive).toString();
    }
    protected String getMinExclusive (boolean isBase) {
        return(isBase)?(((DecimalDatatypeValidator)fBaseValidator).fMinExclusive.toString())
        :((BigDecimal)fMinExclusive).toString();
    }



    protected void checkContent(String content, Object state, Vector enumeration, boolean asBase)
    throws InvalidDatatypeValueException {
        if ( this.fBaseValidator != null ) {
            ((DecimalDatatypeValidator)fBaseValidator).checkContent(content, state, enumeration, true);
        }

        if ( (fFacetsDefined & DatatypeValidator.FACET_PATTERN ) != 0 ) {
            if ( fRegex == null || fRegex.matches( content) == false )
                throw new InvalidDatatypeValueException("Value'"+content+
                                                        "' does not match regular expression facet " + fRegex.getPattern() );
        }

        if ( asBase )
            return;

        try {
            d = new BigDecimal( stripPlusIfPresent( content));
        }
        catch ( Exception nfe ) {
            throw new InvalidDatatypeValueException( getErrorString(DatatypeMessageProvider.NOT_DECIMAL,
                                                                    DatatypeMessageProvider.MSG_NONE,
                                                                    new Object[] { "'" + content +"'"}));
        }

            int size= enumeration.size();
            BigDecimal[]     enumDecimal  = new BigDecimal[size];
            int i = 0;
            try {
                for ( ; i < size; i++ )
                    enumDecimal[i] = new BigDecimal( stripPlusIfPresent(((String) enumeration.elementAt(i))));
            }
            catch ( NumberFormatException nfe ) {
                throw new InvalidDatatypeValueException( getErrorString(DatatypeMessageProvider.INVALID_ENUM_VALUE,
                                                                        DatatypeMessageProvider.MSG_NONE,
                                                                        new Object [] { enumeration.elementAt(i)}));
            }
            enumCheck(d, enumDecimal);
        }

        if ( (fFacetsDefined & DatatypeValidator.FACET_FRACTIONDIGITS)!=0 ) {
            if ( d.scale() > fFractionDigits )
                throw new InvalidDatatypeValueException(
                                                       getErrorString(DatatypeMessageProvider.FRACTION_EXCEEDED,
                                                                      DatatypeMessageProvider.MSG_NONE,
                                                                      new Object[] { "'" + content + "'" + " with fractionDigits = '"+ d.scale() +"'"
                                                                          , "'" + fFractionDigits + "'"}));
        }
        if ( (fFacetsDefined & DatatypeValidator.FACET_TOTALDIGITS)!=0 ) {
            int totalDigits = d.movePointRight(d.scale()).toString().length() -
            if ( totalDigits > fTotalDigits )
                throw new InvalidDatatypeValueException(
                                                       getErrorString(DatatypeMessageProvider.TOTALDIGITS_EXCEEDED,
                                                                      DatatypeMessageProvider.MSG_NONE,
                                                                      new Object[] { "'" + content + "'" + " with totalDigits = '"+ totalDigits +"'"
                                                                          , "'" + fTotalDigits + "'"} ));
        }
        boundsCheck(d);
        if ( fEnumeration != null )
            enumCheck(d, (BigDecimal[]) fEnumeration);

        return;

    }

    /**
     * This class deals with a bug in BigDecimal class
     * present up to version 1.1.2. 1.1.3 knows how
     * to deal with the + sign.
     *
     * This method strips the first '+' if it found
     * alone such as.
     * +33434.344
     *
     * If we find +- then nothing happens we just
     * return the string passed
     *
     * @param value
     * @return
     */
    static private String stripPlusIfPresent( String value ) {
        String strippedPlus = value;

        if ( value.length() >= 2 && value.charAt(0) == '+' && value.charAt(1) != '-' ) {
            strippedPlus = value.substring(1);
        }
        return strippedPlus;
    }

    private void enumCheck(BigDecimal v, BigDecimal[] enum) throws InvalidDatatypeValueException {
        for ( int i = 0; i < enum.length; i++ ) {
            if ( v.equals(enum[i] ) ) {
                return;
            }
        }
        throw new InvalidDatatypeValueException(
                                               getErrorString(DatatypeMessageProvider.NOT_ENUM_VALUE,
                                                              DatatypeMessageProvider.MSG_NONE,
                                                              new Object [] { v}));
    }

}
