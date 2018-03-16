package org.apache.xerces.validators.datatype;

import java.util.Hashtable;

/**
 * AnySimpleType is the base of all simple types.
 * @author Sandy Gao
 * @version $Id: AnySimpleType.java 317377 2001-07-19 22:37:46Z sandygao $
 */
public class AnySimpleType extends AbstractDatatypeValidator {
    public  AnySimpleType() throws InvalidDatatypeFacetException{
    }

    public AnySimpleType(DatatypeValidator base, Hashtable facets, boolean derivedByList)
           throws InvalidDatatypeFacetException {

        fBaseValidator = base;

        if (facets != null && facets.size() != 0) {
            throw new InvalidDatatypeFacetException(getErrorString(DatatypeMessageProvider.ILLEGAL_ANYSIMPLETYPE_FACET,
                                                                   DatatypeMessageProvider.MSG_NONE, null));
        }
    }

    public Object validate(String content, Object state )
           throws InvalidDatatypeValueException {
        return null;
    }

    public int compare( String value1, String value2 ) {
        return -1;
    }


    public Object clone() throws CloneNotSupportedException  {
        throw new CloneNotSupportedException("clone() is not supported in "+this.getClass().getName());
    }

    public short getWSFacet (){
        return DatatypeValidator.PRESERVE;
    }
}
