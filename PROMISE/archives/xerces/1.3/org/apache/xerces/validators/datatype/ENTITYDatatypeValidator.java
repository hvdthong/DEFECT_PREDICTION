package org.apache.xerces.validators.datatype;

import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;
import org.apache.xerces.readers.DefaultEntityHandler;
import org.apache.xerces.utils.XMLMessages;
import org.apache.xerces.utils.StringPool;
import org.apache.xerces.validators.datatype.StateMessageDatatype;



/**
 * ENTITYDatatypeValidator implements the
 * DatattypeValidator interface.
 * This validator embodies the ENTITY attribute type
 * from XML1.0 recommendation.
 * The Value space of ENTITY is the set of all strings
 * that match the NCName production and have been
 * declared as an unparsed entity in a document
 * type definition.
 * The Lexical space of Entity is the set of all
 * strings that match the NCName production.
 * The value space of ENTITY is scoped to a specific
 * instance document.
 * 
 * Some caveats:
 * 
 * Because of the Xerces Architecture, where all
 * symbols are stored in a StringPool and Strings
 * are referenced by int then this datatype needs
 * to know about StringPool.
 * The first time that this datatype is invoked
 * we pass a message containing 2 references needed
 * by this validator:
 * - a reference to the DefaultEntityHandler  used
 * by the XMLValidator.
 * - a reference to the StringPool.
 * 
 * 
 * This validator extends also the XML1.0 validation
 * provided in DTD by providing "only on Schemas"
 * facet validation.
 * This validator also embodies the Derived datatype
 * ENTITIES which is an ENTITY derived by list.
 * 
 * These validators can be supplied by the application writer and may be useful as
 * standalone code as well as plugins to the validator architecture.
 * 
 * @author Jeffrey Rodriguez-
 * @author Mark Swinkles - List Validation refactoring
 * @version $Id: ENTITYDatatypeValidator.java 316317 2000-10-17 00:52:40Z jeffreyr $
 * @see org.apache.xerces.validators.datatype.DatatypeValidator
 * @see org.apache.xerces.validators.datatype.DatatypeValidatorFactoryImpl
 * @see org.apache.xerces.validators.datatype.DatatypeValidatorFactory
 * @see org.apache.xerces.validators.common.XMLValidator
 */
public class ENTITYDatatypeValidator extends AbstractDatatypeValidator {
    private DatatypeValidator        fBaseValidator    = null;
    private DefaultEntityHandler     fEntityHandler    = null;
    private StringPool               fStringPool       = null;

    public  static final int         ENTITY_INITIALIZE = 0;


    public ENTITYDatatypeValidator () throws InvalidDatatypeFacetException {
    }

    public ENTITYDatatypeValidator ( DatatypeValidator base, Hashtable facets,
                                     boolean derivedByList  ) throws InvalidDatatypeFacetException {

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

        StateMessageDatatype message = (StateMessageDatatype) state;
        int                  attValueHandle;


        if ( message!= null && message.getDatatypeState() == ENTITYDatatypeValidator.ENTITY_INITIALIZE ){
            Object[]   unpackMessage = (Object[] ) message.getDatatypeObject();




            this.fEntityHandler      = (DefaultEntityHandler) unpackMessage[0];
            this.fStringPool         = (StringPool) unpackMessage[1];
        } else {


            if ( this.fEntityHandler == null ) {
                InvalidDatatypeValueException error = 
                throw error;
            }
            if ( this.fStringPool == null ) {
                InvalidDatatypeValueException error = 
                throw error;
            }


            attValueHandle = this.fStringPool.addSymbol( content );
            if (!this.fEntityHandler.isUnparsedEntity( attValueHandle ) ) {
                InvalidDatatypeValueException error = 
                error.setMinorCode(XMLMessages.MSG_ENTITY_INVALID );
                error.setMajorCode(XMLMessages.VC_ENTITY_NAME);
                throw error;
            }
        }
        return null;
    }

    /**
     * REVISIT
     * Compares two Datatype for order
     * 
     * @return 
     */
    public int compare( String  content1, String content2){
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
     * 
     * @param base   the validator for this type's base type
     */
    private void setBasetype(DatatypeValidator base){
        fBaseValidator = base;
    }



}
