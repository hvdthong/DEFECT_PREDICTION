package org.apache.xerces.validators.datatype;
import java.util.Hashtable;
import org.apache.xerces.validators.datatype.*;
import org.apache.xerces.validators.schema.SchemaSymbols;


/**
 * @version $Id: DatatypeValidatorFactory.java 315796 2000-06-19 20:44:26Z jeffreyr $
 * @author  Jeffrey Rodriguez
 */
public interface DatatypeValidatorFactory {
    public DatatypeValidator createDatatypeValidator(String typeName, 
                    DatatypeValidator base, Hashtable facets, boolean list ) throws  InvalidDatatypeFacetException;
}

