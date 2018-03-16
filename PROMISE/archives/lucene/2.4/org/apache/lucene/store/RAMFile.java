import java.util.ArrayList;
import java.io.Serializable;

class RAMFile implements Serializable {

  private static final long serialVersionUID = 1l;

  private ArrayList buffers = new ArrayList();
  long length;
  RAMDirectory directory;

  private long lastModified = System.currentTimeMillis();

  RAMFile() {}
  
  RAMFile(RAMDirectory directory) {
    this.directory = directory;
  }

  synchronized long getLength() {
    return length;
  }

  synchronized void setLength(long length) {
    this.length = length;
  }

  synchronized long getLastModified() {
    return lastModified;
  }

  synchronized void setLastModified(long lastModified) {
    this.lastModified = lastModified;
  }

  final synchronized byte[] addBuffer(int size) {
    byte[] buffer = newBuffer(size);
    if (directory!=null)
        buffers.add(buffer);
        directory.sizeInBytes += size;
        sizeInBytes += size;
      }
    else
      buffers.add(buffer);
    return buffer;
  }

  final synchronized byte[] getBuffer(int index) {
    return (byte[]) buffers.get(index);
  }

  final synchronized int numBuffers() {
    return buffers.size();
  }

  /**
   * Expert: allocate a new buffer. 
   * Subclasses can allocate differently. 
   * @param size size of allocated buffer.
   * @return allocated buffer.
   */
  byte[] newBuffer(int size) {
    return new byte[size];
  }

  long getSizeInBytes() {
    synchronized (directory) {
      return sizeInBytes;
    }
  }
  
}
