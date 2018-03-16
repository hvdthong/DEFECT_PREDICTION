package org.apache.xerces.validators.datatype;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Locale;
import java.text.Collator;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.utils.regex.RegularExpression;
import org.apache.xerces.utils.XMLCharacterProperties;

/**
 * AbstractStringValidator is a base class for anyURI, string,
 * hexBinary, base64Binary, QName and Notation datatypes.
 *
 * @author Elena Litani
 * @version $Id: AbstractStringValidator.java 317921 2001-11-15 18:56:03Z  $
 */
public abstract class AbstractStringValidator extends AbstractDatatypeValidator {

    protected int        fLength          = 0;
    protected int        fMaxLength       = Integer.MAX_VALUE;
    protected int        fMinLength       = 0;
    protected Vector     fEnumeration     = null;

    public static final String FACET_SPECIAL_TOKEN       = "specialToken";
    public static final String SPECIAL_TOKEN_NONE        = "NONE";
    public static final String SPECIAL_TOKEN_NMTOKEN     = "NMTOKEN";
    public static final String SPECIAL_TOKEN_NAME        = "Name";
    public static final String SPECIAL_TOKEN_IDNAME      = "ID(Name)";
    public static final String SPECIAL_TOKEN_IDREFNAME   = "IDREF(Name)";
    public static final String SPECIAL_TOKEN_NCNAME      = "NCName";
    public static final String SPECIAL_TOKEN_IDNCNAME    = "ID(NCName)";
    public static final String SPECIAL_TOKEN_IDREFNCNAME = "IDREF(NCName)";
    public static final String SPECIAL_TOKEN_ENTITY      = "ENTITY(NCName)";


    public  AbstractStringValidator () throws InvalidDatatypeFacetException{

    }

    public AbstractStringValidator ( DatatypeValidator base, Hashtable facets,
                                     boolean derivedByList ) throws InvalidDatatypeFacetException {

        fBaseValidator = base;

        if ( derivationList(derivedByList) )
            return;
        if ( facets != null ) {
            for ( Enumeration e = facets.keys(); e.hasMoreElements(); ) {
                String key = (String) e.nextElement();

                if ( key.equals(SchemaSymbols.ELT_LENGTH) ) {
                    fFacetsDefined |= DatatypeValidator.FACET_LENGTH;
                    String lengthValue = (String)facets.get(key);
                    try {
                        fLength     = Integer.parseInt( lengthValue );
                    }
                    catch ( NumberFormatException nfe ) {
                        throw new InvalidDatatypeFacetException("Length value '"+lengthValue+"' is invalid.");
                    }
                    if ( fLength < 0 )
                        throw new InvalidDatatypeFacetException("Length value '"+lengthValue+"'  must be a nonNegativeInteger.");

                }
                else if ( key.equals(SchemaSymbols.ELT_MINLENGTH) ) {
                    fFacetsDefined |= DatatypeValidator.FACET_MINLENGTH;
                    String minLengthValue = (String)facets.get(key);
                    try {
                        fMinLength     = Integer.parseInt( minLengthValue );
                    }
                    catch ( NumberFormatException nfe ) {
                        throw new InvalidDatatypeFacetException("minLength value '"+minLengthValue+"' is invalid.");
                    }
                    if ( fMinLength < 0 )
                        throw new InvalidDatatypeFacetException("minLength value '"+minLengthValue+"'  must be a nonNegativeInteger.");

                }
                else if ( key.equals(SchemaSymbols.ELT_MAXLENGTH) ) {
                    fFacetsDefined |= DatatypeValidator.FACET_MAXLENGTH;
                    String maxLengthValue = (String)facets.get(key);
                    try {
                        fMaxLength     = Integer.parseInt( maxLengthValue );
                    }
                    catch ( NumberFormatException nfe ) {
                        throw new InvalidDatatypeFacetException("maxLength value '"+maxLengthValue+"' is invalid.");
                    }
                    if ( fMaxLength < 0 )
                        throw new InvalidDatatypeFacetException("maxLength value '"+maxLengthValue+"'  must be a nonNegativeInteger.");
                }
                else if ( key.equals(SchemaSymbols.ELT_PATTERN) ) {
                    fFacetsDefined |= DatatypeValidator.FACET_PATTERN;
                    fPattern = (String)facets.get(key);
                    if ( fPattern != null )
                        fRegex = new RegularExpression(fPattern, "X");
                }
                else if ( key.equals(SchemaSymbols.ELT_ENUMERATION) ) {
                    fEnumeration = (Vector)facets.get(key);
                    fFacetsDefined |= DatatypeValidator.FACET_ENUMERATION;
                }
                    fFlags = ((Short)facets.get(key)).shortValue();
                }
                    setTokenType((String)facets.get(key));
                }
                else {
                    assignAdditionalFacets(key, facets);
                }
            }

            if ( base != null ) {
                if ( (fFacetsDefined & DatatypeValidator.FACET_ENUMERATION) != 0 &&
                     (fEnumeration != null) ) {
                    int i = 0;
                    try {
                        for ( ; i < fEnumeration.size(); i++ ) {
                            ((AbstractStringValidator)base).checkContent ((String)fEnumeration.elementAt(i), null, false);
                        }
                    }
                    catch ( Exception idve ) {
                        throw new InvalidDatatypeFacetException( "Value of enumeration = '" + fEnumeration.elementAt(i) +
                                                                 "' must be from the value space of base.");
                    }
                }
            }

            if ( ((fFacetsDefined & DatatypeValidator.FACET_LENGTH ) != 0 ) ) {
                if ( ((fFacetsDefined & DatatypeValidator.FACET_MAXLENGTH ) != 0 ) ) {
                    throw new InvalidDatatypeFacetException("It is an error for both length and maxLength to be members of facets." );
                }
                else if ( ((fFacetsDefined & DatatypeValidator.FACET_MINLENGTH ) != 0 ) ) {
                    throw new InvalidDatatypeFacetException("It is an error for both length and minLength to be members of facets." );
                }
            }

            if ( ( (fFacetsDefined & ( DatatypeValidator.FACET_MINLENGTH |
                                       DatatypeValidator.FACET_MAXLENGTH) ) != 0 ) ) {
                if ( fMinLength > fMaxLength ) {
                    throw new InvalidDatatypeFacetException( "Value of minLength = '" + fMinLength +
                                                             "'must be <= the value of maxLength = '" + fMaxLength + "'.");
                }
            }

            if ( base != null ) {
                AbstractStringValidator strBase = (AbstractStringValidator)base;

                if ( ((fFacetsDefined & DatatypeValidator.FACET_LENGTH ) != 0 ) ) {
                    if ( ((strBase.fFacetsDefined & DatatypeValidator.FACET_MAXLENGTH ) != 0 ) ) {
                        throw new InvalidDatatypeFacetException("It is an error for both length and maxLength to be members of facets." );
                    }
                    else if ( ((strBase.fFacetsDefined & DatatypeValidator.FACET_MINLENGTH ) != 0 ) ) {
                        throw new InvalidDatatypeFacetException("It is an error for both length and minLength to be members of facets." );
                    }
                    else if ( (strBase.fFacetsDefined & DatatypeValidator.FACET_LENGTH) != 0 ) {
                        if ( fLength != strBase.fLength )
                            throw new InvalidDatatypeFacetException( "Value of length = '" + fLength +
                                                                     "' must be = the value of base.length = '" + strBase.fLength + "'.");
                    }
                }

                if ( ((strBase.fFacetsDefined & DatatypeValidator.FACET_LENGTH ) != 0 ) ) {
                    if ( ((fFacetsDefined & DatatypeValidator.FACET_MAXLENGTH ) != 0 ) ) {
                        throw new InvalidDatatypeFacetException("It is an error for both length and maxLength to be members of facets." );
                    }
                    else if ( ((fFacetsDefined & DatatypeValidator.FACET_MINLENGTH ) != 0 ) ) {
                        throw new InvalidDatatypeFacetException("It is an error for both length and minLength to be members of facets." );
                    }
                }

                if ( ((fFacetsDefined & DatatypeValidator.FACET_MINLENGTH ) != 0 ) ) {
                    if ( (strBase.fFacetsDefined & DatatypeValidator.FACET_MAXLENGTH ) != 0 ) {
                        if ( fMinLength > strBase.fMaxLength ) {
                            throw new InvalidDatatypeFacetException( "Value of minLength = '" + fMinLength +
                                                                     "'must be <= the value of maxLength = '" + fMaxLength + "'.");
                        }
                    }
                    else if ( (strBase.fFacetsDefined & DatatypeValidator.FACET_MINLENGTH) != 0 ) {
                        if ( (strBase.fFlags & DatatypeValidator.FACET_MINLENGTH) != 0 &&
                             fMinLength != strBase.fMinLength ) {
                            throw new InvalidDatatypeFacetException( "minLength value = '" + fMinLength +
                                                                     "' must be equal to base.minLength value = '" +
                                                                     strBase.fMinLength + "' with attribute {fixed} = true" );
                        }
                        if ( fMinLength < strBase.fMinLength ) {
                            throw new InvalidDatatypeFacetException( "Value of minLength = '" + fMinLength +
                                                                     "' must be >= the value of base.minLength = '" + strBase.fMinLength + "'.");
                        }
                    }
                }


                if ( ((strBase.fFacetsDefined & DatatypeValidator.FACET_MINLENGTH ) != 0 ) &&
                     ((fFacetsDefined & DatatypeValidator.FACET_MAXLENGTH ) != 0 ) ) {
                    if ( strBase.fMinLength > fMaxLength ) {
                        throw new InvalidDatatypeFacetException( "Value of minLength = '" + fMinLength +
                                                                 "'must be <= the value of maxLength = '" + fMaxLength + "'.");
                    }
                }

                if ( (fFacetsDefined & DatatypeValidator.FACET_MAXLENGTH) != 0 ) {
                    if ( (strBase.fFlags & DatatypeValidator.FACET_MAXLENGTH) != 0 &&
                         fMaxLength != strBase.fMaxLength ) {
                        throw new InvalidDatatypeFacetException( "maxLength value = '" + fMaxLength +
                                                                 "' must be equal to base.maxLength value = '" +
                                                                 strBase.fMaxLength + "' with attribute {fixed} = true" );
                    }
                    if ( (strBase.fFacetsDefined & DatatypeValidator.FACET_MAXLENGTH) != 0 ) {
                        if ( fMaxLength > strBase.fMaxLength ) {

                            throw new InvalidDatatypeFacetException( "Value of maxLength = '" + fMaxLength +
                                                                     "' must be <= the value of base.maxLength = '" + strBase.fMaxLength + "'.");
                        }
                    }
                }


                checkBaseFacetConstraints();

                if ( (strBase.fFacetsDefined & DatatypeValidator.FACET_LENGTH) != 0 ) {
                    if ( (fFacetsDefined & DatatypeValidator.FACET_LENGTH) == 0 ) {
                        fFacetsDefined |= DatatypeValidator.FACET_LENGTH;
                        fLength = strBase.fLength;
                    }
                }
                if ( (strBase.fFacetsDefined & DatatypeValidator.FACET_MINLENGTH) != 0 ) {
                    if ( (fFacetsDefined & DatatypeValidator.FACET_MINLENGTH) == 0 ) {
                        fFacetsDefined |= DatatypeValidator.FACET_MINLENGTH;
                        fMinLength = strBase.fMinLength;
                    }
                }
                if ( (strBase.fFacetsDefined & DatatypeValidator.FACET_MAXLENGTH) != 0 ) {
                    if ( (fFacetsDefined & DatatypeValidator.FACET_MAXLENGTH) == 0 ) {
                        fFacetsDefined |= DatatypeValidator.FACET_MAXLENGTH;
                        fMaxLength = strBase.fMaxLength;
                    }
                }
                if ( (fFacetsDefined & DatatypeValidator.FACET_ENUMERATION) == 0 &&
                     (strBase.fFacetsDefined & DatatypeValidator.FACET_ENUMERATION) != 0 ) {
                    fFacetsDefined |= DatatypeValidator.FACET_ENUMERATION;
                    fEnumeration = strBase.fEnumeration;
                }
                inheritAdditionalFacets();

                fFlags |= strBase.fFlags;
            }
    }

    abstract protected void assignAdditionalFacets(String key, Hashtable facets)
    throws InvalidDatatypeFacetException;

    protected void inheritAdditionalFacets() {
    }

    protected void checkBaseFacetConstraints() throws InvalidDatatypeFacetException {}

    protected boolean derivationList (boolean derivedByList) {
        return derivedByList;
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
    public Object validate(String content, Object state)  throws InvalidDatatypeValueException {
        checkContent( content, state, false );
        return null;
    }


    private void checkContent( String content, Object state, boolean asBase )
    throws InvalidDatatypeValueException {
        if ( this.fBaseValidator != null ) {
            ((AbstractStringValidator)fBaseValidator).checkContent(content, state, true);
        }

        if ( (fFacetsDefined & DatatypeValidator.FACET_PATTERN ) != 0 ) {
            if ( fRegex == null || fRegex.matches( content) == false )
                throw new InvalidDatatypeValueException("Value '"+content+
                                                        "' does not match regular expression facet '" + fPattern + "'." );
        }

        if (fTokenType != SPECIAL_TOKEN_NONE) {
            validateToken(fTokenType, content);
        }

        if ( asBase )
            return;

        checkValueSpace (content);
        int length = getLength(content);

        if ( (fFacetsDefined & DatatypeValidator.FACET_MAXLENGTH) != 0 ) {
            if ( length > fMaxLength ) {
                throw new InvalidDatatypeValueException("Value '"+content+
                                                        "' with length '"+length+
                                                        "' exceeds maximum length facet of '"+fMaxLength+"'.");
            }
        }
        if ( (fFacetsDefined & DatatypeValidator.FACET_MINLENGTH) != 0 ) {
            if ( length < fMinLength ) {
                throw new InvalidDatatypeValueException("Value '"+content+
                                                        "' with length '"+length+
                                                        "' is less than minimum length facet of '"+fMinLength+"'." );
            }
        }

        if ( (fFacetsDefined & DatatypeValidator.FACET_LENGTH) != 0 ) {
            if ( length != fLength ) {
                throw new InvalidDatatypeValueException("Value '"+content+
                                                        "' with length '"+length+
                                                        "' is not equal to length facet '"+fLength+"'.");
            }
        }

        if ( (fFacetsDefined & DatatypeValidator.FACET_ENUMERATION) != 0 &&
             (fEnumeration != null) ) {
            if ( fEnumeration.contains( content ) == false )
                throw new InvalidDatatypeValueException("Value '"+content+"' must be one of "+fEnumeration);
        }
    }
    protected int getLength( String content) {
        return content.length();
    }

    protected void checkValueSpace (String content) throws InvalidDatatypeValueException {}

    /**
     * Returns a copy of this object.
     *
     * @return
     * @exception CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException  {
        throw new CloneNotSupportedException("clone() is not supported in "+this.getClass().getName());
    }

    public void setTokenType (String tokenType) {
        fTokenType = tokenType;
    }


    protected static void validateToken(String tokenType, String content) throws InvalidDatatypeValueException {
        int len;
        if (content == null || (len = content.length()) == 0) {
            throw new InvalidDatatypeValueException(
                           "The length of the content must be greater than 0");
        }
        boolean seenErr = false;
        if (tokenType == SPECIAL_TOKEN_NMTOKEN) {
            seenErr = !XMLCharacterProperties.validNmtoken(content);
        }
        else if (tokenType == SPECIAL_TOKEN_NAME ||
                   tokenType == SPECIAL_TOKEN_IDNAME ||
                   tokenType == SPECIAL_TOKEN_IDREFNAME) {
            seenErr = !XMLCharacterProperties.validName(content);
        } else if (tokenType == SPECIAL_TOKEN_NCNAME ||
                   tokenType == SPECIAL_TOKEN_IDNCNAME ||
                   tokenType == SPECIAL_TOKEN_IDREFNCNAME ||
                   tokenType == SPECIAL_TOKEN_ENTITY) {
            seenErr = !XMLCharacterProperties.validNCName(content);
        }
        if (seenErr) {
            throw new InvalidDatatypeValueException(
                            "Value '"+content+"' is not a valid " + tokenType);
        }
    }
}
