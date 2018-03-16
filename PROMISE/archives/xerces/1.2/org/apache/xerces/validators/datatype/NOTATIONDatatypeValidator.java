package org.apache.xerces.validators.datatype;

import java.util.Hashtable;
import java.util.Locale;

/**
 * NOTATIONValidator defines the interface that data type validators must obey.
 * These validators can be supplied by the application writer and may be useful as
 * standalone code as well as plugins to the validator architecture.
 * 
 * @author Jeffrey Rodriguez-
 * @version $Id: NOTATIONDatatypeValidator.java 315856 2000-06-23 01:26:30Z jeffreyr $
 */
public class NOTATIONDatatypeValidator extends AbstractDatatypeValidator {
    private DatatypeValidator fBaseValidator = null;

    public NOTATIONDatatypeValidator () throws InvalidDatatypeFacetException {
    }

    public NOTATIONDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
         boolean derivedByList ) throws InvalidDatatypeFacetException {
         fDerivedByList = derivedByList;
    }


    /**
     * Checks that "content" string is valid 
     * datatype.
     * If invalid a Datatype validation exception is thrown.
     * 
     * @param content A string containing the content to be validated
     * @param derivedBylist
     *                Flag which is true when type
     *                is derived by list otherwise it
     *                it is derived by extension.
     *                
     * @exception throws InvalidDatatypeException if the content is
     *                   invalid according to the rules for the validators
     * @exception InvalidDatatypeValueException
     * @see         org.apache.xerces.validators.datatype.InvalidDatatypeValueException
     */
    public Object validate(String content, Object state ) throws InvalidDatatypeValueException{
        return null;
    }

    public Hashtable getFacets(){
        return null;
    }



    /**
    * set the locate to be used for error messages
    */
    public void setLocale(Locale locale){
    }

    /**
     * REVISIT
     * Compares two Datatype for order
     * 
     * @param o1
     * @param o2
     * @return 
     */
    public int compare( String content1, String content2){
        return -1;
    }
  /**
     * Returns a copy of this object.
     */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("clone() is not supported in "+this.getClass().getName());
    }

    /**
     * Name of base type as a string.
     * A Native datatype has the string "native"  as its
     * base type.
     * 
     * @param base   the validator for this type's base type
     */

    private void setBasetype(DatatypeValidator base){
        fBaseValidator = base;
    }


}
