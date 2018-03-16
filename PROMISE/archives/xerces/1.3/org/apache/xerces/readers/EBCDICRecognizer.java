package org.apache.xerces.readers;

import org.apache.xerces.framework.XMLErrorReporter;
import org.apache.xerces.utils.ChunkyByteArray;
import org.apache.xerces.utils.StringPool;

import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 *
 * @version
 */
final class EBCDICRecognizer extends XMLDeclRecognizer {
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
        byte b1 = data.byteAt(1);
        byte b2 = data.byteAt(2);
        byte b3 = data.byteAt(3);
        boolean debug = false;

        if (b0 != 0x4c || b1 != 0x6f || b2 != (byte)0xa7 || b3 != (byte)0x94)
            return reader;
        XMLEntityHandler.EntityReader declReader = readerFactory.createCharReader(entityHandler, errorReporter, sendCharDataAsCharArray, new InputStreamReader(data, "CP037"), stringPool);
        int encoding = prescanXMLDeclOrTextDecl(declReader, xmlDecl);
        if (encoding == -1) {
            data.rewind();
            throw new UnsupportedEncodingException(null);
        }
        String enc = stringPool.orphanString(encoding).toUpperCase();
        if ("ISO-10646-UCS-2".equals(enc)) throw new UnsupportedEncodingException(enc);
        if ("ISO-10646-UCS-4".equals(enc)) throw new UnsupportedEncodingException(enc);
        if ("UTF-16".equals(enc)) throw new UnsupportedEncodingException(enc);
        String javaencname = MIME2Java.convert(enc);
        if (null == javaencname) {
            if (allowJavaEncodingName) {
                javaencname = enc;
            } else {
                throw new UnsupportedEncodingException(enc);
            }
        }
        try {
            data.rewind();
            reader = readerFactory.createCharReader(entityHandler, errorReporter, sendCharDataAsCharArray, new InputStreamReader(data, javaencname), stringPool);
        } catch (UnsupportedEncodingException e) {
            throw e;
        } catch (Exception e) {
            if( debug == true )
        }
        return reader;
    }
}
