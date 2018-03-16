package org.apache.xerces.validators.datatype;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Locale;
import java.text.Collator;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.utils.regex.RegularExpression;

/**
 * StringValidator validates that XML content is a W3C string type.
 * @author Ted Leung
 * @author Kito D. Mann, Virtua Communications Corp.
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @version $Id: StringDatatypeValidator.java 317106 2001-05-09 18:29:40Z elena $
 */
public class StringDatatypeValidator extends AbstractStringValidator {

    private short      fWhiteSpace;

    public  StringDatatypeValidator () throws InvalidDatatypeFacetException{

    }
    public StringDatatypeValidator ( DatatypeValidator base, Hashtable facets,
                                     boolean derivedByList ) throws InvalidDatatypeFacetException {

        super (base, facets, derivedByList); 
    }


    protected void assignAdditionalFacets(String key, Hashtable facets)  throws InvalidDatatypeFacetException{
        fWhiteSpace = DatatypeValidator.PRESERVE;
        if ( key.equals(SchemaSymbols.ELT_WHITESPACE) ) {
            fFacetsDefined |= DatatypeValidator.FACET_WHITESPACE;
            String ws = (String)facets.get(key);
            if ( ws.equals(SchemaSymbols.ATT_PRESERVE) ) {
                fWhiteSpace = DatatypeValidator.PRESERVE;
            }
            else if ( ws.equals(SchemaSymbols.ATT_REPLACE) ) {
                fWhiteSpace = DatatypeValidator.REPLACE;
            }
            else if ( ws.equals(SchemaSymbols.ATT_COLLAPSE) ) {
                fWhiteSpace = DatatypeValidator.COLLAPSE;
            }
            else {
                throw new InvalidDatatypeFacetException("whiteSpace value '" + ws +
                                                        "' must be one of 'preserve', 'replace', 'collapse'.");
            }
        }
        else {
            throw new InvalidDatatypeFacetException( getErrorString(DatatypeMessageProvider.ILLEGAL_STRING_FACET,
                                                                    DatatypeMessageProvider.MSG_NONE, new Object[] { key}));
        }
    }


    protected void inheritAdditionalFacets() {
        if ( (fFacetsDefined & DatatypeValidator.FACET_WHITESPACE) == 0 &&
             (((StringDatatypeValidator)fBaseValidator).fFacetsDefined & DatatypeValidator.FACET_WHITESPACE) != 0 ) {
            fFacetsDefined |= DatatypeValidator.FACET_WHITESPACE;
            fWhiteSpace = ((StringDatatypeValidator)fBaseValidator).fWhiteSpace;
        }
    }

    protected void checkBaseFacetConstraints() throws InvalidDatatypeFacetException {
        
        if ( (fFacetsDefined & DatatypeValidator.FACET_WHITESPACE) != 0 &&
             (((StringDatatypeValidator)fBaseValidator).fFacetsDefined & DatatypeValidator.FACET_WHITESPACE) != 0 ) {
                        if ( (((StringDatatypeValidator)fBaseValidator).fFlags & DatatypeValidator.FACET_WHITESPACE) != 0 &&
                         fWhiteSpace != ((StringDatatypeValidator)fBaseValidator).fWhiteSpace ) {
                        throw new InvalidDatatypeFacetException( "whiteSpace value = '" + whiteSpaceValue(fWhiteSpace) + 
                                                                 "' must be equal to base.whiteSpace value = '" +
                                                                 whiteSpaceValue(((StringDatatypeValidator)fBaseValidator).fWhiteSpace) + "' with attribute {fixed} = true" );
            }
            if ( (fWhiteSpace == DatatypeValidator.PRESERVE ||
                  fWhiteSpace == DatatypeValidator.REPLACE) &&
                 ((StringDatatypeValidator)fBaseValidator).fWhiteSpace == DatatypeValidator.COLLAPSE ){
                throw new InvalidDatatypeFacetException( "It is an error if whiteSpace = 'preserve' or 'replace' and base.whiteSpace = 'collapse'.");
            }
            if ( fWhiteSpace == DatatypeValidator.PRESERVE &&
                 ((StringDatatypeValidator)fBaseValidator).fWhiteSpace == DatatypeValidator.REPLACE )
                throw new InvalidDatatypeFacetException( "It is an error if whiteSpace = 'preserve' and base.whiteSpace = 'replace'.");
        }
    }


    /**
     * return value of whiteSpace facet
     */
    public short getWSFacet() {
        return fWhiteSpace;
    }


    public int compare( String value1, String value2 ) {
        Locale    loc       = Locale.getDefault();
        Collator  collator  = Collator.getInstance( loc );
        return collator.compare( value1, value2 );
    }

    private String whiteSpaceValue (short ws){
       return (ws != DatatypeValidator.PRESERVE)? 
              (ws == DatatypeValidator.REPLACE)?"replace":"collapse":"preserve";
    }
    /**
   * Returns a copy of this object.
   */
    public Object clone() throws CloneNotSupportedException  {
        StringDatatypeValidator newObj = null;
        try {
            newObj = new StringDatatypeValidator();

            newObj.fLocale           =  this.fLocale;
            newObj.fBaseValidator    =  this.fBaseValidator;
            newObj.fLength           =  this.fLength;
            newObj.fMaxLength        =  this.fMaxLength;
            newObj.fMinLength        =  this.fMinLength;
            newObj.fPattern          =  this.fPattern;
            newObj.fWhiteSpace       =  this.fWhiteSpace;
            newObj.fEnumeration      =  this.fEnumeration;
            newObj.fFacetsDefined    =  this.fFacetsDefined;
        }
        catch ( InvalidDatatypeFacetException ex ) {
            ex.printStackTrace();
        }
        return newObj;
    }

}

