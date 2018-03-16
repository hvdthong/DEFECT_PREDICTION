import java.io.IOException;

/** Base implementation class for buffered {@link IndexOutput}. */
public abstract class BufferedIndexOutput extends IndexOutput {
  static final int BUFFER_SIZE = 1024;

  private final byte[] buffer = new byte[BUFFER_SIZE];

  /** Writes a single byte.
   * @see IndexInput#readByte()
   */
  public void writeByte(byte b) throws IOException {
    if (bufferPosition >= BUFFER_SIZE)
      flush();
    buffer[bufferPosition++] = b;
  }

  /** Writes an array of bytes.
   * @param b the bytes to write
   * @param length the number of bytes to write
   * @see IndexInput#readBytes(byte[],int,int)
   */
  public void writeBytes(byte[] b, int length) throws IOException {
    int bytesLeft = BUFFER_SIZE - bufferPosition;
    if (bytesLeft >= length) {
      System.arraycopy(b, 0, buffer, bufferPosition, length);
      bufferPosition += length;
      if (BUFFER_SIZE - bufferPosition == 0)
        flush();
    } else {
      if (length > BUFFER_SIZE) {
        if (bufferPosition > 0)
          flush();
        flushBuffer(b, length);
        bufferStart += length;
      } else {
        int pieceLength;
        while (pos < length) {
          pieceLength = (length - pos < bytesLeft) ? length - pos : bytesLeft;
          System.arraycopy(b, pos, buffer, bufferPosition, pieceLength);
          pos += pieceLength;
          bufferPosition += pieceLength;
          bytesLeft = BUFFER_SIZE - bufferPosition;
          if (bytesLeft == 0) {
            flush();
            bytesLeft = BUFFER_SIZE;
          }
        }
      }
    }
  }

  /** Forces any buffered output to be written. */
  public void flush() throws IOException {
    flushBuffer(buffer, bufferPosition);
    bufferStart += bufferPosition;
    bufferPosition = 0;
  }

  /** Expert: implements buffer write.  Writes bytes at the current position in
   * the output.
   * @param b the bytes to write
   * @param len the number of bytes to write
   */
  protected abstract void flushBuffer(byte[] b, int len) throws IOException;

  /** Closes this stream to further operations. */
  public void close() throws IOException {
    flush();
  }

  /** Returns the current position in this file, where the next write will
   * occur.
   * @see #seek(long)
   */
  public long getFilePointer() {
    return bufferStart + bufferPosition;
  }

  /** Sets current position in this file, where the next write will occur.
   * @see #getFilePointer()
   */
  public void seek(long pos) throws IOException {
    flush();
    bufferStart = pos;
  }

  /** The number of bytes in the file. */
  public abstract long length() throws IOException;


}
