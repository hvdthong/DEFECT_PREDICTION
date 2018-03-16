package org.apache.xerces.validators.datatype;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.xerces.utils.XMLCharacterProperties;
import org.apache.xerces.utils.XMLMessages;
import org.apache.xerces.validators.schema.SchemaSymbols;

/**
 * IDREFValidator defines the interface that data type validators must obey.
 * These validators can be supplied by the application writer and may be useful as
 * standalone code as well as plugins to the validator architecture.
 *
 * @author Jeffrey Rodriguez-
 * @author Mark Swinkles - List Validation refactoring
 * @version $Id: IDREFDatatypeValidator.java 317271 2001-06-20 15:09:24Z sandygao $
 */
public class IDREFDatatypeValidator extends StringDatatypeValidator {
    private static Object                   fNullValue      = new Object();

    public static final  int                IDREF_VALIDATE  = 0;
    public static final  int                IDREF_CHECKID   = 1;

    public IDREFDatatypeValidator () throws InvalidDatatypeFacetException {
    }

    public IDREFDatatypeValidator ( DatatypeValidator base, Hashtable facets,
                                    boolean derivedByList ) throws InvalidDatatypeFacetException {

        super (base, facets, derivedByList);

        if ( derivedByList )
            return;

        if (base != null)
            setTokenType(((IDREFDatatypeValidator)base).fTokenType);
        else
            setTokenType(SPECIAL_TOKEN_IDREFNAME);
    }

    /**
     * return value of whiteSpace facet
     */
    public short getWSFacet() {
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
        StateMessageDatatype message = (StateMessageDatatype) state;
        if (message != null && message.getDatatypeState() == IDREF_CHECKID) {
            Object[] params = (Object[])message.getDatatypeObject();
            checkIdRefs((Hashtable)params[0], (Hashtable)params[1]);
        }
        else {
            super.validate(content, state);

            if ( message != null && message.getDatatypeState() == IDREF_VALIDATE )
                addIdRef( content, (Hashtable)message.getDatatypeObject());
        }

        return null;
    }


    /**
       * Returns a copy of this object.
       */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("clone() is not supported in "+this.getClass().getName());
    }

    /** addId. */
    private void addIdRef(String content, Hashtable IDREFList) {
        if ( IDREFList.containsKey( content ) )
            return;

        try {
            IDREFList.put( content, fNullValue );
        }
        catch ( OutOfMemoryError ex ) {
            System.out.println( "Out of Memory: Hashtable of ID's has " + IDREFList.size() + " Elements" );
            ex.printStackTrace();
        }


    private void checkIdRefs(Hashtable IDList, Hashtable IDREFList) throws InvalidDatatypeValueException {
        Enumeration en = IDREFList.keys();

        while ( en.hasMoreElements() ) {
            String key = (String)en.nextElement();
            if ( !IDList.containsKey(key) ) {
                InvalidDatatypeValueException error = new InvalidDatatypeValueException( key );
                error.setMinorCode(XMLMessages.MSG_ELEMENT_WITH_ID_REQUIRED);
                error.setMajorCode(XMLMessages.VC_IDREF);
                throw error;
            }
        }
}
