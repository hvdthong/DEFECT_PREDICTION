package org.apache.xerces.validators.datatype;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Enumeration;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.utils.regex.RegularExpression;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.validators.datatype.InvalidDatatypeFacetException;
/**
 *
 * BooleanValidator validates that content satisfies the W3C XML Datatype for Boolean
 *
 * @author Ted Leung 
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @version  $Id: BooleanDatatypeValidator.java 316317 2000-10-17 00:52:40Z jeffreyr $
 */

public class BooleanDatatypeValidator extends AbstractDatatypeValidator {
    private Locale                  fLocale          = null;
    private String                  fPattern         = null;
    private int                     fFacetsDefined   = 0;
    private DatatypeMessageProvider fMessageProvider = new DatatypeMessageProvider();
    private static  final String    fValueSpace[]    = { "false", "true", "0", "1"};
    private RegularExpression       fRegex           = null;

    public BooleanDatatypeValidator () throws InvalidDatatypeFacetException {
    }

    public BooleanDatatypeValidator ( DatatypeValidator base, Hashtable facets,
                 boolean derivedByList ) throws InvalidDatatypeFacetException {

        if ( facets != null  ) { 
            if ( derivedByList == false ) {
                for (Enumeration e = facets.keys(); e.hasMoreElements();) {
                    String key = (String) e.nextElement();

                    if (key.equals(SchemaSymbols.ELT_PATTERN)) {
                        fFacetsDefined += DatatypeValidator.FACET_PATTERN;
                        fPattern = (String)facets.get(key);
                        if( fPattern != null )
                           fRegex = new RegularExpression(fPattern, "X" );
                    } else {
                        throw new
                           InvalidDatatypeFacetException( 
                                "Only constraining facet in boolean datatype is PATTERN" );
                    }
                }

            }
    }


    /**
     * validate that a string matches the boolean datatype
     * @param content A string containing the content to be validated
     *
     * @exception throws InvalidDatatypeException if the content is
     * is not valid.
     */

    public Object validate(String content, Object state) throws InvalidDatatypeValueException {

        checkContent( content );
        return null;
    }


    /**
     * Compare two boolean data types
     * 
     * @param content1
     * @param content2
     * @return 
     */
    public int compare( String content1, String content2){
        return 0;
    }

    /**
     * Return a Hashtable that contains facet information
     * 
     * @return Hashtable
     */

    public Hashtable getFacets(){
        return null;
    }



    /**
     * Sets the base datatype name.
     * 
     * @param base
     */

    private  void setBasetype(DatatypeValidator base) {
        fBaseValidator = base;
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


    /**
     * Checks content for validity.
     * 
     * @param content
     * @exception InvalidDatatypeValueException
     */
    private void checkContent( String content )throws InvalidDatatypeValueException {
        boolean  isContentInDomain = false;
        for ( int i = 0;i<fValueSpace.length;i++ ) {
            if ( content.equals(fValueSpace[i] ) )
                isContentInDomain = true;
        }
        if (isContentInDomain == false)
            throw new InvalidDatatypeValueException(
                                                   getErrorString(DatatypeMessageProvider.NotBoolean,
                                                                  DatatypeMessageProvider.MSG_NONE,
                                                                  new Object[] { content}));
        if ( (fFacetsDefined & DatatypeValidator.FACET_PATTERN ) != 0 ) {
            if ( fRegex == null || fRegex.matches( content) == false )
                throw new InvalidDatatypeValueException("Value'"+content+
                                                        "does not match regular expression facet" + fPattern );
        }
    }
}
