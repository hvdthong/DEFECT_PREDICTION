package org.apache.xerces.validators.datatype;

import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.xerces.utils.XMLCharacterProperties;
import org.apache.xerces.utils.XMLMessages;
import org.apache.xerces.validators.schema.SchemaSymbols;

/**
 * DataTypeValidator defines the interface that data type validators must obey.
 * These validators can be supplied by the application writer and may be useful as
 * standalone code as well as plugins to the validator architecture.
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @version $Id: IDDatatypeValidator.java 317271 2001-06-20 15:09:24Z sandygao $
 */
public class IDDatatypeValidator extends StringDatatypeValidator {
    private static Object                   fNullValue      = new Object();

    public IDDatatypeValidator () throws InvalidDatatypeFacetException {
    }

    public IDDatatypeValidator ( DatatypeValidator base, Hashtable facets,
                                 boolean derivedByList ) throws InvalidDatatypeFacetException  {

        super (base, facets, derivedByList);

        if ( derivedByList )
            return;

        if (base != null)
            setTokenType(((IDDatatypeValidator)base).fTokenType);
        else
            setTokenType(SPECIAL_TOKEN_IDNAME);
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
     * @param state  Generic Object state that can be use to pass
     *               Structures
     * @return
     * @exception throws InvalidDatatypeException if the content is
     *                   invalid according to the rules for the validators
     * @exception InvalidDatatypeValueException
     * @see org.apache.xerces.validators.datatype.InvalidDatatypeValueException
     */
    public Object validate(String content, Object state ) throws InvalidDatatypeValueException{
        super.validate(content, state);

        if (state != null) {
            if ( !addId( content, (Hashtable)state) ) {
                InvalidDatatypeValueException error =
                new InvalidDatatypeValueException( "ID '" + content +"'  has to be unique" );
                error.setMinorCode(XMLMessages.MSG_ID_NOT_UNIQUE);
                error.setMajorCode(XMLMessages.VC_ID);
                throw error;
            }
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
    private boolean addId(String content, Hashtable IDList) {
        if ( IDList.containsKey( content ) )
            return false;

        try {
            IDList.put( content, fNullValue );
        }
        catch ( OutOfMemoryError ex ) {
            System.out.println( "Out of Memory: Hashtable of ID's has " + IDList.size() + " Elements" );
            ex.printStackTrace();
        }
        return true;
}
