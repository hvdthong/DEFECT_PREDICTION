package org.apache.xerces.validators.datatype;

import java.util.Hashtable;
import java.util.Locale;
import org.apache.xerces.validators.datatype.DatatypeValidator;
import org.apache.xerces.utils.regex.RegularExpression;

public abstract class AbstractDatatypeValidator implements DatatypeValidator, Cloneable {

    protected DatatypeValidator fBaseValidator = null; 
    protected String            fPattern         = null;
    protected RegularExpression fRegex    = null;
    protected short             fFacetsDefined          = 0;
    protected DatatypeMessageProvider fMessageProvider = new DatatypeMessageProvider();
    protected Locale            fLocale  = null;
    
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
     * default value for whiteSpace facet is collapse
     * this function is overwritten in StringDatatypeValidator
     */
    public short getWSFacet (){
        return DatatypeValidator.COLLAPSE;
    }

    public DatatypeValidator getBaseValidator() {
        return fBaseValidator;
    }

    protected String getErrorString(int major, int minor, Object args[]) {
        try {
            return fMessageProvider.createMessage(fLocale, major, minor, args);
        }
        catch ( Exception e ) {
            return "Illegal Errorcode "+minor;
        }
    }


    /**
     * set the locate to be used for error messages
     */
    public void setLocale(Locale locale) {
        fLocale = locale;
    }


}
