package org.apache.xerces.validators.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import java.io.IOException;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.utils.regex.RegularExpression;

/**
 *
 * DecimalValidator validates that content satisfies the W3C XML Datatype for decimal
 *
 * @author Ted Leung
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @version $Id: DecimalDatatypeValidator.java 316786 2001-01-17 20:46:07Z elena $
 */

public class DecimalDatatypeValidator extends AbstractDatatypeValidator {
    private Locale            fLocale           = null;
    private BigDecimal[]      fEnumDecimal      = null;
    private String            fPattern          = null;
    private BigDecimal        fMaxInclusive     = null;
    private BigDecimal        fMaxExclusive     = null;
    private BigDecimal        fMinInclusive     = null;
    private BigDecimal        fMinExclusive     = null;
    private int               fFacetsDefined    = 0;
    private int               fScale            = 0;
    private int               fPrecision        = 0;
    private boolean           isMaxExclusiveDefined = false;
    private boolean           isMaxInclusiveDefined = false;
    private boolean           isMinExclusiveDefined = false;
    private boolean           isMinInclusiveDefined = false;
    private boolean           isScaleDefined        = false;
    private boolean           isPrecisionDefined    = false;
    private DatatypeMessageProvider fMessageProvider = new DatatypeMessageProvider();
    private RegularExpression       fRegex           = null;
    private Hashtable               fFacets          = null;



    public DecimalDatatypeValidator () throws InvalidDatatypeFacetException {
    }

    public DecimalDatatypeValidator ( DatatypeValidator base, Hashtable facets,
                                      boolean derivedByList ) throws InvalidDatatypeFacetException {



            fFacets = facets;

            Vector enumeration = null;
            String value       = null;
            for (Enumeration e = facets.keys(); e.hasMoreElements();) {
                String key   = (String) e.nextElement();
                try {
                    if (key.equals(SchemaSymbols.ELT_PATTERN)) {
                        value = ((String) facets.get(key ));
                        fFacetsDefined += DatatypeValidator.FACET_PATTERN;
                        fPattern        = value;
                        if ( fPattern != null )
                            fRegex = new RegularExpression(fPattern, "X" );
                    } else if (key.equals(SchemaSymbols.ELT_ENUMERATION)) {
                        fFacetsDefined += DatatypeValidator.FACET_ENUMERATION;
                        enumeration     = (Vector)facets.get(key);
                    } else if (key.equals(SchemaSymbols.ELT_MAXINCLUSIVE)) {
                        value = ((String) facets.get(key ));
                        fFacetsDefined += DatatypeValidator.FACET_MAXINCLUSIVE;
                        fMaxInclusive    = new BigDecimal(stripPlusIfPresent(value));
                    } else if (key.equals(SchemaSymbols.ELT_MAXEXCLUSIVE)) {
                        value = ((String) facets.get(key ));
                        fFacetsDefined += DatatypeValidator.FACET_MAXEXCLUSIVE;
                        fMaxExclusive   = new BigDecimal(stripPlusIfPresent( value));
                    } else if (key.equals(SchemaSymbols.ELT_MININCLUSIVE)) {
                        value = ((String) facets.get(key ));
                        fFacetsDefined += DatatypeValidator.FACET_MININCLUSIVE;
                        fMinInclusive   = new BigDecimal(stripPlusIfPresent(value));
                    } else if (key.equals(SchemaSymbols.ELT_MINEXCLUSIVE)) {
                        value = ((String) facets.get(key ));
                        fFacetsDefined += DatatypeValidator.FACET_MINEXCLUSIVE;
                        fMinExclusive   = new BigDecimal(stripPlusIfPresent(value));
                    } else if (key.equals(SchemaSymbols.ELT_PRECISION)) {
                        value = ((String) facets.get(key ));
                        fFacetsDefined += DatatypeValidator.FACET_PRECISSION;
                        isPrecisionDefined = true;
                        fPrecision      = Integer.parseInt(value );
                    } else if (key.equals(SchemaSymbols.ELT_SCALE)) {
                        value = ((String) facets.get(key ));
                        fFacetsDefined += DatatypeValidator.FACET_SCALE;
                        isScaleDefined  = true;
                        fScale          = Integer.parseInt( value );
                    } else {
                        throw new InvalidDatatypeFacetException(
                                                                getErrorString( DatatypeMessageProvider.MSG_FORMAT_FAILURE,
                                                                                DatatypeMessageProvider.MSG_NONE, null));
                    }
                } catch ( Exception ex ){
                    throw new InvalidDatatypeFacetException( getErrorString(
                                                                            DatatypeMessageProvider.IllegalFacetValue, 
                                                                            DatatypeMessageProvider.MSG_NONE, new Object [] { value, key}));
                }
            }
            isMaxExclusiveDefined = ((fFacetsDefined & 
                                        DatatypeValidator.FACET_MAXEXCLUSIVE ) != 0 )?true:false;
            isMaxInclusiveDefined = ((fFacetsDefined & 
                                        DatatypeValidator.FACET_MAXINCLUSIVE ) != 0 )?true:false;

            isMinExclusiveDefined = ((fFacetsDefined &
                                        DatatypeValidator.FACET_MINEXCLUSIVE ) != 0 )?true:false;
            isMinInclusiveDefined = ((fFacetsDefined &
                                        DatatypeValidator.FACET_MININCLUSIVE ) != 0 )?true:false;


            if ( isMaxExclusiveDefined && isMaxInclusiveDefined ) {
                throw new InvalidDatatypeFacetException(
                                                        "It is an error for both maxInclusive and maxExclusive to be specified for the same datatype." ); 
            }
            if ( isMinExclusiveDefined && isMinInclusiveDefined ) {
                throw new InvalidDatatypeFacetException(
                                                        "It is an error for both minInclusive and minExclusive to be specified for the same datatype." ); 
            }

            if ( isMaxExclusiveDefined && isMinExclusiveDefined ){
                int compareTo = this.fMaxExclusive.compareTo( this.fMinExclusive );
                if ( compareTo != 1)
                    throw new InvalidDatatypeFacetException(
                                                            "maxExclusive value ='" + this.fMaxExclusive + "'must be > than minExclusive value ='" + 
                                                            this.fMinExclusive + "'. " );

            }
            if ( isMaxInclusiveDefined && isMinInclusiveDefined ){
                int compareTo = this.fMaxInclusive.compareTo( this.fMinInclusive );

                if ( compareTo == -1  )
                    throw new InvalidDatatypeFacetException(
                                                            "maxInclusive value ='" + this.fMaxInclusive + "'must be >= than minInclusive value ='" + 
                                                            this.fMinInclusive + "'. " );
            }


            if ( isMaxExclusiveDefined && isMinInclusiveDefined ){
                int compareTo = this.fMaxExclusive.compareTo( this.fMinInclusive );
                if ( compareTo != 1)
                    throw new InvalidDatatypeFacetException(
                                                            "maxExclusive value ='" + this.fMaxExclusive + "'must be > than minInclusive value ='" + 
                                                            this.fMinInclusive + "'. " );

            }
            if ( isMaxInclusiveDefined && isMinExclusiveDefined ){
                int compareTo = this.fMaxInclusive.compareTo( this.fMinExclusive );
                if ( compareTo != 1)
                    throw new InvalidDatatypeFacetException(
                                                            "maxInclusive value ='" + this.fMaxInclusive + "'must be > than minExclusive value ='" + 
                                                            this.fMinExclusive + "'. " );
            }


            if ( (fFacetsDefined & DatatypeValidator.FACET_ENUMERATION ) != 0 ) {
                if (enumeration != null) {
                    fEnumDecimal = new BigDecimal[enumeration.size()];
                    int i = 0;
                    try {
                        for ( ; i < enumeration.size(); i++) {
                            fEnumDecimal[i] = 
                            new BigDecimal( stripPlusIfPresent(((String) enumeration.elementAt(i))));
                        }

                    } catch ( Exception idve ){
                        throw new InvalidDatatypeFacetException(
                                                                getErrorString(DatatypeMessageProvider.InvalidEnumValue,
                                                                                DatatypeMessageProvider.MSG_NONE,
                                                                                new Object [] { enumeration.elementAt(i)}));
                    }

                }
            }

    }


    /**
     * validate that a string matches the decimal datatype
     *
     * validate returns true or false depending on whether the string content is a
     * W3C decimal type.
     * 
     * @param content A string containing the content to be validated
     *                            cd 
     * @exception throws InvalidDatatypeException if the content is
     *  is not a W3C decimal type
     */

    public Object validate(String content, Object state) throws InvalidDatatypeValueException {
        checkContentEnum(content, state, null);
        return null;
    }


    /**
     * validate if the content is valid against base datatype and facets (if any)
     * this function might be called directly from UnionDatatype or ListDatatype     
     * 
     * @param content A string containing the content to be validated
     * @param enumeration A vector with enumeration strings  
     * @exception throws InvalidDatatypeException if the content is
     *  is not a W3C decimal type;
     * @exception throws InvalidDatatypeFacetException if enumeration is not BigDecimal
     */

    protected void checkContentEnum(String content, Object state, Vector enumeration) 
                                           throws InvalidDatatypeValueException{
            ((DecimalDatatypeValidator)this.fBaseValidator).checkContentEnum( content, state, enumeration );
        }

        if ( (fFacetsDefined & DatatypeValidator.FACET_PATTERN ) != 0 ) {
            if ( fRegex == null || fRegex.matches( content) == false )
                throw new InvalidDatatypeValueException("Value'"+content+
                    "' does not match regular expression facet " + fRegex.getPattern() );
        }

         
        try {
            d = new BigDecimal( stripPlusIfPresent( content));
        } catch (Exception nfe) {
            throw new InvalidDatatypeValueException(
                                                    getErrorString(DatatypeMessageProvider.NotDecimal,
                                                                    DatatypeMessageProvider.MSG_NONE,
                                                                    new Object[] { "'" + content +"'"}));
        }
        
            int size= enumeration.size();
            BigDecimal[]     enumDecimal  = new BigDecimal[size];
            int i = 0;
            try {
                for ( ; i < size; i++) 
                    enumDecimal[i] = new BigDecimal( stripPlusIfPresent(((String) enumeration.elementAt(i))));
            } catch (NumberFormatException nfe) {
                   throw new InvalidDatatypeValueException(
                                                                getErrorString(DatatypeMessageProvider.InvalidEnumValue,
                                                                                DatatypeMessageProvider.MSG_NONE,
                                                                                new Object [] { enumeration.elementAt(i)}));
            }
            enumCheck(d, enumDecimal);
        }

        if ( isScaleDefined == true ) {
            if (d.scale() > fScale)
                throw new InvalidDatatypeValueException(
                                                        getErrorString(DatatypeMessageProvider.ScaleExceeded,
                                                                        DatatypeMessageProvider.MSG_NONE,
                                                                        new Object[] { content}));
        }
        if ( isPrecisionDefined == true ) {
            int precision = d.movePointRight(d.scale()).toString().length() - 
            if (precision > fPrecision)
                throw new InvalidDatatypeValueException(
                                getErrorString(DatatypeMessageProvider.PrecisionExceeded,
                                 DatatypeMessageProvider.MSG_NONE,
                                 new Object[] { "'" + content + "'" + "with precision = '"+ precision +"'" 
                                              , "'" + fPrecision + "'" } ));
        }
        boundsCheck(d);
        if (  fEnumDecimal != null )
            enumCheck(d, fEnumDecimal);
            
        return;

    }

    /*
     * check that a facet is in range, assumes that facets are compatible -- compatibility ensured by setFacets
     */
    public void boundsCheck(BigDecimal d) throws InvalidDatatypeValueException {
        boolean minOk = false;
        boolean maxOk = false;
        String  upperBound =  (fMaxExclusive != null )? ( fMaxExclusive.toString() ):
                              ( ( fMaxInclusive != null )?fMaxInclusive.toString():"");

        String  lowerBound =  (fMinExclusive != null )? ( fMinExclusive.toString() ):
                              (( fMinInclusive != null )?fMinInclusive.toString():""); 
        String  lowerBoundIndicator = "";
        String  upperBoundIndicator = "";


        if ( isMaxInclusiveDefined){
            maxOk = (d.compareTo(fMaxInclusive) <= 0);
            upperBound          = fMaxInclusive.toString();
            if ( upperBound != null ){
                upperBoundIndicator = "<="; 
            } else {
                upperBound="";
            }
        } else if ( isMaxExclusiveDefined){
            maxOk = (d.compareTo(fMaxExclusive) < 0);
            upperBound = fMaxExclusive.toString();
            if ( upperBound != null ){
                upperBoundIndicator = "<";
            } else {
                upperBound = "";
            }
        } else{
            maxOk = (!isMaxInclusiveDefined && ! isMaxExclusiveDefined);
        }


        if ( isMinInclusiveDefined){
            minOk = (d.compareTo(fMinInclusive) >= 0);
            lowerBound = fMinInclusive.toString();
            if( lowerBound != null ){
            lowerBoundIndicator = "<=";
            }else {
                lowerBound = "";
            }
        } else if ( isMinExclusiveDefined){
            minOk = (d.compareTo(fMinExclusive) > 0);
            lowerBound = fMinExclusive.toString();
            if( lowerBound != null ){
            lowerBoundIndicator = "<";
            } else {
                lowerBound = "";
            }
        } else{
            minOk = (!isMinInclusiveDefined && !isMinExclusiveDefined);
        }

        if (!(minOk && maxOk))
            throw new InvalidDatatypeValueException (
                                                    getErrorString(DatatypeMessageProvider.OutOfBounds,
                                                                   DatatypeMessageProvider.MSG_NONE,
                                                                   new Object [] { d.toString() ,  lowerBound ,
                                                                       upperBound, lowerBoundIndicator, upperBoundIndicator}));

    }

    private void enumCheck(BigDecimal v, BigDecimal[] enum) throws InvalidDatatypeValueException {
        for (int i = 0; i < enum.length; i++) {
            if (v.equals(enum[i] ))
            {
                return;
            }
        }
        throw new InvalidDatatypeValueException(
                                               getErrorString(DatatypeMessageProvider.NotAnEnumValue,
                                                              DatatypeMessageProvider.MSG_NONE,
                                                              new Object [] { v}));
    }

    /**
     * set the locate to be used for error messages
     */
    public void setLocale(Locale locale) {
        fLocale = locale;
    }

    public Hashtable getFacets(){
        return fFacets;
    }

    private String getErrorString(int major, int minor, Object args[]) {
        try {
            return fMessageProvider.createMessage(fLocale, major, minor, args);
        } catch (Exception e) {
            return "Illegal Errorcode "+minor;
        }
    }
    /**
       * Returns a copy of this object.
       */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("clone() is not supported in "+this.getClass().getName());
    }


    /*
    public static void main(String args[]) {
        try {
            DatatypeValidator v = new DecimalValidator();
            Hashtable facets = new Hashtable();
            facets.put("minInclusive","0");
            DatatypeValidator nonneg = new DecimalValidator();
            nonneg.setBasetype(v);
            nonneg.setFacets(facets);
            facets = new Hashtable();
            facets.put("minInclusive","-1");
            DatatypeValidator bad = new DecimalValidator();
            bad.setBasetype(nonneg);
            bad.setFacets(facets);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    public int compare( String content1, String content2){
        return 0;
    }


    private void setBasetype(DatatypeValidator base) {
        fBaseValidator =  base;
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
    static private String stripPlusIfPresent( String value ){
        String strippedPlus = value;

        if ( value.length() >= 2 && value.charAt(0) == '+' && value.charAt(1) != '-' ) {
            strippedPlus = value.substring(1);
        }
        return strippedPlus;
    }

    /**
     * This method checks the current Facet being set
     * against the base Facet.
     * Current Facet should be more restrictive than
     * parent.
     * Facet values are inherited from base. 
     * 
     * @param thisTypeFacets
     * @param baseTypeFacets
     * @return 
     * @exception InvalidDatatypeFacetException
     */
    private Hashtable checkForFacetConsistency( Hashtable thisTypeFacets, 
                                                Hashtable baseTypeFacets ) throws InvalidDatatypeFacetException{
        String  thisTypeFacetValue;
        String  baseValue;
            Enumeration setOfBaseKeys = baseTypeFacets.keys();
            String keyInBase;
            BigDecimal  valueOfThisType = null;
            BigDecimal  valueOfBase     = null;
            while (  setOfBaseKeys.hasMoreElements() ) {
                keyInBase          = (String) setOfBaseKeys.nextElement();
                baseValue          = (String) baseTypeFacets.get(keyInBase);
                thisTypeFacetValue = (String) thisTypeFacets.get(keyInBase);
                if ( thisTypeFacetValue == null )    {
                    String strThisType = null;
                    thisTypeFacets.put( keyInBase, 
                                        baseValue );
                    if ( keyInBase.equals( SchemaSymbols.ELT_MAXEXCLUSIVE ) &&
                         thisTypeFacets.containsKey( SchemaSymbols.ELT_MAXINCLUSIVE ) ){

                        strThisType     = (String) thisTypeFacets.get( SchemaSymbols.ELT_MAXINCLUSIVE );
                        valueOfThisType = new BigDecimal(stripPlusIfPresent(strThisType));
                        valueOfBase     = new BigDecimal(stripPlusIfPresent( baseValue));
                        if ( valueOfThisType.compareTo( valueOfBase) == -1 ){ 
                            thisTypeFacets.remove( keyInBase);
                        } else {
                            thisTypeFacets.remove( SchemaSymbols.ELT_MAXINCLUSIVE );
                        }

                    } else if ( keyInBase.equals( SchemaSymbols.ELT_MAXINCLUSIVE ) && 
                                thisTypeFacets.containsKey( SchemaSymbols.ELT_MAXEXCLUSIVE ) ){
                        strThisType     = (String) thisTypeFacets.get( SchemaSymbols.ELT_MAXEXCLUSIVE );
                        valueOfThisType = new BigDecimal(stripPlusIfPresent(strThisType));
                        valueOfBase     = new BigDecimal(stripPlusIfPresent( baseValue));
                        if ( valueOfThisType.compareTo( valueOfBase) == -1 ){ 
                            thisTypeFacets.remove( keyInBase);
                        } else {
                            thisTypeFacets.remove( SchemaSymbols.ELT_MAXEXCLUSIVE );
                        }
                    } else if ( keyInBase.equals( SchemaSymbols.ELT_MINEXCLUSIVE) &&
                                thisTypeFacets.containsKey( SchemaSymbols.ELT_MININCLUSIVE ) ){
                        strThisType     = (String) thisTypeFacets.get( SchemaSymbols.ELT_MININCLUSIVE );
                        valueOfThisType = new BigDecimal(stripPlusIfPresent(strThisType));
                        valueOfBase     = new BigDecimal(stripPlusIfPresent( baseValue));
                        if ( valueOfThisType.compareTo( valueOfBase) == 1 ){ 
                            thisTypeFacets.remove( keyInBase);
                        } else {
                            thisTypeFacets.remove( SchemaSymbols.ELT_MININCLUSIVE );
                        }
                    } else if ( keyInBase.equals( SchemaSymbols.ELT_MININCLUSIVE ) &&
                                thisTypeFacets.containsKey( SchemaSymbols.ELT_MINEXCLUSIVE ) ){
                        strThisType     = (String) thisTypeFacets.get( SchemaSymbols.ELT_MINEXCLUSIVE );
                        valueOfThisType = new BigDecimal(stripPlusIfPresent(strThisType));
                        valueOfBase     = new BigDecimal(stripPlusIfPresent(baseValue));
                        if ( valueOfThisType.compareTo( valueOfBase) ==  1 ){ 
                            thisTypeFacets.remove( keyInBase);
                        } else {
                            thisTypeFacets.remove( SchemaSymbols.ELT_MINEXCLUSIVE );
                        }

                    }

                    if ( keyInBase.equals( SchemaSymbols.ELT_MAXEXCLUSIVE ) ){
                        valueOfThisType = new BigDecimal(stripPlusIfPresent(thisTypeFacetValue));
                        valueOfBase     = new BigDecimal(stripPlusIfPresent( baseValue));
                        if ( ( valueOfThisType.compareTo( valueOfBase) ) == -1 ){ 
                            ;
                            ;
                        }
                    } else if ( keyInBase.equals( SchemaSymbols.ELT_MAXINCLUSIVE ) ){
                        valueOfThisType = new BigDecimal(stripPlusIfPresent(thisTypeFacetValue));
                        valueOfBase     = new BigDecimal(stripPlusIfPresent( baseValue));
                        if ( ( valueOfThisType.compareTo( valueOfBase) ) == -1 ){ 
                            ;
                            ;
                        }
                    } else if ( keyInBase.equals( SchemaSymbols.ELT_MINEXCLUSIVE ) ){
                        valueOfThisType = new BigDecimal(stripPlusIfPresent(thisTypeFacetValue));
                        valueOfBase     = new BigDecimal(stripPlusIfPresent( baseValue));
                        if ( ( valueOfThisType.compareTo( valueOfBase) ) == -1 ){ 
                            ;
                            ;
                        }
                    } else if ( keyInBase.equals( SchemaSymbols.ELT_MININCLUSIVE ) ){
                        valueOfThisType = new BigDecimal(stripPlusIfPresent(thisTypeFacetValue));
                        valueOfBase     = new BigDecimal(stripPlusIfPresent( baseValue));
                        if ( ( valueOfThisType.compareTo( valueOfBase) ) == -1 ){ 
                            ;
                            ;
                        }

                    }
                }
            }
        }

        return thisTypeFacets;
    }
}

