package org.apache.xerces.validators.datatype;

import java.util.Hashtable;
import java.util.Locale;
import org.apache.xerces.utils.XMLCharacterProperties;
import org.apache.xerces.utils.XMLMessages;

/**
 * DataTypeValidator defines the interface that data type validators must obey.
 * These validators can be supplied by the application writer and may be useful as
 * standalone code as well as plugins to the validator architecture.
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @version $Id: IDDatatypeValidator.java 316317 2000-10-17 00:52:40Z jeffreyr $
 */
public class IDDatatypeValidator extends AbstractDatatypeValidator {
    private DatatypeValidator         fBaseValidator = null;
    private Object                        fNullValue = null;
    private DatatypeMessageProvider fMessageProvider = new DatatypeMessageProvider();
    private Hashtable                     fTableOfId;
    private Locale                 fLocale           = null;
    public static final  int          IDREF_STORE    = 0;
    public static final  int          ID_CLEAR       = 1;



    public IDDatatypeValidator () throws InvalidDatatypeFacetException {
    }

    public IDDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
                                 boolean derivedByList ) throws InvalidDatatypeFacetException  {
    }



    /**
     * Checks that "content" string is valid
     * datatype.
     * If invalid a Datatype validation exception is thrown.
     * 
     * @param content A string containing the content to be validated
     * @param state  Generic Object state that can be use to pass
     *               Structures
     * @return 
     * @exception throws InvalidDatatypeException if the content is
     *                   invalid according to the rules for the validators
     * @exception InvalidDatatypeValueException
     * @see org.apache.xerces.validators.datatype.InvalidDatatypeValueException
     */
    public Object validate(String content, Object IDStorage ) throws InvalidDatatypeValueException{

        StateMessageDatatype message;

        if (IDStorage != null ){
            message = (StateMessageDatatype) IDStorage;    
            if (message.getDatatypeState() == IDDatatypeValidator.ID_CLEAR ){
                if ( this.fTableOfId  != null ){
                    this.fTableOfId = null;
                }
                return null;
            }
        }
        
            InvalidDatatypeValueException error =  new
                                                    InvalidDatatypeValueException( "ID is not valid: " + content );
            error.setMinorCode(XMLMessages.MSG_ID_INVALID);
            error.setMajorCode(XMLMessages.VC_ID);
            throw error;
        }

            InvalidDatatypeValueException error = 
            new InvalidDatatypeValueException( "ID '" + content +"'  has to be unique" );
            error.setMinorCode(XMLMessages.MSG_ID_NOT_UNIQUE);
            error.setMajorCode(XMLMessages.VC_ID);
            throw error;
        }
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

    public Hashtable getFacets(){
        return null;
    }

    /**
       * Returns a copy of this object.
       */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("clone() is not supported in "+this.getClass().getName());
    }


    /**
     * @param base   the validator for this type's base type
     */
    private void setBasetype(DatatypeValidator base){
        fBaseValidator = base;
    }

    /** addId. */
    private boolean addId(String content, Object idTable) {

        if ( this.fTableOfId == null ) {
        } else if ( this.fTableOfId.containsKey( content ) ){ 
            return false;
        }
        if ( this.fNullValue == null ){
            fNullValue = new Object();
        }
        try {
            this.fTableOfId.put( content, fNullValue ); 
        } catch ( Exception ex ){
            ex.printStackTrace();
        }
        return true;


    /**
     * set the locate to be used for error messages
     */
    public void setLocale(Locale locale) {
        fLocale = locale;
    }


    private String getErrorString(int major, int minor, Object args[]) {
        try {
            return fMessageProvider.createMessage(fLocale, major, minor, args);
        } catch (Exception e) {
            return "Illegal Errorcode "+minor;
        }
    }
}
