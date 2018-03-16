package org.apache.xerces.readers;

import org.apache.xerces.framework.XMLErrorReporter;
import org.apache.xerces.utils.CharDataChunk;
import org.apache.xerces.utils.StringPool;
import java.io.Reader;

/**
 * General purpose character stream reader.
 *
 * This class is used when the input source for the document entity is
 * specified using a character stream, when the input source is specified
 * using a byte stream with an explicit encoding, or when a recognizer
 * scans the encoding decl from the byte stream and chooses to use this
 * reader class for that encoding.  For the latter two cases, the byte
 * stream is wrapped in the appropriate InputStreamReader using the
 * desired encoding.
 *
 * @version
 */
final class CharReader extends AbstractCharReader {
    CharReader(XMLEntityHandler entityHandler, XMLErrorReporter errorReporter, boolean sendCharDataAsCharArray, Reader reader, StringPool stringPool) throws Exception {
        super(entityHandler, errorReporter, sendCharDataAsCharArray, stringPool);
        fCharacterStream = reader;
        fillCurrentChunk();
    }
    private Reader fCharacterStream = null;
    private boolean fCheckOverflow = false;
    private char[] fOverflow = null;
    private int fOverflowOffset = 0;
    private int fOverflowEnd = 0;
    private int fOutputOffset = 0;
    private boolean fSkipLinefeed = false;
    protected int fillCurrentChunk() throws Exception {
        char[] recycledData = fCurrentChunk.toCharArray();
        fOutputOffset = 0;
        if (fCheckOverflow) {
            fMostRecentData = recycledData;
            if (fOverflowEnd < CharDataChunk.CHUNK_SIZE) {
                recycledData = null;
                if (fOverflowEnd > 0) {
                    if (fMostRecentData == null || fMostRecentData.length < 1 + fOverflowEnd - fOverflowOffset)
                        fMostRecentData = new char[1 + fOverflowEnd - fOverflowOffset];
                    copyNormalize(fOverflow, fOverflowOffset, fMostRecentData, fOutputOffset);
                } else {
                    if (fMostRecentData == null)
                        fMostRecentData = new char[1];
                }
                fMostRecentData[fOutputOffset] = 0;
                fOverflow = null;
                fLength += fOutputOffset;
                fCurrentIndex = 0;
                fCurrentChunk.setCharArray(fMostRecentData);
                return (fMostRecentChar = fMostRecentData[0]);
            }
            if (fMostRecentData == null || fMostRecentData.length < CharDataChunk.CHUNK_SIZE)
                fMostRecentData = new char[CharDataChunk.CHUNK_SIZE];
            else
                recycledData = null;
            copyNormalize(fOverflow, fOverflowOffset, fMostRecentData, fOutputOffset);
            fCheckOverflow = false;
        } else {
            if (fOverflow == null) {
                fOverflow = recycledData;
                if (fOverflow == null || fOverflow.length < CharDataChunk.CHUNK_SIZE)
                    fOverflow = new char[CharDataChunk.CHUNK_SIZE];
                else
                    recycledData = null;
            }
            fMostRecentData = null;
        }
        while (true) {
            fOverflowOffset = 0;
            fOverflowEnd = 0;
            int capacity = CharDataChunk.CHUNK_SIZE;
            int result = 0;
            do {
                try {
                    result = fCharacterStream.read(fOverflow, fOverflowEnd, capacity);
                } catch (java.io.IOException ex) {
                    result = -1;
                }
                if (result == -1) {
                    fCharacterStream.close();
                    fCharacterStream = null;
                    if (fMostRecentData == null) {
                        fMostRecentData = recycledData;
                        if (fMostRecentData == null || fMostRecentData.length < 1 + fOverflowEnd)
                            fMostRecentData = new char[1 + fOverflowEnd];
                        else
                            recycledData = null;
                        copyNormalize(fOverflow, fOverflowOffset, fMostRecentData, fOutputOffset);
                        fOverflow = null;
                        fMostRecentData[fOutputOffset] = 0;
                    } else {
                        boolean alldone = copyNormalize(fOverflow, fOverflowOffset, fMostRecentData, fOutputOffset);
                        if (alldone) {
                            if (fOutputOffset == CharDataChunk.CHUNK_SIZE) {
                                fCheckOverflow = true;
                                fOverflowOffset = 0;
                                fOverflowEnd = 0;
                            } else {
                                fOverflow = null;
                                fMostRecentData[fOutputOffset] = 0;
                            }
                        } else {
                            fCheckOverflow = true;
                        }
                    }
                    break;
                }
                if (result > 0) {
                    fOverflowEnd += result;
                    capacity -= result;
                }
            } while (capacity > 0);
            if (result == -1)
                break;
            if (fMostRecentData != null) {
                boolean alldone = copyNormalize(fOverflow, fOverflowOffset, fMostRecentData, fOutputOffset);
                if (fOutputOffset == CharDataChunk.CHUNK_SIZE) {
                    if (!alldone) {
                        fCheckOverflow = true;
                    }
                    break;
                }
            } else {
                fMostRecentData = recycledData;
                if (fMostRecentData == null || fMostRecentData.length < CharDataChunk.CHUNK_SIZE)
                    fMostRecentData = new char[CharDataChunk.CHUNK_SIZE];
                else
                    recycledData = null;
                copyNormalize(fOverflow, fOverflowOffset, fMostRecentData, fOutputOffset);
                if (fOutputOffset == CharDataChunk.CHUNK_SIZE) {
                    break;
                }
            }
        }
        fLength += fOutputOffset;
        fCurrentIndex = 0;
        fCurrentChunk.setCharArray(fMostRecentData);
        return (fMostRecentChar = fMostRecentData[0]);
    }
    private boolean copyNormalize(char[] in, int inOffset, char[] out, int outOffset) throws Exception {
        int inEnd = fOverflowEnd;
        int outEnd = out.length;
        if (inOffset == inEnd)
            return true;
        char b = in[inOffset];
        if (fSkipLinefeed) {
            fSkipLinefeed = false;
            if (b == 0x0A) {
                if (++inOffset == inEnd)
                    return exitNormalize(inOffset, outOffset, true);
                b = in[inOffset];
            }
        }
        while (outOffset < outEnd) {
            int inCount = inEnd - inOffset;
            int outCount = outEnd - outOffset;
            if (inCount > outCount)
                inCount = outCount;
            inOffset++;
            while (true) {
                while (b == 0x0D) {
                    out[outOffset++] = 0x0A;
                    if (inOffset == inEnd) {
                        fSkipLinefeed = true;
                        return exitNormalize(inOffset, outOffset, true);
                    }
                    b = in[inOffset];
                    if (b == 0x0A) {
                        if (++inOffset == inEnd)
                            return exitNormalize(inOffset, outOffset, true);
                        b = in[inOffset];
                    }
                    if (outOffset == outEnd)
                        return exitNormalize(inOffset, outOffset, false);
                    inCount = inEnd - inOffset;
                    outCount = outEnd - outOffset;
                    if (inCount > outCount)
                        inCount = outCount;
                    inOffset++;
                }
                while (true) {
                    out[outOffset++] = b;
                    if (--inCount == 0)
                        break;
                    b = in[inOffset++];
                    if (b == 0x0D)
                        break;
                }
                if (inCount == 0)
                    break;
            }
            if (inOffset == inEnd)
                break;
        }
        return exitNormalize(inOffset, outOffset, inOffset == inEnd);
    }
    private boolean exitNormalize(int inOffset, int outOffset, boolean result) {
        fOverflowOffset = inOffset;
        fOutputOffset = outOffset;
        return result;
    }
}
