package org.apache.xerces.readers;

import org.apache.xerces.framework.XMLErrorReporter;

/**
 * This is the class used by the scanner to process the XML data.
 *
 * @see org.apache.xerces.framework.XMLParser
 * @version $Id: XMLEntityReader.java 317440 2001-08-08 17:46:58Z neilg $
 */
abstract class XMLEntityReader implements XMLEntityHandler.EntityReader {
    /*
     * Instance variables.
     */
    protected XMLEntityHandler fEntityHandler = null;
    protected XMLErrorReporter fErrorReporter = null;
    protected boolean fSendCharDataAsCharArray;
    protected XMLEntityHandler.CharDataHandler fCharDataHandler = null;
    protected boolean fInCDSect = false;
    private boolean fStillActive = true;
    /*
     * These are updated directly by the subclass implementation.
     */
    protected int fCarriageReturnCounter = 1;
    protected int fLinefeedCounter = 1;
    protected int fCharacterCounter = 1;
    protected int fCurrentOffset = 0;
    /**
     * Constructor
     */
    protected XMLEntityReader(XMLEntityHandler entityHandler, XMLErrorReporter errorReporter, boolean sendCharDataAsCharArray) {
        fEntityHandler = entityHandler;
        fErrorReporter = errorReporter;
        fSendCharDataAsCharArray = sendCharDataAsCharArray;
        fCharDataHandler = fEntityHandler.getCharDataHandler();
    }
    /**
     * Constructor
     */
    protected XMLEntityReader(XMLEntityHandler entityHandler, XMLErrorReporter errorReporter, boolean sendCharDataAsCharArray, int lineNumber, int columnNumber) {
        fEntityHandler = entityHandler;
        fErrorReporter = errorReporter;
        fSendCharDataAsCharArray = sendCharDataAsCharArray;
        fCharDataHandler = fEntityHandler.getCharDataHandler();
        fLinefeedCounter = lineNumber;
        fCharacterCounter = columnNumber;
    }
    protected void init(XMLEntityHandler entityHandler, XMLErrorReporter errorReporter, boolean sendCharDataAsCharArray, int lineNumber, int columnNumber) {
        fEntityHandler = entityHandler;
        fErrorReporter = errorReporter;
        fSendCharDataAsCharArray = sendCharDataAsCharArray;
        fCharDataHandler = fEntityHandler.getCharDataHandler();
        fLinefeedCounter = lineNumber;
        fCharacterCounter = columnNumber;
        fStillActive = true;
        fInCDSect = false;
        fCarriageReturnCounter = 1;
        fCurrentOffset = 0;
    }

    /**
     * Return the current offset within this reader.
     *
     * @return The offset.
     */
    public int currentOffset() {
        return fCurrentOffset;
    }

    /**
     * Return the line number of the current position within the document that we are processing.
     *
     * @return The current line number.
     */
    public int getLineNumber() {
        if (fLinefeedCounter > 1)
            return fLinefeedCounter;
        else
            return fCarriageReturnCounter;
    }

    /**
     * Return the column number of the current position within the document that we are processing.
     *
     * @return The current column number.
     */
    public int getColumnNumber() {
        return fCharacterCounter;
    }

    /**
     * This method is provided for scanner implementations.
     */
    public void setInCDSect(boolean inCDSect) {
        fInCDSect = inCDSect;
    }

    /**
     * This method is provided for scanner implementations.
     */
    public boolean getInCDSect() {
        return fInCDSect;
    }

    /**
     * This method is called by the reader subclasses at the end of input.
     */
    protected XMLEntityHandler.EntityReader changeReaders() throws Exception {
        XMLEntityHandler.EntityReader nextReader = null;
        if (fStillActive) {
            nextReader = fEntityHandler.changeReaders();
            fStillActive = false;
            fEntityHandler = null;
            fErrorReporter = null;
            fCharDataHandler = null;
        }
        return nextReader;
    }
}
