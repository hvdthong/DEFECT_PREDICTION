import java.util.ArrayList;
import java.io.Serializable;

class RAMFile implements Serializable {

  private static final long serialVersionUID = 1l;

  ArrayList buffers = new ArrayList();
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

  final byte[] addBuffer(int size) {
    byte[] buffer = new byte[size];
    if (directory!=null)
        buffers.add(buffer);
        directory.sizeInBytes += size;
        sizeInBytes += size;
      }
    else
      buffers.add(buffer);
    return buffer;
  }

  long getSizeInBytes() {
    synchronized (directory) {
      return sizeInBytes;
    }
  }
  
}
