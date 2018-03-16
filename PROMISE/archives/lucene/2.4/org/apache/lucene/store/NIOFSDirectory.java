import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO version of FSDirectory.  Uses FileChannel.read(ByteBuffer dst, long position) method
 * which allows multiple threads to read from the file without synchronizing.  FSDirectory
 * synchronizes in the FSIndexInput.readInternal method which can cause pileups when there
 * are many threads accessing the Directory concurrently.  
 *
 * This class only uses FileChannel when reading; writing
 * with an IndexOutput is inherited from FSDirectory.
 * 
 * Note: NIOFSDirectory is not recommended on Windows because of a bug
 * in how FileChannel.read is implemented in Sun's JRE.
 * Inside of the implementation the position is apparently
 * synchronized.  See here for details:

 * 
 * @see FSDirectory
 */

public class NIOFSDirectory extends FSDirectory {

  public IndexInput openInput(String name, int bufferSize) throws IOException {
    ensureOpen();
    return new NIOFSIndexInput(new File(getFile(), name), bufferSize);
  }

  private static class NIOFSIndexInput extends FSDirectory.FSIndexInput {


    private byte[] otherBuffer;
    private ByteBuffer otherByteBuf;

    final FileChannel channel;

    public NIOFSIndexInput(File path, int bufferSize) throws IOException {
      super(path, bufferSize);
      channel = file.getChannel();
    }

    protected void newBuffer(byte[] newBuffer) {
      super.newBuffer(newBuffer);
      byteBuf = ByteBuffer.wrap(newBuffer);
    }

    public void close() throws IOException {
      if (!isClone && file.isOpen) {
        try {
          channel.close();
        } finally {
          file.close();
        }
      }
    }

    protected void readInternal(byte[] b, int offset, int len) throws IOException {

      final ByteBuffer bb;

      if (b == buffer && 0 == offset) {
        assert byteBuf != null;
        byteBuf.clear();
        byteBuf.limit(len);
        bb = byteBuf;
      } else {
        if (offset == 0) {
          if (otherBuffer != b) {
            otherBuffer = b;
            otherByteBuf = ByteBuffer.wrap(b);
          } else
            otherByteBuf.clear();
          otherByteBuf.limit(len);
          bb = otherByteBuf;
        } else
          bb = ByteBuffer.wrap(b, offset, len);
      }

      long pos = getFilePointer();
      while (bb.hasRemaining()) {
        int i = channel.read(bb, pos);
        if (i == -1)
          throw new IOException("read past EOF");
        pos += i;
      }
    }
  }
}
