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
 * StringValidator validates that XML content is a W3C string type.
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @version $Id: ListDatatypeValidator.java 316786 2001-01-17 20:46:07Z elena $
 */
public class ListDatatypeValidator extends AbstractDatatypeValidator{
    private Locale     fLocale          = null;

    private int        fLength           = 0;
    private int        fMaxLength        = Integer.MAX_VALUE;
    private int        fMinLength        = 0;
    private String     fPattern          = null;
    private Vector     fEnumeration      = null;
    private int        fFacetsDefined    = 0;
    private RegularExpression fRegex         = null;

    

    public  ListDatatypeValidator () throws InvalidDatatypeFacetException{

    }

    public ListDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
    boolean derivedByList ) throws InvalidDatatypeFacetException {


        fDerivedByList = derivedByList;

        if ( facets != null  ){
            for (Enumeration e = facets.keys(); e.hasMoreElements();) {
                String key = (String) e.nextElement();
                if ( key.equals(SchemaSymbols.ELT_LENGTH) ) {
                    fFacetsDefined += DatatypeValidator.FACET_LENGTH;
                    String lengthValue = (String)facets.get(key);
                    try {
                        fLength     = Integer.parseInt( lengthValue );
                    } catch (NumberFormatException nfe) {
                        throw new InvalidDatatypeFacetException("Length value '"+lengthValue+"' is invalid.");
                    }
                    if ( fLength < 0 )
                        throw new InvalidDatatypeFacetException("Length value '"+lengthValue+"'  must be a nonNegativeInteger.");

                } else if (key.equals(SchemaSymbols.ELT_MINLENGTH) ) {
                    fFacetsDefined += DatatypeValidator.FACET_MINLENGTH;
                    String minLengthValue = (String)facets.get(key);
                    try {
                        fMinLength     = Integer.parseInt( minLengthValue );
                    } catch (NumberFormatException nfe) {
                        throw new InvalidDatatypeFacetException("maxLength value '"+minLengthValue+"' is invalid.");
                    }
                } else if (key.equals(SchemaSymbols.ELT_MAXLENGTH) ) {
                    fFacetsDefined += DatatypeValidator.FACET_MAXLENGTH;
                    String maxLengthValue = (String)facets.get(key);
                    try {
                        fMaxLength     = Integer.parseInt( maxLengthValue );
                    } catch (NumberFormatException nfe) {
                        throw new InvalidDatatypeFacetException("maxLength value '"+maxLengthValue+"' is invalid.");
                    }
                } else if (key.equals(SchemaSymbols.ELT_ENUMERATION)) {
                    fFacetsDefined += DatatypeValidator.FACET_ENUMERATION;
                    fEnumeration    = (Vector)facets.get(key);
                } else {
                    throw new InvalidDatatypeFacetException("invalid facet tag : " + key);
                }
            }
            if (((fFacetsDefined & DatatypeValidator.FACET_LENGTH ) != 0 ) ) {
                if (((fFacetsDefined & DatatypeValidator.FACET_MAXLENGTH ) != 0 ) ) {
                    throw new InvalidDatatypeFacetException(
                    "It is an error for both length and maxLength to be members of facets." );  
                } else if (((fFacetsDefined & DatatypeValidator.FACET_MINLENGTH ) != 0 ) ) {
                    throw new InvalidDatatypeFacetException(
                    "It is an error for both length and minLength to be members of facets." );
                }
            }

            if ( ( (fFacetsDefined & ( DatatypeValidator.FACET_MINLENGTH |
            DatatypeValidator.FACET_MAXLENGTH) ) != 0 ) ) {
                if ( fMinLength > fMaxLength ) {
                    throw new InvalidDatatypeFacetException( "Value of minLength = " + fMinLength +
                    "must be greater that the value of maxLength" + fMaxLength );
                }
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
        } else{
            checkContentEnum( content, state , null); 
        }
        return null;
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
        return null;
    }

    public int compare( String content, String facetValue ){
        return 0;
    }

    /**
   * Returns a copy of this object.
   */
    public Object clone() throws CloneNotSupportedException  {
        ListDatatypeValidator newObj = null;
        try {
            newObj = new ListDatatypeValidator();

            newObj.fLocale           =  this.fLocale;
            newObj.fBaseValidator    =  this.fBaseValidator;
            newObj.fLength           =  this.fLength;
            newObj.fMaxLength        =  this.fMaxLength;
            newObj.fMinLength        =  this.fMinLength;
            newObj.fPattern          =  this.fPattern;
            newObj.fEnumeration      =  this.fEnumeration;
            newObj.fFacetsDefined    =  this.fFacetsDefined;
            newObj.fDerivedByList    =  this.fDerivedByList;
        } catch ( InvalidDatatypeFacetException ex) {
            ex.printStackTrace();
        }
        return newObj;
    }

    /**
     * validate that a content is valid against base datatype and facets (if any) 
     * 
     * @param content A string containing the content to be validated
     * @param state used with IDREF(s) datatypes
     * @param enumeration enumeration facet
     * @exception throws InvalidDatatypeException if the content is not valid
     * @exception InvalidDatatypeValueException
     */
     protected void checkContentEnum( String content,  Object state, Vector enumeration )
                                throws InvalidDatatypeValueException {
            
        StringTokenizer parsedList = null;
        int numberOfTokens = 0;
        parsedList= new StringTokenizer( content );
        numberOfTokens =  parsedList.countTokens();
        
        if (!this.fDerivedByList) { 
            try {
                if ( (fFacetsDefined & DatatypeValidator.FACET_MAXLENGTH) != 0 ) {
                    if ( numberOfTokens > fMaxLength ) {
                        throw new InvalidDatatypeValueException("Value '"+content+
                        "' with length ='"+  numberOfTokens + "' tokens"+
                        "' exceeds maximum length facet of '"+fMaxLength+"' tokens.");
                    }
                }
                if ( (fFacetsDefined & DatatypeValidator.FACET_MINLENGTH) != 0 ) {
                    if ( numberOfTokens < fMinLength ) {
                        throw new InvalidDatatypeValueException("Value '"+content+
                        "' with length ='"+ numberOfTokens+ "' tokens" +
                        "' is less than minimum length facet of '"+fMinLength+"' tokens." );
                    }
                }

                if ( (fFacetsDefined & DatatypeValidator.FACET_LENGTH) != 0 ) {
                    if ( numberOfTokens != fLength ) {
                        throw new InvalidDatatypeValueException("Value '"+content+
                        "' with length ='"+ numberOfTokens+ "' tokens" +
                        "' is not equal to length facet of '"+fLength+"' tokens.");
                    }
                }
                if (enumeration!=null) {
                    if (!verifyEnum(enumeration)) {
                        throw new InvalidDatatypeValueException("Enumeration '" +enumeration+"' for value '" +content+
                        "' is based on enumeration '"+fEnumeration+"'");
                    }
                }else {
                    enumeration = (fEnumeration!=null) ? fEnumeration : null;
                }
                                      
                ((ListDatatypeValidator)this.fBaseValidator).checkContentEnum( content, state, enumeration );
            } catch ( NoSuchElementException e ) {
                e.printStackTrace();
            }
        } 
        else { 
            if (enumeration !=null) {
                boolean valid = true;

                int eSize = enumeration.size(); 
                enumTemp.setSize(1);
                
                    currentEnumeration = (String)enumeration.elementAt(i);
                    eTokens = new StringTokenizer (currentEnumeration);
                    valid = true;
                    cTokens=parsedList;
                    
                    if (numberOfTokens == eTokens.countTokens()) {
                        try {
                            if (currentEnumeration.equals(content)) {
                                    if ( this.fBaseValidator != null ) {
                                        this.fBaseValidator.validate( cTokens.nextToken(), state );
                                    }
                                }
                                for (int j=0;j<numberOfTokens;j++) {
                                    token = cTokens.nextToken();
                                    eToken = eTokens.nextToken();
                                    enumTemp.setElementAt(eToken,0);
                                    if (fBaseValidator instanceof DecimalDatatypeValidator) {
                                        ((DecimalDatatypeValidator)fBaseValidator).checkContentEnum(token, state, enumTemp);
                                    } else if (fBaseValidator instanceof FloatDatatypeValidator) {
                                        ((FloatDatatypeValidator)fBaseValidator).checkContentEnum(token, state, enumTemp);
                                    } else if (fBaseValidator instanceof DoubleDatatypeValidator) {
                                        ((DoubleDatatypeValidator)fBaseValidator).checkContentEnum(token, state, enumTemp);
                                    } else { 
                                            throw new InvalidDatatypeValueException("Value '"+content+ "' must be one of "+enumeration);
                                        }
                                        this.fBaseValidator.validate( token, state );
                                    }
                                }
                            }
                        } catch (InvalidDatatypeValueException e) {
                            valid = false;
                        }
                        valid = false;
                    if (valid) break;
                if (!valid) {
                    throw new InvalidDatatypeValueException("Value '"+content+ "' does not match list type");
                }
                for (int k=0; k<numberOfTokens;k++) {
                        this.fBaseValidator.validate( parsedList.nextToken(), state );
                    }
                }
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


    
    private void setBasetype( DatatypeValidator base) {
        fBaseValidator = base;
    }

}

