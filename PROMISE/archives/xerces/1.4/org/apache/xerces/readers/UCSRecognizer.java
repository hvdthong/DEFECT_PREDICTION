package org.apache.xerces.readers;

import org.apache.xerces.framework.XMLErrorReporter;
import org.apache.xerces.utils.ChunkyByteArray;
import org.apache.xerces.utils.StringPool;

import java.io.IOException;

/**
 *
 * @version
 */
final class UCSRecognizer extends XMLDeclRecognizer {
    public XMLEntityHandler.EntityReader recognize(XMLEntityReaderFactory readerFactory,
                                                   XMLEntityHandler entityHandler,
                                                   XMLErrorReporter errorReporter,
                                                   boolean sendCharDataAsCharArray,
                                                   StringPool stringPool,
                                                   ChunkyByteArray data,
                                                   boolean xmlDecl,
                                                   boolean allowJavaEncodingName) throws Exception
    {
        XMLEntityHandler.EntityReader reader = null;
        byte b0 = data.byteAt(0);
        if (b0 == 0) {
            int b1 = data.byteAt(1);
            if (b1 == 0) {
                if (data.byteAt(2) == 0 && data.byteAt(3) == '<')
                    reader = new UCSReader(entityHandler, errorReporter, sendCharDataAsCharArray, data, UCSReader.E_UCS4B, stringPool);
            } else if (b1 == '<') {
                if (data.byteAt(2) == 0 && data.byteAt(3) == '?')
                    reader = new UCSReader(entityHandler, errorReporter, sendCharDataAsCharArray, data, UCSReader.E_UCS2B_NOBOM, stringPool);
            }
        } else if (b0 == '<') {
            int b1 = data.byteAt(1);
            if (b1 == 0) {
                int b2 = data.byteAt(2);
                if (data.byteAt(3) == 0) {
                    if (b2 == 0)
                        reader = new UCSReader(entityHandler, errorReporter, sendCharDataAsCharArray, data, UCSReader.E_UCS4L, stringPool);
                    else if (b2 == '?')
                        reader = new UCSReader(entityHandler, errorReporter, sendCharDataAsCharArray, data, UCSReader.E_UCS2L_NOBOM, stringPool);
                }
            }
        } else if (b0 == (byte)0xfe) {
            if (data.byteAt(1) == (byte)0xff)
                reader = new UCSReader(entityHandler, errorReporter, sendCharDataAsCharArray, data, UCSReader.E_UCS2B, stringPool);
        } else if (b0 == (byte)0xff) {
            if (data.byteAt(1) == (byte)0xfe)
                reader = new UCSReader(entityHandler, errorReporter, sendCharDataAsCharArray, data, UCSReader.E_UCS2L, stringPool);
        }
        return reader;
    }
}
