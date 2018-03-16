import java.io.IOException;

/** Base implementation class for buffered {@link IndexInput}. */
public abstract class BufferedIndexInput extends IndexInput {
  static final int BUFFER_SIZE = BufferedIndexOutput.BUFFER_SIZE;

  private byte[] buffer;


  public byte readByte() throws IOException {
    if (bufferPosition >= bufferLength)
      refill();
    return buffer[bufferPosition++];
  }

  public void readBytes(byte[] b, int offset, int len)
       throws IOException {
    if (len < BUFFER_SIZE) {
	b[i + offset] = (byte)readByte();
      long start = getFilePointer();
      seekInternal(start);
      readInternal(b, offset, len);

      bufferPosition = 0;
    }
  }

  private void refill() throws IOException {
    long start = bufferStart + bufferPosition;
    long end = start + BUFFER_SIZE;
      end = length();
    bufferLength = (int)(end - start);
    if (bufferLength <= 0)
      throw new IOException("read past EOF");

    if (buffer == null)
    readInternal(buffer, 0, bufferLength);

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

    if (buffer != null) {
      clone.buffer = new byte[BUFFER_SIZE];
      System.arraycopy(buffer, 0, clone.buffer, 0, bufferLength);
    }

    return clone;
  }

}
