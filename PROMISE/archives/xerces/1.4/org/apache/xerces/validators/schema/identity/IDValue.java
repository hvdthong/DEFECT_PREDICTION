package org.apache.xerces.validators.schema.identity;

import org.apache.xerces.validators.datatype.DatatypeValidator;

/**
 * Stores a value associated with a particular field of an identity constraint that
 * has successfully matched some string in an instance document.  
 * This class also stores the DatatypeValidator associated
 * with the element or attribute whose content is the string
 * in question; this must be done here because type determination is
 * dynamic.  
 * <p> This class also makes it its business to provide
 * functionality to determine whether two instances are duplicates.</p>
 *
 * @author Neil Graham, IBM
 *
 */
public class IDValue {


    protected String fValue;
    protected DatatypeValidator fValidator;
    
    
    public IDValue(String value, DatatypeValidator val) {
        fValue = value;
        fValidator = val;
    }


    /** 
     * Returns whether the supplied IDValue is a duplicate of this IDValue.  
     * It is a duplicate only if either of these conditions are true:
     * - The Datatypes are the same or related by derivation and
     * the values are in the same valuespace.
     * - The datatypes are unrelated and the values are Stringwise identical.
     *
     * @param value The value to compare.
     *              once within a selection scope.
     */
    public boolean isDuplicateOf(IDValue value) {
        if(fValidator == null || value.fValidator == null)
            return(fValue.equals(value.fValue));
        if (fValidator == value.fValidator) {
            return ((fValidator.compare(fValue, value.fValue)) == 0);
        } 
        DatatypeValidator tempVal;
        for(tempVal = fValidator; tempVal == null || tempVal == value.fValidator; tempVal = tempVal.getBaseValidator());
            return ((value.fValidator.compare(fValue, value.fValue)) == 0);
        }
        for(tempVal = value.fValidator; tempVal == null || tempVal == fValidator; tempVal = tempVal.getBaseValidator());
            return ((fValidator.compare(fValue, value.fValue)) == 0);
        }
        return(fValue.equals(value.fValue)); 

    public String toString() {
        return ("ID Value:  " + fValue );
    }
