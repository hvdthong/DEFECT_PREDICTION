package org.apache.xerces.validators.datatype;

import java.util.Hashtable;
import org.apache.xerces.utils.URI;


/**
 * URIValidator validates that XML content is a W3C uri type,
 * according to RFC 2396
 *
 * @author Ted Leung
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @see          RFC 2396
 * @version  $Id: AnyURIDatatypeValidator.java 317207 2001-06-05 20:37:46Z elena $
 */
public class AnyURIDatatypeValidator extends AbstractStringValidator {
    
    private URI fTempURI = null;
    public AnyURIDatatypeValidator () throws InvalidDatatypeFacetException{
    }

    public AnyURIDatatypeValidator ( DatatypeValidator base, Hashtable facets,
                                     boolean derivedByList ) throws InvalidDatatypeFacetException {
        super (base, facets, derivedByList); 
    }

    protected void assignAdditionalFacets(String key, Hashtable facets)  throws InvalidDatatypeFacetException{
        throw new InvalidDatatypeFacetException( getErrorString(DatatypeMessageProvider.ILLEGAL_STRING_FACET,
                                                        DatatypeMessageProvider.MSG_NONE, new Object[] { key }));
    }


    protected void checkValueSpace (String content) throws InvalidDatatypeValueException {
        
        try {
            if (fTempURI == null) {
            }
            if( content.length() != 0 ) {
                new URI(fTempURI, content );
            }
        } catch (  URI.MalformedURIException ex ) {
                throw new InvalidDatatypeValueException("Value '"+content+"' is a Malformed URI ");
        }
    }

    public int compare( String  content1, String content2){
        return content1.equals(content2)?0:-1;
    }
}
