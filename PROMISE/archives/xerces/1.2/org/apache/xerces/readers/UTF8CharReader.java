package org.apache.xerces.readers;

import org.apache.xerces.framework.XMLErrorReporter;
import org.apache.xerces.utils.CharDataChunk;
import org.apache.xerces.utils.StringPool;
import org.apache.xerces.utils.ImplementationMessages;
import java.io.InputStream;

/**
 * Simple character-based version of a UTF8 reader.
 *
 * This class is not commonly used, but is provided as a much simplified
 * example of the UTF8Reader class that uses the AbstractCharReader to
 * perform all of the reader functions except for filling each buffer
 * of the character data when needed (fillCurrentChunk).  We read the
 * input data from an InputStream and perform end-of-line normalization
 * as we process that data.
 *
 * @version
 */
final class UTF8CharReader extends AbstractCharReader {
    UTF8CharReader(XMLEntityHandler entityHandler, XMLErrorReporter errorReporter, boolean sendCharDataAsCharArray, InputStream dataStream, StringPool stringPool) throws Exception {
        super(entityHandler, errorReporter, sendCharDataAsCharArray, stringPool);
        fInputStream = dataStream;
        fillCurrentChunk();
    }
    private InputStream fInputStream = null;
    private boolean fCheckOverflow = false;
    private byte[] fOverflow = null;
    private int fOverflowOffset = 0;
    private int fOverflowEnd = 0;
    private int fOutputOffset = 0;
    private boolean fSkipLinefeed = false;
    private int fPartialMultiByteIn = 0;
    private byte[] fPartialMultiByteChar = new byte[3];
    private int fPartialSurrogatePair = 0;
    private boolean fPartialMultiByteResult = false;
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
            if (fOverflow == null)
                fOverflow = new byte[CharDataChunk.CHUNK_SIZE];
            fMostRecentData = null;
        }
        while (true) {
            fOverflowOffset = 0;
            fOverflowEnd = 0;
            int capacity = CharDataChunk.CHUNK_SIZE;
            int result = 0;
            do {
                try {
                    result = fInputStream.read(fOverflow, fOverflowEnd, capacity);
                } catch (java.io.IOException ex) {
                    result = -1;
                }
                if (result == -1) {
                    fInputStream.close();
                    fInputStream = null;
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
                            if (fOverflowEnd == CharDataChunk.CHUNK_SIZE) {
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
    private boolean copyNormalize(byte[] in, int inOffset, char[] out, int outOffset) throws Exception {
        int inEnd = fOverflowEnd;
        int outEnd = out.length;
        if (inOffset == inEnd)
            return true;
        byte b = in[inOffset];
        if (fSkipLinefeed) {
            fSkipLinefeed = false;
            if (b == 0x0A) {
                if (++inOffset == inEnd)
                    return exitNormalize(inOffset, outOffset, true);
                b = in[inOffset];
            }
        } else if (fPartialMultiByteIn > 0) {
            if (!handlePartialMultiByteChar(b, in, inOffset, inEnd, out, outOffset, outEnd))
                return fPartialMultiByteResult;
            inOffset = fOverflowOffset;
            outOffset = fOutputOffset;
            b = in[inOffset];
        }
        while (outOffset < outEnd) {
            int inCount = inEnd - inOffset;
            int outCount = outEnd - outOffset;
            if (inCount > outCount)
                inCount = outCount;
            inOffset++;
            while (true) {
                while (b == 0x0D || b < 0) {
                    if (b == 0x0D) {
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
                    } else {
                        if (!handleMultiByteChar(b, in, inOffset, inEnd, out, outOffset, outEnd))
                            return fPartialMultiByteResult;
                        inOffset = fOverflowOffset;
                        outOffset = fOutputOffset;
                        b = in[inOffset];
                    }
                    inCount = inEnd - inOffset;
                    outCount = outEnd - outOffset;
                    if (inCount > outCount)
                        inCount = outCount;
                    inOffset++;
                }
                while (true) {
                    out[outOffset++] = (char)b;
                    if (--inCount == 0)
                        break;
                    b = in[inOffset++];
                    if (b == 0x0D || b < 0)
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
    private void savePartialMultiByte(int inCount, byte bz, byte by, byte bx) {
        fPartialMultiByteIn = inCount;
        fPartialMultiByteChar[--inCount] = bz;
        fPartialMultiByteChar[--inCount] = by;
        fPartialMultiByteChar[--inCount] = bx;
    }
    private void savePartialMultiByte(int inCount, byte bz, byte by) {
        fPartialMultiByteIn = inCount;
        fPartialMultiByteChar[--inCount] = bz;
        fPartialMultiByteChar[--inCount] = by;
    }
    private void savePartialMultiByte(int inCount, byte bz) {
        fPartialMultiByteIn = inCount;
        fPartialMultiByteChar[--inCount] = bz;
    }
    private boolean handleMultiByteChar(byte b, byte[] in, int inOffset, int inEnd, char[] out, int outOffset, int outEnd) throws Exception {
        if (inOffset == inEnd) {
            savePartialMultiByte(1, b);
            fPartialMultiByteResult = exitNormalize(inOffset, outOffset, true);
            return false;
        }
        byte b1 = in[inOffset++];
        if ((b1 & 0xc0) != 0x80) {
            Object[] args = {
                Integer.toHexString(b & 0xff),
                Integer.toHexString(b1 & 0xff)
            };
            deferException(ImplementationMessages.ENC5, args, outOffset);
            out[outOffset++] = 0;
            return exitNormalize(inOffset, outOffset, true);
        }
            int ch = ((0x1f & b)<<6) + (0x3f & b1);
            out[outOffset++] = (char)ch;
            if (inOffset == inEnd || outOffset == outEnd) {
                fPartialMultiByteResult = exitNormalize(inOffset, outOffset, inOffset == inEnd);
                return false;
            }
        } else {
            if (inOffset == inEnd) {
                savePartialMultiByte(2, b1, b);
                fPartialMultiByteResult = exitNormalize(inOffset, outOffset, true);
                return false;
            }
            byte b2 = in[inOffset++];
            if ((b2 & 0xc0) != 0x80) {
                Object[] args = {
                    Integer.toHexString(b & 0xff),
                    Integer.toHexString(b1 & 0xff),
                    Integer.toHexString(b2 & 0xff)
                };
                deferException(ImplementationMessages.ENC6, args, outOffset);
                out[outOffset++] = 0;
                return exitNormalize(inOffset, outOffset, true);
            }
                int ch = ((0x0f & b)<<12) + ((0x3f & b1)<<6) + (0x3f & b2);
                out[outOffset++] = (char)ch;
                if (inOffset == inEnd || outOffset == outEnd) {
                    fPartialMultiByteResult = exitNormalize(inOffset, outOffset, inOffset == inEnd);
                    return false;
                }
            } else {
                if ((b & 0xf8) != 0xf0) {
                    Object[] args = { Integer.toHexString(b & 0xff) };
                    deferException(ImplementationMessages.ENC4, args, outOffset);
                    out[outOffset++] = 0;
                    return exitNormalize(inOffset, outOffset, true);
                }
                if (inOffset == inEnd) {
                    savePartialMultiByte(3, b2, b1, b);
                    fPartialMultiByteResult = exitNormalize(inOffset, outOffset, true);
                    return false;
                }
                byte b3 = in[inOffset++];
                if ((b3 & 0xc0) != 0x80) {
                    Object[] args = {
                        Integer.toHexString(b & 0xff),
                        Integer.toHexString(b1 & 0xff),
                        Integer.toHexString(b2 & 0xff),
                        Integer.toHexString(b3 & 0xff)
                    };
                    deferException(ImplementationMessages.ENC7, args, outOffset);
                    out[outOffset++] = 0;
                    return exitNormalize(inOffset, outOffset, true);
                }
                int ch = ((0x0f & b)<<18) + ((0x3f & b1)<<12) + ((0x3f & b2)<<6) + (0x3f & b3);
                if (ch >= 0x10000) {
                    out[outOffset++] = (char)(((ch-0x00010000)>>10)+0xd800);
                    ch = (((ch-0x00010000)&0x3ff)+0xdc00);
                    if (outOffset == outEnd) {
                        fPartialSurrogatePair = ch;
                        fPartialMultiByteResult = exitNormalize(inOffset, outOffset, inOffset == inEnd);
                        return false;
                    }
                }
                out[outOffset++] = (char)ch;
                if (inOffset == inEnd || outOffset == outEnd) {
                    fPartialMultiByteResult = exitNormalize(inOffset, outOffset, inOffset == inEnd);
                    return false;
                }
            }
        }
        return exitNormalize(inOffset, outOffset, true);
    }
    private boolean handlePartialMultiByteChar(byte b, byte[] in, int inOffset, int inEnd, char[] out, int outOffset, int outEnd) throws Exception {
        if (outOffset == outEnd) {
            fPartialMultiByteResult = exitNormalize(inOffset, outOffset, inOffset == inEnd);
            return false;
        }
        if (fPartialMultiByteIn == 4) {
            out[outOffset++] = (char)fPartialSurrogatePair;
            if (outOffset == outEnd) {
                fPartialMultiByteResult = exitNormalize(inOffset, outOffset, false);
                return false;
            }
            fOutputOffset = outOffset;
            return true;
        }
        int byteIn = fPartialMultiByteIn;
        fPartialMultiByteIn = 0;
        byte b1 = 0;
        byte b2 = 0;
        byte b3 = 0;
        switch (byteIn) {
        case 1: b1 = b; break;
        case 2: b2 = b; break;
        case 3: b3 = b; break;
        }
        int i = byteIn;
        switch (byteIn) {
        case 3:
            b2 = fPartialMultiByteChar[--i];
        case 2:
            b1 = fPartialMultiByteChar[--i];
        case 1:
            b = fPartialMultiByteChar[--i];
        }
        switch (byteIn) {
        case 1:
            if ((b1 & 0xc0) != 0x80) {
                Object[] args = {
                    Integer.toHexString(b),
                    Integer.toHexString(b1)
                };
                deferException(ImplementationMessages.ENC5, args, outOffset);
                out[outOffset++] = 0;
                break;
            }
        case 2:
                int ch = ((0x1f & b)<<6) + (0x3f & b1);
                out[outOffset++] = (char)ch;
                if (outOffset == outEnd) {
                    fPartialMultiByteResult = exitNormalize(inOffset, outOffset, false);
                    return false;
                }
                if (byteIn < 2 && ++inOffset == inEnd) {
                    fPartialMultiByteResult = exitNormalize(inOffset, outOffset, true);
                    return false;
                }
                break;
            }
            if (byteIn < 2) {
                if (++inOffset == inEnd) {
                    savePartialMultiByte(2, b1);
                    fPartialMultiByteResult = exitNormalize(inOffset, outOffset, true);
                    return false;
                }
                b2 = in[inOffset];
            }
            if ((b2 & 0xc0) != 0x80) {
                Object[] args = {
                    Integer.toHexString(b),
                    Integer.toHexString(b1),
                    Integer.toHexString(b2)
                };
                deferException(ImplementationMessages.ENC6, args, outOffset);
                out[outOffset++] = 0;
                break;
            }
        case 3:
                int ch = ((0x0f & b)<<12) + ((0x3f & b1)<<6) + (0x3f & b2);
                out[outOffset++] = (char)ch;
                if (outOffset == outEnd) {
                    fPartialMultiByteResult = exitNormalize(inOffset, outOffset, false);
                    return false;
                }
                if (byteIn < 3 && ++inOffset == inEnd) {
                    fPartialMultiByteResult = exitNormalize(inOffset, outOffset, true);
                    return false;
                }
                break;
            }
            if (byteIn < 3) {
                if ((b & 0xf8) != 0xf0) {
                    Object[] args = { Integer.toHexString(b) };
                    deferException(ImplementationMessages.ENC4, args, outOffset);
                    out[outOffset++] = 0;
                    break;
                }
                if (++inOffset == inEnd) {
                    savePartialMultiByte(3, b2, b1);
                    fPartialMultiByteResult = exitNormalize(inOffset, outOffset, true);
                    return false;
                }
                b3 = in[inOffset];
            }
            if ((b3 & 0xc0) != 0x80) {
                Object[] args = {
                    Integer.toHexString(b),
                    Integer.toHexString(b1),
                    Integer.toHexString(b2),
                    Integer.toHexString(b3)
                };
                deferException(ImplementationMessages.ENC7, args, outOffset);
                out[outOffset++] = 0;
                break;
            }
            int ch = ((0x0f & b)<<18) + ((0x3f & b1)<<12) + ((0x3f & b2)<<6) + (0x3f & b3);
            if (ch >= 0x10000) {
                out[outOffset++] = (char)(((ch-0x00010000)>>10)+0xd800);
                ch = (((ch-0x00010000)&0x3ff)+0xdc00);
                if (outOffset == outEnd) {
                    fPartialSurrogatePair = ch;
                    fPartialMultiByteResult = exitNormalize(inOffset, outOffset, false);
                    return false;
                }
            }
            out[outOffset++] = (char)ch;
            if (outOffset == outEnd) {
                fPartialMultiByteResult = exitNormalize(inOffset, outOffset, false);
                return false;
            }
            if (++inOffset == inEnd) {
                fPartialMultiByteResult = exitNormalize(inOffset, outOffset, true);
                return false;
            }
            break;
        }
        return exitNormalize(inOffset, outOffset, true);
    }
}
