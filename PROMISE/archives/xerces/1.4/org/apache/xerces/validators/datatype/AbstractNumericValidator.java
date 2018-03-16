package org.apache.xerces.validators.datatype;


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.io.IOException;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.utils.regex.RegularExpression;

/**
 * AbstractNumericValidator is a base class of decimal, double, and float 
 * schema datatypes
 * 
 * @author Elena Litani
 * @version $Id: AbstractNumericValidator.java 317074 2001-04-25 20:42:11Z elena $
 */

public abstract class AbstractNumericValidator extends AbstractNumericFacetValidator {

    public  AbstractNumericValidator () throws InvalidDatatypeFacetException {
    }

    public AbstractNumericValidator ( DatatypeValidator base, 
                                      Hashtable facets, 
                                      boolean derivedByList) throws InvalidDatatypeFacetException {
        super (base, facets, derivedByList);
    }


    /**
     * Validate string against lexical space of datatype
     * 
     * @param content A string containing the content to be validated
     * @param state
     * @return 
     * @exception throws InvalidDatatypeException if the content is
     *                   is not a W3C decimal type
     * @exception InvalidDatatypeValueException
     */
    public Object validate(String content, Object state) throws InvalidDatatypeValueException {
        checkContent(content, state, null, false);
        return null;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("clone() is not supported in "+this.getClass().getName());
    }

    /**
    * validate if the content is valid against base datatype and facets (if any)
    * this function might be called directly from UnionDatatype or ListDatatype
    *
    * @param content A string containing the content to be validated
    * @param enumeration A vector with enumeration strings
    * @exception throws InvalidDatatypeException if the content is
    *  is not a W3C decimal type;
    * @exception throws InvalidDatatypeFacetException if enumeration is not BigDecimal
    */
    protected void checkContentEnum(String content, Object state, Vector enumeration)
    throws InvalidDatatypeValueException {
        checkContent(content, state, enumeration, false);
    }


    abstract protected void checkContent( String content, Object State, Vector enum, boolean asBase)
                              throws InvalidDatatypeValueException;


    /*
     * check that a facet is in range, assumes that facets are compatible -- compatibility ensured by setFacets
     */
    protected void boundsCheck(Object d) throws InvalidDatatypeValueException {

        boolean minOk = true;
        boolean maxOk = true;
        String  upperBound="";

        String  lowerBound="";
        String  lowerBoundIndicator = "";
        String  upperBoundIndicator = "";
        int compare;
        if ( (fFacetsDefined & DatatypeValidator.FACET_MAXINCLUSIVE) != 0 ) {
            compare = compareValues(d, fMaxInclusive);
            maxOk=(compare==1)?false:true;
            upperBound   = getMaxInclusive(false);
            if ( upperBound != null ) {
                upperBoundIndicator = "<=";
            }
            else {
                upperBound="";
            }
        }
        if ( (fFacetsDefined & DatatypeValidator.FACET_MAXEXCLUSIVE) != 0 ) {
            compare = compareValues(d, fMaxExclusive );
            maxOk = (compare==-1)?true:false;
            upperBound = getMaxExclusive (false);
            if ( upperBound != null ) {
                upperBoundIndicator = "<";
            }
            else {
                upperBound = "";
            }
        }

        if ( (fFacetsDefined & DatatypeValidator.FACET_MININCLUSIVE) != 0 ) {
            compare = compareValues(d, fMinInclusive);
            minOk = (compare==-1)?false:true;
            lowerBound = getMinInclusive (false);
            if ( lowerBound != null ) {
                lowerBoundIndicator = "<=";
            }
            else {
                lowerBound = "";
            }
        }
        if ( (fFacetsDefined & DatatypeValidator.FACET_MINEXCLUSIVE) != 0 ) {
            compare = compareValues(d, fMinExclusive);
            minOk = (compare==1)?true:false;
            lowerBound = getMinExclusive (false );
            if ( lowerBound != null ) {
                lowerBoundIndicator = "<";
            }
            else {
                lowerBound = "";
            }
        }

        if ( !(minOk && maxOk) )
            throw new InvalidDatatypeValueException (
                                                    getErrorString(DatatypeMessageProvider.OUT_OF_BOUNDS,
                                                                   DatatypeMessageProvider.MSG_NONE,
                                                                   new Object [] { d.toString() ,  lowerBound ,
                                                                       upperBound, lowerBoundIndicator, upperBoundIndicator}));


    }



}
