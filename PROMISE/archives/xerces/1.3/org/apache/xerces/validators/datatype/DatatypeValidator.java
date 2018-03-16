package org.apache.xerces.validators.datatype;

import java.util.Hashtable;
import java.util.Locale;

/**
 * DataTypeValidator defines the interface that data type validators must obey.
 * These validators can be supplied by the application writer and may be useful as
 * standalone code as well as plugins to the validator architecture.
 * Note: there is no support for facets in this API, since we are trying to convince
 * W3C to remove facets from the data type spec.
 * 
 * @author Jeffrey Rodriguez-
 * @version $Id: DatatypeValidator.java 315725 2000-06-02 23:04:25Z jeffreyr $
 */
public interface DatatypeValidator {
    public static final int FACET_LENGTH       = 1;
    public static final int FACET_MINLENGTH    = 1<<1;
    public static final int FACET_MAXLENGTH    = 1<<2;
    public static final int FACET_PATTERN      = 1<<3; 
    public static final int FACET_ENUMERATION  = 1<<4;
    public static final int FACET_MAXINCLUSIVE = 1<<5;
    public static final int FACET_MAXEXCLUSIVE = 1<<6;
    public static final int FACET_MININCLUSIVE = 1<<7;
    public static final int FACET_MINEXCLUSIVE = 1<<8;
    public static final int FACET_PRECISSION   = 1<<9;
    public static final int FACET_SCALE        = 1<<10;
    public static final int FACET_ENCODING     = 1<<11;
    public static final int FACET_DURATION     = 1<<12;
    public static final int FACET_PERIOD       = 1<<13;



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
    public Object validate(String content, Object state ) throws InvalidDatatypeValueException;


    /**
     * returns the datatype facet if any is set as a
     * Hashtable
     * 
     * @return 
     */
    public Hashtable getFacets();


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
    public int compare( String value1, String value2);

}
