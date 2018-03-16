package org.apache.xerces.validators.datatype;

import java.util.Hashtable;
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
 * @version  $Id: BooleanDatatypeValidator.java 317074 2001-04-25 20:42:11Z elena $
 */

public class BooleanDatatypeValidator extends AbstractDatatypeValidator {
    
    private static  final String    fValueSpace[]    = { "false", "true", "0", "1"};

    public BooleanDatatypeValidator () throws InvalidDatatypeFacetException {
    }

    public BooleanDatatypeValidator ( DatatypeValidator base, Hashtable facets,
                 boolean derivedByList ) throws InvalidDatatypeFacetException {

        if ( derivedByList )
            return;

        if ( facets != null  ) {
            for (Enumeration e = facets.keys(); e.hasMoreElements();) {
                String key = (String) e.nextElement();

                if (key.equals(SchemaSymbols.ELT_PATTERN)) {
                    fFacetsDefined |= DatatypeValidator.FACET_PATTERN;
                    fPattern = (String)facets.get(key);
                    if( fPattern != null )
                       fRegex = new RegularExpression(fPattern, "X" );
                } else {
                    throw new InvalidDatatypeFacetException( getErrorString(DatatypeMessageProvider.ILLEGAL_BOOLEAN_FACET,
                                                                             DatatypeMessageProvider.MSG_NONE, new Object[] { key }));
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
        checkContent( content, false );
        return null;
    }


    /**
     * Compare two boolean data types
     *
     * @param content1
     * @param content2
     * @return  0 if equal, 1 if not equal
     */
    public int compare( String content1, String content2){
        if (content1.equals(content2)) {
            return 0;
        }
        Boolean b1 = Boolean.valueOf(content1);
        Boolean b2 = Boolean.valueOf(content2);

        return (b1.equals(b2))?0:1;
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
    private void checkContent( String content, boolean asBase )
    throws InvalidDatatypeValueException {
        if ( this.fBaseValidator != null ) {
                ((BooleanDatatypeValidator)fBaseValidator).checkContent(content, true);
        }

        if ( (fFacetsDefined & DatatypeValidator.FACET_PATTERN ) != 0 ) {
            if ( fRegex == null || fRegex.matches( content) == false )
                throw new InvalidDatatypeValueException("Value'"+content+
                                                        "does not match regular expression facet" + fPattern );
        }

        if (asBase)
            return;

        boolean  isContentInDomain = false;
        for ( int i = 0;i<fValueSpace.length;i++ ) {
            if ( content.equals(fValueSpace[i] ) )
                isContentInDomain = true;
        }
        if (isContentInDomain == false)
            throw new InvalidDatatypeValueException(
                                                   getErrorString(DatatypeMessageProvider.NOT_BOOLEAN,
                                                                  DatatypeMessageProvider.MSG_NONE,
                                                                  new Object[] { content}));
    }
}
