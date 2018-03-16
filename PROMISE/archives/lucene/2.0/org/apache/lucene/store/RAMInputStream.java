class RAMInputStream extends BufferedIndexInput implements Cloneable {
  private RAMFile file;
  private long pointer = 0;
  private long length;

  public RAMInputStream(RAMFile f) {
    file = f;
    length = file.length;
  }

  public void readInternal(byte[] dest, int destOffset, int len) {
    int remainder = len;
    long start = pointer;
    while (remainder != 0) {
      int bufferNumber = (int)(start/BUFFER_SIZE);
      int bufferOffset = (int)(start%BUFFER_SIZE);
      int bytesInBuffer = BUFFER_SIZE - bufferOffset;
      int bytesToCopy = bytesInBuffer >= remainder ? remainder : bytesInBuffer;
      byte[] buffer = (byte[])file.buffers.elementAt(bufferNumber);
      System.arraycopy(buffer, bufferOffset, dest, destOffset, bytesToCopy);
      destOffset += bytesToCopy;
      start += bytesToCopy;
      remainder -= bytesToCopy;
    }
    pointer += len;
  }

  public void close() {
  }

  public void seekInternal(long pos) {
    pointer = pos;
  }

  public long length() {
    return length;
  }

}
