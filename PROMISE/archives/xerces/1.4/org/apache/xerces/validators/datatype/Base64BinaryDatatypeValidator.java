package org.apache.xerces.validators.datatype;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import org.apache.xerces.validators.schema.SchemaSymbols;
import org.apache.xerces.utils.regex.RegularExpression;
import org.apache.xerces.utils.Base64;

/**
 * Base64BinaryValidator validates that XML content is a W3C string type.
 * @author Ted Leung
 * @author Kito D. Mann, Virtua Communications Corp.
 * @author Jeffrey Rodriguez
 * @author Mark Swinkles - List Validation refactoring
 * @version $Id: Base64BinaryDatatypeValidator.java 317407 2001-07-31 14:40:59Z neilg $
 */
public class Base64BinaryDatatypeValidator extends AbstractStringValidator{
    


    public  Base64BinaryDatatypeValidator () throws InvalidDatatypeFacetException{

    }

    public Base64BinaryDatatypeValidator ( DatatypeValidator base, Hashtable facets,
                                           boolean derivedByList ) throws InvalidDatatypeFacetException {

        super (base, facets, derivedByList);         
    }

    protected void assignAdditionalFacets(String key, Hashtable facets)  throws InvalidDatatypeFacetException{
        throw new InvalidDatatypeFacetException( getErrorString(DatatypeMessageProvider.ILLEGAL_STRING_FACET,
                                                        DatatypeMessageProvider.MSG_NONE, new Object[] { key }));
    }


    protected void checkValueSpace (String content) throws InvalidDatatypeValueException {
        if (getLength( content) < 0) {
            throw new InvalidDatatypeValueException( "Value '"+content+"' is not encoded in Base64" );
        }
    }

    protected int getLength( String content) {
      int x = 0;
      try {
        x = Base64.getDecodedDataLength(content.getBytes("utf-8"));
      }
      catch (UnsupportedEncodingException e) {
      }
      finally {
        return x;
      }
    }

    public int compare( String value1, String value2 ){
        if (value1 == null || value2 == null)
            return -1;

        if (value1 == value2 || value1.equals(value2))
            return 0;

        byte[] data1=Base64.decode(value1.getBytes());
        byte[] data2=Base64.decode(value2.getBytes());

        if (data1 == null || data2 == null)
            return -1;

        for (int i = 0; i < Math.min(data1.length, data2.length); i++)
            if (data1[i] < data2[i])
                return -1;
            else if (data1[i] > data2[i])
                return 1;

        if (data1.length == data2.length)
            return 0;

        return data1.length > data2.length ? 1 : -1;
    }
}
