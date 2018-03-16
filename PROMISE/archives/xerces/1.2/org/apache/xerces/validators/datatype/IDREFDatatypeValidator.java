package org.apache.xerces.validators.datatype;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Enumeration;
import java.util.StringTokenizer;
import org.apache.xerces.utils.XMLCharacterProperties;
import org.apache.xerces.utils.XMLMessages;





/**
 * IDREFValidator defines the interface that data type validators must obey.
 * These validators can be supplied by the application writer and may be useful as
 * standalone code as well as plugins to the validator architecture.
 * 
 * @author Jeffrey Rodriguez-
 * @version $Id: IDREFDatatypeValidator.java 316016 2000-08-17 00:49:04Z jeffreyr $
 */
public class IDREFDatatypeValidator extends AbstractDatatypeValidator {
    private DatatypeValidator fBaseValidator    = null;
    private boolean           fDerivedByList    = false;
    private Hashtable              fTableIDRefs = null;
    private Object                   fNullValue = null;
    private Locale            fLocale           = null;
    private DatatypeMessageProvider fMessageProvider = new DatatypeMessageProvider();

    public static final  int       IDREF_STORE    = 0;
    public static final  int       IDREF_CLEAR    = 1;
    public static final  int       IDREF_VALIDATE = 2; 



    public IDREFDatatypeValidator () throws InvalidDatatypeFacetException {
    }

    public IDREFDatatypeValidator ( DatatypeValidator base, Hashtable facets, 
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

        StateMessageDatatype message;
        if ( this.fDerivedByList == false ){
            if (state!= null){
                message = (StateMessageDatatype) state;    
                if (message.getDatatypeState() == IDREFDatatypeValidator.IDREF_CLEAR ){
                    if ( this.fTableOfId != null ){
                    }
                    if ( this.fTableIDRefs != null ){
                        fTableIDRefs.clear(); 
                    }
                    return null;
                } else if ( message.getDatatypeState() == IDREFDatatypeValidator.IDREF_VALIDATE ){
                } else if ( message.getDatatypeState() == IDREFDatatypeValidator.IDREF_STORE ) {
                    this.fTableOfId = (Hashtable) message.getDatatypeObject();


                        error.setMinorCode(XMLMessages.MSG_IDREF_INVALID );
                        error.setMajorCode(XMLMessages.VC_IDREF);
                    }
                }
            }
         } else {
            if (state!= null){
                message = (StateMessageDatatype) state;    
                if (message.getDatatypeState() == IDREFDatatypeValidator.IDREF_CLEAR ){
                    if ( this.fTableOfId != null ){
                    }
                    if ( this.fTableIDRefs != null ){
                        fTableIDRefs.clear(); 
                    }
                    return null;

                } else if ( message.getDatatypeState() == IDREFDatatypeValidator.IDREF_VALIDATE ){
                } else if ( message.getDatatypeState() == IDREFDatatypeValidator.IDREF_STORE ) {
                    StringTokenizer   tokenizer = new StringTokenizer( content );
                    this.fTableOfId = (Hashtable) message.getDatatypeObject();
                    while ( tokenizer.hasMoreTokens() ) {
                        String idName = tokenizer.nextToken(); 
                        if( this.fBaseValidator != null ){
                               this.fBaseValidator.validate( idName, state );
                        }
                    }
                }

            }

        }
        return null;
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
      * Name of base type as a string.
      * A Native datatype has the string "native"  as its
      * base type.
      * 
      * @param base   the validator for this type's base type
      */
    private void setBasetype(DatatypeValidator base){
        fBaseValidator = base;
    }

    /** addId. */
    private void addIdRef(String content, Object state) {


        if ( this.fTableOfId != null &&  this.fTableOfId.containsKey( content ) ){
            return;
        }
        if ( this.fTableIDRefs == null ){
            this.fTableIDRefs = new Hashtable();
        } else if ( fTableIDRefs.containsKey( content ) ){
            return;
        }


        if ( this.fNullValue == null ){
            fNullValue = new Object();
        }
        try {
            this.fTableIDRefs.put( content, fNullValue ); 
        } catch( OutOfMemoryError ex ){
            System.out.println( "Out of Memory: Hashtable of ID's has " + this.fTableIDRefs.size() + " Elements" );
            ex.printStackTrace();
        }


    private void checkIdRefs() throws InvalidDatatypeValueException {

        if ( this.fTableIDRefs == null)
            return;
        
        Enumeration en = this.fTableIDRefs.keys();

        while (en.hasMoreElements()) {
            String key = (String)en.nextElement();
            if ( this.fTableOfId == null || ! this.fTableOfId.containsKey(key)) {
                
                InvalidDatatypeValueException error =  new
                            InvalidDatatypeValueException( key );
                error.setMinorCode(XMLMessages.MSG_ELEMENT_WITH_ID_REQUIRED);
                error.setMajorCode(XMLMessages.VC_IDREF);
                throw error;
            }
        }



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

