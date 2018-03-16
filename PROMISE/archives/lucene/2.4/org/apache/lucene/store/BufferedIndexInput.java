import java.io.IOException;

/** Base implementation class for buffered {@link IndexInput}. */
public abstract class BufferedIndexInput extends IndexInput {

  /** Default buffer size */
  public static final int BUFFER_SIZE = 1024;

  private int bufferSize = BUFFER_SIZE;

  protected byte[] buffer;


  public byte readByte() throws IOException {
    if (bufferPosition >= bufferLength)
      refill();
    return buffer[bufferPosition++];
  }

  public BufferedIndexInput() {}

  /** Inits BufferedIndexInput with a specific bufferSize */
  public BufferedIndexInput(int bufferSize) {
    checkBufferSize(bufferSize);
    this.bufferSize = bufferSize;
  }

  /** Change the buffer size used by this IndexInput */
  public void setBufferSize(int newSize) {
    assert buffer == null || bufferSize == buffer.length: "buffer=" + buffer + " bufferSize=" + bufferSize + " buffer.length=" + (buffer != null ? buffer.length : 0);
    if (newSize != bufferSize) {
      checkBufferSize(newSize);
      bufferSize = newSize;
      if (buffer != null) {
        byte[] newBuffer = new byte[newSize];
        final int leftInBuffer = bufferLength-bufferPosition;
        final int numToCopy;
        if (leftInBuffer > newSize)
          numToCopy = newSize;
        else
          numToCopy = leftInBuffer;
        System.arraycopy(buffer, bufferPosition, newBuffer, 0, numToCopy);
        bufferStart += bufferPosition;
        bufferPosition = 0;
        bufferLength = numToCopy;
        newBuffer(newBuffer);
      }
    }
  }

  protected void newBuffer(byte[] newBuffer) {
    buffer = newBuffer;
  }

  /** Returns buffer size.  @see #setBufferSize */
  public int getBufferSize() {
    return bufferSize;
  }

  private void checkBufferSize(int bufferSize) {
    if (bufferSize <= 0)
      throw new IllegalArgumentException("bufferSize must be greater than 0 (got " + bufferSize + ")");
  }

  public void readBytes(byte[] b, int offset, int len) throws IOException {
    readBytes(b, offset, len, true);
  }

  public void readBytes(byte[] b, int offset, int len, boolean useBuffer) throws IOException {

    if(len <= (bufferLength-bufferPosition)){
        System.arraycopy(buffer, bufferPosition, b, offset, len);
      bufferPosition+=len;
    } else {
      int available = bufferLength - bufferPosition;
      if(available > 0){
        System.arraycopy(buffer, bufferPosition, b, offset, available);
        offset += available;
        len -= available;
        bufferPosition += available;
      }
      if (useBuffer && len<bufferSize){
        refill();
        if(bufferLength<len){
          System.arraycopy(buffer, 0, b, offset, bufferLength);
          throw new IOException("read past EOF");
        } else {
          System.arraycopy(buffer, 0, b, offset, len);
          bufferPosition=len;
        }
      } else {
        long after = bufferStart+bufferPosition+len;
        if(after > length())
          throw new IOException("read past EOF");
        readInternal(b, offset, len);
        bufferStart = after;
        bufferPosition = 0;
      }
    }
  }

  private void refill() throws IOException {
    long start = bufferStart + bufferPosition;
    long end = start + bufferSize;
      end = length();
    int newLength = (int)(end - start);
    if (newLength <= 0)
      throw new IOException("read past EOF");

    if (buffer == null) {
      seekInternal(bufferStart);
    }
    readInternal(buffer, 0, newLength);
    bufferLength = newLength;
    bufferStart = start;
    bufferPosition = 0;
  }

  /** Expert: implements buffer refill.  Reads bytes from the current position
   * in the input.
   * @param b the array to read bytes into
   * @param offset the offset in the array to start storing bytes
   * @param length the number of bytes to read
   */
  protected abstract void readInternal(byte[] b, int offset, int length)
          throws IOException;

  public long getFilePointer() { return bufferStart + bufferPosition; }

  public void seek(long pos) throws IOException {
    if (pos >= bufferStart && pos < (bufferStart + bufferLength))
    else {
      bufferStart = pos;
      bufferPosition = 0;
      seekInternal(pos);
    }
  }

  /** Expert: implements seek.  Sets current position in this file, where the
   * next {@link #readInternal(byte[],int,int)} will occur.
   * @see #readInternal(byte[],int,int)
   */
  protected abstract void seekInternal(long pos) throws IOException;

  public Object clone() {
    BufferedIndexInput clone = (BufferedIndexInput)super.clone();

    clone.buffer = null;
    clone.bufferLength = 0;
    clone.bufferPosition = 0;
    clone.bufferStart = getFilePointer();

    return clone;
  }

}
