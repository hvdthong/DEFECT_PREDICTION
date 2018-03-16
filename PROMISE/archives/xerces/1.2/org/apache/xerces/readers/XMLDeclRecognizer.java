package org.apache.xerces.readers;

import org.apache.xerces.framework.XMLErrorReporter;
import org.apache.xerces.utils.ChunkyByteArray;
import org.apache.xerces.utils.StringPool;
import java.util.Stack;

/**
 * Abstract base class for encoding recognizers.
 *
 * When we encounter an external entity, including the document entity,
 * and do not know what the encoding of the underlying byte stream is,
 * we need to look at the contents of the stream to find out.  We do this
 * by asking a set of "recognizers" to look at the stream data and if
 * the recognizer can understand the encoding it will try to read an
 * XML or text declaration, if present, and construct the appropriate
 * reader for that encoding.  The recognizer subclasses will typically
 * use the prescanXMLDeclOrTextDecl() method if the stream looks like
 * it does begin with such a declaration using a temporary reader that
 * can support the calls needed to scan through the encoding declaration.
 */
public abstract class XMLDeclRecognizer {

    /**
     * Register the standard recognizers.
     *
     * @param recognizerStack The stack of recognizers used by the parser.
     */
    public static void registerDefaultRecognizers(Stack recognizerStack) {
        recognizerStack.push(new EBCDICRecognizer());
        recognizerStack.push(new UCSRecognizer());
        recognizerStack.push(new UTF8Recognizer());
    }

    /**
     * Subclasses override this method to support recognizing their encodings.
     *
     * @param readerFactory the factory object to use when constructing the entity reader.
     * @param entityHandler the entity handler to get entity readers from
     * @param errorReporter where to report errors
     * @param sendCharDataAsCharArray true if the reader should use char arrays, not string handles.
     * @param stringPool the <code>StringPool</code> to put strings in
     * @param data initial bytes to perform recognition on
     * @param xmlDecl true if attempting to recognize fron an XMLDecl, false if trying to recognize from a TextDecl.
     * @param allowJavaEncodingName true if Java's encoding names are allowed, false if they are not.
     * @return The reader that will be used to process the contents of the data stream.
     * @exception java.lang.Exception
     */
    public abstract XMLEntityHandler.EntityReader recognize(XMLEntityReaderFactory readerFactory,
                                                            XMLEntityHandler entityHandler,
                                                            XMLErrorReporter errorReporter,
                                                            boolean sendCharDataAsCharArray,
                                                            StringPool stringPool,
                                                            ChunkyByteArray data,
                                                            boolean xmlDecl,
                                                            boolean allowJavaEncodingName) throws Exception;

    /**
     * Support for getting the value of an EncodingDecl using an XMLReader.
     *
     * This is the minimal logic from the scanner to recognize an XMLDecl or TextDecl using
     * the XMLReader interface.
     *
     * @param entityReader data source for prescan
     * @param xmlDecl true if attempting to recognize from an XMLDecl, false if trying to recognize from a TextDecl.
     * @return <code>StringPool</code> handle to the name of the encoding recognized
     * @exception java.lang.Exception
     */
    protected int prescanXMLDeclOrTextDecl(XMLEntityHandler.EntityReader entityReader, boolean xmlDecl) throws Exception
    {
        if (!entityReader.lookingAtChar('<', true)) {
            return -1;
        }
        if (!entityReader.lookingAtChar('?', true)) {
            return -1;
        }
        if (!entityReader.skippedString(xml_string)) {
            return -1;
        }
        entityReader.skipPastSpaces();
        boolean single;
        char qchar;
        if (entityReader.skippedString(version_string)) {
            entityReader.skipPastSpaces();
            if (!entityReader.lookingAtChar('=', true)) {
                return -1;
            }
            entityReader.skipPastSpaces();
            int versionIndex = entityReader.scanStringLiteral();
            if (versionIndex < 0) {
                return -1;
            }
            if (!entityReader.lookingAtSpace(true)) {
                return -1;
            }
            entityReader.skipPastSpaces();
        }
        else if (xmlDecl) {
            return -1;
        }
        if (!entityReader.skippedString(encoding_string)) {
            return -1;
        }
        entityReader.skipPastSpaces();
        if (!entityReader.lookingAtChar('=', true)) {
            return -1;
        }
        entityReader.skipPastSpaces();
        int encodingIndex = entityReader.scanStringLiteral();
        return encodingIndex;
    }
    private static final char[] xml_string = { 'x','m','l' };
    private static final char[] version_string = { 'v','e','r','s','i','o','n' };
    private static final char[] encoding_string = { 'e','n','c','o','d','i','n','g' };
}
