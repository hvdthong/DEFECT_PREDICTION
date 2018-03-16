package org.apache.xerces.readers;

import org.apache.xerces.framework.XMLErrorReporter;
import org.apache.xerces.utils.QName;
import org.apache.xerces.utils.StringPool;
import org.apache.xerces.utils.SymbolCache;
import org.apache.xerces.utils.UTF8DataChunk;
import org.apache.xerces.utils.XMLCharacterProperties;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.LocatorImpl;
import java.io.InputStream;
import java.util.Vector;

/**
 * This is the primary reader used for UTF-8 encoded byte streams.
 * <p>
 * This reader processes requests from the scanners against the
 * underlying UTF-8 byte stream, avoiding when possible any up-front
 * transcoding.  When the StringPool handle interfaces are used,
 * the information in the data stream will be added to the string
 * pool and lazy-evaluated until asked for.
 * <p>
 * We use the SymbolCache to match expected names (element types in
 * end tags) and walk the data structures of that class directly.
 * <p>
 * There is a significant amount of hand-inlining and some blatant
 * voilation of good object oriented programming rules, ignoring
 * boundaries of modularity, etc., in the name of good performance.
 * <p>
 * There are also some places where the code here frequently crashes
 * the SUN java runtime compiler (JIT) and the code here has been
 * carefully "crafted" to avoid those problems.
 * 
 * @version $Id: UTF8Reader.java 316556 2000-11-09 21:51:36Z jeffreyr $
 */
final class UTF8Reader extends XMLEntityReader {
    private final static boolean USE_OUT_OF_LINE_LOAD_NEXT_BYTE = false;
    private final static boolean USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE = true;
    public UTF8Reader(XMLEntityHandler entityHandler, XMLErrorReporter errorReporter, boolean sendCharDataAsCharArray, InputStream dataStream, StringPool stringPool) throws Exception {
        super(entityHandler, errorReporter, sendCharDataAsCharArray);
        fInputStream = dataStream;
        fStringPool = stringPool;
        fCharArrayRange = fStringPool.createCharArrayRange();
        fCurrentChunk = UTF8DataChunk.createChunk(fStringPool, null);
        fillCurrentChunk();
    }
    /**
     *
     */
    public int addString(int offset, int length) {
        if (length == 0)
            return 0;
        return fCurrentChunk.addString(offset, length);
    }
    /**
     *
     */
    public int addSymbol(int offset, int length) {
        if (length == 0)
            return 0;
        return fCurrentChunk.addSymbol(offset, length, 0);
    }
    /**
     *
     */
    private int addSymbol(int offset, int length, int hashcode) {
        if (length == 0)
            return 0;
        return fCurrentChunk.addSymbol(offset, length, hashcode);
    }
    /**
     *
     */
    public void append(XMLEntityHandler.CharBuffer charBuffer, int offset, int length) {
        fCurrentChunk.append(charBuffer, offset, length);
    }
    private int slowLoadNextByte()  throws Exception {
        fCallClearPreviousChunk = true;
        if (fCurrentChunk.nextChunk() != null) {
            fCurrentChunk = fCurrentChunk.nextChunk();
            fCurrentIndex = 0;
            fMostRecentData = fCurrentChunk.toByteArray();
            return(fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
        } else {
            fCurrentChunk = UTF8DataChunk.createChunk(fStringPool, fCurrentChunk);
            return fillCurrentChunk();
        }
    }
    private int loadNextByte() throws Exception {
        fCurrentOffset++;
        if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
            fCurrentIndex++;
            try {
                fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                return fMostRecentByte;
            } catch (ArrayIndexOutOfBoundsException ex) {
                return slowLoadNextByte();
            }
        } else {
            if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                return slowLoadNextByte();
            else
                return(fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
        }
    }
    private boolean atEOF(int offset) {
        return(offset > fLength);
    }
    public XMLEntityHandler.EntityReader changeReaders() throws Exception {
        XMLEntityHandler.EntityReader nextReader = super.changeReaders();
        fCurrentChunk.releaseChunk();
        fCurrentChunk = null;
        fMostRecentData = null;
        fMostRecentByte = 0;
        return nextReader;
    }
    public boolean lookingAtChar(char ch, boolean skipPastChar) throws Exception {
        int b0 = fMostRecentByte;
        if (b0 != ch) {
            if (b0 == 0) {
                if (atEOF(fCurrentOffset + 1)) {
                    return changeReaders().lookingAtChar(ch, skipPastChar);
                }
            }
            if (ch == 0x0A && b0 == 0x0D) {
                if (skipPastChar) {
                    fCarriageReturnCounter++;
                    fCharacterCounter = 1;
                    if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                        b0 = loadNextByte();
                    } else {
                        fCurrentOffset++;
                        if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                            fCurrentIndex++;
                            try {
                                fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                                b0 = fMostRecentByte;
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                b0 = slowLoadNextByte();
                            }
                        } else {
                            if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                b0 = slowLoadNextByte();
                            else
                                b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                        }
                    }
                    if (b0 == 0x0A) {
                        fLinefeedCounter++;
                        if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                            loadNextByte();
                        } else {
                            fCurrentOffset++;
                            if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                                fCurrentIndex++;
                                try {
                                    fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                                } catch (ArrayIndexOutOfBoundsException ex) {
                                    slowLoadNextByte();
                                }
                            } else {
                                if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                    slowLoadNextByte();
                                else
                                    fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                            }
                        }
                    }
                }
                return true;
            }
            return false;
        }
        if (ch == 0x0D)
            return false;
        if (skipPastChar) {
            fCharacterCounter++;
            if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                loadNextByte();
            } else {
                fCurrentOffset++;
                if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                    fCurrentIndex++;
                    try {
                        fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        slowLoadNextByte();
                    }
                } else {
                    if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                        slowLoadNextByte();
                    else
                        fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                }
            }
        }
        return true;
    }
    public boolean lookingAtValidChar(boolean skipPastChar) throws Exception {
        int b0 = fMostRecentByte;
            if (b0 >= 0x20 || b0 == 0x09) {
                if (skipPastChar) {
                    fCharacterCounter++;
                    if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                        loadNextByte();
                    } else {
                        fCurrentOffset++;
                        if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                            fCurrentIndex++;
                            try {
                                fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                slowLoadNextByte();
                            }
                        } else {
                            if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                slowLoadNextByte();
                            else
                                fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                        }
                    }
                }
                return true;
            }
            if (b0 == 0x0A) {
                if (skipPastChar) {
                    fLinefeedCounter++;
                    fCharacterCounter = 1;
                    if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                        loadNextByte();
                    } else {
                        fCurrentOffset++;
                        if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                            fCurrentIndex++;
                            try {
                                fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                slowLoadNextByte();
                            }
                        } else {
                            if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                slowLoadNextByte();
                            else
                                fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                        }
                    }
                }
                return true;
            }
            if (b0 == 0x0D) {
                if (skipPastChar) {
                    fCarriageReturnCounter++;
                    fCharacterCounter = 1;
                    if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                        b0 = loadNextByte();
                    } else {
                        fCurrentOffset++;
                        if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                            fCurrentIndex++;
                            try {
                                fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                                b0 = fMostRecentByte;
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                b0 = slowLoadNextByte();
                            }
                        } else {
                            if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                b0 = slowLoadNextByte();
                            else
                                b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                        }
                    }
                    if (b0 == 0x0A) {
                        fLinefeedCounter++;
                        if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                            loadNextByte();
                        } else {
                            fCurrentOffset++;
                            if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                                fCurrentIndex++;
                                try {
                                    fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                                } catch (ArrayIndexOutOfBoundsException ex) {
                                    slowLoadNextByte();
                                }
                            } else {
                                if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                    slowLoadNextByte();
                                else
                                    fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                            }
                        }
                    }
                }
                return true;
            }
            if (b0 == 0) {
                if (atEOF(fCurrentOffset + 1)) {
                    return changeReaders().lookingAtValidChar(skipPastChar);
                }
            }
            return false;
        }
        UTF8DataChunk saveChunk = fCurrentChunk;
        int saveIndex = fCurrentIndex;
        int saveOffset = fCurrentOffset;
        int b1 = loadNextByte();
            if (skipPastChar) {
                fCharacterCounter++;
                loadNextByte();
            } else {
                fCurrentChunk = saveChunk;
                fCurrentIndex = saveIndex;
                fCurrentOffset = saveOffset;
                fMostRecentData = saveChunk.toByteArray();
                fMostRecentByte = b0;
            }
        }
        int b2 = loadNextByte();
            boolean result = false;
                if (skipPastChar) {
                    fCharacterCounter++;
                    loadNextByte();
                    return true;
                }
                result = true;
            }
            fCurrentChunk = saveChunk;
            fCurrentIndex = saveIndex;
            fCurrentOffset = saveOffset;
            fMostRecentData = saveChunk.toByteArray();
            fMostRecentByte = b0;
            return result;
        }
        boolean result = false;

        if ( ((b0&0xf8) == 0xf0) && ((b1&0xc0)==0x80) &&
             ((b2&0xc0) == 0x80) && ((b3&0xc0)==0x80)){

                if (skipPastChar) {
                    fCharacterCounter++;
                    loadNextByte();
                    return true;
                }
                result = true;
            }
            fCurrentChunk = saveChunk;
            fCurrentIndex = saveIndex;
            fCurrentOffset = saveOffset;
            fMostRecentData = saveChunk.toByteArray();
            fMostRecentByte = b0;
            return result;
        } else{
            fCurrentChunk = saveChunk;
            fCurrentIndex = saveIndex;
            fCurrentOffset = saveOffset;
            fMostRecentData = saveChunk.toByteArray();
            fMostRecentByte = b0;
            return result;
        }
    }
    public boolean lookingAtSpace(boolean skipPastChar) throws Exception {
        int ch = fMostRecentByte;
        if (ch > 0x20)
            return false;
        if (ch == 0x20 || ch == 0x09) {
            if (!skipPastChar)
                return true;
            fCharacterCounter++;
        } else if (ch == 0x0A) {
            if (!skipPastChar)
                return true;
            fLinefeedCounter++;
            fCharacterCounter = 1;
        } else if (ch == 0x0D) {
            if (!skipPastChar)
                return true;
            fCarriageReturnCounter++;
            fCharacterCounter = 1;
            if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                ch = loadNextByte();
            } else {
                fCurrentOffset++;
                if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                    fCurrentIndex++;
                    try {
                        fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                        ch = fMostRecentByte;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        ch = slowLoadNextByte();
                    }
                } else {
                    if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                        ch = slowLoadNextByte();
                    else
                        ch = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                }
            }
            if (ch != 0x0A)
                return true;
            fLinefeedCounter++;
        } else {
                if (atEOF(fCurrentOffset + 1)) {
                    return changeReaders().lookingAtSpace(skipPastChar);
                }
            }
            return false;
        }
        if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
            loadNextByte();
        } else {
            fCurrentOffset++;
            if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                fCurrentIndex++;
                try {
                    fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    slowLoadNextByte();
                }
            } else {
                if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                    slowLoadNextByte();
                else
                    fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
            }
        }
        return true;
    }
    public void skipToChar(char ch) throws Exception {
        int b0 = fMostRecentByte;
        while (true) {
                return;
            if (b0 == 0) {
                if (atEOF(fCurrentOffset + 1)) {
                    changeReaders().skipToChar(ch);
                    return;
                }
                fCharacterCounter++;
            } else if (b0 == 0x0A) {
                fLinefeedCounter++;
                fCharacterCounter = 1;
            } else if (b0 == 0x0D) {
                fCarriageReturnCounter++;
                fCharacterCounter = 1;
                b0 = loadNextByte();
                if (b0 != 0x0A)
                    continue;
                fLinefeedCounter++;
                fCharacterCounter++;
            } else {
                fCharacterCounter++;
                    loadNextByte();
                    loadNextByte();
                    loadNextByte();
                    loadNextByte();
                    loadNextByte();
                    loadNextByte();
                }
            }
            b0 = loadNextByte();
        }
    }
    public void skipPastSpaces() throws Exception {
        int ch = fMostRecentByte;
        while (true) {
            if (ch == 0x20 || ch == 0x09) {
                fCharacterCounter++;
            } else if (ch == 0x0A) {
                fLinefeedCounter++;
                fCharacterCounter = 1;
            } else if (ch == 0x0D) {
                fCarriageReturnCounter++;
                fCharacterCounter = 1;
                if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                    ch = loadNextByte();
                } else {
                    fCurrentOffset++;
                    if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                        fCurrentIndex++;
                        try {
                            fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                            ch = fMostRecentByte;
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            ch = slowLoadNextByte();
                        }
                    } else {
                        if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                            ch = slowLoadNextByte();
                        else
                            ch = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                    }
                }
                if (ch != 0x0A)
                    continue;
                fLinefeedCounter++;
            } else {
                if (ch == 0 && atEOF(fCurrentOffset + 1))
                    changeReaders().skipPastSpaces();
                return;
            }
            if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                ch = loadNextByte();
            } else {
                fCurrentOffset++;
                if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                    fCurrentIndex++;
                    try {
                        fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                        ch = fMostRecentByte;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        ch = slowLoadNextByte();
                    }
                } else {
                    if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                        ch = slowLoadNextByte();
                    else
                        ch = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                }
            }
        }
    }
    protected boolean skippedMultiByteCharWithFlag(int b0, int flag) throws Exception {
        UTF8DataChunk saveChunk = fCurrentChunk;
        int saveOffset = fCurrentOffset;
        int saveIndex = fCurrentIndex;
        if (!fCalledCharPropInit) {
            XMLCharacterProperties.initCharFlags();
            fCalledCharPropInit = true;
        }
        int b1 = loadNextByte();
                fCurrentChunk = saveChunk;
                fCurrentIndex = saveIndex;
                fCurrentOffset = saveOffset;
                fMostRecentData = saveChunk.toByteArray();
                fMostRecentByte = b0;
                return false;
            }
            return true;
        }
        int b2 = loadNextByte();
            if ((b0 == 0xED && b1 >= 0xA0) || (b0 == 0xEF && b1 == 0xBF && b2 >= 0xBE)) {
                fCurrentChunk = saveChunk;
                fCurrentIndex = saveIndex;
                fCurrentOffset = saveOffset;
                fMostRecentData = saveChunk.toByteArray();
                fMostRecentByte = b0;
                return false;
            }
                fCurrentChunk = saveChunk;
                fCurrentIndex = saveIndex;
                fCurrentOffset = saveOffset;
                fMostRecentData = saveChunk.toByteArray();
                fMostRecentByte = b0;
                return false;
            }
            return true;
            fCurrentChunk = saveChunk;
            fCurrentIndex = saveIndex;
            fCurrentOffset = saveOffset;
            fMostRecentData = saveChunk.toByteArray();
            fMostRecentByte = b0;
            return false;
        }
    }
    public void skipPastName(char fastcheck) throws Exception {
        int b0 = fMostRecentByte;
        if (b0 < 0x80) {
            if (XMLCharacterProperties.fgAsciiInitialNameChar[b0] == 0)
                return;
        } else {
            if (!fCalledCharPropInit) {
                XMLCharacterProperties.initCharFlags();
                fCalledCharPropInit = true;
            }
            if (!skippedMultiByteCharWithFlag(b0, XMLCharacterProperties.E_InitialNameCharFlag))
                return;
        }
        while (true) {
            fCharacterCounter++;
            b0 = loadNextByte();
            if (fastcheck == b0)
                return;
            if (b0 < 0x80) {
                if (XMLCharacterProperties.fgAsciiNameChar[b0] == 0)
                    return;
            } else {
                if (!fCalledCharPropInit) {
                    XMLCharacterProperties.initCharFlags();
                    fCalledCharPropInit = true;
                }
                if (!skippedMultiByteCharWithFlag(b0, XMLCharacterProperties.E_NameCharFlag))
                    return;
            }
        }
    }
    public void skipPastNmtoken(char fastcheck) throws Exception {
        int b0 = fMostRecentByte;
        while (true) {
            if (fastcheck == b0)
                return;
            if (b0 < 0x80) {
                if (XMLCharacterProperties.fgAsciiNameChar[b0] == 0)
                    return;
            } else {
                if (!skippedMultiByteCharWithFlag(b0, XMLCharacterProperties.E_NameCharFlag))
                    return;
            }
            fCharacterCounter++;
            b0 = loadNextByte();
        }
    }
    public boolean skippedString(char[] s) throws Exception {
        int length = s.length;
        byte[] data = fMostRecentData;
        int index = fCurrentIndex + length;
        int sindex = length;
        try {
            while (sindex-- > 0) {
                if (data[--index] != s[sindex])
                    return false;
            }
            fCurrentIndex += length;
        } catch (ArrayIndexOutOfBoundsException ex) {
            int i = 0;
            index = fCurrentIndex;
            while (index < UTF8DataChunk.CHUNK_SIZE) {
                if (data[index++] != s[i++])
                    return false;
            }
            UTF8DataChunk dataChunk = fCurrentChunk;
            int savedOffset = fCurrentOffset;
            int savedIndex = fCurrentIndex;
            slowLoadNextByte();
            data = fMostRecentData;
            index = 0;
            while (i < length) {
                if (data[index++] != s[i++]) {
                    fCurrentChunk = dataChunk;
                    fCurrentIndex = savedIndex;
                    fCurrentOffset = savedOffset;
                    fMostRecentData = fCurrentChunk.toByteArray();
                    fMostRecentByte = fMostRecentData[savedIndex] & 0xFF;
                    return false;
                }
            }
            fCurrentIndex = index;
        }
        fCharacterCounter += length;
        fCurrentOffset += length;
        try {
            fMostRecentByte = data[fCurrentIndex] & 0xFF;
        } catch (ArrayIndexOutOfBoundsException ex) {
            slowLoadNextByte();
        }
        return true;
    }
    public int scanInvalidChar() throws Exception {
        int b0 = fMostRecentByte;
        int ch = b0;
        if (ch == 0x0A) {
            fLinefeedCounter++;
            fCharacterCounter = 1;
        } else if (ch == 0x0D) {
            fCarriageReturnCounter++;
            fCharacterCounter = 1;
            ch = loadNextByte();
            if (ch != 0x0A)
                return 0x0A;
            fLinefeedCounter++;
        } else if (ch == 0) {
            if (atEOF(fCurrentOffset + 1)) {
                return changeReaders().scanInvalidChar();
            }
            fCharacterCounter++;
        } else if (b0 >= 0x80) {
            fCharacterCounter++;
            int b1 = loadNextByte();
            int b2 = 0;
                ch = ((0x1f & b0)<<6) + (0x3f & b1);
            } else if ( (0xf0 & b0) == 0xe0 ) { 
                b2 = loadNextByte();
                ch = ((0x0f & b0)<<12) + ((0x3f & b1)<<6) + (0x3f & b2);
            } else if (( 0xf8 & b0 ) == 0xf0 ){
                b2 = loadNextByte();
                ch = ((0x0f & b0)<<18) + ((0x3f & b1)<<12)
                     + ((0x3f & b2)<<6) + (0x3f & b3);
            }
        }
        loadNextByte();
        return ch;
    }
    public int scanCharRef(boolean hex) throws Exception {
        int ch = fMostRecentByte;
        if (ch == 0) {
            if (atEOF(fCurrentOffset + 1)) {
                return changeReaders().scanCharRef(hex);
            }
            return XMLEntityHandler.CHARREF_RESULT_INVALID_CHAR;
        }
        int num = 0;
        if (hex) {
            if (ch > 'f' || XMLCharacterProperties.fgAsciiXDigitChar[ch] == 0)
                return XMLEntityHandler.CHARREF_RESULT_INVALID_CHAR;
            num = ch - (ch < 'A' ? '0' : (ch < 'a' ? 'A' : 'a') - 10);
        } else {
            if (ch < '0' || ch > '9')
                return XMLEntityHandler.CHARREF_RESULT_INVALID_CHAR;
            num = ch - '0';
        }
        fCharacterCounter++;
        loadNextByte();
        boolean toobig = false;
        while (true) {
            ch = fMostRecentByte;
            if (ch == 0)
                break;
            if (hex) {
                if (ch > 'f' || XMLCharacterProperties.fgAsciiXDigitChar[ch] == 0)
                    break;
            } else {
                if (ch < '0' || ch > '9')
                    break;
            }
            fCharacterCounter++;
            loadNextByte();
            if (hex) {
                int dig = ch - (ch < 'A' ? '0' : (ch < 'a' ? 'A' : 'a') - 10);
                num = (num << 4) + dig;
            } else {
                int dig = ch - '0';
                num = (num * 10) + dig;
            }
            if (num > 0x10FFFF) {
                toobig = true;
                num = 0;
            }
        }
        if (ch != ';')
            return XMLEntityHandler.CHARREF_RESULT_SEMICOLON_REQUIRED;
        fCharacterCounter++;
        loadNextByte();
        if (toobig)
            return XMLEntityHandler.CHARREF_RESULT_OUT_OF_RANGE;
        return num;
    }
    public int scanStringLiteral() throws Exception {
        boolean single;
        if (!(single = lookingAtChar('\'', true)) && !lookingAtChar('\"', true)) {
            return XMLEntityHandler.STRINGLIT_RESULT_QUOTE_REQUIRED;
        }
        int offset = fCurrentOffset;
        char qchar = single ? '\'' : '\"';
        while (!lookingAtChar(qchar, false)) {
            if (!lookingAtValidChar(true)) {
                return XMLEntityHandler.STRINGLIT_RESULT_INVALID_CHAR;
            }
        }
        int stringIndex = fCurrentChunk.addString(offset, fCurrentOffset - offset);
        return stringIndex;
    }
    public static final byte fgAsciiAttValueChar[] = {
        4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    public int scanAttValue(char qchar, boolean asSymbol) throws Exception
    {
        int offset = fCurrentOffset;
        int b0 = fMostRecentByte;
        while (true) {
            if (b0 < 0x80) {
                switch (fgAsciiAttValueChar[b0]) {
                    if (b0 == qchar) {
                        int length = fCurrentOffset - offset;
                        int result = length == 0 ? StringPool.EMPTY_STRING : (asSymbol ? fCurrentChunk.addSymbol(offset, length, 0) : fCurrentChunk.addString(offset, length));
                        fCharacterCounter++;
                        if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                            loadNextByte();
                        } else {
                            fCurrentOffset++;
                            if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                                fCurrentIndex++;
                                try {
                                    fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                                } catch (ArrayIndexOutOfBoundsException ex) {
                                    slowLoadNextByte();
                                }
                            } else {
                                if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                    slowLoadNextByte();
                                else
                                    fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                            }
                        }
                        return result;
                    }
                    fCharacterCounter++;
                    if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                        b0 = loadNextByte();
                    } else {
                        fCurrentOffset++;
                        if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                            fCurrentIndex++;
                            try {
                                b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                b0 = slowLoadNextByte();
                            }
                        } else {
                            if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                b0 = slowLoadNextByte();
                            else
                                b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                        }
                    }
                    continue;
                    return XMLEntityHandler.ATTVALUE_RESULT_COMPLEX;
                    return XMLEntityHandler.ATTVALUE_RESULT_LESSTHAN;
                    return XMLEntityHandler.ATTVALUE_RESULT_INVALID_CHAR;
                }
            } else {
                if (!skipMultiByteCharData(b0))
                    return XMLEntityHandler.ATTVALUE_RESULT_INVALID_CHAR;
                b0 = fMostRecentByte;
            }
        }
    }
    public static final byte fgAsciiEntityValueChar[] = {
        4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    public int scanEntityValue(int qchar, boolean createString) throws Exception
    {
        int offset = fCurrentOffset;
        int b0 = fMostRecentByte;
        while (true) {
            if (b0 < 0x80) {
                switch (fgAsciiEntityValueChar[b0]) {
                    if (b0 == qchar) {
                        if (!createString)
                            return XMLEntityHandler.ENTITYVALUE_RESULT_FINISHED;
                        int length = fCurrentOffset - offset;
                        int result = length == 0 ? StringPool.EMPTY_STRING : fCurrentChunk.addString(offset, length);
                        fCharacterCounter++;
                        if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                            loadNextByte();
                        } else {
                            fCurrentOffset++;
                            if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                                fCurrentIndex++;
                                try {
                                    fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                                } catch (ArrayIndexOutOfBoundsException ex) {
                                    slowLoadNextByte();
                                }
                            } else {
                                if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                    slowLoadNextByte();
                                else
                                    fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                            }
                        }
                        return result;
                    }
                    fCharacterCounter++;
                    if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                        b0 = loadNextByte();
                    } else {
                        fCurrentOffset++;
                        if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                            fCurrentIndex++;
                            try {
                                b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                b0 = slowLoadNextByte();
                            }
                        } else {
                            if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                b0 = slowLoadNextByte();
                            else
                                b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                        }
                    }
                    continue;
                    fLinefeedCounter++;
                    fCharacterCounter = 1;
                    if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                        b0 = loadNextByte();
                    } else {
                        fCurrentOffset++;
                        if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                            fCurrentIndex++;
                            try {
                                b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                b0 = slowLoadNextByte();
                            }
                        } else {
                            if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                b0 = slowLoadNextByte();
                            else
                                b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                        }
                    }
                    continue;
                    fCarriageReturnCounter++;
                    fCharacterCounter = 1;
                    if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                        b0 = loadNextByte();
                    } else {
                        fCurrentOffset++;
                        if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                            fCurrentIndex++;
                            try {
                                b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                b0 = slowLoadNextByte();
                            }
                        } else {
                            if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                b0 = slowLoadNextByte();
                            else
                                b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                        }
                    }
                    if (b0 != 0x0A) {
                        continue;
                    }
                    fLinefeedCounter++;
                    if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                        b0 = loadNextByte();
                    } else {
                        fCurrentOffset++;
                        if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                            fCurrentIndex++;
                            try {
                                b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                b0 = slowLoadNextByte();
                            }
                        } else {
                            if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                b0 = slowLoadNextByte();
                            else
                                b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                        }
                    }
                    continue;
                    return XMLEntityHandler.ENTITYVALUE_RESULT_REFERENCE;
                    return XMLEntityHandler.ENTITYVALUE_RESULT_PEREF;
                case 7:
                    if (atEOF(fCurrentOffset + 1)) {
                        return XMLEntityHandler.ENTITYVALUE_RESULT_END_OF_INPUT;
                    }
                    return XMLEntityHandler.ENTITYVALUE_RESULT_INVALID_CHAR;
                }
            } else {
                if (!skipMultiByteCharData(b0))
                    return XMLEntityHandler.ENTITYVALUE_RESULT_INVALID_CHAR;
                b0 = fMostRecentByte;
            }
        }
    }
    public boolean scanExpectedName(char fastcheck, StringPool.CharArrayRange expectedName) throws Exception {
        char[] expected = expectedName.chars;
        int offset = expectedName.offset;
        int len = expectedName.length;
        int b0 = fMostRecentByte;
        int ch = 0;
        int i = 0;
        while (true) {
            if (b0 < 0x80) {
                ch = b0;
                if (i == len)
                    break;
                if (ch != expected[offset]) {
                    skipPastNmtoken(fastcheck);
                    return false;
                }
            } else {
                UTF8DataChunk saveChunk = fCurrentChunk;
                int saveIndex = fCurrentIndex;
                int saveOffset = fCurrentOffset;
                int b1;
                if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                    b1 = loadNextByte();
                } else {
                    fCurrentOffset++;
                    if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                        fCurrentIndex++;
                        try {
                            b1 = fMostRecentData[fCurrentIndex] & 0xFF;
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            b1 = slowLoadNextByte();
                        }
                    } else {
                        if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                            b1 = slowLoadNextByte();
                        else
                            b1 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                    }
                }
                    ch = ((0x1f & b0)<<6) + (0x3f & b1);
                    if (i == len)
                        break;
                    if (ch != expected[offset]) {
                        fCurrentChunk = saveChunk;
                        fCurrentIndex = saveIndex;
                        fCurrentOffset = saveOffset;
                        fMostRecentData = saveChunk.toByteArray();
                        fMostRecentByte = b0;
                        skipPastNmtoken(fastcheck);
                        return false;
                    }
                } else {
                    int b2;
                    if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                        b2 = loadNextByte();
                    } else {
                        fCurrentOffset++;
                        if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                            fCurrentIndex++;
                            try {
                                b2 = fMostRecentData[fCurrentIndex] & 0xFF;
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                b2 = slowLoadNextByte();
                            }
                        } else {
                            if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                b2 = slowLoadNextByte();
                            else
                                b2 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                        }
                    }
                        if ((b0 == 0xED && b1 >= 0xA0) || (b0 == 0xEF && b1 == 0xBF && b2 >= 0xBE)) {
                            fCurrentChunk = saveChunk;
                            fCurrentIndex = saveIndex;
                            fCurrentOffset = saveOffset;
                            fMostRecentData = saveChunk.toByteArray();
                            fMostRecentByte = b0;
                            return false;
                        }
                        ch = ((0x0f & b0)<<12) + ((0x3f & b1)<<6) + (0x3f & b2);
                        if (i == len)
                            break;
                        if (ch != expected[offset]) {
                            fCurrentChunk = saveChunk;
                            fCurrentIndex = saveIndex;
                            fCurrentOffset = saveOffset;
                            fMostRecentData = saveChunk.toByteArray();
                            fMostRecentByte = b0;
                            skipPastNmtoken(fastcheck);
                            return false;
                        }
                        fCurrentChunk = saveChunk;
                        fCurrentIndex = saveIndex;
                        fCurrentOffset = saveOffset;
                        fMostRecentData = saveChunk.toByteArray();
                        fMostRecentByte = b0;
                        return false;
                    }
                }
            }
            i++;
            offset++;
            fCharacterCounter++;
            fCurrentOffset++;
            if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                fCurrentIndex++;
                try {
                    b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    b0 = slowLoadNextByte();
                }
            } else {
                if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                    b0 = slowLoadNextByte();
                else
                    b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
            }
        }
        if (ch == fastcheck)
            return true;
        if (ch < 0x80) {
            if (XMLCharacterProperties.fgAsciiNameChar[ch] == 0)
                return true;
        } else {
            if (!fCalledCharPropInit) {
                XMLCharacterProperties.initCharFlags();
                fCalledCharPropInit = true;
            }
            if ((XMLCharacterProperties.fgCharFlags[ch] & XMLCharacterProperties.E_NameCharFlag) == 0)
                return true;
        }
        skipPastNmtoken(fastcheck);
        return false;
    }

    public void scanQName(char fastcheck, QName qname) throws Exception {
        int offset = fCurrentOffset;
        int ch = fMostRecentByte;
        if (ch < 0x80) {
            if (XMLCharacterProperties.fgAsciiInitialNameChar[ch] == 0) {
                qname.clear();
                return;
            }
            if (ch == ':') {
                qname.clear();
                return;
            }
        } else {
            if (!fCalledCharPropInit) {
                XMLCharacterProperties.initCharFlags();
                fCalledCharPropInit = true;
            }
            ch = getMultiByteSymbolChar(ch);
            fCurrentIndex--;
            fCurrentOffset--;
            if ((XMLCharacterProperties.fgCharFlags[ch] & XMLCharacterProperties.E_InitialNameCharFlag) == 0) {
                qname.clear();
                return;
            }
        }
        int index = fCurrentIndex;
        byte[] data = fMostRecentData;
        int prefixend = -1;
        while (true) {
            fCharacterCounter++;
            fCurrentOffset++;
            index++;
            try {
                ch = data[index] & 0xFF;
            } catch (ArrayIndexOutOfBoundsException ex) {
                ch = slowLoadNextByte();
                index = 0;
                data = fMostRecentData;
            }
            if (fastcheck == ch)
                break;
            if (ch < 0x80) {
                if (XMLCharacterProperties.fgAsciiNameChar[ch] == 0)
                    break;
                if (ch == ':') {
                    if (prefixend != -1)
                        break;
                    prefixend = fCurrentOffset;
                    try {
                        ch = data[index + 1] & 0xFF;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        UTF8DataChunk savedChunk = fCurrentChunk;
                        int savedOffset = fCurrentOffset;
                        ch = slowLoadNextByte();
                        fCurrentChunk = savedChunk;
                        fCurrentOffset = savedOffset;
                        fMostRecentData = fCurrentChunk.toByteArray();
                    }
                    boolean lpok = true;
                    if (ch < 0x80) {
                        if (XMLCharacterProperties.fgAsciiInitialNameChar[ch] == 0 || ch == ':')
                            lpok = false;
                    } else {
                        if (!fCalledCharPropInit) {
                            XMLCharacterProperties.initCharFlags();
                            fCalledCharPropInit = true;
                        }
                        if ((XMLCharacterProperties.fgCharFlags[ch] & XMLCharacterProperties.E_InitialNameCharFlag) == 0)
                            lpok = false;
                    }
                    ch = ':';
                    if (!lpok) {
                        prefixend = -1;
                        break;
                    }
                }
            } else {
                if (!fCalledCharPropInit) {
                    XMLCharacterProperties.initCharFlags();
                    fCalledCharPropInit = true;
                }
                fCurrentIndex = index;
                fMostRecentByte = ch;
                ch = getMultiByteSymbolChar(ch);
                fCurrentIndex--;
                fCurrentOffset--;
                index = fCurrentIndex;
                if ((XMLCharacterProperties.fgCharFlags[ch] & XMLCharacterProperties.E_NameCharFlag) == 0)
                    break;
            }
        }
        fCurrentIndex = index;
        fMostRecentByte = ch;
        int length = fCurrentOffset - offset;
        qname.rawname = addSymbol(offset, length);
        qname.prefix = prefixend == -1 ? -1 : addSymbol(offset, prefixend - offset);
        qname.localpart = prefixend == -1 ? qname.rawname : addSymbol(prefixend + 1, fCurrentOffset - (prefixend + 1));
        qname.uri = -1;


    private int getMultiByteSymbolChar(int b0) throws Exception {
        UTF8DataChunk saveChunk = fCurrentChunk;
        int saveIndex = fCurrentIndex;
        int saveOffset = fCurrentOffset;
        if (!fCalledCharPropInit) {
            XMLCharacterProperties.initCharFlags();
            fCalledCharPropInit = true;
        }
        int b1;
        if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
            b1 = loadNextByte();
        } else {
            fCurrentOffset++;
            if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                fCurrentIndex++;
                try {
                    b1 = fMostRecentData[fCurrentIndex] & 0xFF;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    b1 = slowLoadNextByte();
                }
            } else {
                if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                    b1 = slowLoadNextByte();
                else
                    b1 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
            }
        }
            int ch = ((0x1f & b0)<<6) + (0x3f & b1);
                fCurrentChunk = saveChunk;
                fCurrentIndex = saveIndex;
                fCurrentOffset = saveOffset;
                fMostRecentData = saveChunk.toByteArray();
                fMostRecentByte = b0;
                return -1;
            }
            loadNextByte();
            return ch;
        }
        int b2;
        if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
            b2 = loadNextByte();
        } else {
            fCurrentOffset++;
            if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                fCurrentIndex++;
                try {
                    b2 = fMostRecentData[fCurrentIndex] & 0xFF;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    b2 = slowLoadNextByte();
                }
            } else {
                if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                    b2 = slowLoadNextByte();
                else
                    b2 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
            }
        }
            if ((b0 == 0xED && b1 >= 0xA0) || (b0 == 0xEF && b1 == 0xBF && b2 >= 0xBE)) {
                fCurrentChunk = saveChunk;
                fCurrentIndex = saveIndex;
                fCurrentOffset = saveOffset;
                fMostRecentData = saveChunk.toByteArray();
                fMostRecentByte = b0;
                return -1;
            }
            int ch = ((0x0f & b0)<<12) + ((0x3f & b1)<<6) + (0x3f & b2);
                fCurrentChunk = saveChunk;
                fCurrentIndex = saveIndex;
                fCurrentOffset = saveOffset;
                fMostRecentData = saveChunk.toByteArray();
                fMostRecentByte = b0;
                return -1;
            }
            loadNextByte();
            return ch;
        }
        fCurrentChunk = saveChunk;
        fCurrentIndex = saveIndex;
        fCurrentOffset = saveOffset;
        fMostRecentData = saveChunk.toByteArray();
        fMostRecentByte = b0;
        return -1;
    }
    public int scanName(char fastcheck) throws Exception {
        int b0 = fMostRecentByte;
        int ch;
        if (b0 < 0x80) {
            if (XMLCharacterProperties.fgAsciiInitialNameChar[b0] == 0) {
                if (b0 == 0 && atEOF(fCurrentOffset + 1)) {
                    return changeReaders().scanName(fastcheck);
                }
                return -1;
            }
            ch = b0;
        } else {
            UTF8DataChunk saveChunk = fCurrentChunk;
            int saveIndex = fCurrentIndex;
            int saveOffset = fCurrentOffset;
            if (!fCalledCharPropInit) {
                XMLCharacterProperties.initCharFlags();
                fCalledCharPropInit = true;
            }
            int b1;
            if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                b1 = loadNextByte();
            } else {
                fCurrentOffset++;
                if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                    fCurrentIndex++;
                    try {
                        b1 = fMostRecentData[fCurrentIndex] & 0xFF;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        b1 = slowLoadNextByte();
                    }
                } else {
                    if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                        b1 = slowLoadNextByte();
                    else
                        b1 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                }
            }
                ch = ((0x1f & b0)<<6) + (0x3f & b1);
                    fCurrentChunk = saveChunk;
                    fCurrentIndex = saveIndex;
                    fCurrentOffset = saveOffset;
                    fMostRecentData = saveChunk.toByteArray();
                    fMostRecentByte = b0;
                    return -1;
                }
            } else {
                int b2;
                if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                    b2 = loadNextByte();
                } else {
                    fCurrentOffset++;
                    if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                        fCurrentIndex++;
                        try {
                            b2 = fMostRecentData[fCurrentIndex] & 0xFF;
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            b2 = slowLoadNextByte();
                        }
                    } else {
                        if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                            b2 = slowLoadNextByte();
                        else
                            b2 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                    }
                }
                    if ((b0 == 0xED && b1 >= 0xA0) || (b0 == 0xEF && b1 == 0xBF && b2 >= 0xBE)) {
                        fCurrentChunk = saveChunk;
                        fCurrentIndex = saveIndex;
                        fCurrentOffset = saveOffset;
                        fMostRecentData = saveChunk.toByteArray();
                        fMostRecentByte = b0;
                        return -1;
                    }
                    ch = ((0x0f & b0)<<12) + ((0x3f & b1)<<6) + (0x3f & b2);
                        fCurrentChunk = saveChunk;
                        fCurrentIndex = saveIndex;
                        fCurrentOffset = saveOffset;
                        fMostRecentData = saveChunk.toByteArray();
                        fMostRecentByte = b0;
                        return -1;
                    }
                    fCurrentChunk = saveChunk;
                    fCurrentIndex = saveIndex;
                    fCurrentOffset = saveOffset;
                    fMostRecentData = saveChunk.toByteArray();
                    fMostRecentByte = b0;
                    return -1;
                }
            }
        }
        fCharacterCounter++;
        if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
            b0 = loadNextByte();
        } else {
            fCurrentOffset++;
            if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                fCurrentIndex++;
                try {
                    b0 = fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    b0 = slowLoadNextByte();
                }
            } else {
                if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                    b0 = slowLoadNextByte();
                else
                    b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
            }
        }
        return scanMatchingName(ch, b0, fastcheck);
    }
    private int scanMatchingName(int ch, int b0, int fastcheck) throws Exception {
        SymbolCache cache = fStringPool.getSymbolCache();
        int[][] cacheLines = cache.fCacheLines;
        char[] symbolChars = cache.fSymbolChars;
        boolean lengthOfOne = fastcheck == fMostRecentByte;
        int startOffset = cache.fSymbolCharsOffset;
        int entry = 0;
        int[] entries = cacheLines[entry];
        int offset = 1 + ((entries[0] - 1) * SymbolCache.CACHE_RECORD_SIZE);
        int totalMisses = 0;
        if (lengthOfOne) {
            while (offset > 0) {
                if (entries[offset + SymbolCache.CHAR_OFFSET] == ch) {
                    if (entries[offset + SymbolCache.INDEX_OFFSET] != -1) {
                        int symbolIndex = entries[offset + SymbolCache.INDEX_OFFSET];
                        if (totalMisses > 3)
                            fStringPool.updateCacheLine(symbolIndex, totalMisses, 1);
                        return symbolIndex;
                    }
                    break;
                }
                offset -= SymbolCache.CACHE_RECORD_SIZE;
                totalMisses++;
            }
            try {
                symbolChars[cache.fSymbolCharsOffset] = (char)ch;
            } catch (ArrayIndexOutOfBoundsException ex) {
                symbolChars = new char[cache.fSymbolCharsOffset * 2];
                System.arraycopy(cache.fSymbolChars, 0, symbolChars, 0, cache.fSymbolCharsOffset);
                cache.fSymbolChars = symbolChars;
                symbolChars[cache.fSymbolCharsOffset] = (char)ch;
            }
            cache.fSymbolCharsOffset++;
            if (offset < 0) {
                offset = 1 + (entries[0] * SymbolCache.CACHE_RECORD_SIZE);
                entries[0]++;
                try {
                    entries[offset + SymbolCache.CHAR_OFFSET] = ch;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    int newSize = 1 + ((offset - 1) * 2);
                    entries = new int[newSize];
                    System.arraycopy(cacheLines[entry], 0, entries, 0, offset);
                    cacheLines[entry] = entries;
                    entries[offset + SymbolCache.CHAR_OFFSET] = ch;
                }
                entries[offset + SymbolCache.NEXT_OFFSET] = -1;
            }
            int result = fStringPool.createNonMatchingSymbol(startOffset, entry, entries, offset);
            return result;
        }
        try {
            symbolChars[cache.fSymbolCharsOffset] = (char)ch;
        } catch (ArrayIndexOutOfBoundsException ex) {
            symbolChars = new char[cache.fSymbolCharsOffset * 2];
            System.arraycopy(cache.fSymbolChars, 0, symbolChars, 0, cache.fSymbolCharsOffset);
            cache.fSymbolChars = symbolChars;
            symbolChars[cache.fSymbolCharsOffset] = (char)ch;
        }
        cache.fSymbolCharsOffset++;
        int depth = 1;
        while (true) {
            if (offset < 0)
                break;
            if (entries[offset + SymbolCache.CHAR_OFFSET] != ch) {
                offset -= SymbolCache.CACHE_RECORD_SIZE;
                totalMisses++;
                continue;
            }
            if (b0 >= 0x80) {
                ch = getMultiByteSymbolChar(b0);
                b0 = fMostRecentByte;
            } else if (b0 == fastcheck || XMLCharacterProperties.fgAsciiNameChar[b0] == 0) {
                ch = -1;
            } else {
                ch = b0;
                fCharacterCounter++;
                if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                    b0 = loadNextByte();
                } else {
                    fCurrentOffset++;
                    if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                        fCurrentIndex++;
                        try {
                            b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            b0 = slowLoadNextByte();
                        }
                    } else {
                        if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                            b0 = slowLoadNextByte();
                        else
                            b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                    }
                }
            }
            if (ch == -1) {
                if (entries[offset + SymbolCache.INDEX_OFFSET] == -1) {
                    return fStringPool.createNonMatchingSymbol(startOffset, entry, entries, offset);
                }
                cache.fSymbolCharsOffset = startOffset;
                int symbolIndex = entries[offset + SymbolCache.INDEX_OFFSET];
                if (totalMisses > (depth * 3))
                    fStringPool.updateCacheLine(symbolIndex, totalMisses, depth);
                return symbolIndex;
            }
            try {
                symbolChars[cache.fSymbolCharsOffset] = (char)ch;
            } catch (ArrayIndexOutOfBoundsException ex) {
                symbolChars = new char[cache.fSymbolCharsOffset * 2];
                System.arraycopy(cache.fSymbolChars, 0, symbolChars, 0, cache.fSymbolCharsOffset);
                cache.fSymbolChars = symbolChars;
                symbolChars[cache.fSymbolCharsOffset] = (char)ch;
            }
            cache.fSymbolCharsOffset++;
            entry = entries[offset + SymbolCache.NEXT_OFFSET];
            try {
                entries = cacheLines[entry];
            } catch (ArrayIndexOutOfBoundsException ex) {
                if (entry == -1) {
                    entry = cache.fCacheLineCount++;
                    entries[offset + SymbolCache.NEXT_OFFSET] = entry;
                    entries = new int[1+(SymbolCache.INITIAL_CACHE_RECORD_COUNT*SymbolCache.CACHE_RECORD_SIZE)];
                    try {
                        cacheLines[entry] = entries;
                    } catch (ArrayIndexOutOfBoundsException ex2) {
                        cacheLines = new int[entry * 2][];
                        System.arraycopy(cache.fCacheLines, 0, cacheLines, 0, entry);
                        cache.fCacheLines = cacheLines;
                        cacheLines[entry] = entries;
                    }
                } else {
                    entries = cacheLines[entry];
                }
            }
            offset = 1 + ((entries[0] - 1) * SymbolCache.CACHE_RECORD_SIZE);
            depth++;
        }
        if (offset < 0)
            offset = 1 + (entries[0] * SymbolCache.CACHE_RECORD_SIZE);
        while (true) {
            entries[0]++;
            try {
                entries[offset + SymbolCache.CHAR_OFFSET] = ch;
            } catch (ArrayIndexOutOfBoundsException ex) {
                int newSize = 1 + ((offset - 1) * 2);
                entries = new int[newSize];
                System.arraycopy(cacheLines[entry], 0, entries, 0, offset);
                cacheLines[entry] = entries;
                entries[offset + SymbolCache.CHAR_OFFSET] = ch;
            }
            if (b0 >= 0x80) {
                ch = getMultiByteSymbolChar(b0);
                b0 = fMostRecentByte;
            } else if (b0 == fastcheck || XMLCharacterProperties.fgAsciiNameChar[b0] == 0) {
                ch = -1;
            } else {
                ch = b0;
                fCharacterCounter++;
                if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                    b0 = loadNextByte();
                } else {
                    fCurrentOffset++;
                    if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                        fCurrentIndex++;
                        try {
                            b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            b0 = slowLoadNextByte();
                        }
                    } else {
                        if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                            b0 = slowLoadNextByte();
                        else
                            b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                    }
                }
            }
            if (ch == -1) {
                entries[offset + SymbolCache.NEXT_OFFSET] = -1;
                break;
            }
            entry = cache.fCacheLineCount++;
            entries[offset + SymbolCache.INDEX_OFFSET] = -1;
            entries[offset + SymbolCache.NEXT_OFFSET] = entry;
            entries = new int[1+(SymbolCache.INITIAL_CACHE_RECORD_COUNT*SymbolCache.CACHE_RECORD_SIZE)];
            try {
                cacheLines[entry] = entries;
            } catch (ArrayIndexOutOfBoundsException ex) {
                cacheLines = new int[entry * 2][];
                System.arraycopy(cache.fCacheLines, 0, cacheLines, 0, entry);
                cache.fCacheLines = cacheLines;
                cacheLines[entry] = entries;
            }
            offset = 1;
            try {
                symbolChars[cache.fSymbolCharsOffset] = (char)ch;
            } catch (ArrayIndexOutOfBoundsException ex) {
                symbolChars = new char[cache.fSymbolCharsOffset * 2];
                System.arraycopy(cache.fSymbolChars, 0, symbolChars, 0, cache.fSymbolCharsOffset);
                cache.fSymbolChars = symbolChars;
                symbolChars[cache.fSymbolCharsOffset] = (char)ch;
            }
            cache.fSymbolCharsOffset++;
        }

        int result = fStringPool.createNonMatchingSymbol(startOffset, entry, entries, offset);
        return result;
    }
    private int recognizeMarkup(int b0, QName element) throws Exception {
        switch (b0) {
        case 0:
            return XMLEntityHandler.CONTENT_RESULT_MARKUP_END_OF_INPUT;
        case '?':
            fCharacterCounter++;
            loadNextByte();
            return XMLEntityHandler.CONTENT_RESULT_START_OF_PI;
        case '!':
            fCharacterCounter++;
            b0 = loadNextByte();
            if (b0 == 0) {
                fCharacterCounter--;
                fCurrentOffset--;
                return XMLEntityHandler.CONTENT_RESULT_MARKUP_END_OF_INPUT;
            }
            if (b0 == '-') {
                fCharacterCounter++;
                b0 = loadNextByte();
                if (b0 == 0) {
                    fCharacterCounter -= 2;
                    fCurrentOffset -= 2;
                    return XMLEntityHandler.CONTENT_RESULT_MARKUP_END_OF_INPUT;
                }
                if (b0 == '-') {
                    fCharacterCounter++;
                    b0 = loadNextByte();
                    return XMLEntityHandler.CONTENT_RESULT_START_OF_COMMENT;
                }
                break;
            }
            if (b0 == '[') {
                for (int i = 0; i < 6; i++) {
                    fCharacterCounter++;
                    b0 = loadNextByte();
                    if (b0 == 0) {
                        fCharacterCounter -= (2 + i);
                        fCurrentOffset -= (2 + i);
                        return XMLEntityHandler.CONTENT_RESULT_MARKUP_END_OF_INPUT;
                    }
                    if (b0 != cdata_string[i]) {
                        return XMLEntityHandler.CONTENT_RESULT_MARKUP_NOT_RECOGNIZED;
                    }
                }
                fCharacterCounter++;
                loadNextByte();
                return XMLEntityHandler.CONTENT_RESULT_START_OF_CDSECT;
            }
            break;
        case '/':
            fCharacterCounter++;
            if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                b0 = loadNextByte();
            } else {
                fCurrentOffset++;
                if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                    fCurrentIndex++;
                    try {
                        b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        b0 = slowLoadNextByte();
                    }
                } else {
                    if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                        b0 = slowLoadNextByte();
                    else
                        b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                }
            }
            int expectedName = element.rawname;
            fStringPool.getCharArrayRange(expectedName, fCharArrayRange);
            char[] expected = fCharArrayRange.chars;
            int offset = fCharArrayRange.offset;
            int len = fCharArrayRange.length;
            if (b0 == expected[offset++]) {
                UTF8DataChunk savedChunk = fCurrentChunk;
                int savedIndex = fCurrentIndex;
                int savedOffset = fCurrentOffset;
                for (int i = 1; i < len; i++) {
                    if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                        b0 = loadNextByte();
                    } else {
                        fCurrentOffset++;
                        if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                            fCurrentIndex++;
                            try {
                                b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                b0 = slowLoadNextByte();
                            }
                        } else {
                            if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                b0 = slowLoadNextByte();
                            else
                                b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                        }
                    }
                    if (b0 != expected[offset++]) {
                        fCurrentChunk = savedChunk;
                        fCurrentIndex = savedIndex;
                        fCurrentOffset = savedOffset;
                        fMostRecentData = fCurrentChunk.toByteArray();
                        fMostRecentByte = fMostRecentData[savedIndex] & 0xFF;
                        return XMLEntityHandler.CONTENT_RESULT_START_OF_ETAG;
                    }
                }
                fCharacterCounter++;
                if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                    b0 = loadNextByte();
                } else {
                    fCurrentOffset++;
                    if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                        fCurrentIndex++;
                        try {
                            b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            b0 = slowLoadNextByte();
                        }
                    } else {
                        if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                            b0 = slowLoadNextByte();
                        else
                            b0 = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                    }
                }
                if (b0 == '>') {
                    fCharacterCounter++;
                    if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                        loadNextByte();
                    } else {
                        fCurrentOffset++;
                        if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                            fCurrentIndex++;
                            try {
                                fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                slowLoadNextByte();
                            }
                        } else {
                            if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                slowLoadNextByte();
                            else
                                fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                        }
                    }
                    return XMLEntityHandler.CONTENT_RESULT_MATCHING_ETAG;
                }
                while (b0 == 0x20 || b0 == 0x09 || b0 == 0x0A || b0 == 0x0D) {
                    if (b0 == 0x0A) {
                        fLinefeedCounter++;
                        fCharacterCounter = 1;
                        b0 = loadNextByte();
                    } else if (b0 == 0x0D) {
                        fCarriageReturnCounter++;
                        fCharacterCounter = 1;
                        b0 = loadNextByte();
                        if (b0 == 0x0A) {
                            fLinefeedCounter++;
                            b0 = loadNextByte();
                        }
                    } else {
                        fCharacterCounter++;
                        b0 = loadNextByte();
                    }
                    if (b0 == '>') {
                        fCharacterCounter++;
                        if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                            loadNextByte();
                        } else {
                            fCurrentOffset++;
                            if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                                fCurrentIndex++;
                                try {
                                    fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                                } catch (ArrayIndexOutOfBoundsException ex) {
                                    slowLoadNextByte();
                                }
                            } else {
                                if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                    slowLoadNextByte();
                                else
                                    fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF;
                            }
                        }
                        return XMLEntityHandler.CONTENT_RESULT_MATCHING_ETAG;
                    }
                }
                fCurrentChunk = savedChunk;
                fCurrentIndex = savedIndex;
                fCurrentOffset = savedOffset;
                fMostRecentData = fCurrentChunk.toByteArray();
                fMostRecentByte = fMostRecentData[savedIndex] & 0xFF;
            }
            return XMLEntityHandler.CONTENT_RESULT_START_OF_ETAG;
        default:
            return XMLEntityHandler.CONTENT_RESULT_START_OF_ELEMENT;
        }
        return XMLEntityHandler.CONTENT_RESULT_MARKUP_NOT_RECOGNIZED;
    }
    private int recognizeReference(int ch) throws Exception {
        if (ch == 0) {
            return XMLEntityHandler.CONTENT_RESULT_REFERENCE_END_OF_INPUT;
        }
        if (ch == '#') {
            fCharacterCounter++;
            loadNextByte();
            return XMLEntityHandler.CONTENT_RESULT_START_OF_CHARREF;
        } else {
            return XMLEntityHandler.CONTENT_RESULT_START_OF_ENTITYREF;
        }
    }
    public int scanContent(QName element) throws Exception {
        if (fCallClearPreviousChunk && fCurrentChunk.clearPreviousChunk())
            fCallClearPreviousChunk = false;
        fCharDataLength = 0;
        int charDataOffset = fCurrentOffset;
        int ch = fMostRecentByte;
        if (ch < 0x80) {
            switch (XMLCharacterProperties.fgAsciiWSCharData[ch]) {
            case 0:
                if (fSendCharDataAsCharArray) {
                    try {
                        fCharacters[fCharDataLength] = (char)ch;
                        fCharDataLength++;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        slowAppendCharData(ch);
                    }
                }
                fCharacterCounter++;
                if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                    ch = loadNextByte();
                } else {
                    fCurrentOffset++;
                    if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                        fCurrentIndex++;
                        try {
                            ch = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            ch = slowLoadNextByte();
                        }
                    } else {
                        if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                            ch = slowLoadNextByte();
                        else
                            ch = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                    }
                }
                break;
                fCharacterCounter++;
                if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                    ch = loadNextByte();
                } else {
                    fCurrentOffset++;
                    if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                        fCurrentIndex++;
                        try {
                            ch = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            ch = slowLoadNextByte();
                        }
                    } else {
                        if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                            ch = slowLoadNextByte();
                        else
                            ch = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                    }
                }
                if (!fInCDSect) {
                    return recognizeMarkup(ch, element);
                }
                if (fSendCharDataAsCharArray)
                    appendCharData('<');
                break;
                fCharacterCounter++;
                ch = loadNextByte();
                if (!fInCDSect) {
                    return recognizeReference(ch);
                }
                if (fSendCharDataAsCharArray)
                    appendCharData('&');
                break;
                fCharacterCounter++;
                ch = loadNextByte();
                if (ch != ']') {
                    if (fSendCharDataAsCharArray)
                        appendCharData(']');
                    break;
                }
                if (fCurrentIndex + 1 == UTF8DataChunk.CHUNK_SIZE) {
                    UTF8DataChunk saveChunk = fCurrentChunk;
                    int saveIndex = fCurrentIndex;
                    int saveOffset = fCurrentOffset;
                    if (loadNextByte() != '>') {
                        fCurrentChunk = saveChunk;
                        fCurrentIndex = saveIndex;
                        fCurrentOffset = saveOffset;
                        fMostRecentData = fCurrentChunk.toByteArray();
                        fMostRecentByte = ']';
                        if (fSendCharDataAsCharArray)
                            appendCharData(']');
                        break;
                    }
                } else {
                    if (fMostRecentData[fCurrentIndex + 1] != '>') {
                        if (fSendCharDataAsCharArray)
                            appendCharData(']');
                        break;
                    }
                    fCurrentIndex++;
                    fCurrentOffset++;
                }
                loadNextByte();
                fCharacterCounter += 2;
                return XMLEntityHandler.CONTENT_RESULT_END_OF_CDSECT;
                if (ch == 0 && atEOF(fCurrentOffset + 1)) {
                    changeReaders();
                }
                return XMLEntityHandler.CONTENT_RESULT_INVALID_CHAR;
            case 5:
                do {
                    if (ch == 0x0A) {
                        fLinefeedCounter++;
                        fCharacterCounter = 1;
                    } else if (ch == 0x0D) {
                        fCarriageReturnCounter++;
                        fCharacterCounter = 1;
                        if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                            ch = loadNextByte();
                        } else {
                            fCurrentOffset++;
                            if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                                fCurrentIndex++;
                                try {
                                    ch = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                                } catch (ArrayIndexOutOfBoundsException ex) {
                                    ch = slowLoadNextByte();
                                }
                            } else {
                                if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                    ch = slowLoadNextByte();
                                else
                                    ch = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                            }
                        }
                        if (ch != 0x0A) {
                            if (fSendCharDataAsCharArray)
                                appendCharData(0x0A);
                            if (ch == 0x20 || ch == 0x09 || ch == 0x0D)
                                continue;
                            break;
                        }
                        fLinefeedCounter++;
                    } else {
                        fCharacterCounter++;
                    }
                    if (fSendCharDataAsCharArray) {
                        try {
                            fCharacters[fCharDataLength] = (char)ch;
                            fCharDataLength++;
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            slowAppendCharData(ch);
                        }
                    }
                    if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                        ch = loadNextByte();
                    } else {
                        fCurrentOffset++;
                        if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                            fCurrentIndex++;
                            try {
                                ch = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                ch = slowLoadNextByte();
                            }
                        } else {
                            if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                ch = slowLoadNextByte();
                            else
                                ch = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                        }
                    }
                } while (ch == 0x20 || ch == 0x09 || ch == 0x0A || ch == 0x0D);
                if (ch < 0x80) {
                    switch (XMLCharacterProperties.fgAsciiCharData[ch]) {
                    case 0:
                        if (fSendCharDataAsCharArray)
                            appendCharData(ch);
                        fCharacterCounter++;
                        ch = loadNextByte();
                        break;
                        if (!fInCDSect) {
                            if (fSendCharDataAsCharArray) {
                                fCharDataHandler.processWhitespace(fCharacters, 0, fCharDataLength);
                            } else {
                                int stringIndex = addString(charDataOffset, fCurrentOffset - charDataOffset);
                                fCharDataHandler.processWhitespace(stringIndex);
                            }
                            fCharacterCounter++;
                            if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                                ch = loadNextByte();
                            } else {
                                fCurrentOffset++;
                                if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                                    fCurrentIndex++;
                                    try {
                                        ch = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                                    } catch (ArrayIndexOutOfBoundsException ex) {
                                        ch = slowLoadNextByte();
                                    }
                                } else {
                                    if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                        ch = slowLoadNextByte();
                                    else
                                        ch = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                                }
                            }
                            return recognizeMarkup(ch, element);
                        }
                        if (fSendCharDataAsCharArray)
                            appendCharData('<');
                        fCharacterCounter++;
                        ch = loadNextByte();
                        break;
                        if (!fInCDSect) {
                            whitespace(charDataOffset, fCurrentOffset);
                            fCharacterCounter++;
                            ch = loadNextByte();
                            return recognizeReference(ch);
                        }
                        if (fSendCharDataAsCharArray)
                            appendCharData('&');
                        fCharacterCounter++;
                        ch = loadNextByte();
                        break;
                        int endOffset = fCurrentOffset;
                        ch = loadNextByte();
                        if (ch != ']') {
                            fCharacterCounter++;
                            if (fSendCharDataAsCharArray)
                                appendCharData(']');
                            break;
                        }
                        if (fCurrentIndex + 1 == UTF8DataChunk.CHUNK_SIZE) {
                            UTF8DataChunk saveChunk = fCurrentChunk;
                            int saveIndex = fCurrentIndex;
                            int saveOffset = fCurrentOffset;
                            if (loadNextByte() != '>') {
                                fCurrentChunk = saveChunk;
                                fCurrentIndex = saveIndex;
                                fCurrentOffset = saveOffset;
                                fMostRecentData = fCurrentChunk.toByteArray();
                                fMostRecentByte = ']';
                                fCharacterCounter++;
                                if (fSendCharDataAsCharArray)
                                    appendCharData(']');
                                break;
                            }
                        } else {
                            if (fMostRecentData[fCurrentIndex + 1] != '>') {
                                fCharacterCounter++;
                                if (fSendCharDataAsCharArray)
                                    appendCharData(']');
                                break;
                            }
                            fCurrentIndex++;
                            fCurrentOffset++;
                        }
                        loadNextByte();
                        whitespace(charDataOffset, endOffset);
                        fCharacterCounter += 3;
                        return XMLEntityHandler.CONTENT_RESULT_END_OF_CDSECT;
                        whitespace(charDataOffset, fCurrentOffset);
                        if (ch == 0 && atEOF(fCurrentOffset + 1)) {
                            changeReaders();
                        }
                        return XMLEntityHandler.CONTENT_RESULT_INVALID_CHAR;
                    }
                } else {
                    if (fSendCharDataAsCharArray) {
                        if (!copyMultiByteCharData(ch)) {
                            whitespace(charDataOffset, fCurrentOffset);
                            return XMLEntityHandler.CONTENT_RESULT_INVALID_CHAR;
                        }
                    } else if (!skipMultiByteCharData(ch)) {
                        whitespace(charDataOffset, fCurrentOffset);
                        return XMLEntityHandler.CONTENT_RESULT_INVALID_CHAR;
                    }
                }
                break;
            }
        } else {
            if (fSendCharDataAsCharArray) {
                if (!copyMultiByteCharData(ch)) {
                    return XMLEntityHandler.CONTENT_RESULT_INVALID_CHAR;
                }
            } else {
                if (!skipMultiByteCharData(ch)) {
                    return XMLEntityHandler.CONTENT_RESULT_INVALID_CHAR;
                }
            }
        }
        if (fSendCharDataAsCharArray)
            ch = copyAsciiCharData();
        else
            ch = skipAsciiCharData();
        while (true) {
            if (ch < 0x80) {
                switch (XMLCharacterProperties.fgAsciiCharData[ch]) {
                case 0:
                    if (fSendCharDataAsCharArray)
                        appendCharData(ch);
                    fCharacterCounter++;
                    ch = loadNextByte();
                    break;
                    if (!fInCDSect) {
                        if (fSendCharDataAsCharArray) {
                            fCharDataHandler.processCharacters(fCharacters, 0, fCharDataLength);
                        } else {
                            int stringIndex = addString(charDataOffset, fCurrentOffset - charDataOffset);
                            fCharDataHandler.processCharacters(stringIndex);
                        }
                        fCharacterCounter++;
                        if (USE_OUT_OF_LINE_LOAD_NEXT_BYTE) {
                            ch = loadNextByte();
                        } else {
                            fCurrentOffset++;
                            if (USE_TRY_CATCH_FOR_LOAD_NEXT_BYTE) {
                                fCurrentIndex++;
                                try {
                                    ch = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                                } catch (ArrayIndexOutOfBoundsException ex) {
                                    ch = slowLoadNextByte();
                                }
                            } else {
                                if (++fCurrentIndex == UTF8DataChunk.CHUNK_SIZE)
                                    ch = slowLoadNextByte();
                                else
                                    ch = (fMostRecentByte = fMostRecentData[fCurrentIndex] & 0xFF);
                            }
                        }
                        return recognizeMarkup(ch, element);
                    }
                    if (fSendCharDataAsCharArray)
                        appendCharData('<');
                    fCharacterCounter++;
                    ch = loadNextByte();
                    break;
                    if (!fInCDSect) {
                        characters(charDataOffset, fCurrentOffset);
                        fCharacterCounter++;
                        ch = loadNextByte();
                        return recognizeReference(ch);
                    }
                    if (fSendCharDataAsCharArray)
                        appendCharData('&');
                    fCharacterCounter++;
                    ch = loadNextByte();
                    break;
                    int endOffset = fCurrentOffset;
                    ch = loadNextByte();
                    if (ch != ']') {
                        fCharacterCounter++;
                        if (fSendCharDataAsCharArray)
                            appendCharData(']');
                        break;
                    }
                    if (fCurrentIndex + 1 == UTF8DataChunk.CHUNK_SIZE) {
                        UTF8DataChunk saveChunk = fCurrentChunk;
                        int saveIndex = fCurrentIndex;
                        int saveOffset = fCurrentOffset;
                        if (loadNextByte() != '>') {
                            fCurrentChunk = saveChunk;
                            fCurrentIndex = saveIndex;
                            fCurrentOffset = saveOffset;
                            fMostRecentData = fCurrentChunk.toByteArray();
                            fMostRecentByte = ']';
                            fCharacterCounter++;
                            if (fSendCharDataAsCharArray)
                                appendCharData(']');
                            break;
                        }
                    } else {
                        if (fMostRecentData[fCurrentIndex + 1] != '>') {
                            fCharacterCounter++;
                            if (fSendCharDataAsCharArray)
                                appendCharData(']');
                            break;
                        }
                        fCurrentIndex++;
                        fCurrentOffset++;
                    }
                    loadNextByte();
                    characters(charDataOffset, endOffset);
                    fCharacterCounter += 3;
                    return XMLEntityHandler.CONTENT_RESULT_END_OF_CDSECT;
                    if (ch == 0x0A) {
                        if (fSendCharDataAsCharArray)
                            appendCharData(ch);
                        fLinefeedCounter++;
                        fCharacterCounter = 1;
                        ch = loadNextByte();
                        break;
                    }
                    if (ch == 0x0D) {
                        if (fSendCharDataAsCharArray)
                            appendCharData(0x0A);
                        fCarriageReturnCounter++;
                        fCharacterCounter = 1;
                        ch = loadNextByte();
                        if (ch == 0x0A) {
                            fLinefeedCounter++;
                            ch = loadNextByte();
                        }
                        break;
                    }
                    characters(charDataOffset, fCurrentOffset);
                    if (ch == 0 && atEOF(fCurrentOffset + 1)) {
                        changeReaders();
                    }
                    return XMLEntityHandler.CONTENT_RESULT_INVALID_CHAR;
                }
            } else {
                if (fSendCharDataAsCharArray) {
                    if (!copyMultiByteCharData(ch)) {
                        characters(charDataOffset, fCurrentOffset);
                        return XMLEntityHandler.CONTENT_RESULT_INVALID_CHAR;
                    }
                } else if (!skipMultiByteCharData(ch)) {
                    characters(charDataOffset, fCurrentOffset);
                    return XMLEntityHandler.CONTENT_RESULT_INVALID_CHAR;
                }
                ch = fMostRecentByte;
            }
        }
    }
    private boolean copyMultiByteCharData(int b0) throws Exception {
        UTF8DataChunk saveChunk = fCurrentChunk;
        int saveOffset = fCurrentOffset;
        int saveIndex = fCurrentIndex;
        int b1 = loadNextByte();
            int ch = ((0x1f & b0)<<6) + (0x3f & b1);
            loadNextByte();
            return true;
        }
        int b2 = loadNextByte();
            if ((b0 == 0xED && b1 >= 0xA0) || (b0 == 0xEF && b1 == 0xBF && b2 >= 0xBE)) {
                fCurrentChunk = saveChunk;
                fCurrentIndex = saveIndex;
                fCurrentOffset = saveOffset;
                fMostRecentData = saveChunk.toByteArray();
                fMostRecentByte = b0;
                return false;
            }
            int ch = ((0x0f & b0)<<12) + ((0x3f & b1)<<6) + (0x3f & b2);
            loadNextByte();
            return true;
        }

        if (( 0xf8 & b0 ) == 0xf0 ) {
            if (b0 > 0xF4 || (b0 == 0xF4 && b1 >= 0x90)) {
                fCurrentChunk = saveChunk;
                fCurrentIndex = saveIndex;
                fCurrentOffset = saveOffset;
                fMostRecentData = saveChunk.toByteArray();
                fMostRecentByte = b0;
                return false;
            }
            int ch = ((0x0f & b0)<<18) + ((0x3f & b1)<<12) + ((0x3f & b2)<<6) + (0x3f & b3);
            if (ch < 0x10000) {
                appendCharData(ch);
            } else {
                appendCharData(((ch-0x00010000)>>10)+0xd800);
                appendCharData(((ch-0x00010000)&0x3ff)+0xdc00);
            }
            loadNextByte();
            return true;
        } else {
            fCurrentChunk = saveChunk;
            fCurrentIndex = saveIndex;
            fCurrentOffset = saveOffset;
            fMostRecentData = saveChunk.toByteArray();
            fMostRecentByte = b0;
            return false;
        }
    }
    private boolean skipMultiByteCharData(int b0) throws Exception {
        UTF8DataChunk saveChunk = fCurrentChunk;
        int saveOffset = fCurrentOffset;
        int saveIndex = fCurrentIndex;
        int b1 = loadNextByte();
            loadNextByte();
            return true;
        }
        int b2 = loadNextByte();
            if ((b0 == 0xED && b1 >= 0xA0) || (b0 == 0xEF && b1 == 0xBF && b2 >= 0xBE)) {
                fCurrentChunk = saveChunk;
                fCurrentIndex = saveIndex;
                fCurrentOffset = saveOffset;
                fMostRecentData = saveChunk.toByteArray();
                fMostRecentByte = b0;
                return false;
            }
            loadNextByte();
            return true;
        }
        if (b0 > 0xF4 || (b0 == 0xF4 && b1 >= 0x90)) {
            fCurrentChunk = saveChunk;
            fCurrentIndex = saveIndex;
            fCurrentOffset = saveOffset;
            fMostRecentData = saveChunk.toByteArray();
            fMostRecentByte = b0;
            return false;
        }
        loadNextByte();
        return true;
    }
    private int copyAsciiCharData() throws Exception {
        int srcIndex = fCurrentIndex;
        int offset = fCurrentOffset - srcIndex;
        byte[] data = fMostRecentData;
        int dstIndex = fCharDataLength;
        boolean skiplf = false;
        while (true) {
            int ch;
            try {
                ch = data[srcIndex] & 0xFF;
            } catch (ArrayIndexOutOfBoundsException ex) {
                offset += srcIndex;
                slowLoadNextByte();
                srcIndex = 0;
                data = fMostRecentData;
                ch = data[srcIndex] & 0xFF;
            }
            if (ch >= 0x80) {
                fCurrentOffset = offset + srcIndex;
                fCurrentIndex = srcIndex;
                fMostRecentByte = ch;
                return ch;
            }
            if (XMLCharacterProperties.fgAsciiCharData[ch] == 0) {
                fCharacterCounter++;
                skiplf = false;
            } else if (ch == 0x0A) {
                fLinefeedCounter++;
                if (skiplf) {
                    skiplf = false;
                    srcIndex++;
                    continue;
                }
                fCharacterCounter = 1;
            } else if (ch == 0x0D) {
                fCarriageReturnCounter++;
                fCharacterCounter = 1;
                skiplf = true;
                ch = 0x0A;
            } else {
                fCurrentOffset = offset + srcIndex;
                fCurrentIndex = srcIndex;
                fMostRecentByte = ch;
                return ch;
            }
            srcIndex++;
            try {
                fCharacters[fCharDataLength] = (char)ch;
                fCharDataLength++;
            } catch (ArrayIndexOutOfBoundsException ex) {
                slowAppendCharData(ch);
            }
        }
    }
    private int skipAsciiCharData() throws Exception {
        int srcIndex = fCurrentIndex;
        int offset = fCurrentOffset - srcIndex;
        byte[] data = fMostRecentData;
        while (true) {
            int ch;
            try {
                ch = data[srcIndex] & 0xFF;
            } catch (ArrayIndexOutOfBoundsException ex) {
                offset += srcIndex;
                slowLoadNextByte();
                srcIndex = 0;
                data = fMostRecentData;
                ch = data[srcIndex] & 0xFF;
            }
            if (ch >= 0x80) {
                fCurrentOffset = offset + srcIndex;
                fCurrentIndex = srcIndex;
                fMostRecentByte = ch;
                return ch;
            }
            if (XMLCharacterProperties.fgAsciiCharData[ch] == 0) {
                fCharacterCounter++;
            } else if (ch == 0x0A) {
                fLinefeedCounter++;
                fCharacterCounter = 1;
            } else if (ch == 0x0D) {
                fCarriageReturnCounter++;
                fCharacterCounter = 1;
            } else {
                fCurrentOffset = offset + srcIndex;
                fCurrentIndex = srcIndex;
                fMostRecentByte = ch;
                return ch;
            }
            srcIndex++;
        }
    }
    private char[] fCharacters = new char[UTF8DataChunk.CHUNK_SIZE];
    private int fCharDataLength = 0;
    private void appendCharData(int ch) throws Exception {
        try {
            fCharacters[fCharDataLength] = (char)ch;
            fCharDataLength++;
        } catch (ArrayIndexOutOfBoundsException ex) {
            slowAppendCharData(ch);
        }
    }
    private void slowAppendCharData(int ch) throws Exception {
        characters(0, fCharDataLength); /* DEFECT !! whitespace this long is unlikely, but possible */
        fCharDataLength = 0;
        fCharacters[fCharDataLength++] = (char)ch;
    }
    private void characters(int offset, int endOffset) throws Exception {
        if (!fSendCharDataAsCharArray) {
            int stringIndex = addString(offset, endOffset - offset);
            fCharDataHandler.processCharacters(stringIndex);
            return;
        }
        fCharDataHandler.processCharacters(fCharacters, 0, fCharDataLength);
    }
    private void whitespace(int offset, int endOffset) throws Exception {
        if (!fSendCharDataAsCharArray) {
            int stringIndex = addString(offset, endOffset - offset);
            fCharDataHandler.processWhitespace(stringIndex);
            return;
        }
        fCharDataHandler.processWhitespace(fCharacters, 0, fCharDataLength);
    }
    private static final char[] cdata_string = { 'C','D','A','T','A','['};
    private StringPool.CharArrayRange fCharArrayRange = null;
    private InputStream fInputStream = null;
    private StringPool fStringPool = null;
    private UTF8DataChunk fCurrentChunk = null;
    private int fCurrentIndex = 0;
    private byte[] fMostRecentData = null;
    private int fMostRecentByte = 0;
    private int fLength = 0;
    private boolean fCalledCharPropInit = false;
    private boolean fCallClearPreviousChunk = true;
    private int fillCurrentChunk() throws Exception {
        byte[] buf = fCurrentChunk.toByteArray();
        if (fInputStream == null) {
            if (buf == null)
                buf = new byte[1];
            buf[0] = 0;
            fMostRecentData = buf;
            fCurrentIndex = 0;
            fCurrentChunk.setByteArray(fMostRecentData);
            return(fMostRecentByte = fMostRecentData[0] & 0xFF);
        }
        if (buf == null)
            buf = new byte[UTF8DataChunk.CHUNK_SIZE];
        int offset = 0;
        int capacity = UTF8DataChunk.CHUNK_SIZE;
        int result = 0;
        do {
            try {
                result = fInputStream.read(buf, offset, capacity);
            } catch (java.io.IOException ex) {
                result = -1;
            }
            if (result == -1) {
                fInputStream.close();
                fInputStream = null;
                try {
                    buf[offset] = 0;
                } catch (ArrayIndexOutOfBoundsException ex) {
                }
                break;
            }
            if (result > 0) {
                offset += result;
                capacity -= result;
            }
        } while (capacity > 0);
        fMostRecentData = buf;
        fLength += offset;
        fCurrentIndex = 0;
        fCurrentChunk.setByteArray(fMostRecentData);
        return(fMostRecentByte = fMostRecentData[0] & 0xFF);
    }
}
