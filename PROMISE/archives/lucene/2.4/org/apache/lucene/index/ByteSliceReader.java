import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import java.io.IOException;

/* IndexInput that knows how to read the byte slices written
 * by Posting and PostingVector.  We read the bytes in
 * each slice until we hit the end of that slice at which
 * point we read the forwarding address of the next slice
 * and then jump to it.*/
final class ByteSliceReader extends IndexInput {
  ByteBlockPool pool;
  int bufferUpto;
  byte[] buffer;
  public int upto;
  int limit;
  int level;
  public int bufferOffset;

  public int endIndex;

  public void init(ByteBlockPool pool, int startIndex, int endIndex) {

    assert endIndex-startIndex >= 0;
    assert startIndex >= 0;
    assert endIndex >= 0;

    this.pool = pool;
    this.endIndex = endIndex;

    level = 0;
    bufferUpto = startIndex / DocumentsWriter.BYTE_BLOCK_SIZE;
    bufferOffset = bufferUpto * DocumentsWriter.BYTE_BLOCK_SIZE;
    buffer = pool.buffers[bufferUpto];
    upto = startIndex & DocumentsWriter.BYTE_BLOCK_MASK;

    final int firstSize = ByteBlockPool.levelSizeArray[0];

    if (startIndex+firstSize >= endIndex) {
      limit = endIndex & DocumentsWriter.BYTE_BLOCK_MASK;
    } else
      limit = upto+firstSize-4;
  }

  public boolean eof() {
    assert upto + bufferOffset <= endIndex;
    return upto + bufferOffset == endIndex;
  }

  public byte readByte() {
    assert !eof();
    assert upto <= limit;
    if (upto == limit)
      nextSlice();
    return buffer[upto++];
  }

  public long writeTo(IndexOutput out) throws IOException {
    long size = 0;
    while(true) {
      if (limit + bufferOffset == endIndex) {
        assert endIndex - bufferOffset >= upto;
        out.writeBytes(buffer, upto, limit-upto);
        size += limit-upto;
        break;
      } else {
        out.writeBytes(buffer, upto, limit-upto);
        size += limit-upto;
        nextSlice();
      }
    }

    return size;
  }

  public void nextSlice() {

    final int nextIndex = ((buffer[limit]&0xff)<<24) + ((buffer[1+limit]&0xff)<<16) + ((buffer[2+limit]&0xff)<<8) + (buffer[3+limit]&0xff);

    level = ByteBlockPool.nextLevelArray[level];
    final int newSize = ByteBlockPool.levelSizeArray[level];

    bufferUpto = nextIndex / DocumentsWriter.BYTE_BLOCK_SIZE;
    bufferOffset = bufferUpto * DocumentsWriter.BYTE_BLOCK_SIZE;

    buffer = pool.buffers[bufferUpto];
    upto = nextIndex & DocumentsWriter.BYTE_BLOCK_MASK;

    if (nextIndex + newSize >= endIndex) {
      assert endIndex - nextIndex > 0;
      limit = endIndex - bufferOffset;
    } else {
      limit = upto+newSize-4;
    }
  }

  public void readBytes(byte[] b, int offset, int len) {
    while(len > 0) {
      final int numLeft = limit-upto;
      if (numLeft < len) {
        System.arraycopy(buffer, upto, b, offset, numLeft);
        offset += numLeft;
        len -= numLeft;
        nextSlice();
      } else {
        System.arraycopy(buffer, upto, b, offset, len);
        upto += len;
        break;
      }
    }
  }

  public long getFilePointer() {throw new RuntimeException("not implemented");}
  public long length() {throw new RuntimeException("not implemented");}
  public void seek(long pos) {throw new RuntimeException("not implemented");}
  public void close() {throw new RuntimeException("not implemented");}
}

