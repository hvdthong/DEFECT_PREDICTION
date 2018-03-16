package org.apache.xerces.utils;

import org.apache.xerces.readers.XMLEntityHandler;
import java.util.Vector;

public class UTF8DataChunk implements StringPool.StringProducer {
    public static final int CHUNK_SIZE = (1 << CHUNK_SHIFT);
    public static final int CHUNK_MASK = CHUNK_SIZE - 1;
    public static UTF8DataChunk createChunk(StringPool stringPool, UTF8DataChunk prev) {

        synchronized (UTF8DataChunk.class) {
            if (fgFreeChunks != null) {
                UTF8DataChunk newChunk = fgFreeChunks;
                fgFreeChunks = newChunk.fNextChunk;
                newChunk.fNextChunk = null;
                newChunk.init(stringPool, prev);
                return newChunk;
            }
        }
        UTF8DataChunk chunk = new UTF8DataChunk(stringPool, prev);
        return chunk;
    }
    public final byte[] toByteArray() {
        return fData;
    }
    public void setByteArray(byte[] data) {
        fData = data;
    }
    public UTF8DataChunk nextChunk() {
        return fNextChunk;
    }
    public boolean clearPreviousChunk() {
        if (fPreviousChunk != null) {
            fPreviousChunk.setNextChunk(null);
            fPreviousChunk.removeRef();
            fPreviousChunk = null;
            return true;
        }
        return fChunk == 0;
    }
    public void releaseChunk() {
        removeRef();
    }
    public void releaseString(int offset, int length) {
        removeRef();
    }
    public String toString(int offset, int length) {

        synchronized (fgTempBufferLock) {
            int outOffset = 0;
            UTF8DataChunk dataChunk = this;
            int endOffset = offset + length;
            int index = offset & CHUNK_MASK;
            byte[] data = fData;
            boolean skiplf = false;
            while (offset < endOffset) {
                int b0 = data[index++] & 0xff;
                offset++;
                if (index == CHUNK_SIZE && offset < endOffset) {
                    dataChunk = dataChunk.fNextChunk;
                    data = dataChunk.fData;
                    index = 0;
                }
                if (b0 < 0x80) {
                    if (skiplf) {
                        skiplf = false;
                        if (b0 == 0x0A)
                            continue;
                    }
                    if (b0 == 0x0D) {
                        b0 = 0x0A;
                        skiplf = true;
                    }
                    try {
                        fgTempBuffer[outOffset] = (char)b0;
                        outOffset++;
                    } catch (NullPointerException ex) {
                        fgTempBuffer = new char[CHUNK_SIZE];
                        fgTempBuffer[outOffset++] = (char)b0;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        char[] newBuffer = new char[outOffset * 2];
                        System.arraycopy(fgTempBuffer, 0, newBuffer, 0, outOffset);
                        fgTempBuffer = newBuffer;
                        fgTempBuffer[outOffset++] = (char)b0;
                    }
                    continue;
                }
                int b1 = data[index++] & 0xff;
                offset++;
                if (index == CHUNK_SIZE && offset < endOffset) {
                    dataChunk = dataChunk.fNextChunk;
                    data = dataChunk.fData;
                    index = 0;
                }
                    try {
                        fgTempBuffer[outOffset] = (char)ch;
                        outOffset++;
                    } catch (NullPointerException ex) {
                        fgTempBuffer = new char[CHUNK_SIZE];
                        fgTempBuffer[outOffset++] = (char)ch;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        char[] newBuffer = new char[outOffset * 2];
                        System.arraycopy(fgTempBuffer, 0, newBuffer, 0, outOffset);
                        fgTempBuffer = newBuffer;
                        fgTempBuffer[outOffset++] = (char)ch;
                    }
                    continue;
                }
                int b2 = data[index++] & 0xff;
                offset++;
                if (index == CHUNK_SIZE && offset < endOffset) {
                    dataChunk = dataChunk.fNextChunk;
                    data = dataChunk.fData;
                    index = 0;
                }
                    try {
                        fgTempBuffer[outOffset] = (char)ch;
                        outOffset++;
                    } catch (NullPointerException ex) {
                        fgTempBuffer = new char[CHUNK_SIZE];
                        fgTempBuffer[outOffset++] = (char)ch;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        char[] newBuffer = new char[outOffset * 2];
                        System.arraycopy(fgTempBuffer, 0, newBuffer, 0, outOffset);
                        fgTempBuffer = newBuffer;
                        fgTempBuffer[outOffset++] = (char)ch;
                    }
                    continue;
                }
                offset++;
                if (index == CHUNK_SIZE && offset < endOffset) {
                    dataChunk = dataChunk.fNextChunk;
                    data = dataChunk.fData;
                    index = 0;
                }
                int ch = ((0x0f & b0)<<18) + ((0x3f & b1)<<12) + ((0x3f & b2)<<6) + (0x3f & b3);
                if (ch < 0x10000) {
                    try {
                        fgTempBuffer[outOffset] = (char)ch;
                        outOffset++;
                    } catch (NullPointerException ex) {
                        fgTempBuffer = new char[CHUNK_SIZE];
                        fgTempBuffer[outOffset++] = (char)ch;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        char[] newBuffer = new char[outOffset * 2];
                        System.arraycopy(fgTempBuffer, 0, newBuffer, 0, outOffset);
                        fgTempBuffer = newBuffer;
                        fgTempBuffer[outOffset++] = (char)ch;
                    }
                } else {
                    char ch1 = (char)(((ch-0x00010000)>>10)+0xd800);
                    char ch2 = (char)(((ch-0x00010000)&0x3ff)+0xdc00);
                    try {
                        fgTempBuffer[outOffset] = (char)ch1;
                        outOffset++;
                    } catch (NullPointerException ex) {
                        fgTempBuffer = new char[CHUNK_SIZE];
                        fgTempBuffer[outOffset++] = (char)ch1;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        char[] newBuffer = new char[outOffset * 2];
                        System.arraycopy(fgTempBuffer, 0, newBuffer, 0, outOffset);
                        fgTempBuffer = newBuffer;
                        fgTempBuffer[outOffset++] = (char)ch1;
                    }
                    try {
                        fgTempBuffer[outOffset] = (char)ch2;
                        outOffset++;
                    } catch (NullPointerException ex) {
                        fgTempBuffer = new char[CHUNK_SIZE];
                        fgTempBuffer[outOffset++] = (char)ch2;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        char[] newBuffer = new char[outOffset * 2];
                        System.arraycopy(fgTempBuffer, 0, newBuffer, 0, outOffset);
                        fgTempBuffer = newBuffer;
                        fgTempBuffer[outOffset++] = (char)ch2;
                    }
                }
            }
            return new String(fgTempBuffer, 0, outOffset);
        }
    }
    public boolean equalsString(int offset, int length, char[] strChars, int strOffset, int strLength) {
        UTF8DataChunk dataChunk = this;
        int endOffset = offset + length;
        int index = offset & CHUNK_MASK;
        byte[] data = fData;
        boolean skiplf = false;
        while (offset < endOffset) {
            if (strLength-- == 0)
                return false;
            int b0 = data[index++] & 0xff;
            offset++;
            if (index == CHUNK_SIZE && offset < endOffset) {
                dataChunk = dataChunk.fNextChunk;
                data = dataChunk.fData;
                index = 0;
            }
            if (b0 < 0x80) {
                if (skiplf) {
                    skiplf = false;
                    if (b0 == 0x0A)
                        continue;
                }
                if (b0 == 0x0D) {
                    b0 = 0x0A;
                    skiplf = true;
                }
                if (b0 != strChars[strOffset++])
                    return false;
                continue;
            }
            int b1 = data[index++] & 0xff;
            offset++;
            if (index == CHUNK_SIZE && offset < endOffset) {
                dataChunk = dataChunk.fNextChunk;
                data = dataChunk.fData;
                index = 0;
            }
                int ch = ((0x1f & b0)<<6) + (0x3f & b1);
                if (ch != strChars[strOffset++])
                    return false;
                continue;
            }
            int b2 = data[index++] & 0xff;
            offset++;
            if (index == CHUNK_SIZE && offset < endOffset) {
                dataChunk = dataChunk.fNextChunk;
                data = dataChunk.fData;
                index = 0;
            }
                int ch = ((0x0f & b0)<<12) + ((0x3f & b1)<<6) + (0x3f & b2);
                if (ch != strChars[strOffset++])
                    return false;
                continue;
            }
            offset++;
            if (index == CHUNK_SIZE && offset < endOffset) {
                dataChunk = dataChunk.fNextChunk;
                data = dataChunk.fData;
                index = 0;
            }
            int ch = ((0x0f & b0)<<18) + ((0x3f & b1)<<12)
                   + ((0x3f & b2)<<6) + (0x3f & b3);
            if (ch < 0x10000) {
                if (ch != strChars[strOffset++])
                    return false;
            } else {
                if ((((ch-0x00010000)>>10)+0xd800) != strChars[strOffset++])
                    return false;
                if (strLength-- == 0)
                    return false;
                if ((((ch-0x00010000)&0x3ff)+0xdc00) != strChars[strOffset++])
                    return false;
            }
        }
        return (strLength == 0);
    }
    public int addString(int offset, int length) {
        if (length == 0)
            return StringPool.EMPTY_STRING;
        int chunk = offset >> CHUNK_SHIFT;
        if (chunk != fChunk) {
            if (fPreviousChunk == null)
                throw new RuntimeException(new ImplementationMessages().createMessage(null, ImplementationMessages.INT_PCN, 0, null));
            return fPreviousChunk.addString(offset, length);
        }
        int lastChunk = (offset + length - 1) >> CHUNK_SHIFT;
        if (chunk == lastChunk) {
            addRef();
            return fStringPool.addString(this, offset & CHUNK_MASK, length);
        }
        String str = toString(offset & CHUNK_MASK, length);
        return fStringPool.addString(str);
    }
    public int addSymbol(int offset, int length, int hashcode) {
        if (length == 0)
            return StringPool.EMPTY_STRING;
        int chunk = offset >> CHUNK_SHIFT;
        if (chunk != fChunk) {
            if (fPreviousChunk == null)
                throw new RuntimeException(new ImplementationMessages().createMessage(null, ImplementationMessages.INT_PCN, 0, null));
            return fPreviousChunk.addSymbol(offset, length, hashcode);
        }
        int lastChunk = (offset + length - 1) >> CHUNK_SHIFT;
        int index = offset & CHUNK_MASK;
        if (chunk == lastChunk) {
            if (hashcode == 0) {
                hashcode = getHashcode(index, length);
            }
            int symbol = fStringPool.lookupSymbol(this, index, length, hashcode);
            if (symbol == -1) {
                String str = toString(index, length);
                symbol = fStringPool.addNewSymbol(str, hashcode);
            }
            return symbol;
        }
        String str = toString(index, length);
        return fStringPool.addSymbol(str);
    }
    public void append(XMLEntityHandler.CharBuffer charBuffer, int offset, int length) {
        UTF8DataChunk dataChunk = chunkFor(offset);
        int endOffset = offset + length;
        int index = offset & CHUNK_MASK;
        byte[] data = dataChunk.fData;
        boolean skiplf = false;
        while (offset < endOffset) {
            int b0 = data[index++] & 0xff;
            offset++;
            if (index == CHUNK_SIZE && offset < endOffset) {
                dataChunk = dataChunk.fNextChunk;
                data = dataChunk.fData;
                index = 0;
            }
            if (b0 < 0x80) {
                if (skiplf) {
                    skiplf = false;
                    if (b0 == 0x0A)
                        continue;
                }
                if (b0 == 0x0D) {
                    b0 = 0x0A;
                    skiplf = true;
                }
                charBuffer.append((char)b0);
                continue;
            }
            int b1 = data[index++] & 0xff;
            offset++;
            if (index == CHUNK_SIZE && offset < endOffset) {
                dataChunk = dataChunk.fNextChunk;
                data = dataChunk.fData;
                index = 0;
            }
                int ch = ((0x1f & b0)<<6) + (0x3f & b1);
                continue;
            }
            int b2 = data[index++] & 0xff;
            offset++;
            if (index == CHUNK_SIZE && offset < endOffset) {
                dataChunk = dataChunk.fNextChunk;
                data = dataChunk.fData;
                index = 0;
            }
                int ch = ((0x0f & b0)<<12) + ((0x3f & b1)<<6) + (0x3f & b2);
                continue;
            }
            offset++;
            if (index == CHUNK_SIZE && offset < endOffset) {
                dataChunk = dataChunk.fNextChunk;
                data = dataChunk.fData;
                index = 0;
            }
            int ch = ((0x0f & b0)<<18) + ((0x3f & b1)<<12)
                   + ((0x3f & b2)<<6) + (0x3f & b3);
            if (ch < 0x10000)
                charBuffer.append((char)ch);
            else {
                charBuffer.append((char)(((ch-0x00010000)>>10)+0xd800));
                charBuffer.append((char)(((ch-0x00010000)&0x3ff)+0xdc00));
            }
        }
    }
    private int getHashcode(int index, int length) {
        int endIndex = index + length;
        int hashcode = 0;
        byte[] data = fData;
        while (index < endIndex) {
            int b0 = data[index++] & 0xff;
            if ((b0 & 0x80) == 0) {
                hashcode = StringHasher.hashChar(hashcode, b0);
                continue;
            }
            int b1 = data[index++] & 0xff;
                hashcode = StringHasher.hashChar(hashcode, ch);
                continue;
            }
            int b2 = data[index++] & 0xff;
                hashcode = StringHasher.hashChar(hashcode, ch);
                continue;
            }
            int ch = ((0x0f & b0)<<18) + ((0x3f & b1)<<12)
                    + ((0x3f & b2)<<6) + (0x3f & b3);
            if (ch < 0x10000)
                hashcode = StringHasher.hashChar(hashcode, ch);
            else {
                hashcode = StringHasher.hashChar(hashcode, (int)(((ch-0x00010000)>>10)+0xd800));
                hashcode = StringHasher.hashChar(hashcode, (int)(((ch-0x00010000)&0x3ff)+0xdc00));
            }
        }
        return StringHasher.finishHash(hashcode);
    }
    private void init(StringPool stringPool, UTF8DataChunk prev) {
        fStringPool = stringPool;
        fRefCount = 1;
        fChunk = prev == null ? 0 : prev.fChunk + 1;
        fNextChunk = null;
        fPreviousChunk = prev;
        if (prev != null) {
            prev.addRef();
            prev.setNextChunk(this);
            prev.removeRef();
        }
    }
    private UTF8DataChunk(StringPool stringPool, UTF8DataChunk prev) {
        init(stringPool, prev);
    }
    private final UTF8DataChunk chunkFor(int offset) {
        if ((offset >> CHUNK_SHIFT) == fChunk)
            return this;
        return slowChunkFor(offset);
    }
    private UTF8DataChunk slowChunkFor(int offset) {
        int firstChunk = offset >> CHUNK_SHIFT;
        UTF8DataChunk dataChunk = this;
        while (firstChunk != dataChunk.fChunk)
            dataChunk = dataChunk.fPreviousChunk;
        return dataChunk;
    }
    private final void addRef() {
        fRefCount++;
    }
    private final void removeRef() {
        fRefCount--;
        if (fRefCount == 0) {
            fStringPool = null;
            fChunk = -1;
            fPreviousChunk = null;
            synchronized (UTF8DataChunk.class) {
                /*** Only keep one free chunk at a time! ***
                fNextChunk = fgFreeChunks;
                /***/
                fNextChunk = null;
                fgFreeChunks = this;
            }
        }
    }
    private void setNextChunk(UTF8DataChunk nextChunk) {
        if (nextChunk == null) {
            if (fNextChunk != null)
                fNextChunk.removeRef();
        } else if (fNextChunk == null) {
            nextChunk.addRef();
        } else
            throw new RuntimeException("UTF8DataChunk::setNextChunk");
        fNextChunk = nextChunk;
    }
    private StringPool fStringPool;
    private int fRefCount;
    private int fChunk;
    private byte[] fData = null;
    private UTF8DataChunk fNextChunk;
    private UTF8DataChunk fPreviousChunk;
    private static UTF8DataChunk fgFreeChunks = null;
    private static char[] fgTempBuffer = null;
    private static Object fgTempBufferLock = new Object();
}
