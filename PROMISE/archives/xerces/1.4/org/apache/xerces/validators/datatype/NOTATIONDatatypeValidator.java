package org.apache.xerces.validators.datatype;

import java.util.Hashtable;

/**
 * NOTATIONValidator defines the interface that data type validators must obey.
 * These validators can be supplied by the application writer and may be useful as
 * standalone code as well as plugins to the validator architecture.
 *
 * @author Elena Litani
 * @author Jeffrey Rodriguez-
 * @author Mark Swinkles - List Validation refactoring
 * @version $Id: NOTATIONDatatypeValidator.java 317199 2001-05-31 21:13:17Z neilg $
 */
public class NOTATIONDatatypeValidator extends AbstractStringValidator {
    

    /*
    private static StringDatatypeValidator fgStrValidator  = null;
    private static AnyURIDatatypeValidator fgURIValidator  = null;
    */

    public NOTATIONDatatypeValidator () throws InvalidDatatypeFacetException {
    }

    public NOTATIONDatatypeValidator ( DatatypeValidator base, Hashtable facets,
         boolean derivedByList ) throws InvalidDatatypeFacetException {

        super (base, facets, derivedByList);

        /*
        if ( fgStrValidator == null) {
            Hashtable strFacets = new Hashtable();
            strFacets.put(SchemaSymbols.ELT_WHITESPACE, SchemaSymbols.ATT_COLLAPSE);
            strFacets.put(SchemaSymbols.ELT_PATTERN , "[\\i-[:]][\\c-[:]]*"  );
            fgStrValidator = new StringDatatypeValidator (null, strFacets, false);
            fgURIValidator = new AnyURIDatatypeValidator (null, null, false);
        }
        */
        
    }

    protected void assignAdditionalFacets(String key, Hashtable facets)  throws InvalidDatatypeFacetException{
        throw new InvalidDatatypeFacetException( getErrorString(DatatypeMessageProvider.ILLEGAL_STRING_FACET,
                                                        DatatypeMessageProvider.MSG_NONE, new Object[] { key }));
    }


    protected void checkValueSpace (String content) throws InvalidDatatypeValueException {
        
        /*
        try {
            int posColon = content.lastIndexOf(':');
            if (posColon >= 0)
                fgURIValidator.validate(content.substring(0,posColon), null);
            fgStrValidator.validate(content.substring(posColon+1), null);
        } catch (InvalidDatatypeValueException idve) {
            throw new InvalidDatatypeValueException("Value '"+content+"' is not a valid NOTATION");
        }
        */
    
    }    

    public int compare( String  content1, String content2){
        return content1.equals(content2)?0:-1;
    }
}
