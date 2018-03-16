import java.io.IOException;

/**
 * A memory-resident {@link IndexOutput} implementation.
 * 
 * @version $Id: RAMOutputStream.java 691694 2008-09-03 17:34:29Z mikemccand $
 */

public class RAMOutputStream extends IndexOutput {
  static final int BUFFER_SIZE = 1024;

  private RAMFile file;

  private byte[] currentBuffer;
  private int currentBufferIndex;
  
  private int bufferPosition;
  private long bufferStart;
  private int bufferLength;

  /** Construct an empty output buffer. */
  public RAMOutputStream() {
    this(new RAMFile());
  }

  RAMOutputStream(RAMFile f) {
    file = f;

    currentBufferIndex = -1;
    currentBuffer = null;
  }

  /** Copy the current contents of this buffer to the named output. */
  public void writeTo(IndexOutput out) throws IOException {
    flush();
    final long end = file.length;
    long pos = 0;
    int buffer = 0;
    while (pos < end) {
      int length = BUFFER_SIZE;
      long nextPos = pos + length;
        length = (int)(end - pos);
      }
      out.writeBytes((byte[])file.getBuffer(buffer++), length);
      pos = nextPos;
    }
  }

  /** Resets this to an empty buffer. */
  public void reset() {
    try {
      seek(0);
      throw new RuntimeException(e.toString());
    }

    file.setLength(0);
  }

  public void close() throws IOException {
    flush();
  }

  public void seek(long pos) throws IOException {
    setFileLength();
    if (pos < bufferStart || pos >= bufferStart + bufferLength) {
      currentBufferIndex = (int) (pos / BUFFER_SIZE);
      switchCurrentBuffer();
    }

    bufferPosition = (int) (pos % BUFFER_SIZE);
  }

  public long length() {
    return file.length;
  }

  public void writeByte(byte b) throws IOException {
    if (bufferPosition == bufferLength) {
      currentBufferIndex++;
      switchCurrentBuffer();
    }
    currentBuffer[bufferPosition++] = b;
  }

  public void writeBytes(byte[] b, int offset, int len) throws IOException {
    assert b != null;
    while (len > 0) {
      if (bufferPosition ==  bufferLength) {
        currentBufferIndex++;
        switchCurrentBuffer();
      }

      int remainInBuffer = currentBuffer.length - bufferPosition;
      int bytesToCopy = len < remainInBuffer ? len : remainInBuffer;
      System.arraycopy(b, offset, currentBuffer, bufferPosition, bytesToCopy);
      offset += bytesToCopy;
      len -= bytesToCopy;
      bufferPosition += bytesToCopy;
    }
  }

  private final void switchCurrentBuffer() throws IOException {
    if (currentBufferIndex == file.numBuffers()) {
      currentBuffer = file.addBuffer(BUFFER_SIZE);
    } else {
      currentBuffer = (byte[]) file.getBuffer(currentBufferIndex);
    }
    bufferPosition = 0;
    bufferStart = (long) BUFFER_SIZE * (long) currentBufferIndex;
    bufferLength = currentBuffer.length;
  }

  private void setFileLength() {
    long pointer = bufferStart + bufferPosition;
    if (pointer > file.length) {
      file.setLength(pointer);
    }
  }

  public void flush() throws IOException {
    file.setLastModified(System.currentTimeMillis());
    setFileLength();
  }

  public long getFilePointer() {
    return currentBufferIndex < 0 ? 0 : bufferStart + bufferPosition;
  }

  /** Returns byte usage of all buffers. */
  public long sizeInBytes() {
    return file.numBuffers() * BUFFER_SIZE;
  }
}
