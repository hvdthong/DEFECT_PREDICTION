package org.apache.xerces.validators.datatype;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.utils.regex.RegularExpression;
import org.apache.xerces.utils.Base64;
import org.apache.xerces.utils.HexBin;


/**
 *
 * binaryValidator validates that XML content is a W3C binary type.
 *
 * @author Ted Leung
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @version $Id: BinaryDatatypeValidator.java 316317 2000-10-17 00:52:40Z jeffreyr $
 */

public class BinaryDatatypeValidator extends AbstractDatatypeValidator {
    private int                fLength          = 0;
    private int                fMaxLength       = Integer.MAX_VALUE;
    private int                fMinLength       = 0;
    private String             fPattern         = null;
    private Vector             fEnumeration     = null;
    private int                fFacetsDefined   = 0;

    public BinaryDatatypeValidator () throws InvalidDatatypeFacetException {
    }

    public BinaryDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
                                     boolean derivedByList ) throws InvalidDatatypeFacetException {
        if ( base != null )



        if ( facets != null  )  {
            for (Enumeration e = facets.keys(); e.hasMoreElements();) {
                String key = (String) e.nextElement();
                if ( key.equals(SchemaSymbols.ELT_LENGTH ) ) {
                    fFacetsDefined += DatatypeValidator.FACET_LENGTH;
                    String lengthValue = (String)facets.get(key);
                    try {
                        fLength     = Integer.parseInt( lengthValue );
                    } catch (NumberFormatException nfe) {
                        throw new InvalidDatatypeFacetException("Length value '"+
                                                                lengthValue+"' is invalid.");
                    }
                    if ( fLength < 0 )
                        throw new InvalidDatatypeFacetException("Length value '"+
                                                                lengthValue+"'  must be a nonNegativeInteger.");

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
                } else if (key.equals(SchemaSymbols.ELT_PATTERN)) {
                    fFacetsDefined += DatatypeValidator.FACET_PATTERN;
                    fPattern = (String)facets.get(key);
                } else if (key.equals(SchemaSymbols.ELT_ENUMERATION)) {
                    fFacetsDefined += DatatypeValidator.FACET_ENUMERATION;
                    fEnumeration = (Vector)facets.get(key);
                } else if (key.equals(SchemaSymbols.ELT_ENCODING )) {
                    fFacetsDefined += DatatypeValidator.FACET_MAXINCLUSIVE;
                    fEncoding = (String)facets.get(key);
                } else {
                    throw new InvalidDatatypeFacetException();
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
                    throw new InvalidDatatypeFacetException( "Value of maxLength = " + fMaxLength +
                                                                "must be greater that the value of minLength" + fMinLength );
                }
            }
    }



    /**
     * validate that a string is a W3C binary type
     *
     * validate returns true or false depending on whether the string content is an
     * instance of the W3C binary datatype
     *
     * @param content A string containing the content to be validated
     *
     * @exception throws InvalidDatatypeException if the content is
     *  not a W3C binary type
     */
    public Object validate(String content, Object state ) throws InvalidDatatypeValueException {

            this.fBaseValidator.validate( content, state );
        }

                if ( Base64.isBase64( content ) == false ) {
                    throw new InvalidDatatypeValueException( "Value '"+
                                                                content+ "'  must be" + "is not encoded in Base64" );
                }
                if ( HexBin.isHex( content ) == false ){
                    throw new InvalidDatatypeValueException( "Value '"+
                                                                content+ "'  must be" + "is not encoded in Hex" );
                }
            }
        }
        return null;
    }

    /**
     * Compare two Binary Datatype Lexical values.
     * 
     * @param content1
     * @param content2
     * @return 
     */
    public int compare( String content1, String content2){
        return 0;
    }

    /**
     * Returns a Hastable that represent the facets
     * state of the datatype.
     * 
     * @return 
     */
    public Hashtable getFacets(){
        return null;
    }


    /**
    * Returns a copy of this object.
    */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("clone() is not supported in "+this.getClass().getName());
    }


    /**
     * Set base type
     * 
     * @param base
     */
    private void setBasetype(DatatypeValidator base) {
        fBaseValidator = base;
    }



}
