package org.apache.xerces.validators.datatype;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Locale;
import java.text.Collator;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.utils.regex.RegularExpression;



/**
 * @author Jeffrey Rodriguez
 * @author Elena Litani
 * UnionValidator validates that XML content is a W3C string type.
 * Implements the September 22 XML Schema datatype Union Datatype type
 */
public class UnionDatatypeValidator extends AbstractDatatypeValidator {
    private Locale     fLocale          = null;
    private int fValidatorsSize = 0;
    private String     fPattern          = null;
    private Vector     fEnumeration      = null;
    private int        fFacetsDefined    = 0;

    private StringBuffer errorMsg = null;   
    private RegularExpression fRegex         = null;


    public  UnionDatatypeValidator () throws InvalidDatatypeFacetException{

    }


    public UnionDatatypeValidator ( DatatypeValidator base, Hashtable facets, boolean derivedBy ) throws InvalidDatatypeFacetException {
        setBasetype( base );  
        if ( facets != null ) {
            for ( Enumeration e = facets.keys(); e.hasMoreElements(); ) {
                String key = (String) e.nextElement();
                if ( key.equals(SchemaSymbols.ELT_ENUMERATION) ) {
                    fFacetsDefined += DatatypeValidator.FACET_ENUMERATION;
                    fEnumeration    = (Vector)facets.get(key);
                }
                else if ( key.equals(SchemaSymbols.ELT_PATTERN) ) {
                    fFacetsDefined += DatatypeValidator.FACET_PATTERN;
                    fPattern = (String)facets.get(key);
                    fRegex   = new RegularExpression(fPattern, "X");


                }
                else {
                    throw new InvalidDatatypeFacetException("invalid facet tag : " + key);
                }


    }

    public UnionDatatypeValidator ( Vector base)  {

        if ( base !=null ) {
            fValidatorsSize = base.size();
            fBaseValidators = new Vector(fValidatorsSize);
            fBaseValidators = base;

        }

    }



    /**
     * validate that a string is a W3C string type
     * 
     * @param content A string containing the content to be validated
     * @param list
     * @exception throws InvalidDatatypeException if the content is
     *                   not a W3C string type
     * @exception InvalidDatatypeValueException
     */
    public Object validate(String content, Object state)  throws InvalidDatatypeValueException
    {
        if ( content == null && state != null ) {
        }
        else {
            checkContentEnum( content, state, false , null );
        }
        return(null);
    }


    /**
     * set the locate to be used for error messages
     */
    public void setLocale(Locale locale) {
        fLocale = locale;
    }


    /**
     * 
     * @return                          A Hashtable containing the facets
     *         for this datatype.
     */
    public Hashtable getFacets(){
        return(null);
    }

    public int compare( String content, String facetValue ){
        return(0);
    }

    /**
   * Returns a copy of this object.
   */
    public Object clone() throws CloneNotSupportedException  {
        UnionDatatypeValidator newObj = null;
        try {
            newObj = new UnionDatatypeValidator();
            newObj.fLocale           =  this.fLocale;
            newObj.fBaseValidator    =  this.fBaseValidator;
            newObj.fBaseValidators   =  (Vector)this.fBaseValidators.clone();  
            newObj.fPattern          =  this.fPattern;
            newObj.fEnumeration      =  this.fEnumeration;
            newObj.fFacetsDefined    =  this.fFacetsDefined;
        }
        catch ( InvalidDatatypeFacetException ex ) {
            ex.printStackTrace();
        }
        return(newObj);

    }


    /**
    * check if enum is subset of fEnumeration
    * enum 1: <enumeration value="1 2"/>
    * enum 2: <enumeration value="1.0 2"/>
    *
    * @param enumeration facet
    *
    * @returns true if enumeration is subset of fEnumeration, false otherwise
    */
    private boolean verifyEnum (Vector enum){
        /* REVISIT: won't work for list datatypes in some cases: */
        if ((fFacetsDefined & DatatypeValidator.FACET_ENUMERATION ) != 0) {
            for (Enumeration e = enum.elements() ; e.hasMoreElements() ;) {
                if (fEnumeration.contains(e.nextElement()) == false) {
                    return false;                             
                }
            }
        }
        return true;
    }

    /**
     * validate if the content is valid against base datatype and facets (if any) 
     * 
     * @param content A string containing the content to be validated
     * @param pattern: true if pattern facet was applied, false otherwise
     * @param enumeration enumeration facet
     * @exception throws InvalidDatatypeException if the content is not valid
     */
    private void checkContentEnum( String content,  Object state, boolean pattern, Vector enumeration ) throws InvalidDatatypeValueException
    {
        boolean valid=false; 
        DatatypeValidator currentDV = null;
            if ( (fFacetsDefined & DatatypeValidator.FACET_PATTERN ) != 0 ) {
                if ( fRegex == null || fRegex.matches( content) == false )
                    throw new InvalidDatatypeValueException("Value '"+content+
                    "' does not match regular expression facet '" + fPattern + "'." );
                pattern = true;
            }

            if (enumeration!=null) {
                if (!verifyEnum(enumeration)) {
                    throw new InvalidDatatypeValueException("Enumeration '" +enumeration+"' for value '" +content+
                    "' is based on enumeration '"+fEnumeration+"'");
                }
            }
            else {
                enumeration = (fEnumeration!=null) ? fEnumeration : null;
            }
            ((UnionDatatypeValidator)this.fBaseValidator).checkContentEnum( content, state, pattern, enumeration  );
            return;
        }
        while ( (fValidatorsSize-1) > index++ ) {  

            currentDV =  (DatatypeValidator)this.fBaseValidators.elementAt(index);
            if ( valid ) break;
            try {
                if ( currentDV instanceof ListDatatypeValidator ) {
                    if ( pattern ) {
                        throw new InvalidDatatypeValueException("Facet \"Pattern\" can not be applied to a list datatype" );  
                    }
                    ((ListDatatypeValidator)currentDV).checkContentEnum( content, state, enumeration );
                }
                else if ( currentDV instanceof UnionDatatypeValidator ) {
                    ((UnionDatatypeValidator)currentDV).checkContentEnum( content, state, pattern, enumeration );
                }
                else {
                    if (enumeration!=null) {
                        if (currentDV instanceof DecimalDatatypeValidator) {
                            ((DecimalDatatypeValidator)currentDV).checkContentEnum(content, state, enumeration);
                        }
                        else if (currentDV instanceof FloatDatatypeValidator) {
                            ((FloatDatatypeValidator)currentDV).checkContentEnum(content, state, enumeration);
                        }
                        else if (currentDV instanceof DoubleDatatypeValidator) {
                            ((DoubleDatatypeValidator)currentDV).checkContentEnum(content, state, enumeration);
                        }
                        else {
                            if (enumeration.contains( content ) == false) {
                                throw new InvalidDatatypeValueException("Value '"+content+ "' must be one of "+ enumeration);
                            }
                            ((DatatypeValidator)currentDV).validate( content, state );
                        }   
                    }
                    else {
                        ((DatatypeValidator)currentDV).validate( content, state );
                    }
                }
                valid=true;

            }
            catch ( InvalidDatatypeValueException e ) {
            }
        }
        if ( !valid ) {
            throw new InvalidDatatypeValueException( "Content '"+content+"' does not match any union types" );  
        }
    }


    private void setBasetype( DatatypeValidator base) {
        fBaseValidator = base;
    }

}


