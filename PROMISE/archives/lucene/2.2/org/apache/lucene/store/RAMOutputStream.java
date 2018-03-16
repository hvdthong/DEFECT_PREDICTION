import java.io.IOException;

/**
 * A memory-resident {@link IndexOutput} implementation.
 * 
 * @version $Id: RAMOutputStream.java 542561 2007-05-29 15:14:07Z mikemccand $
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
      out.writeBytes((byte[])file.buffers.get(buffer++), length);
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
    if (currentBufferIndex == file.buffers.size()) {
      currentBuffer = file.addBuffer(BUFFER_SIZE);
    } else {
      currentBuffer = (byte[]) file.buffers.get(currentBufferIndex);
    }
    bufferPosition = 0;
    bufferStart = BUFFER_SIZE * currentBufferIndex;
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
}
