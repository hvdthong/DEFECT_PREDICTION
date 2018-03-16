package org.apache.xerces.validators.datatype;

import java.util.Hashtable;
import java.util.Locale;
import java.text.Collator;
import org.apache.xerces.validators.schema.SchemaSymbols;

/**
 * QName Validator validates a QName type.
 * QName represents XML qualified names. The value
 * space of QName is the set of tuples
 * {namespace name, local part}, where namespace
 * name is a anyURI and local part is an NCName.
 * The lexical space of QName is the set of strings
 * that match the QName production of [Namespaces in
 * XML].
 *
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @version $Id: QNameDatatypeValidator.java 317271 2001-06-20 15:09:24Z sandygao $
 */
public class QNameDatatypeValidator extends  AbstractStringValidator {


    private static DatatypeValidator  fgStrValidator  = null;

    public QNameDatatypeValidator () throws InvalidDatatypeFacetException {
    }

    public QNameDatatypeValidator ( DatatypeValidator base, Hashtable facets,
                                    boolean derivedByList ) throws InvalidDatatypeFacetException  {

        super (base, facets, derivedByList);
    }

    protected void assignAdditionalFacets(String key, Hashtable facets)  throws InvalidDatatypeFacetException{
           throw new InvalidDatatypeFacetException( getErrorString(DatatypeMessageProvider.ILLEGAL_STRING_FACET,
                                                           DatatypeMessageProvider.MSG_NONE, new Object[] { key }));
       }


    protected void checkValueSpace (String content) throws InvalidDatatypeValueException {

        try {
            int posColon = content.indexOf(':');
            if (posColon >= 0)
                fgStrValidator.validate(content.substring(0,posColon), null);
            fgStrValidator.validate(content.substring(posColon+1), null);
        } catch (InvalidDatatypeValueException idve) {
            throw new InvalidDatatypeValueException("Value '"+content+"' is not a valid QName");
        }
    }

    public int compare( String content, String facetValue ){
        Locale    loc       = Locale.getDefault();
        Collator  collator  = Collator.getInstance( loc );
        return collator.compare( content, facetValue );
    }

    protected static void setNCNameValidator (DatatypeValidator dv) {
        if ( fgStrValidator == null) {
            fgStrValidator = dv;
        }
    }
}
