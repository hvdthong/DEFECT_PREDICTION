import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/** Writes bytes through to a primary IndexOutput, computing
 *  checksum as it goes. Note that you cannot use seek(). */
public class ChecksumIndexInput extends IndexInput {
  IndexInput main;
  Checksum digest;

  public ChecksumIndexInput(IndexInput main) {
    this.main = main;
    digest = new CRC32();
  }

  public byte readByte() throws IOException {
    final byte b = main.readByte();
    digest.update(b);
    return b;
  }

  public void readBytes(byte[] b, int offset, int len)
    throws IOException {
    main.readBytes(b, offset, len);
    digest.update(b, offset, len);
  }

  
  public long getChecksum() {
    return digest.getValue();
  }

  public void close() throws IOException {
    main.close();
  }

  public long getFilePointer() {
    return main.getFilePointer();
  }

  public void seek(long pos) {
    throw new RuntimeException("not allowed");
  }

  public long length() {
    return main.length();
  }
}
