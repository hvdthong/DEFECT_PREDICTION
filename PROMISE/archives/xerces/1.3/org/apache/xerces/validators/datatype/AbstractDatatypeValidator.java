package org.apache.xerces.validators.datatype;
import java.util.Hashtable;
import org.apache.xerces.validators.datatype.DatatypeValidator;


public abstract class AbstractDatatypeValidator implements DatatypeValidator, Cloneable {






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
    abstract public Object validate(String content, Object state) throws InvalidDatatypeValueException;



    abstract public Object clone() throws CloneNotSupportedException ;


    /**
     * returns the datatype facet if any is set as a
     * Hashtable
     *
     * @return
     */
    public Hashtable getFacets() {
    }
    /**
     * Compares content in the Domain value vs. lexical
     * value.
     * e.g. If type is a float then 1.0 may be equivalent
     * to 1 even tough both are lexically different.
     *
     * @param value1
     * @param valu2
     * @return
     */
    public int compare(String value1, String valu2) {
    }


}
