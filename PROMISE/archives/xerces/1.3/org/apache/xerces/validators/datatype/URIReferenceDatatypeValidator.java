package org.apache.xerces.validators.datatype;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import org.apache.xerces.utils.URI;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.utils.regex.RegularExpression;



/**
 * URIValidator validates that XML content is a W3C uri type,
 * according to RFC 2396
 * 
 * @author Ted Leung
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @see          RFC 2396 
 * @version  $Id: URIReferenceDatatypeValidator.java 316317 2000-10-17 00:52:40Z jeffreyr $
 */
public class URIReferenceDatatypeValidator extends AbstractDatatypeValidator {
    private DatatypeValidator fBaseValidator     = null;

    private int       fLength          = 0;
    private int       fMaxLength       = Integer.MAX_VALUE;
    private int       fMinLength       = 0;
    private String    fPattern         = null;
    private Vector    fEnumeration     = null;
    private int       fFacetsDefined   = 0;
    private RegularExpression    fRegex = null;



    public URIReferenceDatatypeValidator () throws InvalidDatatypeFacetException{
    }

    public URIReferenceDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
                                           boolean derivedByList ) throws InvalidDatatypeFacetException {


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
                } else if (key.equals(SchemaSymbols.ELT_PATTERN)) {
                    fFacetsDefined += DatatypeValidator.FACET_PATTERN;
                    fPattern = (String)facets.get(key);
                    if ( fPattern != null )
                        fRegex = new RegularExpression(fPattern, "X" );

                } else if (key.equals(SchemaSymbols.ELT_ENUMERATION)) {
                    fFacetsDefined += DatatypeValidator.FACET_ENUMERATION;
                    fEnumeration = (Vector)facets.get(key);
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
     * Validates content to conform to a URIReference
     * definition and to conform to the facets allowed
     * for this datatype.
     * 
     * @param content
     * @param state
     * @return 
     * @exception InvalidDatatypeValueException
     */
    public Object validate(String content, Object state) 
    throws InvalidDatatypeValueException
    {
        StringTokenizer parsedList = null;
        URI             uriContent = null;

        if ( (fFacetsDefined & DatatypeValidator.FACET_PATTERN ) != 0 ) {
            if ( fRegex == null || fRegex.matches( content) == false )
                throw new InvalidDatatypeValueException("Value '"+content+
                                                        "' does not match regular expression facet" + fPattern );
        }

           
        try {
                uriContent = new URI( content );
        } catch (  URI.MalformedURIException ex ) {
            throw new InvalidDatatypeValueException("Value '"+content+
                                                                        "' is a Malformed URI ");

        }
        return null;
    }

    /**
     * Compares two URIReferences for equality.
     * This is not really well defined.
     * 
     * @param content1
     * @param content2
     * @return 
     */
    public int compare( String content1, String content2){
        return 0;
    }
    /**
     * 
     * @return Returns a Hashtable containing the facet information
     *         for this datatype.
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



    private void setBasetype(DatatypeValidator base) {
        fBaseValidator = base;
    }

}
