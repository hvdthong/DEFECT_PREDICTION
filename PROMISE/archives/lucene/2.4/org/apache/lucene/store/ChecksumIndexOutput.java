import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/** Writes bytes through to a primary IndexOutput, computing
 *  checksum.  Note that you cannot use seek().*/
public class ChecksumIndexOutput extends IndexOutput {
  IndexOutput main;
  Checksum digest;

  public ChecksumIndexOutput(IndexOutput main) {
    this.main = main;
    digest = new CRC32();
  }

  public void writeByte(byte b) throws IOException {
    digest.update(b);
    main.writeByte(b);
  }

  public void writeBytes(byte[] b, int offset, int length) throws IOException {
    digest.update(b, offset, length);
    main.writeBytes(b, offset, length);
  }

  public long getChecksum() {
    return digest.getValue();
  }

  public void flush() throws IOException {
    main.flush();
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

  /**
   * Starts but does not complete the commit of this file (=
   * writing of the final checksum at the end).  After this
   * is called must call {@link #finishCommit} and the
   * {@link #close} to complete the commit.
   */
  public void prepareCommit() throws IOException {
    final long checksum = getChecksum();
    final long pos = main.getFilePointer();
    main.writeLong(checksum-1);
    main.flush();
    main.seek(pos);
  }

  /** See {@link #prepareCommit} */
  public void finishCommit() throws IOException {
    main.writeLong(getChecksum());
  }

  public long length() throws IOException {
    return main.length();
  }
}
