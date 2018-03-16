package org.apache.xerces.readers;

import org.apache.xerces.framework.XMLErrorReporter;
import org.apache.xerces.utils.StringPool;
import org.xml.sax.InputSource;
import java.io.InputStream;
import java.io.Reader;

/**
 * This is the factory interface used to create readers.
 *
 * @version
 */
public interface XMLEntityReaderFactory {
    /**
     * Adds a recognizer.
     *
     * @param recognizer The XML recognizer to add.
     */
    public void addRecognizer(XMLDeclRecognizer recognizer);

    /**
     * Set char data processing preference.
     */
    public void setSendCharDataAsCharArray(boolean flag);

    /**
     *
     */
    public void setAllowJavaEncodingName(boolean flag);

    /**
     *
     */
    public boolean getAllowJavaEncodingName();

    /**
     * Create an entity reader for the source.
     *
     * @param source The input source.
     * @param systemId The system identifier for the input.
     * @param xmlDecl <code>true</code> if an XMLDecl may be present; otherwise
     *                <code>false</code> if a TextDecl may be present.
     * @param stringPool The string pool.
     * @return The reader that will process the source.
     * @exception java.lang.Exception
     */
    public XMLEntityHandler.EntityReader createReader(XMLEntityHandler entityHandler,
                                                      XMLErrorReporter errorReporter,
                                                      InputSource source,
                                                      String systemId,
                                                      boolean xmlDecl,
                                                      StringPool stringPool) throws Exception;

    /**
     * Create an entity reader for a character stream.
     *
     * @param enityHandler The entity handler.
     * @param errorReporter The error reporter.
     * @param sendCharDataAsCharArray true if char data should be reported using
     *                                char arrays instead of string handles.
     * @param reader The character stream.
     * @param stringPool The string pool.
     * @return The reader that will process the character data.
     * @exception java.lang.Exception
     */
    public XMLEntityHandler.EntityReader createCharReader(XMLEntityHandler entityHandler,
                                                          XMLErrorReporter errorReporter,
                                                          boolean sendCharDataAsCharArray,
                                                          Reader reader,
                                                          StringPool stringPool) throws Exception;

    /**
     * Create an entity reader for a byte stream encoded in UTF-8.
     *
     * @param enityHandler The entity handler.
     * @param errorReporter The error reporter.
     * @param sendCharDataAsCharArray true if char data should be reported using
     *                                char arrays instead of string handles.
     * @param data The byte stream.
     * @param stringPool The string pool.
     * @return The reader that will process the UTF-8 data.
     * @exception java.lang.Exception
     */
    public XMLEntityHandler.EntityReader createUTF8Reader(XMLEntityHandler entityHandler,
                                                          XMLErrorReporter errorReporter,
                                                          boolean sendCharDataAsCharArray,
                                                          InputStream data,
                                                          StringPool stringPool) throws Exception;

    /**
     * Create an entity reader for data from a String.
     *
     * @param entityHandler The current entity handler.
     * @param errorReporter The current error reporter.
     * @param sendCharDataAsCharArray true if char data should be reported using
     *                                char arrays instead of string handles.
     * @param lineNumber The line number to return as our position.
     * @param columnNumber The column number to return as our position.
     * @param stringHandle The StringPool handle for the data to process.
     * @param stringPool The string pool.
     * @param addEnclosingSpaces If true, treat the data to process as if
     *                           there were a leading and trailing space
     *                           character enclosing the string data.
     * @return The reader that will process the string data.
     * @exception java.lang.Exception
     */
    public XMLEntityHandler.EntityReader createStringReader(XMLEntityHandler entityHandler,
                                                            XMLErrorReporter errorReporter,
                                                            boolean sendCharDataAsCharArray,
                                                            int lineNumber,
                                                            int columnNumber,
                                                            int stringHandle,
                                                            StringPool stringPool,
                                                            boolean addEnclosingSpaces) throws Exception;
}
