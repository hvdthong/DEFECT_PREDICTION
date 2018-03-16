package org.apache.xerces.validators.datatype;

import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.readers.DefaultEntityHandler;
import org.apache.xerces.utils.XMLMessages;
import org.apache.xerces.utils.StringPool;
import org.apache.xerces.validators.schema.SchemaSymbols;


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
 * @version $Id: ENTITYDatatypeValidator.java 317277 2001-06-20 18:37:39Z sandygao $
 * @see org.apache.xerces.validators.datatype.DatatypeValidator
 * @see org.apache.xerces.validators.datatype.DatatypeValidatorFactoryImpl
 * @see org.apache.xerces.validators.datatype.DatatypeValidatorFactory
 * @see org.apache.xerces.validators.common.XMLValidator
 */
public class ENTITYDatatypeValidator extends StringDatatypeValidator {

    public static final  int                ENTITY_VALIDATE  = 0;

    public ENTITYDatatypeValidator () throws InvalidDatatypeFacetException {
    }

    public ENTITYDatatypeValidator ( DatatypeValidator base, Hashtable facets,
                                     boolean derivedByList  ) throws InvalidDatatypeFacetException {

        super (base, facets, derivedByList);

        if ( derivedByList )
            return;

        setTokenType(SPECIAL_TOKEN_ENTITY);
    }

    /**
     * return value of whiteSpace facet
     */
    public short getWSFacet(){
        return COLLAPSE;
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
        super.validate(content, state);

        StateMessageDatatype message = (StateMessageDatatype) state;
        if (message != null && message.getDatatypeState() == ENTITY_VALIDATE) {
            Object[] params = (Object[])message.getDatatypeObject();
            DefaultEntityHandler entityHandler = (DefaultEntityHandler)params[0];
            StringPool stringPool = (StringPool)params[1];

            int attValueHandle = stringPool.addSymbol( content );
            if (!entityHandler.isUnparsedEntity( attValueHandle ) ) {
                InvalidDatatypeValueException error =
                new InvalidDatatypeValueException( "ENTITY '"+ content +"' is not valid" );
                error.setMinorCode(XMLMessages.MSG_ENTITY_INVALID);
                error.setMajorCode(XMLMessages.VC_ENTITY_DECLARED);
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
        return content1.equals(content2)?0:-1;
    }


    /**
       * Returns a copy of this object.
       */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("clone() is not supported in "+this.getClass().getName());
    }
}
