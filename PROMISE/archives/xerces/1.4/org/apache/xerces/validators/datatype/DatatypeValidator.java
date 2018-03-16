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
 * @version $Id: DatatypeValidator.java 317131 2001-05-15 12:58:35Z elena $
 */
public interface DatatypeValidator {
    public static final short FACET_LENGTH       = 0x1<<0;
    public static final short FACET_MINLENGTH    = 0x1<<1;
    public static final short FACET_MAXLENGTH    = 0x1<<2;
    public static final short FACET_PATTERN      = 0x1<<3; 
    public static final short FACET_ENUMERATION  = 0x1<<4;
    public static final short FACET_MAXINCLUSIVE = 0x1<<5;
    public static final short FACET_MAXEXCLUSIVE = 0x1<<6;
    public static final short FACET_MININCLUSIVE = 0x1<<7;
    public static final short FACET_MINEXCLUSIVE = 0x1<<8;
    public static final short FACET_TOTALDIGITS  = 0x1<<9;
    public static final short FACET_FRACTIONDIGITS = 0x1<<10;
    public static final short FACET_ENCODING     = 0x1<<11;
    public static final short FACET_DURATION     = 0x1<<12;
    public static final short FACET_PERIOD       = 0x1<<13;
    public static final short FACET_WHITESPACE   = 0x1<<14;

    public static final String FACET_FIXED   = "fixed";
    
    public static final short PRESERVE = 0;
    public static final short REPLACE  = 1;
    public static final short COLLAPSE = 2;



    /**
     * Checks that "content" string is valid 
     * datatype.
     * If invalid a Datatype validation exception is thrown.
     * 
     * @param content A string containing the content to be validated
     *                
     * @exception throws InvalidDatatypeException if the content is
     *                   invalid according to the rules for the validators
     * @exception InvalidDatatypeValueException
     * @see         org.apache.xerces.validators.datatype.InvalidDatatypeValueException
     */
    public Object validate(String content, Object state ) throws InvalidDatatypeValueException;

    public short getWSFacet ();
    
    public DatatypeValidator getBaseValidator();

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
