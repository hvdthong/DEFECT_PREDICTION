package org.apache.xerces.framework;

import org.apache.xerces.readers.XMLEntityHandler;
import org.apache.xerces.readers.DefaultEntityHandler;
import org.apache.xerces.utils.ChunkyCharArray;
import org.apache.xerces.utils.QName;
import org.apache.xerces.utils.StringPool;
import org.apache.xerces.utils.XMLCharacterProperties;
import org.apache.xerces.utils.XMLMessages;
import org.apache.xerces.validators.common.GrammarResolver;

import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

/**
 * This class recognizes most of the grammer for an XML processor.
 * Additional support is provided by the XMLEntityHandler, via the
 * XMLEntityReader instances it creates, which are used to process
 * simple constructs like string literals and character data between
 * markup.  The XMLDTDScanner class contains the remaining support
 * for the grammer of DTD declarations.  When a &lt;!DOCTYPE ...&gt; is
 * found in the document, the scanDoctypeDecl method will then be
 * called and the XMLDocumentScanner subclass is responsible for
 * "connecting" that method to the corresponding method provided
 * by the XMLDTDScanner class.
 *
 * @version $Id: XMLDocumentScanner.java 316746 2000-12-20 23:23:27Z lehors $
 */
public final class XMLDocumentScanner {


    static final char[] cdata_string = { '[','C','D','A','T','A','[' };
    static final char[] xml_string = { 'x','m','l' };
    private static final char[] version_string = { 'v','e','r','s','i','o','n' };
    static final char[] doctype_string = { 'D','O','C','T','Y','P','E' };
    private static final char[] standalone_string = { 's','t','a','n','d','a','l','o','n','e' };
    private static final char[] encoding_string = { 'e','n','c','o','d','i','n','g' };

    /*
     * Return values for the EventHandler scanAttValue method.
     */
    public static final int
        RESULT_SUCCESS          =  0,
        RESULT_FAILURE          = -1,
        RESULT_DUPLICATE_ATTR   = -2;

    /** Scanner states */
    static final int
        SCANNER_STATE_XML_DECL                  =  0,
        SCANNER_STATE_START_OF_MARKUP           =  1,
        SCANNER_STATE_COMMENT                   =  2,
        SCANNER_STATE_PI                        =  3,
        SCANNER_STATE_DOCTYPE                   =  4,
        SCANNER_STATE_PROLOG                    =  5,
        SCANNER_STATE_ROOT_ELEMENT              =  6,
        SCANNER_STATE_CONTENT                   =  7,
        SCANNER_STATE_REFERENCE                 =  8,
        SCANNER_STATE_ATTRIBUTE_LIST            =  9,
        SCANNER_STATE_ATTRIBUTE_NAME            = 10,
        SCANNER_STATE_ATTRIBUTE_VALUE           = 11,
        SCANNER_STATE_TRAILING_MISC             = 12,
        SCANNER_STATE_END_OF_INPUT              = 13,
        SCANNER_STATE_TERMINATED                = 14;

    /***/
    private StringPool.CharArrayRange fCurrentElementCharArrayRange = null;
    /***/
    int fAttrListHandle = -1;
    XMLAttrList fAttrList = null;
    GrammarResolver fGrammarResolver = null;
    XMLDTDScanner fDTDScanner = null;
    boolean fNamespacesEnabled = false;
    boolean fValidationEnabled = false;
    boolean fLoadExternalDTD = true;
    QName fElementQName = new QName();
    QName fAttributeQName = new QName();
    QName fCurrentElementQName = new QName();
    ScannerDispatcher fDispatcher = null;
    EventHandler fEventHandler = null;
    XMLDocumentHandler.DTDHandler fDTDHandler = null;
    StringPool fStringPool = null;
    XMLErrorReporter fErrorReporter = null;
    XMLEntityHandler fEntityHandler = null;
    XMLEntityHandler.EntityReader fEntityReader = null;
    XMLEntityHandler.CharBuffer fLiteralData = null;
    boolean fSeenRootElement = false;
    boolean fSeenDoctypeDecl = false;
    boolean fStandalone = false;
    boolean fParseTextDecl = false;
    boolean fScanningDTD = false;
    int fScannerState = SCANNER_STATE_XML_DECL;
    int fReaderId = -1;
    int fAttValueReader = -1;
    int fAttValueElementType = -1;
    int fAttValueAttrName = -1;
    int fAttValueOffset = -1;
    int fAttValueMark = -1;
    int fScannerMarkupDepth = 0;


    /**
     * This interface must be implemented by the users of the XMLDocumentScanner class.
     * These methods form the abstraction between the implementation semantics and the
     * more generic task of scanning the XML non-DTD grammar.
     */
    public interface EventHandler {
        /**
         * Signal standalone = "yes"
         *
         * @exception java.lang.Exception
         */
        public void callStandaloneIsYes() throws Exception;

        /**
         * Signal the start of a document
         *
         * @exception java.lang.Exception
         */
        public void callStartDocument() throws Exception;
        /**
         * Signal the end of a document
         *
         * @exception java.lang.Exception
         */
        public void callEndDocument() throws Exception;
        /**
         * Signal the XML declaration of a document
         *
         * @param version the handle in the string pool for the version number
         * @param encoding the handle in the string pool for the encoding
         * @param standalong the handle in the string pool for the standalone value
         * @exception java.lang.Exception
         */
        public void callXMLDecl(int version, int encoding, int standalone) throws Exception;
        /**
         * Signal the Text declaration of an external entity.
         *
         * @param version the handle in the string pool for the version number
         * @param encoding the handle in the string pool for the encoding
         * @exception java.lang.Exception
         */
        public void callTextDecl(int version, int encoding) throws Exception;
        /**
         * signal the scanning of a start element tag
         * 
         * @param element Element name scanned.
         * @exception java.lang.Exception
         */
        public void callStartElement(QName element) throws Exception;
        /**
         * Signal the scanning of an element name in a start element tag.
         *
         * @param element Element name scanned.
         */
        public void element(QName element) throws Exception;
        /**
         * Signal the scanning of an attribute associated to the previous
         * start element tag.
         *
         * @param element Element name scanned.
         * @param attrName Attribute name scanned.
         * @param attrValue The string pool index of the attribute value.
         */
        public boolean attribute(QName element, QName attrName, int attrValue) throws Exception;
        /**
         * signal the scanning of an end element tag
         *
         * @param readerId the Id of the reader being used to scan the end tag.
         * @exception java.lang.Exception
         */
        public void callEndElement(int readerId) throws Exception;
        /**
         * Signal the start of a CDATA section
         * @exception java.lang.Exception
         */
        public void callStartCDATA() throws Exception;
        /**
         * Signal the end of a CDATA section
         * @exception java.lang.Exception
         */
        public void callEndCDATA() throws Exception;
        /**
         * Report the scanning of character data
         *
         * @param ch the handle in the string pool of the character data that was scanned
         * @exception java.lang.Exception
         */
        public void callCharacters(int ch) throws Exception;
        /**
         * Report the scanning of a processing instruction
         *
         * @param piTarget the handle in the string pool of the processing instruction targe
         * @param piData the handle in the string pool of the processing instruction data
         * @exception java.lang.Exception
         */
        public void callProcessingInstruction(int piTarget, int piData) throws Exception;
        /**
         * Report the scanning of a comment
         *
         * @param data the handle in the string pool of the comment text
         * @exception java.lang.Exception
         */
        public void callComment(int data) throws Exception;
    }

    /**
     * Constructor
     */
    public XMLDocumentScanner(StringPool stringPool,
                              XMLErrorReporter errorReporter,
                              XMLEntityHandler entityHandler,
                              XMLEntityHandler.CharBuffer literalData) {
        fStringPool = stringPool;
        fErrorReporter = errorReporter;
        fEntityHandler = entityHandler;
        fLiteralData = literalData;
        fDispatcher = new XMLDeclDispatcher();
        fAttrList = new XMLAttrList(fStringPool);
    }

    /**
     * Set the event handler
     *
     * @param eventHandler The place to send our callbacks.
     */
    public void setEventHandler(XMLDocumentScanner.EventHandler eventHandler) {
        fEventHandler = eventHandler;
    }

    /** Set the DTD handler. */
    public void setDTDHandler(XMLDocumentHandler.DTDHandler dtdHandler) {
        fDTDHandler = dtdHandler;
    }

    /** Sets the grammar resolver. */
    public void setGrammarResolver(GrammarResolver resolver) {
        fGrammarResolver = resolver;
    }

    /**
     * reset the parser so that the instance can be reused
     *
     * @param stringPool the string pool instance to be used by the reset parser
     */
    public void reset(StringPool stringPool, XMLEntityHandler.CharBuffer literalData) {
        fStringPool = stringPool;
        fLiteralData = literalData;
        fParseTextDecl = false;
        fSeenRootElement = false;
        fSeenDoctypeDecl = false;
        fStandalone = false;
        fScanningDTD = false;
        fDispatcher = new XMLDeclDispatcher();
        fScannerState = SCANNER_STATE_XML_DECL;
        fScannerMarkupDepth = 0;
        fAttrList = new XMLAttrList(fStringPool);
    }

    /**
     * Entry point for parsing
     *
     * @param doItAll if true the entire document is parsed otherwise just 
     *                the next segment of the document is parsed
     */
    public boolean parseSome(boolean doItAll) throws Exception
    {
        do {
            if (!fDispatcher.dispatch(doItAll))
                return false;
        } while (doItAll);
        return true;
    }

    /**
     * Change readers
     *
     * @param nextReader the new reader that the scanner will use
     * @param nextReaderId id of the reader to change to
     * @exception throws java.lang.Exception
     */
    public void readerChange(XMLEntityHandler.EntityReader nextReader, int nextReaderId) throws Exception {
        fEntityReader = nextReader;
        fReaderId = nextReaderId;
        if (fScannerState == SCANNER_STATE_ATTRIBUTE_VALUE) {
            fAttValueOffset = fEntityReader.currentOffset();
            fAttValueMark = fAttValueOffset;
        }

        if (fDTDScanner != null && fScanningDTD)
            fDTDScanner.readerChange(nextReader, nextReaderId);
    }

    /**
     * Handle the end of input
     *
     * @param entityName the handle in the string pool of the name of the entity which has reached end of input
     * @param moreToFollow if true, there is still input left to process in other readers
     * @exception java.lang.Exception
     */
    public void endOfInput(int entityName, boolean moreToFollow) throws Exception {
        if (fDTDScanner != null && fScanningDTD){
            fDTDScanner.endOfInput(entityName, moreToFollow);
        }
        fDispatcher.endOfInput(entityName, moreToFollow);
    }

    /** 
     * Tell if scanner has reached end of input
     * @return true if scanner has reached end of input.
     */
    public boolean atEndOfInput() {
        return fScannerState == SCANNER_STATE_END_OF_INPUT;
    }

    /**
     * Scan an attribute value
     *
     * @param elementType handle to the element whose attribute value is being scanned
     * @param attrName handle in the string pool of the name of attribute being scanned
     * @param asSymbol controls whether the value is a string (duplicates allowed) or a symbol (duplicates not allowed)
     * @return handle in the string pool of the scanned value
     * @exception java.lang.Exception
     */
    public int scanAttValue(QName element, QName attribute, boolean asSymbol) throws Exception {
        boolean single;
        if (!(single = fEntityReader.lookingAtChar('\'', true)) && !fEntityReader.lookingAtChar('\"', true)) {
            reportFatalXMLError(XMLMessages.MSG_QUOTE_REQUIRED_IN_ATTVALUE,
                                XMLMessages.P10_QUOTE_REQUIRED,
                                element.rawname,
                                attribute.rawname);
            return -1;
        }
        char qchar = single ? '\'' : '\"';
        fAttValueMark = fEntityReader.currentOffset();
        int attValue = fEntityReader.scanAttValue(qchar, asSymbol);
        if (attValue >= 0)
            return attValue;
        int previousState = setScannerState(SCANNER_STATE_ATTRIBUTE_VALUE);
        fAttValueReader = fReaderId;
        fAttValueElementType = element.rawname;
        fAttValueAttrName = attribute.rawname;
        fAttValueOffset = fEntityReader.currentOffset();
        int dataOffset = fLiteralData.length();
        if (fAttValueOffset - fAttValueMark > 0)
            fEntityReader.append(fLiteralData, fAttValueMark, fAttValueOffset - fAttValueMark);
        fAttValueMark = fAttValueOffset;
        boolean setMark = false;
        boolean skippedCR;
        while (true) {
            if (fEntityReader.lookingAtChar(qchar, true)) {
                if (fReaderId == fAttValueReader)
                    break;
            } else if (fEntityReader.lookingAtChar(' ', true)) {
            } else if ((skippedCR = fEntityReader.lookingAtChar((char)0x0D, true)) || fEntityReader.lookingAtSpace(true)) {
                if (fAttValueOffset - fAttValueMark > 0)
                    fEntityReader.append(fLiteralData, fAttValueMark, fAttValueOffset - fAttValueMark);
                setMark = true;
                fLiteralData.append(' ');
                if (skippedCR) {
                }
            } else if (fEntityReader.lookingAtChar('&', true)) {
                if (fAttValueOffset - fAttValueMark > 0)
                    fEntityReader.append(fLiteralData, fAttValueMark, fAttValueOffset - fAttValueMark);
                setMark = true;
                if (fEntityReader.lookingAtChar('#', true)) {
                    int ch = scanCharRef();
                    if (ch != -1) {
                        if (ch < 0x10000)
                            fLiteralData.append((char)ch);
                        else {
                            fLiteralData.append((char)(((ch-0x00010000)>>10)+0xd800));
                            fLiteralData.append((char)(((ch-0x00010000)&0x3ff)+0xdc00));
                        }
                    }
                } else {
                    int nameOffset = fEntityReader.currentOffset();
                    fEntityReader.skipPastName(';');
                    int nameLength = fEntityReader.currentOffset() - nameOffset;
                    if (nameLength == 0) {
                        reportFatalXMLError(XMLMessages.MSG_NAME_REQUIRED_IN_REFERENCE,
                                            XMLMessages.P68_NAME_REQUIRED);
                    } else if (!fEntityReader.lookingAtChar(';', true)) {
                        reportFatalXMLError(XMLMessages.MSG_SEMICOLON_REQUIRED_IN_REFERENCE,
                                            XMLMessages.P68_SEMICOLON_REQUIRED,
                                            fEntityReader.addString(nameOffset, nameLength));
                    } else {
                        int entityName = fEntityReader.addSymbol(nameOffset, nameLength);
                        fEntityHandler.startReadingFromEntity(entityName, fScannerMarkupDepth, XMLEntityHandler.ENTITYREF_IN_ATTVALUE);
                    }
                }
            } else if (fEntityReader.lookingAtChar('<', true)) {
                if (fAttValueOffset - fAttValueMark > 0)
                    fEntityReader.append(fLiteralData, fAttValueMark, fAttValueOffset - fAttValueMark);
                setMark = true;
                reportFatalXMLError(XMLMessages.MSG_LESSTHAN_IN_ATTVALUE,
                                    XMLMessages.WFC_NO_LESSTHAN_IN_ATTVALUE,
                                    element.rawname,
                                    attribute.rawname);
            } else if (!fEntityReader.lookingAtValidChar(true)) {
                if (fAttValueOffset - fAttValueMark > 0)
                    fEntityReader.append(fLiteralData, fAttValueMark, fAttValueOffset - fAttValueMark);
                setMark = true;
                int invChar = fEntityReader.scanInvalidChar();
                if (fScannerState == SCANNER_STATE_END_OF_INPUT)
                    return -1;
                if (invChar >= 0) {
                    reportFatalXMLError(XMLMessages.MSG_INVALID_CHAR_IN_ATTVALUE,
                                        XMLMessages.P10_INVALID_CHARACTER,
                                        fStringPool.toString(element.rawname),
                                        fStringPool.toString(attribute.rawname),
                                        Integer.toHexString(invChar));
                }
            }
            fAttValueOffset = fEntityReader.currentOffset();
            if (setMark) {
                fAttValueMark = fAttValueOffset;
                setMark = false;
            }
        }
        restoreScannerState(previousState);
        int dataLength = fLiteralData.length() - dataOffset;
        if (dataLength == 0) {
            return fEntityReader.addString(fAttValueMark, fAttValueOffset - fAttValueMark);
        }
        if (fAttValueOffset - fAttValueMark > 0) {
            fEntityReader.append(fLiteralData, fAttValueMark, fAttValueOffset - fAttValueMark);
            dataLength = fLiteralData.length() - dataOffset;
        }
        int value = fLiteralData.addString(dataOffset, dataLength);
        return value;
    }

    /**
     * Check the value of an XML Language attribute
     * @param langValue the handle in the string pool of the value to be checked
     * @exception java.lang.Exception
     */
    public void checkXMLLangAttributeValue(int langValue) throws Exception {
        String lang = fStringPool.toString(langValue);
        int offset = -1;
        if (lang.length() >= 2) {
            char ch0 = lang.charAt(0);
            if (lang.charAt(1) == '-') {
                if (ch0 == 'i' || ch0 == 'I' || ch0 == 'x' || ch0 == 'X') {
                    offset = 1;
                }
            } else {
                char ch1 = lang.charAt(1);
                if (((ch0 >= 'a' && ch0 <= 'z') || (ch0 >= 'A' && ch0 <= 'Z')) &&
                    ((ch1 >= 'a' && ch1 <= 'z') || (ch1 >= 'A' && ch1 <= 'Z'))) {
                        offset = 2;
                }
            }
        }
        if (offset > 0 && lang.length() > offset) {
            char ch = lang.charAt(offset++);
            if (ch != '-') {
                offset = -1;
            } else {
                while (true) {
                    if (ch == '-') {
                        if (lang.length() == offset) {
                            offset = -1;
                            break;
                        }
                        ch = lang.charAt(offset++);
                        if ((ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z')) {
                            offset = -1;
                            break;
                        }
                        if (lang.length() == offset)
                            break;
                    } else if ((ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z')) {
                        offset = -1;
                        break;
                    } else if (lang.length() == offset)
                        break;
                    ch = lang.charAt(offset++);
                }
            }
        }
        if (offset == -1) {
            reportFatalXMLError(XMLMessages.MSG_XML_LANG_INVALID,
                                XMLMessages.P33_INVALID,
                                lang);
        }
    }

    void reportFatalXMLError(int majorCode, int minorCode) throws Exception {
        fErrorReporter.reportError(fErrorReporter.getLocator(),
                                   XMLMessages.XML_DOMAIN,
                                   majorCode,
                                   minorCode,
                                   null,
                                   XMLErrorReporter.ERRORTYPE_FATAL_ERROR);
    }
    void reportFatalXMLError(int majorCode, int minorCode, int stringIndex1) throws Exception {
        Object[] args = { fStringPool.toString(stringIndex1) };
        fErrorReporter.reportError(fErrorReporter.getLocator(),
                                   XMLMessages.XML_DOMAIN,
                                   majorCode,
                                   minorCode,
                                   args,
                                   XMLErrorReporter.ERRORTYPE_FATAL_ERROR);
    }
    void reportFatalXMLError(int majorCode, int minorCode, String string1) throws Exception {
        Object[] args = { string1 };
        fErrorReporter.reportError(fErrorReporter.getLocator(),
                                   XMLMessages.XML_DOMAIN,
                                   majorCode,
                                   minorCode,
                                   args,
                                   XMLErrorReporter.ERRORTYPE_FATAL_ERROR);
    }
    void reportFatalXMLError(int majorCode, int minorCode, int stringIndex1, int stringIndex2) throws Exception {
        Object[] args = { fStringPool.toString(stringIndex1),
                          fStringPool.toString(stringIndex2) };
        fErrorReporter.reportError(fErrorReporter.getLocator(),
                                   XMLMessages.XML_DOMAIN,
                                   majorCode,
                                   minorCode,
                                   args,
                                   XMLErrorReporter.ERRORTYPE_FATAL_ERROR);
    }
    void reportFatalXMLError(int majorCode, int minorCode, String string1, String string2) throws Exception {
        Object[] args = { string1, string2 };
        fErrorReporter.reportError(fErrorReporter.getLocator(),
                                   XMLMessages.XML_DOMAIN,
                                   majorCode,
                                   minorCode,
                                   args,
                                   XMLErrorReporter.ERRORTYPE_FATAL_ERROR);
    }
    void reportFatalXMLError(int majorCode, int minorCode, String string1, String string2, String string3) throws Exception {
        Object[] args = { string1, string2, string3 };
        fErrorReporter.reportError(fErrorReporter.getLocator(),
                                   XMLMessages.XML_DOMAIN,
                                   majorCode,
                                   minorCode,
                                   args,
                                   XMLErrorReporter.ERRORTYPE_FATAL_ERROR);
    }
    void abortMarkup(int majorCode, int minorCode) throws Exception {
        reportFatalXMLError(majorCode, minorCode);
        skipPastEndOfCurrentMarkup();
    }
    void abortMarkup(int majorCode, int minorCode, int stringIndex1) throws Exception {
        reportFatalXMLError(majorCode, minorCode, stringIndex1);
        skipPastEndOfCurrentMarkup();
    }
    void abortMarkup(int majorCode, int minorCode, String string1) throws Exception {
        reportFatalXMLError(majorCode, minorCode, string1);
        skipPastEndOfCurrentMarkup();
    }
    void abortMarkup(int majorCode, int minorCode, int stringIndex1, int stringIndex2) throws Exception {
        reportFatalXMLError(majorCode, minorCode, stringIndex1, stringIndex2);
        skipPastEndOfCurrentMarkup();
    }
    void skipPastEndOfCurrentMarkup() throws Exception {
        fEntityReader.skipToChar('>');
        if (fEntityReader.lookingAtChar('>', true))
            fScannerMarkupDepth--;
    }
    int setScannerState(int state) {
        int oldState = fScannerState;
        fScannerState = state;
        return oldState;
    }
    void restoreScannerState(int state) {
        if (fScannerState != SCANNER_STATE_END_OF_INPUT)
            fScannerState = state;
    }
    /**
     * The main loop of the scanner is implemented by calling the dispatch method
     * of ScannerDispatcher with a flag which tells the dispatcher whether to continue
     * or return.  The scanner logic is split up into dispatchers for various syntatic
     */
    interface ScannerDispatcher {
        /**
         * scan an XML syntactic component 
         *
         * @param keepgoing if true continue on to the next dispatcher, otherwise return
         * @exception java.lang.Exception
         */
        boolean dispatch(boolean keepgoing) throws Exception;
        /**
         * endOfInput encapsulates the end of entity handling for each dispatcher 
         *
         * @param entityName StringPool handle of the entity that has reached the end
         * @param moreToFollow true if there is more input to be read
         * @exception
         */
        void endOfInput(int entityName, boolean moreToFollow) throws Exception;
    }
    final class XMLDeclDispatcher implements ScannerDispatcher {
        public boolean dispatch(boolean keepgoing) throws Exception {
            fEventHandler.callStartDocument();
            if (fEntityReader.lookingAtChar('<', true)) {
                fScannerMarkupDepth++;
                setScannerState(SCANNER_STATE_START_OF_MARKUP);
                if (fEntityReader.lookingAtChar('?', true)) {
                    int piTarget = fEntityReader.scanName(' ');
                    if (piTarget == -1) {
                        abortMarkup(XMLMessages.MSG_PITARGET_REQUIRED,
                                    XMLMessages.P16_PITARGET_REQUIRED);
                    } else if ("xml".equals(fStringPool.toString(piTarget))) {
                            scanXMLDeclOrTextDecl(false);
                            abortMarkup(XMLMessages.MSG_RESERVED_PITARGET,
                                        XMLMessages.P17_RESERVED_PITARGET);
                        }
                      scanPI(piTarget);
                    }
                    fDispatcher = new PrologDispatcher();
                    restoreScannerState(SCANNER_STATE_PROLOG);
                    return true;
                }
                if (fEntityReader.lookingAtChar('!', true)) {
                        if (fEntityReader.lookingAtChar('-', true)) {
                        } else {
                            abortMarkup(XMLMessages.MSG_MARKUP_NOT_RECOGNIZED_IN_PROLOG,
                                        XMLMessages.P22_NOT_RECOGNIZED);
                        }
                    } else {
                        if (fEntityReader.skippedString(doctype_string)) {
                            setScannerState(SCANNER_STATE_DOCTYPE);
                            fSeenDoctypeDecl = true;
                            fScannerMarkupDepth--;
                            fDispatcher = new PrologDispatcher();
                            restoreScannerState(SCANNER_STATE_PROLOG);
                            return true;
                        } else {
                            abortMarkup(XMLMessages.MSG_MARKUP_NOT_RECOGNIZED_IN_PROLOG,
                                        XMLMessages.P22_NOT_RECOGNIZED);
                        }
                    }
                } else {
                    fDispatcher = new ContentDispatcher();
                    restoreScannerState(SCANNER_STATE_ROOT_ELEMENT);
                    return true;
                }
            } else {
                if (fEntityReader.lookingAtSpace(true)) {
                    fEntityReader.skipPastSpaces();
                } else if (!fEntityReader.lookingAtValidChar(false)) {
                    int invChar = fEntityReader.scanInvalidChar();
                    if (fScannerState != SCANNER_STATE_END_OF_INPUT) {
                        if (invChar >= 0) {
                            String arg = Integer.toHexString(invChar);
                            reportFatalXMLError(XMLMessages.MSG_INVALID_CHAR_IN_PROLOG,
                                                XMLMessages.P22_INVALID_CHARACTER,
                                                arg);
                        }
                    }
                } else {
                    reportFatalXMLError(XMLMessages.MSG_MARKUP_NOT_RECOGNIZED_IN_PROLOG,
                                        XMLMessages.P22_NOT_RECOGNIZED);
                    fEntityReader.lookingAtValidChar(true);
                }
            }
            fDispatcher = new PrologDispatcher();
            restoreScannerState(SCANNER_STATE_PROLOG);
            return true;
        }
        public void endOfInput(int entityName, boolean moreToFollow) throws Exception {
            switch (fScannerState) {
            case SCANNER_STATE_XML_DECL:
            case SCANNER_STATE_START_OF_MARKUP:
            case SCANNER_STATE_DOCTYPE:
                break;
            case SCANNER_STATE_COMMENT:
                if (!moreToFollow) {
                    reportFatalXMLError(XMLMessages.MSG_COMMENT_UNTERMINATED,
                                        XMLMessages.P15_UNTERMINATED);
                } else {
                    reportFatalXMLError(XMLMessages.MSG_COMMENT_NOT_IN_ONE_ENTITY,
                                        XMLMessages.P78_NOT_WELLFORMED);
                }
                break;
            case SCANNER_STATE_PI:
                if (!moreToFollow) {
                    reportFatalXMLError(XMLMessages.MSG_PI_UNTERMINATED,
                                        XMLMessages.P16_UNTERMINATED);
                } else {
                    reportFatalXMLError(XMLMessages.MSG_PI_NOT_IN_ONE_ENTITY,
                                        XMLMessages.P78_NOT_WELLFORMED);
                }
                break;
            default:
                throw new RuntimeException("FWK001 1] ScannerState="+fScannerState+"\n" + "1\t"+fScannerState);
            }
            if (!moreToFollow) {
                reportFatalXMLError(XMLMessages.MSG_ROOT_ELEMENT_REQUIRED,
                                    XMLMessages.P1_ELEMENT_REQUIRED);
                fDispatcher = new EndOfInputDispatcher();
                setScannerState(SCANNER_STATE_END_OF_INPUT);
            }
        }
    }
    final class PrologDispatcher implements ScannerDispatcher {
        public boolean dispatch(boolean keepgoing) throws Exception {
            do {
                if (fEntityReader.lookingAtChar('<', true)) {
                    fScannerMarkupDepth++;
                    setScannerState(SCANNER_STATE_START_OF_MARKUP);
                    if (fEntityReader.lookingAtChar('?', true)) {
                        int piTarget = fEntityReader.scanName(' ');
                        if (piTarget == -1) {
                            abortMarkup(XMLMessages.MSG_PITARGET_REQUIRED,
                                        XMLMessages.P16_PITARGET_REQUIRED);
                        } else if ("xml".equals(fStringPool.toString(piTarget))) {
                                abortMarkup(XMLMessages.MSG_XMLDECL_MUST_BE_FIRST,
                                            XMLMessages.P22_XMLDECL_MUST_BE_FIRST);
                                abortMarkup(XMLMessages.MSG_RESERVED_PITARGET,
                                            XMLMessages.P17_RESERVED_PITARGET);
                            }
                            scanPI(piTarget);
                        }
                    } else if (fEntityReader.lookingAtChar('!', true)) {
                            if (fEntityReader.lookingAtChar('-', true)) {
                            } else {
                                abortMarkup(XMLMessages.MSG_MARKUP_NOT_RECOGNIZED_IN_PROLOG,
                                            XMLMessages.P22_NOT_RECOGNIZED);
                            }
                        } else {
                            if (!fSeenDoctypeDecl && fEntityReader.skippedString(doctype_string)) {
                                setScannerState(SCANNER_STATE_DOCTYPE);
                                fSeenDoctypeDecl = true;
                                fScannerMarkupDepth--;
                            } else {
                                abortMarkup(XMLMessages.MSG_MARKUP_NOT_RECOGNIZED_IN_PROLOG,
                                            XMLMessages.P22_NOT_RECOGNIZED);
                            }
                        }
                    } else {
                        fDispatcher = new ContentDispatcher();
                        restoreScannerState(SCANNER_STATE_ROOT_ELEMENT);
                        return true;
                    }
                    restoreScannerState(SCANNER_STATE_PROLOG);
                } else if (fEntityReader.lookingAtSpace(true)) {
                    fEntityReader.skipPastSpaces();
                } else if (!fEntityReader.lookingAtValidChar(false)) {
                    int invChar = fEntityReader.scanInvalidChar();
                    if (fScannerState != SCANNER_STATE_END_OF_INPUT) {
                        if (invChar >= 0) {
                            String arg = Integer.toHexString(invChar);
                            reportFatalXMLError(XMLMessages.MSG_INVALID_CHAR_IN_PROLOG,
                                                XMLMessages.P22_INVALID_CHARACTER,
                                                arg);
                        }
                    }
                } else {
                    reportFatalXMLError(XMLMessages.MSG_MARKUP_NOT_RECOGNIZED_IN_PROLOG,
                                        XMLMessages.P22_NOT_RECOGNIZED);
                    fEntityReader.lookingAtValidChar(true);
                }
            } while (fScannerState != SCANNER_STATE_END_OF_INPUT && keepgoing);
            return true;
        }
        public void endOfInput(int entityName, boolean moreToFollow) throws Exception {
            switch (fScannerState) {
            case SCANNER_STATE_PROLOG:
            case SCANNER_STATE_START_OF_MARKUP:
            case SCANNER_STATE_DOCTYPE:
                break;
            case SCANNER_STATE_COMMENT:
                if (!moreToFollow) {
                    reportFatalXMLError(XMLMessages.MSG_COMMENT_UNTERMINATED,
                                        XMLMessages.P15_UNTERMINATED);
                } else {
                    reportFatalXMLError(XMLMessages.MSG_COMMENT_NOT_IN_ONE_ENTITY,
                                        XMLMessages.P78_NOT_WELLFORMED);
                }
                break;
            case SCANNER_STATE_PI:
                if (!moreToFollow) {
                    reportFatalXMLError(XMLMessages.MSG_PI_UNTERMINATED,
                                        XMLMessages.P16_UNTERMINATED);
                } else {
                    reportFatalXMLError(XMLMessages.MSG_PI_NOT_IN_ONE_ENTITY,
                                        XMLMessages.P78_NOT_WELLFORMED);
                }
                break;
            default:
                throw new RuntimeException("FWK001 2] ScannerState="+fScannerState+"\n" + "2\t"+fScannerState);
            }
            if (!moreToFollow) {
                reportFatalXMLError(XMLMessages.MSG_ROOT_ELEMENT_REQUIRED,
                                    XMLMessages.P1_ELEMENT_REQUIRED);
                fDispatcher = new EndOfInputDispatcher();
                setScannerState(SCANNER_STATE_END_OF_INPUT);
            }
        }
    }
    int fCurrentElementType = -1;
    public int getCurrentElementType() {
        return fCurrentElementType;
    }
    final class ContentDispatcher implements ScannerDispatcher {
        private int fContentReader = -1;
        private int fElementDepth = 0;
        private int[] fElementTypeStack = new int[8];

        void popElementType() {
            if (fElementDepth-- == 0) {
                throw new RuntimeException("FWK002 popElementType: fElementDepth-- == 0.");
            }
            if (fElementDepth == 0) {
                fCurrentElementType = - 1;
            } else {
                fCurrentElementType = fElementTypeStack[fElementDepth - 1];
            }
        }

        public boolean dispatch(boolean keepgoing) throws Exception {
            do {
                switch (fScannerState) {
                case SCANNER_STATE_ROOT_ELEMENT:
                {
                    scanElementType(fEntityReader, '>', fElementQName);
                    if (fElementQName.rawname != -1) {
                        fContentReader = fReaderId;
                        fSeenRootElement = true;
                        if (fEntityReader.lookingAtChar('>', true)) {
                            fEventHandler.callStartElement(fElementQName);
                            fScannerMarkupDepth--;
                            if (fElementDepth == fElementTypeStack.length) {
                                int[] newStack = new int[fElementDepth * 2];
                                System.arraycopy(fElementTypeStack, 0, newStack, 0, fElementDepth);
                                fElementTypeStack = newStack;
                            }
                            fCurrentElementType = fElementQName.rawname;
                            fElementTypeStack[fElementDepth] = fElementQName.rawname;
                            fElementDepth++;
                            restoreScannerState(SCANNER_STATE_CONTENT);
                        } else if (scanElement(fElementQName)) {
                            if (fElementDepth == fElementTypeStack.length) {
                                int[] newStack = new int[fElementDepth * 2];
                                System.arraycopy(fElementTypeStack, 0, newStack, 0, fElementDepth);
                                fElementTypeStack = newStack;
                            }
                            fCurrentElementType = fElementQName.rawname;
                            fElementTypeStack[fElementDepth] = fElementQName.rawname;
                            fElementDepth++;
                            restoreScannerState(SCANNER_STATE_CONTENT);
                        } else {
                            fDispatcher = new TrailingMiscDispatcher();
                            restoreScannerState(SCANNER_STATE_TRAILING_MISC);
                            return true;
                        }
                    } else {
                        reportFatalXMLError(XMLMessages.MSG_MARKUP_NOT_RECOGNIZED_IN_PROLOG,
                                            XMLMessages.P22_NOT_RECOGNIZED);
                        fDispatcher = new PrologDispatcher();
                        restoreScannerState(SCANNER_STATE_PROLOG);
                        return true;
                    }
                    break;
                }
                case SCANNER_STATE_START_OF_MARKUP:
                    if (fEntityReader.lookingAtChar('?', true)) {
                        int piTarget = fEntityReader.scanName(' ');
                        if (piTarget == -1) {
                            abortMarkup(XMLMessages.MSG_PITARGET_REQUIRED,
                                        XMLMessages.P16_PITARGET_REQUIRED);
                        } else if ("xml".equals(fStringPool.toString(piTarget))) {
                                if (fParseTextDecl) {
                                    scanXMLDeclOrTextDecl(true);
                                    fParseTextDecl = false;
                                } else {
                                    abortMarkup(XMLMessages.MSG_TEXTDECL_MUST_BE_FIRST,
                                                XMLMessages.P30_TEXTDECL_MUST_BE_FIRST);
                                }
                                abortMarkup(XMLMessages.MSG_RESERVED_PITARGET,
                                            XMLMessages.P17_RESERVED_PITARGET);
                            }
                            scanPI(piTarget);
                        }
                        restoreScannerState(SCANNER_STATE_CONTENT);
                    } else if (fEntityReader.lookingAtChar('!', true)) {
                            if (fEntityReader.lookingAtChar('-', true)) {
                            } else {
                                abortMarkup(XMLMessages.MSG_MARKUP_NOT_RECOGNIZED_IN_CONTENT,
                                            XMLMessages.P43_NOT_RECOGNIZED);
                            }
                        } else {
                            if (fEntityReader.skippedString(cdata_string)) {
                                fEntityReader.setInCDSect(true);
                                fEventHandler.callStartCDATA();
                            } else {
                                abortMarkup(XMLMessages.MSG_MARKUP_NOT_RECOGNIZED_IN_CONTENT,
                                            XMLMessages.P43_NOT_RECOGNIZED);
                            }
                        }
                    } else {
                        if (fEntityReader.lookingAtChar('/', true)) {
                            if (!scanExpectedElementType(fEntityReader, '>', fCurrentElementType)) {
                                abortMarkup(XMLMessages.MSG_ETAG_REQUIRED,
                                            XMLMessages.P39_UNTERMINATED,
                                            fCurrentElementType);
                            } else {
                                if (!fEntityReader.lookingAtChar('>', true)) {
                                    fEntityReader.skipPastSpaces();
                                    if (!fEntityReader.lookingAtChar('>', true)) {
                                        reportFatalXMLError(XMLMessages.MSG_ETAG_UNTERMINATED,
                                                            XMLMessages.P42_UNTERMINATED,
                                                            fCurrentElementType);
                                    }
                                }
                                fScannerMarkupDepth--;
                                fEventHandler.callEndElement(fReaderId);
                                if (fElementDepth-- == 0) {
                                    throw new RuntimeException("FWK002 popElementType: fElementDepth-- == 0.");
                                }
                                if (fElementDepth == 0) {
                                    fCurrentElementType = - 1;
                                    fDispatcher = new TrailingMiscDispatcher();
                                    restoreScannerState(SCANNER_STATE_TRAILING_MISC);
                                    return true;
                                } else {
                                    fCurrentElementType = fElementTypeStack[fElementDepth - 1];
                                }
                            }
                        } else {
                            scanElementType(fEntityReader, '>', fElementQName);
                            if (fElementQName.rawname != -1) {
                                if (fEntityReader.lookingAtChar('>', true)) {
                                    fEventHandler.callStartElement(fElementQName);
                                    fScannerMarkupDepth--;
                                    if (fElementDepth == fElementTypeStack.length) {
                                        int[] newStack = new int[fElementDepth * 2];
                                        System.arraycopy(fElementTypeStack, 0, newStack, 0, fElementDepth);
                                        fElementTypeStack = newStack;
                                    }
                                    fCurrentElementType = fElementQName.rawname;
                                    fElementTypeStack[fElementDepth] = fElementQName.rawname;
                                    fElementDepth++;
                                } else {
                                    if (scanElement(fElementQName)) {
                                        if (fElementDepth == fElementTypeStack.length) {
                                            int[] newStack = new int[fElementDepth * 2];
                                            System.arraycopy(fElementTypeStack, 0, newStack, 0, fElementDepth);
                                            fElementTypeStack = newStack;
                                        }
                                        fCurrentElementType = fElementQName.rawname;
                                        fElementTypeStack[fElementDepth] = fElementQName.rawname;
                                        fElementDepth++;
                                    }
                                }
                            } else {
                                abortMarkup(XMLMessages.MSG_MARKUP_NOT_RECOGNIZED_IN_CONTENT,
                                            XMLMessages.P43_NOT_RECOGNIZED);
                            }
                        }
                    }
                    restoreScannerState(SCANNER_STATE_CONTENT);
                    break;
                case SCANNER_STATE_CONTENT:
                    if (fParseTextDecl && fEntityReader.lookingAtChar('<', true)) {
                        fScannerMarkupDepth++;
                        setScannerState(SCANNER_STATE_START_OF_MARKUP);
                        continue;
                    }
                    fCurrentElementQName.setValues(-1, -1, fCurrentElementType);
                    switch (fEntityReader.scanContent(fCurrentElementQName)) {
                    case XMLEntityHandler.CONTENT_RESULT_START_OF_PI:
                        fScannerMarkupDepth++;
                        int piTarget = fEntityReader.scanName(' ');
                        if (piTarget == -1) {
                            abortMarkup(XMLMessages.MSG_PITARGET_REQUIRED,
                                        XMLMessages.P16_PITARGET_REQUIRED);
                        } else if ("xml".equals(fStringPool.toString(piTarget))) {
                                if (fReaderId == fContentReader) {
                                    abortMarkup(XMLMessages.MSG_XMLDECL_MUST_BE_FIRST,
                                                XMLMessages.P22_XMLDECL_MUST_BE_FIRST);
                                } else {
                                    abortMarkup(XMLMessages.MSG_TEXTDECL_MUST_BE_FIRST,
                                                XMLMessages.P30_TEXTDECL_MUST_BE_FIRST);
                                }
                                abortMarkup(XMLMessages.MSG_RESERVED_PITARGET,
                                            XMLMessages.P17_RESERVED_PITARGET);
                            }
                            scanPI(piTarget);
                        }
                        break;
                    case XMLEntityHandler.CONTENT_RESULT_START_OF_COMMENT:
                        fScannerMarkupDepth++;
                        fParseTextDecl = false;
                        break;
                    case XMLEntityHandler.CONTENT_RESULT_START_OF_CDSECT:
                        fScannerMarkupDepth++;
                        fParseTextDecl = false;
                        fEntityReader.setInCDSect(true);
                        fEventHandler.callStartCDATA();
                        break;
                    case XMLEntityHandler.CONTENT_RESULT_START_OF_ETAG:
                        fScannerMarkupDepth++;
                        fParseTextDecl = false;
                        if (!scanExpectedElementType(fEntityReader, '>', fCurrentElementType)) {
                            abortMarkup(XMLMessages.MSG_ETAG_REQUIRED,
                                        XMLMessages.P39_UNTERMINATED,
                                        fCurrentElementType);
                        } else {
                            if (!fEntityReader.lookingAtChar('>', true)) {
                                fEntityReader.skipPastSpaces();
                                if (!fEntityReader.lookingAtChar('>', true)) {
                                    reportFatalXMLError(XMLMessages.MSG_ETAG_UNTERMINATED,
                                                        XMLMessages.P42_UNTERMINATED,
                                                        fCurrentElementType);
                                }
                            }
                            fScannerMarkupDepth--;
                            fEventHandler.callEndElement(fReaderId);
                            if (fElementDepth-- == 0) {
                                throw new RuntimeException("FWK002 popElementType: fElementDepth-- == 0.");
                            }
                            if (fElementDepth == 0) {
                                fCurrentElementType = - 1;
                                fDispatcher = new TrailingMiscDispatcher();
                                restoreScannerState(SCANNER_STATE_TRAILING_MISC);
                                return true;
                            } else {
                                fCurrentElementType = fElementTypeStack[fElementDepth - 1];
                            }
                        }
                        restoreScannerState(SCANNER_STATE_CONTENT);
                        break;
                    case XMLEntityHandler.CONTENT_RESULT_START_OF_ELEMENT:
                    {
                        fScannerMarkupDepth++;
                        fParseTextDecl = false;
                        scanElementType(fEntityReader, '>', fElementQName);
                        if (fElementQName.rawname != -1) {
                            if (fEntityReader.lookingAtChar('>', true)) {
                                fEventHandler.callStartElement(fElementQName);
                                fScannerMarkupDepth--;
                                if (fElementDepth == fElementTypeStack.length) {
                                    int[] newStack = new int[fElementDepth * 2];
                                    System.arraycopy(fElementTypeStack, 0, newStack, 0, fElementDepth);
                                    fElementTypeStack = newStack;
                                }
                                fCurrentElementType = fElementQName.rawname;
                                fElementTypeStack[fElementDepth] = fElementQName.rawname;
                                fElementDepth++;
                            } else {
                                if (scanElement(fElementQName)) {
                                    if (fElementDepth == fElementTypeStack.length) {
                                        int[] newStack = new int[fElementDepth * 2];
                                        System.arraycopy(fElementTypeStack, 0, newStack, 0, fElementDepth);
                                        fElementTypeStack = newStack;
                                    }
                                    fCurrentElementType = fElementQName.rawname;
                                    fElementTypeStack[fElementDepth] = fElementQName.rawname;
                                    fElementDepth++;
                                }
                            }
                        } else {
                            abortMarkup(XMLMessages.MSG_MARKUP_NOT_RECOGNIZED_IN_CONTENT,
                                        XMLMessages.P43_NOT_RECOGNIZED);
                        }
                        if (fScannerState != SCANNER_STATE_END_OF_INPUT)
                            fScannerState = SCANNER_STATE_CONTENT;
                        break;
                    }
                    case XMLEntityHandler.CONTENT_RESULT_MATCHING_ETAG:
                    {
                        fParseTextDecl = false;
                        fEventHandler.callEndElement(fReaderId);
                        if (fElementDepth-- == 0) {
                            throw new RuntimeException("FWK002 popElementType: fElementDepth-- == 0.");
                        }
                        if (fElementDepth == 0) {
                            fCurrentElementType = - 1;
                            if (fScannerState != SCANNER_STATE_END_OF_INPUT) {
                                fDispatcher = new TrailingMiscDispatcher();
                                fScannerState = SCANNER_STATE_TRAILING_MISC;
                            }
                            return true;
                        } else {
                            fCurrentElementType = fElementTypeStack[fElementDepth - 1];
                        }
                        if (fScannerState != SCANNER_STATE_END_OF_INPUT)
                            fScannerState = SCANNER_STATE_CONTENT;
                        break;
                    }
                    case XMLEntityHandler.CONTENT_RESULT_START_OF_CHARREF:
                        fParseTextDecl = false;
                        setScannerState(SCANNER_STATE_REFERENCE);
                        int num = scanCharRef();
                        if (num != -1)
                            fEventHandler.callCharacters(num);
                        restoreScannerState(SCANNER_STATE_CONTENT);
                        break;
                    case XMLEntityHandler.CONTENT_RESULT_REFERENCE_END_OF_INPUT:
                    case XMLEntityHandler.CONTENT_RESULT_START_OF_ENTITYREF:
                        fParseTextDecl = false;
                        setScannerState(SCANNER_STATE_REFERENCE);
                        int nameOffset = fEntityReader.currentOffset();
                        fEntityReader.skipPastName(';');
                        int nameLength = fEntityReader.currentOffset() - nameOffset;
                        if (nameLength == 0) {
                            reportFatalXMLError(XMLMessages.MSG_NAME_REQUIRED_IN_REFERENCE,
                                                XMLMessages.P68_NAME_REQUIRED);
                            restoreScannerState(SCANNER_STATE_CONTENT);
                        } else if (!fEntityReader.lookingAtChar(';', true)) {
                            reportFatalXMLError(XMLMessages.MSG_SEMICOLON_REQUIRED_IN_REFERENCE,
                                                XMLMessages.P68_SEMICOLON_REQUIRED,
                                                fEntityReader.addString(nameOffset, nameLength));
                            restoreScannerState(SCANNER_STATE_CONTENT);
                        } else {
                            restoreScannerState(SCANNER_STATE_CONTENT);
                            int entityName = fEntityReader.addSymbol(nameOffset, nameLength);
                            fParseTextDecl = fEntityHandler.startReadingFromEntity(entityName, fElementDepth, XMLEntityHandler.ENTITYREF_IN_CONTENT);
                        }
                        break;
                    case XMLEntityHandler.CONTENT_RESULT_END_OF_CDSECT:
                        fParseTextDecl = false;
                        if (fEntityReader.getInCDSect()) {
                            fEntityReader.setInCDSect(false);
                            fEventHandler.callEndCDATA();
                            fScannerMarkupDepth--;
                        } else {
                            reportFatalXMLError(XMLMessages.MSG_CDEND_IN_CONTENT,
                                                XMLMessages.P14_INVALID);
                        }
                        restoreScannerState(SCANNER_STATE_CONTENT);
                        break;
                    case XMLEntityHandler.CONTENT_RESULT_INVALID_CHAR:
                        fParseTextDecl = false;
                        if (fScannerState != SCANNER_STATE_END_OF_INPUT) {
                            if (!fEntityReader.lookingAtValidChar(false)) {
                                int invChar = fEntityReader.scanInvalidChar();
                                if (fScannerState != SCANNER_STATE_END_OF_INPUT) {
                                    if (invChar >= 0) {
                                        if (fEntityReader.getInCDSect()) {
                                            reportFatalXMLError(XMLMessages.MSG_INVALID_CHAR_IN_CDSECT,
                                                                XMLMessages.P20_INVALID_CHARACTER,
                                                                Integer.toHexString(invChar));
                                        } else {
                                            reportFatalXMLError(XMLMessages.MSG_INVALID_CHAR_IN_CONTENT,
                                                                XMLMessages.P43_INVALID_CHARACTER,
                                                                Integer.toHexString(invChar));
                                        }
                                    }
                                }
                            }
                            restoreScannerState(SCANNER_STATE_CONTENT);
                        }
                        break;
                    case XMLEntityHandler.CONTENT_RESULT_MARKUP_NOT_RECOGNIZED:
                        fParseTextDecl = false;
                        abortMarkup(XMLMessages.MSG_MARKUP_NOT_RECOGNIZED_IN_CONTENT,
                                    XMLMessages.P43_NOT_RECOGNIZED);
                        break;
                    case XMLEntityHandler.CONTENT_RESULT_MARKUP_END_OF_INPUT:
                        fScannerMarkupDepth++;
                        fParseTextDecl = false;
                        fScannerState = SCANNER_STATE_START_OF_MARKUP;
                        break;
                    default:
                    }
                    break;
                default:
                    throw new RuntimeException("FWK001 4] ScannerState="+fScannerState+"\n" + "4\t"+fScannerState);
                }
            } while (fScannerState != SCANNER_STATE_END_OF_INPUT && keepgoing);
            return true;
        }
        public void endOfInput(int entityName, boolean moreToFollow) throws Exception {
            switch (fScannerState) {
            case SCANNER_STATE_ROOT_ELEMENT:
            case SCANNER_STATE_START_OF_MARKUP:
                break;
            case SCANNER_STATE_CONTENT:
                if (fEntityReader.getInCDSect()) {
                    reportFatalXMLError(XMLMessages.MSG_CDSECT_UNTERMINATED,
                                        XMLMessages.P18_UNTERMINATED);
                }
                break;
            case SCANNER_STATE_ATTRIBUTE_LIST:
                if (!moreToFollow) {
                } else {
                }
                break;
            case SCANNER_STATE_ATTRIBUTE_NAME:
                if (!moreToFollow) {
                } else {
                }
                break;
            case SCANNER_STATE_ATTRIBUTE_VALUE:
                if (!moreToFollow) {
                    reportFatalXMLError(XMLMessages.MSG_ATTRIBUTE_VALUE_UNTERMINATED,
                                        XMLMessages.P10_UNTERMINATED,
                                        fAttValueElementType,
                                        fAttValueAttrName);
                } else if (fReaderId == fAttValueReader) {
                } else {
                    fEntityReader.append(fLiteralData, fAttValueMark, fAttValueOffset - fAttValueMark);
                }
                break;
            case SCANNER_STATE_COMMENT:
                if (!moreToFollow) {
                    reportFatalXMLError(XMLMessages.MSG_COMMENT_UNTERMINATED,
                                        XMLMessages.P15_UNTERMINATED);
                } else {
                    reportFatalXMLError(XMLMessages.MSG_COMMENT_NOT_IN_ONE_ENTITY,
                                        XMLMessages.P78_NOT_WELLFORMED);
                }
                break;
            case SCANNER_STATE_PI:
                if (!moreToFollow) {
                    reportFatalXMLError(XMLMessages.MSG_PI_UNTERMINATED,
                                        XMLMessages.P16_UNTERMINATED);
                } else {
                    reportFatalXMLError(XMLMessages.MSG_PI_NOT_IN_ONE_ENTITY,
                                        XMLMessages.P78_NOT_WELLFORMED);
                }
                break;
            case SCANNER_STATE_REFERENCE:
                if (!moreToFollow) {
                    reportFatalXMLError(XMLMessages.MSG_REFERENCE_UNTERMINATED,
                                        XMLMessages.P67_UNTERMINATED);
                } else {
                    reportFatalXMLError(XMLMessages.MSG_REFERENCE_NOT_IN_ONE_ENTITY,
                                        XMLMessages.P78_NOT_WELLFORMED);
                }
                break;
            default:
                throw new RuntimeException("FWK001 5] ScannerState="+fScannerState+"\n" + "5\t"+fScannerState);
            }
            if (!moreToFollow) {
                if (fElementDepth > 0) {
                    reportFatalXMLError(XMLMessages.MSG_ETAG_REQUIRED,
                                        XMLMessages.P39_UNTERMINATED,
                                        fCurrentElementType);
                } else {
                    reportFatalXMLError(XMLMessages.MSG_ROOT_ELEMENT_REQUIRED,
                                        XMLMessages.P1_ELEMENT_REQUIRED,
                                        null);
                }
                fDispatcher = new EndOfInputDispatcher();
                setScannerState(SCANNER_STATE_END_OF_INPUT);
            }
        }
    }
    final class TrailingMiscDispatcher implements ScannerDispatcher {
        public boolean dispatch(boolean keepgoing) throws Exception {
            do {
                if (fEntityReader.lookingAtChar('<', true)) {
                    fScannerMarkupDepth++;
                    setScannerState(SCANNER_STATE_START_OF_MARKUP);
                    if (fEntityReader.lookingAtChar('?', true)) {
                        int piTarget = fEntityReader.scanName(' ');
                        if (piTarget == -1) {
                            abortMarkup(XMLMessages.MSG_PITARGET_REQUIRED,
                                        XMLMessages.P16_PITARGET_REQUIRED);
                        } else if ("xml".equals(fStringPool.toString(piTarget))) {
                                abortMarkup(XMLMessages.MSG_XMLDECL_MUST_BE_FIRST,
                                            XMLMessages.P22_XMLDECL_MUST_BE_FIRST);
                                abortMarkup(XMLMessages.MSG_RESERVED_PITARGET,
                                            XMLMessages.P17_RESERVED_PITARGET);
                            }
                            scanPI(piTarget);
                        }
                    } else if (fEntityReader.lookingAtChar('!', true)) {
                        if (fEntityReader.lookingAtChar('-', true) &&
                        } else {
                            abortMarkup(XMLMessages.MSG_MARKUP_NOT_RECOGNIZED_IN_MISC,
                                        XMLMessages.P27_NOT_RECOGNIZED);
                        }
                    } else {
                        abortMarkup(XMLMessages.MSG_MARKUP_NOT_RECOGNIZED_IN_MISC,
                                    XMLMessages.P27_NOT_RECOGNIZED);
                    }
                    restoreScannerState(SCANNER_STATE_TRAILING_MISC);
                } else if (fEntityReader.lookingAtSpace(true)) {
                    fEntityReader.skipPastSpaces();
                } else if (!fEntityReader.lookingAtValidChar(false)) {
                    int invChar = fEntityReader.scanInvalidChar();
                    if (fScannerState != SCANNER_STATE_END_OF_INPUT) {
                        if (invChar >= 0) {
                            String arg = Integer.toHexString(invChar);
                            reportFatalXMLError(XMLMessages.MSG_INVALID_CHAR_IN_MISC,
                                                XMLMessages.P27_INVALID_CHARACTER,
                                                arg);
                        }
                    }
                } else {
                    reportFatalXMLError(XMLMessages.MSG_MARKUP_NOT_RECOGNIZED_IN_MISC,
                                        XMLMessages.P27_NOT_RECOGNIZED);
                    fEntityReader.lookingAtValidChar(true);
                }
            } while (fScannerState != SCANNER_STATE_END_OF_INPUT && keepgoing);
            return true;
        }
        public void endOfInput(int entityName, boolean moreToFollow) throws Exception {
            if (moreToFollow)
                throw new RuntimeException("FWK003 TrailingMiscDispatcher.endOfInput moreToFollow");
            switch (fScannerState) {
            case SCANNER_STATE_TRAILING_MISC:
            case SCANNER_STATE_START_OF_MARKUP:
                break;
            case SCANNER_STATE_COMMENT:
                reportFatalXMLError(XMLMessages.MSG_COMMENT_UNTERMINATED,
                                    XMLMessages.P15_UNTERMINATED);
                break;
            case SCANNER_STATE_PI:
                reportFatalXMLError(XMLMessages.MSG_PI_UNTERMINATED,
                                    XMLMessages.P16_UNTERMINATED);
                break;
            default:
                throw new RuntimeException("FWK001 6] ScannerState="+fScannerState+"\n" + "6\t"+fScannerState);
            }
            fDispatcher = new EndOfInputDispatcher();
            setScannerState(SCANNER_STATE_END_OF_INPUT);
        }
    }
    final class EndOfInputDispatcher implements ScannerDispatcher {
        public boolean dispatch(boolean keepgoing) throws Exception {
            if (fScannerState != SCANNER_STATE_TERMINATED)
                fEventHandler.callEndDocument();
            setScannerState(SCANNER_STATE_TERMINATED);
            return false;
        }
        public void endOfInput(int entityName, boolean moreToFollow) throws Exception {
            throw new RuntimeException("FWK001 7] ScannerState="+fScannerState+"\n" + "7\t"+fScannerState);
        }
    }
    void scanXMLDeclOrTextDecl(boolean scanningTextDecl) throws Exception
    {
        int version = -1;
        int encoding = -1;
        int standalone = -1;
        final int XMLDECL_START = 0;
        final int XMLDECL_VERSION = 1;
        final int XMLDECL_ENCODING = 2;
        final int XMLDECL_STANDALONE = 3;
        final int XMLDECL_FINISHED = 4;
        int state = XMLDECL_START;
        do {
            fEntityReader.skipPastSpaces();
            int offset = fEntityReader.currentOffset();
            if (scanningTextDecl) {
                if (state == XMLDECL_START && fEntityReader.skippedString(version_string)) {
                    state = XMLDECL_VERSION;
                } else if (fEntityReader.skippedString(encoding_string)) {
                    state = XMLDECL_ENCODING;
                } else {
                    abortMarkup(XMLMessages.MSG_ENCODINGDECL_REQUIRED,
                                XMLMessages.P77_ENCODINGDECL_REQUIRED);
                    return;
                }
            } else {
                if (state == XMLDECL_START) {
                    if (!fEntityReader.skippedString(version_string)) {
                        abortMarkup(XMLMessages.MSG_VERSIONINFO_REQUIRED,
                                    XMLMessages.P23_VERSIONINFO_REQUIRED);
                        return;
                    }
                    state = XMLDECL_VERSION;
                } else {
                    if (state == XMLDECL_VERSION) {
                        if (fEntityReader.skippedString(encoding_string))
                            state = XMLDECL_ENCODING;
                        else
                            state = XMLDECL_STANDALONE;
                    } else
                        state = XMLDECL_STANDALONE;
                    if (state == XMLDECL_STANDALONE && !fEntityReader.skippedString(standalone_string))
                        break;
                }
            }
            int length = fEntityReader.currentOffset() - offset;
            fEntityReader.skipPastSpaces();
            if (!fEntityReader.lookingAtChar('=', true)) {
                int majorCode = scanningTextDecl ?
                                XMLMessages.MSG_EQ_REQUIRED_IN_TEXTDECL :
                                XMLMessages.MSG_EQ_REQUIRED_IN_XMLDECL;
                int minorCode = state == XMLDECL_VERSION ?
                                XMLMessages.P24_EQ_REQUIRED :
                                (state == XMLDECL_ENCODING ?
                                 XMLMessages.P80_EQ_REQUIRED :
                                 XMLMessages.P32_EQ_REQUIRED);
                abortMarkup(majorCode, minorCode, fEntityReader.addString(offset, length));
                return;
            }
            fEntityReader.skipPastSpaces();
            int result = fEntityReader.scanStringLiteral();
            switch (result) {
            case XMLEntityHandler.STRINGLIT_RESULT_QUOTE_REQUIRED:
            {
                int majorCode = scanningTextDecl ?
                                XMLMessages.MSG_QUOTE_REQUIRED_IN_TEXTDECL :
                                XMLMessages.MSG_QUOTE_REQUIRED_IN_XMLDECL;
                int minorCode = state == XMLDECL_VERSION ?
                                XMLMessages.P24_QUOTE_REQUIRED :
                                (state == XMLDECL_ENCODING ?
                                 XMLMessages.P80_QUOTE_REQUIRED :
                                 XMLMessages.P32_QUOTE_REQUIRED);
                abortMarkup(majorCode, minorCode, fEntityReader.addString(offset, length));
                return;
            }
            case XMLEntityHandler.STRINGLIT_RESULT_INVALID_CHAR:
                int invChar = fEntityReader.scanInvalidChar();
                if (fScannerState != SCANNER_STATE_END_OF_INPUT) {
                    if (invChar >= 0) {
                        int majorCode = scanningTextDecl ?
                                        XMLMessages.MSG_INVALID_CHAR_IN_TEXTDECL :
                                        XMLMessages.MSG_INVALID_CHAR_IN_XMLDECL;
                        int minorCode = state == XMLDECL_VERSION ?
                                        XMLMessages.P26_INVALID_CHARACTER :
                                        (state == XMLDECL_ENCODING ?
                                         XMLMessages.P81_INVALID_CHARACTER :
                                         XMLMessages.P32_INVALID_CHARACTER);
                        reportFatalXMLError(majorCode, minorCode, Integer.toHexString(invChar));
                    }
                    skipPastEndOfCurrentMarkup();
                }
                return;
            default:
                break;
            }
            switch (state) {
            case XMLDECL_VERSION:
                version = result;
                String versionString = fStringPool.toString(version);
                if (!"1.0".equals(versionString)) {
                    if (!validVersionNum(versionString)) {
                        abortMarkup(XMLMessages.MSG_VERSIONINFO_INVALID,
                                            XMLMessages.P26_INVALID_VALUE,
                                            versionString);
                        return;
                    }
                    Object[] args = { versionString };
                    fErrorReporter.reportError(fErrorReporter.getLocator(),
                                               XMLMessages.XML_DOMAIN,
                                               XMLMessages.MSG_VERSION_NOT_SUPPORTED,
                                               XMLMessages.P26_NOT_SUPPORTED,
                                               args,
                                               XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
                }
                if (!fEntityReader.lookingAtSpace(true)) {
                    if (scanningTextDecl) {
                        abortMarkup(XMLMessages.MSG_SPACE_REQUIRED_IN_TEXTDECL,
                                    XMLMessages.P80_WHITESPACE_REQUIRED);
                        return;
                    }
                    state = XMLDECL_FINISHED;
                }
                break;
            case XMLDECL_ENCODING:
                encoding = result;
                String encodingString = fStringPool.toString(encoding);
                if (!validEncName(encodingString)) {
                    abortMarkup(XMLMessages.MSG_ENCODINGDECL_INVALID,
                                XMLMessages.P81_INVALID_VALUE,
                                encodingString);
                    return;
                }
                if (!fEntityReader.lookingAtSpace(true)) {
                    state = XMLDECL_FINISHED;
                } else if (scanningTextDecl) {
                    fEntityReader.skipPastSpaces();
                    state = XMLDECL_FINISHED;
                }
                break;
            case XMLDECL_STANDALONE:
                standalone = result;
                String standaloneString = fStringPool.toString(standalone);
                boolean yes = "yes".equals(standaloneString);
                if (!yes && !"no".equals(standaloneString)) {
                    abortMarkup(XMLMessages.MSG_SDDECL_INVALID,
                                XMLMessages.P32_INVALID_VALUE,
                                standaloneString);
                    return;
                }
                fStandalone = yes;
                fEntityReader.skipPastSpaces();
                state = XMLDECL_FINISHED;
                break;
            }
        } while (state != XMLDECL_FINISHED);
        if (!fEntityReader.lookingAtChar('?', true) || !fEntityReader.lookingAtChar('>', true)) {
            int majorCode, minorCode;
            if (scanningTextDecl) {
                majorCode = XMLMessages.MSG_TEXTDECL_UNTERMINATED;
                minorCode = XMLMessages.P77_UNTERMINATED;
            } else {
                majorCode = XMLMessages.MSG_XMLDECL_UNTERMINATED;
                minorCode = XMLMessages.P23_UNTERMINATED;
            }
            abortMarkup(majorCode, minorCode);
            return;
        }
        fScannerMarkupDepth--;
        if (scanningTextDecl) {
            fEventHandler.callTextDecl(version, encoding);
        } else {
            fEventHandler.callXMLDecl(version, encoding, standalone);
            if (fStandalone) {
                fEventHandler.callStandaloneIsYes();
            }
        }
    }
    boolean scanElement(QName element) throws Exception
    {
        boolean greater = false;
        boolean slash = false;
        if (greater = fEntityReader.lookingAtChar('>', true)) {
        } else if (fEntityReader.lookingAtSpace(true)) {
            int previousState = setScannerState(SCANNER_STATE_ATTRIBUTE_LIST);
            while (true) {
                fEntityReader.skipPastSpaces();
                if ((greater = fEntityReader.lookingAtChar('>', true)) || (slash = fEntityReader.lookingAtChar('/', true)))
                    break;
                setScannerState(SCANNER_STATE_ATTRIBUTE_NAME);
                scanAttributeName(fEntityReader, element, fAttributeQName);
                if (fAttributeQName.rawname == -1) {
                    break;
                }
                fEntityReader.skipPastSpaces();
                if (!fEntityReader.lookingAtChar('=', true)) {
                    if (fScannerState != SCANNER_STATE_END_OF_INPUT) {
                        abortMarkup(XMLMessages.MSG_EQ_REQUIRED_IN_ATTRIBUTE,
                                    XMLMessages.P41_EQ_REQUIRED,
                                    element.rawname, fAttributeQName.rawname);
                        restoreScannerState(previousState);
                    }
                    return false;
                }
                fEntityReader.skipPastSpaces();
                int result = scanAttValue(element, fAttributeQName, false);
                if (result == RESULT_FAILURE) {
                    if (fScannerState != SCANNER_STATE_END_OF_INPUT) {
                        skipPastEndOfCurrentMarkup();
                        restoreScannerState(previousState);
                    }
                    return false;
                } else if (result == RESULT_DUPLICATE_ATTR) {
                    reportFatalXMLError(XMLMessages.MSG_ATTRIBUTE_NOT_UNIQUE,
                                        XMLMessages.WFC_UNIQUE_ATT_SPEC,
                                        element.rawname, fAttributeQName.rawname);
                }
                if ( fEventHandler.attribute(element, fAttributeQName, result) ) {
                    reportFatalXMLError(XMLMessages.MSG_ATTRIBUTE_NOT_UNIQUE,
                                        XMLMessages.WFC_UNIQUE_ATT_SPEC,
                                        element.rawname, fAttributeQName.rawname);
                }
                restoreScannerState(SCANNER_STATE_ATTRIBUTE_LIST);
                if (!fEntityReader.lookingAtSpace(true)) {
                    if (!(greater = fEntityReader.lookingAtChar('>', true)))
                        slash = fEntityReader.lookingAtChar('/', true);
                    break;
                }
            }
            restoreScannerState(previousState);
        } else {
            slash = fEntityReader.lookingAtChar('/', true);
        }
            if (fScannerState != SCANNER_STATE_END_OF_INPUT) {
                abortMarkup(XMLMessages.MSG_ELEMENT_UNTERMINATED,
                            XMLMessages.P40_UNTERMINATED,
                            element.rawname);
            }
            return false;
        }
        fEventHandler.callStartElement(element);
        fScannerMarkupDepth--;
            fEventHandler.callEndElement(fReaderId);
            return false;
        } else {
            return true;
        }
    }
    int scanCharRef() throws Exception {
        int valueOffset = fEntityReader.currentOffset();
        boolean hex = fEntityReader.lookingAtChar('x', true);
        int num = fEntityReader.scanCharRef(hex);
        if (num < 0) {
            switch (num) {
            case XMLEntityHandler.CHARREF_RESULT_SEMICOLON_REQUIRED:
                reportFatalXMLError(XMLMessages.MSG_SEMICOLON_REQUIRED_IN_CHARREF,
                                    XMLMessages.P66_SEMICOLON_REQUIRED);
                return -1;
            case XMLEntityHandler.CHARREF_RESULT_INVALID_CHAR:
                int majorCode = hex ? XMLMessages.MSG_HEXDIGIT_REQUIRED_IN_CHARREF :
                                      XMLMessages.MSG_DIGIT_REQUIRED_IN_CHARREF;
                int minorCode = hex ? XMLMessages.P66_HEXDIGIT_REQUIRED :
                                      XMLMessages.P66_DIGIT_REQUIRED;
                reportFatalXMLError(majorCode, minorCode);
                return -1;
            case XMLEntityHandler.CHARREF_RESULT_OUT_OF_RANGE:
                break;
            }
        }
        if (num < 0x20) {
            if (num == 0x09 || num == 0x0A || num == 0x0D) {
                return num;
            }
        } else if (num <= 0xD7FF || (num >= 0xE000 && (num <= 0xFFFD || (num >= 0x10000 && num <= 0x10FFFF)))) {
            return num;
        }
        int valueLength = fEntityReader.currentOffset() - valueOffset;
        reportFatalXMLError(XMLMessages.MSG_INVALID_CHARREF,
                            XMLMessages.WFC_LEGAL_CHARACTER,
                            fEntityReader.addString(valueOffset, valueLength));
        return -1;
    }
    void scanComment() throws Exception
    {
        int commentOffset = fEntityReader.currentOffset();
        boolean sawDashDash = false;
        int previousState = setScannerState(SCANNER_STATE_COMMENT);
        while (fScannerState == SCANNER_STATE_COMMENT) {
            if (fEntityReader.lookingAtChar('-', false)) {
                int nextEndOffset = fEntityReader.currentOffset();
                int endOffset = 0;
                fEntityReader.lookingAtChar('-', true);
                int offset = fEntityReader.currentOffset();
                int count = 1;
                while (fEntityReader.lookingAtChar('-', true)) {
                    count++;
                    endOffset = nextEndOffset;
                    nextEndOffset = offset;
                    offset = fEntityReader.currentOffset();
                }
                if (count > 1) {
                    if (fEntityReader.lookingAtChar('>', true)) {
                        if (!sawDashDash && count > 2) {
                            reportFatalXMLError(XMLMessages.MSG_DASH_DASH_IN_COMMENT,
                                                XMLMessages.P15_DASH_DASH);
                            sawDashDash = true;
                        }
                        fScannerMarkupDepth--;
                        fEventHandler.callComment(fEntityReader.addString(commentOffset, endOffset - commentOffset));
                        restoreScannerState(previousState);
                        return;
                    } else if (!sawDashDash) {
                        reportFatalXMLError(XMLMessages.MSG_DASH_DASH_IN_COMMENT,
                                            XMLMessages.P15_DASH_DASH);
                        sawDashDash = true;
                    }
                }
            } else {
                if (!fEntityReader.lookingAtValidChar(true)) {
                    int invChar = fEntityReader.scanInvalidChar();
                    if (fScannerState != SCANNER_STATE_END_OF_INPUT) {
                        if (invChar >= 0) {
                            reportFatalXMLError(XMLMessages.MSG_INVALID_CHAR_IN_COMMENT,
                                                XMLMessages.P15_INVALID_CHARACTER,
                                                Integer.toHexString(invChar));
                        }
                    }
                }
            }
        }
        restoreScannerState(previousState);
    }
    void scanPI(int piTarget) throws Exception
    {
        String piTargetString = fStringPool.toString(piTarget);
        if (piTargetString.length() == 3 &&
            (piTargetString.charAt(0) == 'X' || piTargetString.charAt(0) == 'x') &&
            (piTargetString.charAt(1) == 'M' || piTargetString.charAt(1) == 'm') &&
            (piTargetString.charAt(2) == 'L' || piTargetString.charAt(2) == 'l')) {
            abortMarkup(XMLMessages.MSG_RESERVED_PITARGET,
                        XMLMessages.P17_RESERVED_PITARGET);
            return;
        }
        int prevState = setScannerState(SCANNER_STATE_PI);
        int piDataOffset = -1;
        int piDataLength = -1;
        if (!fEntityReader.lookingAtSpace(true)) {
            if (!fEntityReader.lookingAtChar('?', true) || !fEntityReader.lookingAtChar('>', true)) {
                if (fScannerState != SCANNER_STATE_END_OF_INPUT) {
                    abortMarkup(XMLMessages.MSG_SPACE_REQUIRED_IN_PI,
                                XMLMessages.P16_WHITESPACE_REQUIRED);
                    restoreScannerState(prevState);
                }
                return;
            }
            piDataLength = 0;
        } else {
            fEntityReader.skipPastSpaces();
            piDataOffset = fEntityReader.currentOffset();
            while (fScannerState == SCANNER_STATE_PI) {
                while (fEntityReader.lookingAtChar('?', false)) {
                    int offset = fEntityReader.currentOffset();
                    fEntityReader.lookingAtChar('?', true);
                    if (fEntityReader.lookingAtChar('>', true)) {
                        piDataLength = offset - piDataOffset;
                        break;
                    }
                }
                if (piDataLength >= 0)
                    break;
                if (!fEntityReader.lookingAtValidChar(true)) {
                    int invChar = fEntityReader.scanInvalidChar();
                    if (fScannerState != SCANNER_STATE_END_OF_INPUT) {
                        if (invChar >= 0) {
                            reportFatalXMLError(XMLMessages.MSG_INVALID_CHAR_IN_PI,
                                                XMLMessages.P16_INVALID_CHARACTER,
                                                Integer.toHexString(invChar));
                        }
                        skipPastEndOfCurrentMarkup();
                        restoreScannerState(prevState);
                    }
                    return;
                }
            }
        }
        fScannerMarkupDepth--;
        restoreScannerState(prevState);
        int piData = piDataLength == 0 ?
                     StringPool.EMPTY_STRING : fEntityReader.addString(piDataOffset, piDataLength);
        fEventHandler.callProcessingInstruction(piTarget, piData);
    }

    /** Sets whether the parser preprocesses namespaces. */
    public void setNamespacesEnabled(boolean enabled) {
        fNamespacesEnabled = enabled;
    }

    /** Returns whether the parser processes namespaces. */
    public boolean getNamespacesEnabled() {
        return fNamespacesEnabled;
    }

    /** Sets whether the parser validates. */
    public void setValidationEnabled(boolean enabled) {
        fValidationEnabled = enabled;
        if (fDTDScanner != null) {
            fDTDScanner.setValidationEnabled(enabled);
        }
    }

    /** Returns true if validation is turned on. */
    public boolean getValidationEnabled() {
        return fValidationEnabled;
    }

    /** Sets whether the parser loads the external DTD. */
    public void setLoadExternalDTD(boolean enabled) {
        fLoadExternalDTD = enabled;
        if (fDTDScanner != null) {
            fDTDScanner.setLoadExternalDTD(enabled);
        }
    }

    /** Returns true if loading the external DTD is turned on. */
    public boolean getLoadExternalDTD() {
        return fLoadExternalDTD;
    }


    /** Scans element type. */
    private void scanElementType(XMLEntityHandler.EntityReader entityReader, 
                                char fastchar, QName element) throws Exception {

        if (!fNamespacesEnabled) {
            element.clear();
            element.localpart = entityReader.scanName(fastchar);
            element.rawname = element.localpart;
        } 
        else {
            entityReader.scanQName(fastchar, element);
            if (entityReader.lookingAtChar(':', false)) {
                fErrorReporter.reportError(fErrorReporter.getLocator(),
                                           XMLMessages.XML_DOMAIN,
                                           XMLMessages.MSG_TWO_COLONS_IN_QNAME,
                                           XMLMessages.P5_INVALID_CHARACTER,
                                           null,
                                           XMLErrorReporter.ERRORTYPE_FATAL_ERROR);
                 entityReader.skipPastNmtoken(' ');
            }
        }

        fEventHandler.element(element);


    /** Scans expected element type. */
    private boolean scanExpectedElementType(XMLEntityHandler.EntityReader entityReader, 
                                           char fastchar, int elementType) 
        throws Exception {

        /***/
        if (fCurrentElementCharArrayRange == null) {
            fCurrentElementCharArrayRange = fStringPool.createCharArrayRange();
        }
        fStringPool.getCharArrayRange(elementType, fCurrentElementCharArrayRange);
        return entityReader.scanExpectedName(fastchar, fCurrentElementCharArrayRange);
        /***
        entityReader.scanQName(fastchar, element);
        return true;
        /***/


    /** Scans attribute name. */
    private void scanAttributeName(XMLEntityHandler.EntityReader entityReader, 
                                  QName element, QName attribute) 
        throws Exception {

        /***
        if (!fSeenRootElement) {
            fSeenRootElement = true;
            rootElementSpecified(element);
            fStringPool.resetShuffleCount();
        }
        /***/

        if (!fNamespacesEnabled) {
            attribute.clear();
            attribute.localpart = entityReader.scanName('=');
            attribute.rawname = attribute.localpart;
        } 
        else {
            entityReader.scanQName('=', attribute);
            if (entityReader.lookingAtChar(':', false)) {
                fErrorReporter.reportError(fErrorReporter.getLocator(),
                                           XMLMessages.XML_DOMAIN,
                                           XMLMessages.MSG_TWO_COLONS_IN_QNAME,
                                           XMLMessages.P5_INVALID_CHARACTER,
                                           null,
                                           XMLErrorReporter.ERRORTYPE_FATAL_ERROR);
                entityReader.skipPastNmtoken(' ');
            }
        }


    /** Scan doctype declaration. */
    private void scanDoctypeDecl(boolean standalone) throws Exception {
        
        fScanningDTD = true;

        /***
        fScanningDTD = true;
        fCheckedForSchema = true;
        /***/
        fSeenDoctypeDecl = true;
        /***
        fStandaloneReader = standalone ? fEntityHandler.getReaderId() : -1;
        fDeclsAreExternal = false;
        if (fDTDImporter == null) {
            fDTDImporter = new DTDImporter(fStringPool, fErrorReporter, fEntityHandler, this);
        } 
        else {
            fDTDImporter.reset(fStringPool);
        }
        fDTDImporter.initHandlers(fDTDHandler);
        fDTDImporter.setValidating(fValidating);
        fDTDImporter.setNamespacesEnabled(fNamespacesEnabled);
        if (fDTDImporter.scanDoctypeDecl(standalone) && fValidating) {
            if (fWarningOnUndeclaredElements) {
            }

            fEntityHandler.checkRequiredNotations();
        }
        fScanningDTD = false;
        /***/
        if (fDTDScanner == null) {
            fDTDScanner = new XMLDTDScanner(fStringPool, fErrorReporter, fEntityHandler, new ChunkyCharArray(fStringPool));
            fDTDScanner.setValidationEnabled(fValidationEnabled);
            fDTDScanner.setNamespacesEnabled(fNamespacesEnabled);
            fDTDScanner.setLoadExternalDTD(fLoadExternalDTD);
        }
        else {
            fDTDScanner.reset(fStringPool, new ChunkyCharArray(fStringPool));
        }
        fDTDScanner.setDTDHandler(fDTDHandler);
        fDTDScanner.setGrammarResolver(fGrammarResolver);
        if (fDTDScanner.scanDoctypeDecl()) {
            if (fDTDScanner.getReadingExternalEntity()) {
                fDTDScanner.scanDecls(true);
            }
        }
        if (fValidationEnabled) {
            ((DefaultEntityHandler)fEntityHandler).checkRequiredNotations();
        }
        /***/
        fScanningDTD = false;


    /** Scan attribute value. */
    private int scanAttValue(QName element, QName attribute) throws Exception {

        int attValue = scanAttValue(element, attribute, fValidationEnabled);
        if (attValue == -1) {
            return XMLDocumentScanner.RESULT_FAILURE;
        }


        /***
        if (!fValidating && fAttDefCount == 0) {
            int attType = fCDATASymbol;
            if (fAttrListHandle == -1)
                fAttrListHandle = fAttrList.startAttrList();
            if (fAttrList.addAttr(attribute, attValue, attType, true, true) == -1) {
                return XMLDocumentScanner.RESULT_DUPLICATE_ATTR;
            }
            return XMLDocumentScanner.RESULT_SUCCESS;
        }
        /****/

        /****
        int attDefIndex = getAttDef(element, attribute);
        if (attDefIndex == -1) {

            if (fValidating) {
                Object[] args = { fStringPool.toString(element.rawname),
                                  fStringPool.toString(attribute.rawname) };
                fErrorReporter.reportError(fAttrNameLocator,
                                           XMLMessages.XML_DOMAIN,
                                           XMLMessages.MSG_ATTRIBUTE_NOT_DECLARED,
                                           XMLMessages.VC_ATTRIBUTE_VALUE_TYPE,
                                           args,
                                           XMLErrorReporter.ERRORTYPE_RECOVERABLE_ERROR);
            }

            int attType = fCDATASymbol;
            if (fAttrListHandle == -1) {
                fAttrListHandle = fAttrList.startAttrList();
            }
            if (fAttrList.addAttr(attribute, attValue, attType, true, true) == -1) {
                return XMLDocumentScanner.RESULT_DUPLICATE_ATTR;
            }
            return XMLDocumentScanner.RESULT_SUCCESS;
        }
        /****/

        /****
        int attType = getAttType(attDefIndex);
        if (attType != fCDATASymbol) {
            AttributeValidator av = getAttributeValidator(attDefIndex);
            int enumHandle = getEnumeration(attDefIndex);
            attValue = av.normalize(element, attribute, 
                                    attValue, attType, enumHandle);
        }

        if (fAttrListHandle == -1) {
            fAttrListHandle = fAttrList.startAttrList();
        }
        if (fAttrList.addAttr(attribute, attValue, attType, true, true) == -1) {
            return XMLDocumentScanner.RESULT_DUPLICATE_ATTR;
        }
        /***/

        return XMLDocumentScanner.RESULT_SUCCESS;


    /** Returns true if the version number is valid. */
    private boolean validVersionNum(String version) {
        return XMLCharacterProperties.validVersionNum(version);
    }

    /** Returns true if the encoding name is valid. */
    private boolean validEncName(String encoding) {
        return XMLCharacterProperties.validEncName(encoding);
    }

