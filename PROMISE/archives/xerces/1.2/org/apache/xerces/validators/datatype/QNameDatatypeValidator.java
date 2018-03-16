package org.apache.xerces.validators.datatype;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Locale;
import java.text.ParseException;
import java.text.Collator;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.utils.regex.RegularExpression;

/**
 * QName Validator validates a QName type.
 * QName represents XML qualified names. The value 
 * space of QName is the set of tuples
 * {namespace name, local part}, where namespace 
 * name is a uriReference and local part is an NCName.
 * The lexical space of QName is the set of strings
 * that match the QName production of [Namespaces in
 * XML]. 
 * 
 * @author Jeffrey Rodriguez
 * @version $Id: QNameDatatypeValidator.java 315856 2000-06-23 01:26:30Z jeffreyr $
 */
public class QNameDatatypeValidator extends  AbstractDatatypeValidator {
    private Locale    fLocale          = null;
    private DatatypeValidator    fBaseValidator   = null;
    private boolean fDerivedByList             = false;

    private int       fLength          = 0;
    private int       fMaxLength       = Integer.MAX_VALUE;
    private int       fMinLength       = 0;
    private String    fPattern         = null;
    private Vector    fEnumeration     = null;
    private String    fMaxInclusive    = null;
    private String    fMaxExclusive    = null;
    private String    fMinInclusive    = null;
    private String    fMinExclusive    = null;
    private int       fFacetsDefined   = 0;

    private boolean isMaxExclusiveDefined = false;
    private boolean isMaxInclusiveDefined = false;
    private boolean isMinExclusiveDefined = false;
    private boolean isMinInclusiveDefined = false;
    private RegularExpression fRegex         = null;


    public QNameDatatypeValidator () throws InvalidDatatypeFacetException {
    }

    public QNameDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
                                    boolean derivedByList ) throws InvalidDatatypeFacetException  {


        fDerivedByList = derivedByList;

        if ( facets != null  ){
            if ( fDerivedByList == false) {
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
                        fRegex   = new RegularExpression(fPattern, "X");
                    } else if (key.equals(SchemaSymbols.ELT_ENUMERATION)) {
                        fFacetsDefined += DatatypeValidator.FACET_ENUMERATION;
                        fEnumeration = (Vector)facets.get(key);
                    } else if (key.equals(SchemaSymbols.ELT_MAXINCLUSIVE)) {
                        fFacetsDefined += DatatypeValidator.FACET_MAXINCLUSIVE;
                        fMaxInclusive = (String)facets.get(key);
                    } else if (key.equals(SchemaSymbols.ELT_MAXEXCLUSIVE)) {
                        fFacetsDefined += DatatypeValidator.FACET_MAXEXCLUSIVE;
                        fMaxExclusive = (String)facets.get(key);
                    } else if (key.equals(SchemaSymbols.ELT_MININCLUSIVE)) {
                        fFacetsDefined += DatatypeValidator.FACET_MININCLUSIVE;
                        fMinInclusive = (String)facets.get(key);
                    } else if (key.equals(SchemaSymbols.ELT_MINEXCLUSIVE)) {
                        fFacetsDefined += DatatypeValidator.FACET_MINEXCLUSIVE;
                        fMinExclusive = (String)facets.get(key);
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
                    if ( fMinLength < fMaxLength ) {
                        throw new InvalidDatatypeFacetException( "Value of minLength = " + fMinLength +
                                                                 "must be greater that the value of maxLength" + fMaxLength );
                    }
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
        StringTokenizer parsedList = null;

            parsedList = new StringTokenizer( content );
            try {
                while ( parsedList.hasMoreTokens() ) {
                    checkContentList( parsedList.nextToken() );
                }
            } catch ( NoSuchElementException e ) {
                e.printStackTrace();
            }
            checkContent( content );
        }
        return null;
    }



/**
* set the locate to be used for error messages
*/
    public void setLocale(Locale locale) {
        fLocale = locale;
    }




    private void checkContent( String content )throws InvalidDatatypeValueException
    {
        if ( (fFacetsDefined & DatatypeValidator.FACET_MAXLENGTH) != 0 ) {
            if ( content.length() > fMaxLength ) {
                throw new InvalidDatatypeValueException("Value '"+content+
                                                        "' with length '"+content.length()+
                                                        "' exceeds maximum length of "+fMaxLength+".");
            }
        }
        if ( (fFacetsDefined & DatatypeValidator.FACET_ENUMERATION) != 0 ) {
            if ( fEnumeration.contains( content ) == false )
                throw new InvalidDatatypeValueException("Value '"+content+"' must be one of "+fEnumeration);
        }


        if ( isMaxExclusiveDefined == true ) {
            int comparisonResult;
            comparisonResult  = compare( content, fMaxExclusive );

            if ( comparisonResult >= 0 ) {
                throw new InvalidDatatypeValueException( "Value '"+content+ "'  must be " +
                                  "lexicographically less than '" + fMaxExclusive + "'."  );

            }

        }
        if ( isMaxInclusiveDefined == true ) {
            int comparisonResult;
            comparisonResult  = compare( content, fMaxInclusive );
            if ( comparisonResult > 0 )
                throw new InvalidDatatypeValueException( "Value '"+content+ "' must be " +
                                  "lexicographically less or equal than '" + fMaxInclusive +"'." );
        }

        if ( isMinExclusiveDefined == true ) {
            int comparisonResult;
            comparisonResult  = compare( content, fMinExclusive );

            if ( comparisonResult <= 0 )
                throw new InvalidDatatypeValueException( "Value '"+content+ "' must be " +
                                                         "lexicographically greater than '" + fMinExclusive + "'." );
        }
        if ( isMinInclusiveDefined == true ) {
            int comparisonResult;
            comparisonResult = compare( content, fMinInclusive );
            if ( comparisonResult < 0 )
                throw new InvalidDatatypeValueException( "Value '"+content+ "' must be " +
                                                         "lexicographically greater or equal than '" + fMinInclusive  + "'." );
        }


        if ( (fFacetsDefined & DatatypeValidator.FACET_PATTERN ) != 0 ) {
            if ( fRegex == null || fRegex.matches( content) == false )
                throw new InvalidDatatypeValueException("Value '"+content+
                                                        "' does not match regular expression facet '" + fPattern + "'." );
        }
    }

    public Hashtable getFacets(){
        return null;
    }

    public int compare( String content, String facetValue ){
        Locale    loc       = Locale.getDefault();
        Collator  collator  = Collator.getInstance( loc );
        return collator.compare( content, facetValue );
    }



    /**
       * Returns a copy of this object.
       */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("clone() is not supported in "+this.getClass().getName());
    }



    private void checkContentList( String content )throws InvalidDatatypeValueException
    {
    }


    private void setBasetype( DatatypeValidator base) {
        fBaseValidator = base;
    }



}

